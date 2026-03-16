# --- Keep annotations (important for Compose, Koin, Retrofit, etc.)
-keepattributes *Annotation*
-keepattributes Signature

# --- Jetpack Compose
-keep class androidx.compose.runtime.** { *; }
-dontwarn androidx.compose.**

# --- Koin (Dependency Injection)
-keep class org.koin.** { *; }

# --- Retrofit + OkHttp
-keep,allowobfuscation interface retrofit2.Call
-keep,allowobfuscation interface retrofit2.Response
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**

# --- Gson (used with GsonConverterFactory)
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# --- ViewModels
-keep class * extends androidx.lifecycle.ViewModel { *; }

# --- Navigation Compose type-safe routes
-keepclassmembers class com.example.nbaapp.ui.navigation.Routes$* { *; }
-keep class com.example.nbaapp.ui.navigation.** { *; }

# --- Kotlinx Serialization
-keepattributes InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keep,includedescriptorclasses class com.example.nbaapp.**$$serializer { *; }
-keepclassmembers class com.example.nbaapp.** { *** Companion; }

# --- Glide (image loading)
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.GeneratedAppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** { *; }
-dontwarn com.bumptech.glide.**

# --- Room (offline caching)
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# --- Paging 3 (pagination library)
-keep class androidx.paging.** { *; }
-dontwarn androidx.paging.**

# --- Data models (API DTOs - needed for Gson serialization)
-keepclassmembers class com.example.nbaapp.data.model.** {
    <fields>;
    <init>(...);
}

# --- Domain models
-keepclassmembers class com.example.nbaapp.domain.model.** {
    <fields>;
    <init>(...);
}

# --- Logging - remove logs from release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** i(...);
    public static *** v(...);
}
