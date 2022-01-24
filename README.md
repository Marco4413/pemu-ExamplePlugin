# Example Plugin

## What's this project all about?

It's the official [PEMU](https://github.com/hds536jhmk/ProcessorEmulator) Example Plugin

## How to run:

This project is built using [**Adopt OpenJDK 8**](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot),
though any newer JRE should be able to run this properly.

Running on IntelliJ IDEA:
 1. Open the project and create a new **Application Run Config**:
    - Java Version: `1.8` (Adopt OpenJDK)
    - Class Path: `ExamplePlugin.main`
    - Main Class: `io.github.hds.pemu.exampleplugin.Main`
 2. Run the newly created config, the main file automatically opens PEMU with the Plugin registered.

Running on PEMU:
 1. Download the Prebuilt Jar file in Releases or build it yourself using `gradlew build`.
 2. Follow this guide to see where the plugins' folder is on your PC: [Plugins Guide](https://github.com/hds536jhmk/ProcessorEmulator/blob/master/plugins/README.md)

## Dependencies:

There's only 2 dependencies:
 - [PEMU](https://github.com/hds536jhmk/ProcessorEmulator)
 - [JetBrains Annotations](https://github.com/JetBrains/java-annotations)
