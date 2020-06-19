// swift-tools-version:5.2

import PackageDescription

let package = Package(
    name: "MyanmarTools",
    products: [
        .library(
            name: "MyanmarTools",
            targets: ["MyanmarTools"]),
    ],
    targets: [
        .target(
            name: "MyanmarTools",
            dependencies: ["CMyanmarTools"]),
        .systemLibrary(name: "CMyanmarTools"),
        .testTarget(
            name: "MyanmarToolsTests",
            dependencies: ["MyanmarTools"]),
    ]
)
