/*
 * Copyright 2023-2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

object Plugins {

  object BuildPlugins {
    const val androidLib = "com.android.library"
    const val application = "com.android.application"
    const val benchmark = "androidx.benchmark"
    const val dokka = "org.jetbrains.dokka"
    const val kotlin = "kotlin"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinCompose = "org.jetbrains.kotlin.plugin.compose"
    const val kotlinKsp = "com.google.devtools.ksp"
    const val kotlinSerialization = "org.jetbrains.kotlin.plugin.serialization"
    const val mavenPublish = "maven-publish"
    const val fladle = "com.osacky.fladle"
    const val navSafeArgs = "androidx.navigation.safeargs.kotlin"
    const val ruler = "com.spotify.ruler"
    const val spotless = "com.diffplug.spotless"
    const val androidTest = "com.android.test"
  }

  // classpath plugins
  const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
  const val benchmarkGradlePlugin =
    "androidx.benchmark:benchmark-gradle-plugin:${Versions.benchmarkPlugin}"
  const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
  const val kotlinComposePlugin =
    "org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:${Versions.kotlin}"
  const val kotlinSerializationPlugin =
    "org.jetbrains.kotlin.plugin.serialization:org.jetbrains.kotlin.plugin.serialization.gradle.plugin:${Versions.kotlin}"
  const val navSafeArgsGradlePlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0"
  const val rulerGradlePlugin = "com.spotify.ruler:ruler-gradle-plugin:1.2.1"
  const val flankGradlePlugin = "com.osacky.flank.gradle:fladle:0.17.4"
  const val kspGradlePlugin =
    "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${Versions.kspPlugin}"

  object Versions {
    const val androidGradlePlugin = "8.9.2"
    const val benchmarkPlugin = "1.4.0-rc01"
    const val dokka = "1.9.20"
    const val kspPlugin = "2.1.20-2.0.1"
    const val kotlin = "2.1.20"
  }
}
