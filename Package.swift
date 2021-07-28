// swift-tools-version:5.3
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "eq-words",
    dependencies: [
        // Dependencies declare other packages that this package depends on.
        // .package(url: /* package url */, from: "1.0.0"),
    ],
    targets: [
        // Targets are the basic building blocks of a package. A target can define a module or a test suite.
        // Targets can depend on other targets in this package, and on products in packages this package depends on.
        .target(
            name: "eq-words",
            dependencies: []),
        .target(
            name: "gen-legis",
	    dependencies: [],
	    resources: [
	       .copy("Resources/chap-1.txt"),
	       .copy("Resources/chap-2.txt"),
	       .copy("Resources/chap-3.txt"),
	       .copy("Resources/comment.txt"),
	       .copy("Resources/extra-words.txt"),
	    ]),
    ]
)
