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

# A class that behaves as if it were two Markov chains, called Chain A and Chain B. Whereas a
# normal Markov chain would expose the log probability of a transition, this class exposes the
# difference between the log probabilities of the two Markov chains.
class BinaryMarkov
    # Magic number used to identify this object in byte streams. (Reads in ASCII as "BMARKOV ")
    BINARY_TAG = "424d41524b4f5620"
  
    # Current serial format version number, used in association with the magic number.
    BINARY_VERSION = 0

    private_constant :BINARY_TAG, :BINARY_VERSION

    attr_reader :log_probability_differences

    def initialize(stream)
      # Check magic number and serial version number
      binary_tag = stream.read(8).unpack('H*')[0]
      if binary_tag != BINARY_TAG
        raise "Unexpected magic number: expected #{BINARY_TAG} but got #{binary_tag}"
      end
  
      binary_version = stream.read(4).unpack('H*')[0].to_i
      if binary_version != BINARY_VERSION
        raise "Unexpected serial version number: expected #{BINARY_VERSION} but got #{binary_version}"
      end

      generate_probability_differences(stream)
    end
  
    private

    def generate_probability_differences(stream)
      size = stream.read(2).unpack('n*')[0]
      probability_diffs = Array.new(size){ Array.new(size) }
      i1 = 0
      while i1 < size
        entries = stream.read(2).unpack('n*')[0]
        fallback = entries == 0 ? 0.0 : stream.read(4).unpack('g*')[0]
        next_target = -1
        i2 = 0
        while i2 < size
          if entries > 0 && next_target < i2
            next_target = stream.read(2).unpack('n*')[0]
            entries -= 1
          end
          probability_diffs[i1][i2] = next_target == i2 ? stream.read(4).unpack('g*')[0] : fallback
          i2 += 1
        end
        i1 += 1
      end
      @log_probability_differences = probability_diffs
    end
  end