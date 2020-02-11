
lib = File.expand_path('../lib', __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require 'myanmar-tools/version'

Gem::Specification.new do |spec|
  spec.name          = 'myanmar-tools'
  spec.version       = MyanmarTools::VERSION
  spec.authors       = ['Aung Kyaw Phyo']
  spec.email         = ['kiru.kiru28@gmail.com']

  spec.summary       = 'Tools for handling the Zawgyi font encoding in Myanmar.'
  spec.description   = 'Tools for handling the Zawgyi font encoding in Myanmar.'
  spec.homepage      = 'https://github.com/google/myanmar-tools'
  spec.required_ruby_version = '>= 2.0.0'
  spec.license       = 'Apache-2.0'

  spec.files = `git ls-files -z`.split("\x0").reject do |f|
    f.match(%r{^(spec|features)/})
  end

  spec.bindir        = 'exe'
  spec.executables   = spec.files.grep(%r{^exe/}) { |f| File.basename(f) }
  spec.require_paths = ['lib']

  spec.add_development_dependency 'bundler', '>= 1.0'
  spec.add_development_dependency 'rake', '~> 10.0'
  spec.add_development_dependency 'minitest', '~> 3.0'
end
