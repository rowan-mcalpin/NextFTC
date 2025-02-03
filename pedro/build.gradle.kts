plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    `maven-publish`
}

android {
    namespace = "com.rowanmcalpin.nextftc.pedro"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        jvmTarget = "17"
    }
}

dependencies {
    compileOnly(libs.pedro)
    implementation(libs.ftc.hardware)
    implementation(libs.ftc.robotcore)

    implementation(project(":core"))
    implementation(project(":ftc"))
}


// CONFIGURE PUBLISHING
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.rowanmcalpin.nextftc"
            artifactId = "pedro"
            version = libs.versions.nextftc.get()

            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        maven {
            name = "publishing"
            url = uri("../maven.rowanmcalpin.com")
        }
    }
}