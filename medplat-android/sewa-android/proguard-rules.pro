# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.google.android.gms.internal.** { *; }
-keep class com.argusoft.sewa.android.app.model.** { *; }
-keep class com.argusoft.sewa.android.app.databean.** { *; }
#-keep class com.argusoft.sewa.android.app.activity.** { *; }
#-keep class com.argusoft.sewa.android.app.SewaApplication_

-dontwarn org.androidannotations.**
-dontwarn com.fasterxml.**


########################### Material Design ###########################
-keep class com.google.android.material.** { *; }

-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
# AndroidAnnotations

#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontpreverify
#-verbose
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
#-keepclasseswithmembernames class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembernames class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

########################### Debug Log ###########################
# This allows proguard to strip isLoggable() blocks containing only <=INFO log
# code from release builds.
-assumenosideeffects class com.argusoft.sewa.android.app.util.Log {
  static *** i(...);
  static *** d(...);
  static *** v(...);
  static *** isLoggable(...);
}
#-maximumremovedandroidloglevel 4

########################### OrmLite ###########################
# ormlite
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keepclassmembers class * {
  public <init>(android.content.Context);
}

# Keep the helper class and its constructor
-keep class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
-keepclassmembers class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper {
  public <init>(android.content.Context);
}

# Keep the annotations
-keepattributes *Annotation*

# Keep all model classes that are used by OrmLite
# Also keep their field names and the constructor
-keep @com.j256.ormlite.table.DatabaseTable class * {
    @com.j256.ormlite.field.DatabaseField <fields>;
    @com.j256.ormlite.field.ForeignCollectionField <fields>;
    # Add the ormlite field annotations that your model uses here
    <init>();
}

########################### Retrofit ###########################
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

########################### Gson ###########################
-dontwarn javax.annotation.**

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

##---------------End: proguard configuration for Gson  ----------

########################### joda-time ###########################
# All the resources are retrieved via reflection, so we need to make sure we keep them
-keep class net.danlew.android.joda.R$raw { *; }

# These aren't necessary if including joda-convert, but
# most people aren't, so it's helpful to include it.
-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString

# Joda classes use the writeObject special method for Serializable, so
# if it's stripped, we'll run into NotSerializableExceptions.
# https://www.guardsquare.com/en/products/proguard/manual/examples#serializable
-keepnames class org.joda.** implements java.io.Serializable
-keepclassmembers class org.joda.** implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

###########################  Crashlytics ###########################
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-assumenosideeffects class com.argusoft.sewa.android.app.util.Log {
    public static *** i(...);
    public static *** d(...);
    public static *** v(...);
    public static *** e(...);
}

################### zxing-android-embedded ############################
# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

# Optimizations: If you don't want to optimize, use the
# proguard-android.txt configuration file instead of this one, which
# turns off the optimization flags.  Adding optimization introduces
# certain risks, since for example not all optimizations performed by
# ProGuard works on all versions of Dalvik.  The following flags turn
# off various optimizations known to have issues, but the list may not
# be complete or up to date. (The "arithmetic" optimization can be
# used if you are only targeting Android 2.0 or later.)  Make sure you
# test thoroughly if you go this route.
#-optimizations !code/simplification/cast,!code/allocation/*,!field/*,!class/merging/*
#-optimizationpasses 5
#-allowaccessmodification
#-dontpreverify

# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).

#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-verbose

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# ADDED
-keep class com.google.zxing.client.android.camera.open.**
#-keep class com.google.zxing.client.android.camera.exposure.**
-keep class com.google.zxing.client.android.common.executor.**

# ADDED
#-dontobfuscate
#-useuniqueclassmembernames

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
#-keepclasseswithmembernames class * {
#    native <methods>;
#}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
#-dontwarn android.support.**


########################### pdfview ###########################
-keep class com.shockwave.**