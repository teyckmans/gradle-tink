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

## TODO 
- document tasks
- document embedding