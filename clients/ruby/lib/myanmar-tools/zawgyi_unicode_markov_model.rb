# Copyright 2018 Aung Kyaw Phyo

# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at

#     http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# A Markov model to predict whether a string is more likely Unicode or Zawgyi.

# Internally, this class maintains two Markov chains, one representing Unicode and the other
# representing Zawgyi. An input string is evaluated against both chains, and the chain that returns
# the higher probability becomes the prediction.

# A string is evaluated as a sequence of transitions between states, including transitions to
# the edges of the string. For example, the string "ABC" contains 4 transitions: NULL to A, A to B,
# B to C, and C to NULL.

# For the purposes of Unicode/Zawgyi detection, all characters are treated as the NULL state
# except for characters in the Myanmar script or characters in the Unicode whitespace range U+2000
# through U+200B.
class ZawgyiUnicodeMarkovModel

    # Magic number used to identify this object in byte streams. (Reads in ASCII as "UZMODEL ")
    BINARY_TAG = "555a4d4f44454c20"
  
    # Standard Myanmar code point range before digits
    STD_CP0 = 0x1000
    STD_CP1 = 0x103F
  
    # Standard Myanmar code point range after digits
    AFT_CP0 = 0x104A
    AFT_CP1 = 0x109F
  
    # Extended Myanmar code point range A
    EXA_CP0 = 0xAA60
    EXA_CP1 = 0xAA7F
  
    # Extended Myanmar code point range B
    EXB_CP0 = 0xA9E0
    EXB_CP1 = 0xA9FF
  
    # Unicode space characters
    SPC_CP0 = 0x2000
    SPC_CP1 = 0x200B
  
    # Indices into Markov nodes
    STD_OFFSET = 1
    AFT_OFFSET = STD_OFFSET + STD_CP1 - STD_CP0 + 1
    EXA_OFFSET = AFT_OFFSET + AFT_CP1 - AFT_CP0 + 1
    EXB_OFFSET = EXA_OFFSET + EXA_CP1 - EXA_CP0 + 1
    SPC_OFFSET = EXB_OFFSET + EXB_CP1 - EXB_CP0 + 1
    NUM_STATES = SPC_OFFSET + SPC_CP1 - SPC_CP0 + 1

    private_constant :BINARY_TAG, :STD_CP0, :STD_CP1, :AFT_CP0, :AFT_CP1,
                     :EXA_CP0, :EXA_CP1, :EXB_CP0, :EXB_CP1, :SPC_CP0, :SPC_CP1, :STD_OFFSET,
                     :AFT_OFFSET, :EXA_OFFSET, :EXB_OFFSET, :SPC_OFFSET, :NUM_STATES
  
    # Creates an instance from a binary data stream.
    def initialize(stream)
      # Check magic number and serial version number
      binary_tag = stream.read(8).unpack('H*')[0]
      if binary_tag != BINARY_TAG
        raise "Unexpected magic number: expected #{BINARY_TAG} but got #{binary_tag}"
      end
  
      binary_version = stream.read(4).unpack('H*')[0].to_i
      if binary_version == 1
        @ssv = 0
      elsif binary_version == 2
        # TODO: Support nonzero SSV if needed in the future
        @ssv = stream.read(4).unpack('H*')[0].to_i
        if @ssv != 0
          raise "Unsupported ssv: #{@ssv}"
        end
      else          
        raise "Unexpected serial version number: expected 1 or 2 but got #{binary_version}"
      end
  
      @classifier = BinaryMarkov.new(stream)
    end
  
    # Returns the index of the state in the Markov chain corresponding to the given code point.
    # Code points in the standard Myanmar range, Myanmar Extended A, Myanmar Extended B, and
    # Unicode Whitespace each have a unique state assigned to them. All other code points are mapped
    # to state 0.  
    # Package-private so that the builder can use this method.
    # @param cp The code point to convert to a state index.
    # @return The index of the state in the Markov chain. 0 <= state < getSize()
    def self.get_index_for_code_point(cp)
      marko_chain_index = 0
      if STD_CP0 <= cp && cp <= STD_CP1
        marko_chain_index = cp - STD_CP0 + STD_OFFSET
      elsif AFT_CP0 <= cp && cp <= AFT_CP1
        marko_chain_index = cp - AFT_CP0 + AFT_OFFSET
      elsif EXA_CP0 <= cp && cp <= EXA_CP1
        marko_chain_index = cp - EXA_CP0 + EXA_OFFSET
      elsif EXB_CP0 <= cp && cp <= EXB_CP1
        marko_chain_index = cp - EXB_CP0 + EXB_OFFSET
      elsif SPC_CP0 <= cp && cp <= SPC_CP1
        marko_chain_index = cp - SPC_CP0 + SPC_OFFSET
      end
      marko_chain_index
    end

    # Runs the given input string on both internal Markov chains and computes the probability of the
    # string being unicode or zawgyi.
    # @param1 input The string to evaluate.
    # @param2 verbose Whether to print the log probabilities for debugging.
    # @return The probability that the string is Zawgyi given that it is either Unicode or Zawgyi, or
    # -Infinity if there are no Myanmar range code points in the string.
    def predict(input, verbose=false)
      if verbose
        puts "Running detector on string: #{input}"
      end
  
      # Start at the base state
      prev_cp = 0
      prev_state = 0
  
      total_delta = 0.0
      seen_transition = false
  
      offset = 0
      while offset <= input.length
        if offset == input.length
          cp = 0
          curr_state = 0
        else
          cp = input.codepoints[offset]
          curr_state = self.class.get_index_for_code_point(cp)
        end
  
        # Ignore 0-to-0 transitions
        if prev_state != 0 || curr_state != 0
          # Gets the difference in log probabilities between chain A and chain B.
          # First param: The index of the source node to transition from.
          # Second param: The index of the destination node to transition to.
          delta = @classifier.log_probability_differences[prev_state][curr_state].to_f
  
          if verbose
            puts "#{prev_cp} -> #{cp}: delta=#{delta}"
            puts "ABS: #{delta.abs}"
            delta_index = 1
            while delta_index < delta.abs
              print "!"
              delta_index +=1
            end
            puts ""
          end
  
          total_delta += delta
          seen_transition = true
        end
  
        offset += char_count(cp)
        prev_cp = cp
        prev_state = curr_state
      end
  
      if verbose
        puts "Final: delta=#{total_delta}"
      end
  
      # Special case: if there is no signal, return -Infinity,
      # which will get interpreted by users as strong Unicode.
      # This happens when the input string contains no Myanmar-range code points.
      unless seen_transition
        return -1.0/0.0
      end
      1.0 / (1.0 + Math.exp(total_delta))
    end

    # Determine the number of char values needed to represent the specified character (Unicode code point)
    # if the code point is equal to or greater than 0x10000, then the method returns 2.
    # otherwise, the method returns 1.
    def char_count(code_point)
      code_point >= 0x10000 ? 2 : 1
    end
  end
  