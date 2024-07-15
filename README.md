[![JitPack](https://img.shields.io/jitpack/version/com.rowanmcalpin/nextftc?label=JitPack)](https://jitpack.io/#com.rowanmcalpin/nextftc)
[![GitHub commit activity](https://img.shields.io/github/commit-activity/t/rowan-mcalpin/nextftc?label=Commits)](https://github.com/rowan-mcalpin/nextftc/commits/main/)

# NextFTC

NextFTC is an advanced and open-source toolkit designed specifically for the First Tech Challenge (FTC) robotics competition. This library provides a comprehensive suite of functionalities that facilitate the development of robust and efficient robot control systems. It aims to simplify the coding process for FTC participants by offering reusable components and intuitive interfaces, making it easier to implement complex robotic behaviors.

NextFTC is built with Kotlin and leverages modern software practices to ensure high performance and reliability. By integrating NextFTC, teams can enhance their robots' capabilities, streamline their development workflows, and focus more on innovation and strategic planning for the competition.

## Installation

#### Install NextFTC using Gradle.

In your project's `build.dependencies.gradle` file, change the `repositories` block to look like this:

```groovy
repositories {
    mavenCentral()
    google() // Needed for androidx

    maven { url = "https://jitpack.io/" }
}
```

Now, in your `TeamCode` module's `build.gradle`, add the following line to the end of the `dependencies` block:

```groovy
implementation 'com.rowanmcalpin:nextftc:0.1.2'
```

Sync Gradle and you're good to go!
