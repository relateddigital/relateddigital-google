<p align="center">
  <a target="_blank" rel="noopener noreferrer" href="https://github.com/relateddigital/relateddigital-google"><img src="https://github.com/relateddigital/relateddigital-google/blob/master/app/relateddigital.png" alt="Related Digital Google Library" width="500" style="max-width:100%;"></a>
</p>

# Latest Version 

***Feb 2, 2025*** - [v1.4.2](https://github.com/relateddigital/relateddigital-google/releases)

# About SDK

This SDK has been developed to be the Android/Google client for the Related Digital services.

It is written in Kotlin.

You can find the documents about how to use the SDK on the following links:

[documents-eng](https://relateddigital.atlassian.net/wiki/spaces/KB/pages/2207809583/Setup)

[documents-tr](https://relateddigital.atlassian.net/wiki/spaces/RMCKBT/pages/2204827661/Kurulum)


# Installation

Related Digital Android SDK requires minimum API level 21.

Please, add Maven jitpack repository into your project-level build.gradle file as shown below:
 
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
 ```

Please, add the dependency of relateddigital-google library into your module-level build.gradle file as shown below:

 ```gradle
implementation 'com.github.relateddigital:relateddigital-google:1.5.7'
 ```

# Licences


[Related Digital](https://www.relateddigital.com/)
