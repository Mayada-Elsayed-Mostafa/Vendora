import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.apollographql.apollo") version "4.3.0"
}

apollo {
    service("service") {
        packageName.set("com.example.vendora")
    }
}

android {
    namespace = "com.example.vendora"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.vendora"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //here we get the access token we hide in local.properties file
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField(
            "String",
            "adminApiAccessToken",
            "\"${properties.getProperty("adminApiAccessToken")}\""
        )
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {

    // Navigation
    implementation(libs.androidx.navigation.compose)
    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Jetpack DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation("androidx.datastore:datastore-preferences-core:1.1.7")

    //Lottie
    implementation(libs.lottie.compose.v630)

    // Firebase Firestore + Coroutines
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(platform(libs.firebase.bom))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")
    
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")

    // Apollo
    implementation("com.apollographql.apollo:apollo-runtime:4.3.0")
    //Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    // Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")


    //Serialization for NavArgs
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    //kotlinx Datetime
    implementation(libs.kotlinx.datetime)

    //////////////////
    ///For Testing
    // Dependencies for local unit tests
    val junitVersion = "4.13.2"
    val hamcrestVersion = "1.3"
    val archTestingVersion = "2.2.0"
    val robolectricVersion = "4.5.1"
    testImplementation ("junit:junit:$junitVersion")
    testImplementation ("org.hamcrest:hamcrest-all:$hamcrestVersion")
    testImplementation ("androidx.arch.core:core-testing:$archTestingVersion")
    testImplementation ("org.robolectric:robolectric:$robolectricVersion")

    // AndroidX Test - JVM testing
    val androidXTestCoreVersion="1.6.1"
    val androidXTestExtKotlinRunnerVersion="1.1.3"
    val espressoVersion="3.4.0"
    testImplementation ("androidx.test:core-ktx:$androidXTestCoreVersion")
    // testImplementation ("androidx.test.ext:junit:$androidXTestExtKotlinRunnerVersion")

    // AndroidX Test - Instrumented testing

    androidTestImplementation ("androidx.test:core:$androidXTestExtKotlinRunnerVersion")
    androidTestImplementation ("androidx.test.espresso:espresso-core:$espressoVersion")

    //Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")

    // hamcrest
//    testImplementation ("org.hamcrest:hamcrest:2.2")
//    testImplementation ("org.hamcrest:hamcrest-library:2.2")
//    androidTestImplementation ("org.hamcrest:hamcrest:2.2")
//    androidTestImplementation ("org.hamcrest:hamcrest-library:2.2")
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    androidTestImplementation("org.hamcrest:hamcrest-all:1.3")


    // AndroidX and Robolectric
    testImplementation ("androidx.test.ext:junit-ktx:$androidXTestExtKotlinRunnerVersion")
    testImplementation ("androidx.test:core-ktx:$androidXTestCoreVersion")
    testImplementation ("org.robolectric:robolectric:4.11")

    // InstantTaskExecutorRule
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")

    //kotlinx-coroutines
    val coroutinesVersion="1.5.0"
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")


    //MockK
    testImplementation( "io.mockk:mockk-android:1.13.17")
    testImplementation ("io.mockk:mockk-agent:1.13.17")
    testImplementation("io.mockk:mockk:1.13.17")
}