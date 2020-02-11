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

require 'myanmar-tools/version'
require 'myanmar-tools/binary_markov'
require 'myanmar-tools/zawgyi_unicode_markov_model'

module MyanmarTools
# Uses a machine learning model to determine whether a string of text is Zawgyi or Unicode.
# For more details and documentation, see https://github.com/google/myanmar-tools
class ZawgyiDetector
  def initialize
    @model = ZawgyiUnicodeMarkovModel.new(stream_markov_model)
  end

  # Performs detection on the given string. Returns the probability that the string is Zawgyi given
  # that it is either Unicode or Zawgyi. Values approaching 1 are strong Zawgyi; values approaching
  # 0 are strong Unicode; and values close to 0.5 are toss-ups.

  # If the string does not contain any Myanmar range code points, -Infinity is returned.

  # @param1 input The string on which to run detection.
  # @param2 verbose If true, print debugging information to standard output.
  # @return The probability that the string is Zawgyi (between 0 and 1), or -Infinity if the string
  #   contains no Myanmar range code points.
  def get_zawgyi_probability(input='', verbose = false)
    @model.predict(String(input), verbose)
  end

  private

  # Open markov model from the specified path
  def stream_markov_model
    file_path = File.join(File.dirname(__FILE__), '/myanmar-tools/resources/zawgyiUnicodeModel.dat')
    if File.file?(file_path)
      begin
        File.open(file_path, File::RDONLY)
      rescue
        raise 'Could not open Markov Model file zawgyiUnicodeModel.dat.'
      end
    else
      raise 'Could not find Markov model file.'
    end
  end
end
end
