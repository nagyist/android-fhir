plugins {
  id(Plugins.BuildPlugins.androidLib)
  id(Plugins.BuildPlugins.kotlinAndroid)
  id(Plugins.BuildPlugins.benchmark)
}

android {
  namespace = "com.google.android.fhir.engine.microbenchmark"
  compileSdk = Sdk.COMPILE_SDK
  defaultConfig {
    minSdk = Sdk.MIN_SDK
    testInstrumentationRunner = Dependencies.androidBenchmarkRunner
  }

  testBuildType = "release"
  buildTypes { release {} }
  packaging {
    resources.excludes.addAll(
      listOf(
        "license.html",
        "META-INF/ASL2.0",
        "META-INF/ASL-2.0.txt",
        "META-INF/DEPENDENCIES",
        "META-INF/LGPL-3.0.txt",
        "META-INF/LICENSE",
        "META-INF/LICENSE.txt",
        "META-INF/license.txt",
        "META-INF/license.html",
        "META-INF/LICENSE.md",
        "META-INF/NOTICE",
        "META-INF/NOTICE.txt",
        "META-INF/NOTICE.md",
        "META-INF/notice.txt",
        "META-INF/LGPL-3.0.txt",
        "META-INF/sun-jaxb.episode",
        "META-INF/*.kotlin_module",
        "readme.html",
      ),
    )
  }

  compileOptions {
    // Flag to enable support for the new language APIs
    // See https = //developer.android.com/studio/write/java8-support
    isCoreLibraryDesugaringEnabled = true
  }

  kotlin { jvmToolchain(11) }
}

afterEvaluate { configureFirebaseTestLabForMicroBenchmark() }

dependencies {
  androidTestImplementation(Dependencies.Retrofit.coreRetrofit)
  androidTestImplementation(Dependencies.mockWebServer)
  androidTestImplementation(libs.androidx.benchmark.junit4)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.work.runtime)
  androidTestImplementation(libs.androidx.work.testing)
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.kotlinx.coroutines.android)
  androidTestImplementation(libs.truth)

  androidTestImplementation(project(":engine"))
  // for test json files only
  androidTestImplementation(project(":workflow-testing"))

  coreLibraryDesugaring(Dependencies.desugarJdkLibs)
}
