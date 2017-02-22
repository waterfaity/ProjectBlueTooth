# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# -optimizationpasses 5
 #-dontusemixedcaseclassnames
 #-dontskipnonpubliclibraryclasses
 #-dontpreverify
 #-verbose
#-keep class com.waterfairy.widget.**{*;}#自定义控件
#-dontwarn com.squareup.**
#-keep class com.squareup.** { *;}
#-dontwarn okio.**
#-keep class com.google.**{*;}
#-keep class com.waterfairy.okhttp.**{*;}
#-keep class android.support.**{*;}
#-keepclassmembernames class com.waterfairy.tool.update.we.**{*;}


