plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    `maven-publish`
}

android {
    namespace = "com.rowanmcalpin.nextftc.ftc"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    implementation(project(":core"))

    compileOnly(libs.ftc.robotcore)
    compileOnly(libs.ftc.hardware)
}

// CONFIGURE PUBLISHING
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.rowanmcalpin.nextftc"
            artifactId = "ftc"
            version = libs.versions.module.ftc.get()

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