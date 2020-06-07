#
#  Be sure to run `pod spec lint myanmartools.podspec' to ensure this is a
#  valid spec and to remove all comments including this before submitting the spec.
#
#  To learn more about Podspec attributes see https://guides.cocoapods.org/syntax/podspec.html
#  To see working Podspecs in the CocoaPods repo see https://github.com/CocoaPods/Specs/
#

Pod::Spec.new do |spec|

  # ―――  Spec Metadata  ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #
  #
  #  These will help people to find your library, and whilst it
  #  can feel like a chore to fill in it's definitely to your advantage. The
  #  summary should be tweet-length, and the description more in depth.
  #

  spec.name         = "myanmartools"
  spec.version      = "1.0"
  spec.summary      = "Zawgyi detection tool"
  spec.homepage     = "https://github.com/google/myanmar-tools"
  spec.license      = { :type => "Apache", :file => "LICENCE.md" }
  spec.author             = { "La Win Ko" => "lawinko11@gmail.com" }
  spec.social_media_url   = "https://www.facebook.com/lawinko.dev/"
  spec.source       = { :git => "https://github.com/google/myanmar-tools.git", :tag => "v1.0" }
  spec.source_files  = "clients/swift/myanmartools/src/**/*.{h,m,swift}"
  spec.resource  = "clients/swift/myanmartools/resources/zawgyiUnicodeModel.dat"
  spec.swift_version = '5.2.4'
  spec.ios.deployment_target  = '9.0'
  spec.osx.deployment_target  = '10.10'

end
