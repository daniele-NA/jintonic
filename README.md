# Tired of the Same Old Helper Classes?

<div align="center">
  <img src="ASSETS/logo.svg" alt="Jintonic Logo" width="600" />
</div>

<p style="font-size:18px; text-align:center;">
  <b>Jintonic</b> is the <b>first Android library fully based on AOP (Aspect-Oriented Programming)</b>.<br/><br>
  It allows you to <b>hook and weave behaviors at build time</b> directly into your <code>.class</code> files through Gradle plugins.
</p>



---

## ✨ Features

- **Easy-to-use annotations** for checks and safety:

```kotlin
@RequireInternet(mustThrow = true)
@MinBattery(minOrEqualValue = 60)
fun kotlinApiCall() = "Kotlin Api call done successfully"

@Timed
fun hello() = runBlocking {
    delay(5000)
}

@RequireVpn
fun vpnMethod() {
    LOG("Executed Vpn Method")
}

@WithRoot(forRoot = false)
fun rootMethod() {
    LOG("Inside root method")
}
```

## 🚀 Getting Started

To start using **Jintonic** in your Android project, simply include the Gradle plugin and the core dependency as shown below.

Add the following to your **module-level `build.gradle.kts`** file:

```kotlin
// MAKE SURE TO ADD JITPACK AND GRADLE PLUGIN REPO INTO SETTINGS.gradle //
plugins {
    id("io.github.daniele-NA.gradle") version "x.z.y"
}

android {
//........
}

dependencies {
    implementation("com.github.daniele-NA:jintonic:vX:Y:Z")  // DO NOT OMIT THE 'v' BEFORE VERSION
}
```

- ✅ Hook and intercept methods with zero boilerplate
- ⚡ Build-time weaving for optimized performance
- 🔧 Full control over **cross-cutting concerns** like logging, validation, and execution policies

---

## ⚠️ Known Limitation: Native Methods


The current version of the Jintonic Gradle plugin performs AspectJ weaving on your Java/Kotlin classes.  
However, **it does not yet properly handle native methods** (e.g., methods implemented in C/C++ via JNI). Attempting to weave aspects into classes with native methods may cause build errors or unexpected behavior.

### Suggested Improvement

A recommended enhancement is to add a feature to **exclude specific packages or classes** from the weaving process.  
This can prevent common errors caused by third-party libraries (such as `com.google.*` or `com.firebase.*`) and allows safer incremental adoption of AspectJ weaving.

Example of a possible configuration once implemented (TODO YET) :

```kotlin
jintonic {

    excludedPackages = listOf("com/google", "com/firebase")

}
```

---

## ⚠️ What I Have to Know

| Annotation      | Info                                                                                                                        | Manifest / Permissions Required                                                                                                                                                                                                                                                                                                                                                                      |
|-----------------|-----------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| MinBattery      | Ensures that the current battery level is greater than or equal to `minOrEqualValue`. Throws exception if not. Range 0-100. | NO PERMISSIONS REQUIRED                                                                                                                                                                                                                                                                                                                                                                              |
| RequireInternet | Checks if there is an active Internet connection before executing the method.Otherwise throws exception.                    | `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>` <br> `<uses-permission android:name="android.permission.INTERNET"/>`                                                                                                                                                                                                                                                     |
| RequireVpn      | Throws an exception if a VPN is not active during method execution.                                                         | `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`                                                                                                                                                                                                                                                                                                                          |
| Timed           | Logs the execution time of the method. Does not modify method execution.                                                    | NO PERMISSIONS REQUIRED                                                                                                                                                                                                                                                                                                                                                                              |
| WithRoot        | Checks if the device is rooted. Executes method based on `forRoot` parameter.                                               | `<queries>` <br> `<package android:name="com.noshufou.android.su" />` <br> `<package android:name="com.thirdparty.superuser" />` <br> `<package android:name="eu.chainfire.supersu" />` <br> `<package android:name="com.koushikdutta.superuser" />` <br> `<package android:name="com.zachspong.temprootremovejb" />` <br> `<package android:name="com.ramdroid.appquarantine" />` <br> `</queries>` |
| SecureWindow    | Allows blocking screenshots and screen recording for the annotated Activity class.                                          | NO PERMISSIONS REQUIRED                                                                                                                                                                                                                                                                                                                                                                              |


---


## 🔍 How it works?

<div align="center">
  <img src="ASSETS/graph.png" alt="Jintonic Graph" width="500" />
</div>

---

## 🤝 Contributing

Contributions are welcome!

```bash
# Clone the repo
git clone https://github.com/daniele-NA/jintonic
cd jintonic

# Build and test locally
./gradlew build
./gradlew test
```

- Open issues for bugs or feature requests
- Fork the repo, make your changes, and submit a PR
- Follow the existing coding style and keep commits clean and descriptive

---

## 📄 License

This project is **open source** under the **MIT license**.  
See the **LICENSE** file for details.
