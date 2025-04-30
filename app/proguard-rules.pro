# --- Keep annotations (important for Compose, Koin, Retrofit, etc.)
-keepattributes *Annotation*

# --- Jetpack Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# --- Koin (Dependency Injection)
-keep class org.koin.** { *; }

# --- Retrofit + OkHttp
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**

# --- Gson (used with GsonConverterFactory)
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# --- ViewModels
-keep class **ViewModel { *; }

# --- Voyager (navigation library)
-keep class cafe.adriel.voyager.** { *; }
-dontwarn cafe.adriel.voyager.**

# --- Glide (image loading)
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.GeneratedAppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** { *; }
-dontwarn com.bumptech.glide.**

# --- Paging 3 (pagination library)
-keep class androidx.paging.** { *; }
-dontwarn androidx.paging.**

# --- Logging - remove logs from release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** i(...);
    public static *** v(...);
}

# --- Keep constructors for dependency injection and reflection
-keepclassmembers class * {
    public <init>(...);
}
