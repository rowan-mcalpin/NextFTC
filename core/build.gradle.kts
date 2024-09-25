import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android") version "1.9.20"

    `maven-publish`

    id("org.jetbrains.dokka") version "1.9.10"
    id("kotlin-android")
}

android {
    namespace = "com.rowanmcalpin.nextftc"
    compileSdk = 32

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    publishing {
        singleVariant("release")
    }
    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/kotlin")
        }
    }
}

dependencies {
//
//    implementation("androidx.core:core-ktx:1.12.0")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.10.0")

    compileOnly("org.firstinspires.ftc:RobotCore:10.0.0")
    compileOnly("org.firstinspires.ftc:Hardware:10.0.0")
    compileOnly("org.firstinspires.ftc:FtcCommon:10.0.0")
    compileOnly("org.firstinspires.ftc:RobotServer:10.0.0")
    compileOnly("org.firstinspires.ftc:OnBotJava:10.0.0")

    implementation("com.acmerobotics.dashboard:dashboard:0.4.6") {
        exclude(group = "org.firstinspires.ftc")
    }

    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("com.acmerobotics.roadrunner:core:0.5.5")
    implementation("com.github.rowan-mcalpin:MeepMeep-1:actualworking")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")
}

// CONFIGURE DOKKA
tasks.dokkaHtml.configure {
    moduleName.set("NextFTC")

    dokkaSourceSets.configureEach {
        includes.from("packages.md")

    }
}

tasks.withType(DokkaTask::class).configureEach {
    pluginsMapConfiguration.set(
        mapOf(Pair("org.jetbrains.dokka.base.DokkaBase", """
        {
            "footerMessage": "Copyright © 2024 Rowan McAlpin"
        }"""))
    )
}

// CONFIGURE PUBLICATION FOR JITPACK
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.rowanmcalpin.nextftc"
            artifactId = "core"
            version = "0.4.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        maven {
            name = "publishing"
            url = uri("../../../maven.rowanmcalpin.com")
        }
    }
}