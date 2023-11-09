plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.SiAntik"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.SiAntik"
        minSdk = 25
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures{
        viewBinding = true
    }

    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.material:material:1.2.0")
    implementation("com.google.android.material:material:1.4.0")
    //nav bar
    //implementation("com.google.android.material:material:1.11.0-alpha03")
    //chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.6.0")
    //chip nav bar
    implementation ("com.ismaeldivita.chipnavigation:chip-navigation-bar:1.3.2")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.4.31")
    // picasso image
    implementation ("com.squareup.picasso:picasso:2.71828")

}

