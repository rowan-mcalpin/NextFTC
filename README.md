[![Build](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fmaven.rowanmcalpin.com%2Fcom%2Frowanmcalpin%2Fnextftc%2Fcore%2Fmaven-metadata.xml&query=%2Fmetadata%2Fversioning%2Flatest&prefix=v&label=Build&color=%2310e000
)](https://github.com/rowan-mcalpin/NextFTC/releases/latest)
[![GitHub commit activity](https://img.shields.io/github/commit-activity/t/rowan-mcalpin/nextftc?label=Commits)](https://github.com/rowan-mcalpin/nextftc/commits/main/)

# NextFTC

NextFTC is an advanced and open-source toolkit designed specifically for the First Tech Challenge 
(FTC) robotics competition. This library provides a comprehensive suite of functionalities that 
facilitate the development of robust and efficient robot control systems. It aims to simplify the 
coding process for FTC participants by offering reusable components and intuitive interfaces, making 
it easier to implement complex robotic behaviors.

NextFTC is built with Kotlin and leverages modern software practices to ensure high performance and 
reliability. By integrating NextFTC, teams can enhance their robots' capabilities, streamline their 
development workflows, and focus more on innovation and strategic planning for the competition.

NextFTC is officially supported by [Pedro Pathing](https://pedropathing.com) and integrates
seamlessly with it.

Join the NextFTC Discord server [here](https://discord.gg/PjP9Ze6fkX)

## Installation

### Install NextFTC using Gradle

In your project's `build.dependencies.gradle` file, add the following lines to the `repositories` block:

```groovy
maven { url = "https://maven.rowanmcalpin.com/" }
maven { url = "https://maven.pedropathing.com/" }
maven { url = "https://maven.brott.dev/" }
```

Next, add the following lines to the `dependencies` block:

```groovy
implementation 'com.rowanmcalpin.nextftc:core:0.5.5-beta2'
implementation 'com.rowanmcalpin.nextftc:ftc:0.5.5-beta2'
implementation 'com.rowanmcalpin.nextftc:pedro:0.5.5-beta2'
implementation 'com.pedropathing:pedro:1.0.3'
implementation 'com.acmerobotics.dashboard:dashboard:0.4.16'
```

Finally, sync Gradle. This will add NextFTC, as well as its requirements (PedroPathing and FTC Dashboard)
