# Gradle-Tink

![GitHub tag (latest SemVer)](https://img.shields.io/github/tag/teyckmans/gradle-tink.svg?label=latest%20release)
[![teyckmans](https://circleci.com/gh/teyckmans/gradle-tink.svg?style=svg)](https://app.circleci.com/pipelines/github/teyckmans/gradle-tink)

## What
Gradle plugin with tasks to perform encrypt/decrypt operations using the [Google Tink](https://github.com/google/tink) library.

The Gradle-Tink project has two modules:
- Gradle plugin providing basic tasks that can be used as is.
- Core module for embedding in other plugins.

## Why
Make it easy to securely work with sensitive information. Either by using this plugin directly or using the core module 
to create plugins that deal with sensitive information correctly. All too often this is overlooked which makes using 
plugins in a security sensitive environment harder than it needs to be.

## How-to
In your build.gradle.kts file apply the plugin to get started.

> NOTE: The plugin is pending approval at the moment so this wont work yet.
```kotlin
plugins {
    id("dev.rigel.gradle.tink") version "0.1.1" // TODO use latest version
}

repositories {
    jcenter()
}
```

### Available tasks
Short overview:
- createKeyset: Create a new Google Tink keyset, needed to perform encrypt/decrypt operations.
- encryptFile: Encrypt a complete file.
- decryptFile: Decrypt a complete file.
- encryptString: Encrypt a string.
- decryptString: Decrypt a string.
- encryptProperty: Encrypt a property value in a properties file.
- decryptProperty: Decrypt a property value in a properties file.

With the following command you can list the task this plugin makes available. 
```shell script
$ ./gradlew tasks --group tink
```

Using the following command you can check what options a specific task has.

In the example bellow we 
```shell script
$ ./gradlew help --task createKeyset
```
The command output shows what options are available and shows the description.
```
> Task :help
Detailed task information for createKeyset

Path
     :createKeyset

Type
     CreateKeySetTask (dev.rigel.gradle.tink.tasks.CreateKeySetTask)

Options
     --keyStorageFormat     Options are json or binary (default: json)

     --keyTemplate     Keyset template name (default: AES256_CTR_HMAC_SHA256)

     --keysetFile     Filename to store the generation keyset to.

Description
     -

Group
     tink

BUILD SUCCESSFUL in 581ms
1 actionable task: 1 executed
```

## Plugin Configuration

If you know Tink and the KeyTemplate that you are using is not known by the plugin you need to create an implementation of dev.rigel.gradle.tink.core.TinkKeyTemplate.
 
Register it in your build.gradle.kts file like this:
```kotlin
tink {
    register(MyCustomTinkKeyTemplate())
}
```

Now you can use it by providing the `--keyTemplate` option on the plugin tasks. 

## Embedding

The plugin delegates all tasks to the dev.rigel.gradle.tink.core.KeysetSupport class which has all the utility methods to perform encrypt/decrypt operations.

The specifics of how to perform the operations with the [Google Tink](https://github.com/google/tink) library can be found in implementation of dev.rigel.gradle.tink.core.TinkKeyTemplate.
A TinkKeyTemplate gives a name to a specific KeyTemplate in Tink.

You can use your own TinkKeyTemplate(s) by registering them using dev.rigel.gradle.tink.core.TinkKeyTemplates.register.  

```kotlin
dependencies {
    implementation("dev.rigel.gradle.tink:core:0.1.1")
}
```