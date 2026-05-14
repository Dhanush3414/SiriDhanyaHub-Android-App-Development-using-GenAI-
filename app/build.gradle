plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.gms.google-services'
    id 'kotlin-parcelize'
}

android {
    namespace 'com.siridhanya.hub'
    compileSdk 34

    defaultConfig {
        applicationId "com.siridhanya.hub"
        minSdk 26
        targetSdk 34
        versionCode 1
        versionName "1.0.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"

        def localProps = new Properties()
        def localFile = rootProject.file("local.properties")
        if (localFile.exists()) {
            localProps.load(new FileInputStream(localFile))
        }
        buildConfigField "String", "GEMINI_API_KEY",
                "\"${localProps.getProperty('GEMINI_API_KEY', '')}\""
    }

    buildFeatures {
        viewBinding true
        buildConfig true
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'),
                    'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_21
        targetCompatibility JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = '21'
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.appcompat:appcompat:1.7.0'
    implementation 'androidx.activity:activity-ktx:1.9.0'
    implementation 'androidx.fragment:fragment-ktx:1.8.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.8.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'
    implementation 'com.google.android.material:material:1.12.0'

    // Firebase
    implementation platform('com.google.firebase:firebase-bom:33.1.0')
    implementation 'com.google.firebase:firebase-database-ktx'
    implementation 'com.google.firebase:firebase-analytics-ktx'

    // Glide
    implementation 'com.github.bumptech.glide:glide:4.16.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'

    // MPAndroidChart
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

    // Shimmer
    implementation 'com.facebook.shimmer:shimmer:0.5.0'

    // Coroutines only - NO Gemini library needed anymore!
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.2.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.6.1'
}