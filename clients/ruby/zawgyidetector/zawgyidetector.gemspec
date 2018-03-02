
lib = File.expand_path("../lib", __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require "zawgyidetector/version"

Gem::Specification.new do |spec|
  spec.name          = 'zawgyidetector'
  spec.version       = Zawgyidetector::VERSION
  spec.authors       = ['Aung Kyaw Phyo']
  spec.email         = ['kiru.kiru28@gmail.com']

  spec.summary       = 'Tools for handling the Zawgyi font encoding in Myanmar.'
  spec.description   = 'Tools for handling the Zawgyi font encoding in Myanmar.'
  spec.homepage      = 'https://github.com/googlei18n/myanmar-tools'
  spec.license       = 'Apache-2.0'

  # Prevent pushing this gem to RubyGems.org. To allow pushes either set the 'allowed_push_host'
  # to allow pushing to a single host or delete this section to allow pushing to any host.
  # if spec.respond_to?(:metadata)
  #   spec.metadata["allowed_push_host"] = "TODO: Set to 'http://mygemserver.com'"
  # else
  #   raise "RubyGems 2.0 or newer is required to protect against " \
  #     "public gem pushes."
  # end

  spec.files = `git ls-files -z`.split("\x0").reject do |f|
    f.match(%r{^(spec|features)/})
  end

  spec.bindir        = 'exe'
  spec.executables   = spec.files.grep(%r{^exe/}) { |f| File.basename(f) }
  spec.require_paths = ['lib']

  spec.add_development_dependency 'bundler', '~> 1.0'
  spec.add_development_dependency 'rake', '~> 10.0'
  spec.add_development_dependency 'minitest', '~> 3.0'
end
