# Gradle-Tink

## What
Gradle plugin with tasks to perform encrypt/decrypt operations using the [Google Tink](https://github.com/google/tink) library.

The Gradle-Tink project has two modules:
- Gradle plugin providing basic tasks that can be used as is.
- Core module for embedding in other plugins.

## Why
Make it easy to securely work with sensitive information. Either by using this plugin directly or using the core module 
to create plugins that deal with sensitive information correctly. All too often this is overlooked which makes using 
plugins in a security sensitive environment harder than it needs to be.



TODO 
- release to gradle plugin portal / jcenter
- document tasks
- document embedding