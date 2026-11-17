#!/usr/bin/env python3
"""Minimal branding changes on the exact NotMurPh TeamSpeak Android TS3 build-25 source."""
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent

def edit(path, old, new):
    file = ROOT / path
    content = file.read_text(encoding="utf-8")
    if old not in content:
        raise RuntimeError(f"Expected original source not found: {path}: {old[:70]!r}")
    file.write_text(content.replace(old, new), encoding="utf-8")

def write(path, content):
    file = ROOT / path
    file.parent.mkdir(parents=True, exist_ok=True)
    file.write_text(content, encoding="utf-8")

app = "android/app/src/main/"
edit("android/app/build.gradle.kts", 'applicationId = "com.tsrewrite.client"', 'applicationId = "nl.dg.talk"')
edit(app + "res/values/strings.xml", "TS Client", "DG-Talk")
edit(app + "java/com/tsrewrite/client/ui/BookmarksScreen.kt", 'Text("TS Client",', 'Text("DG-Talk",')
for file in (
    app + "java/com/tsrewrite/client/ui/SettingsScreen.kt",
    app + "java/com/tsrewrite/client/ui/PatchNotes.kt",
    app + "java/com/tsrewrite/client/Updater.kt",
):
    p = ROOT / file
    p.write_text(p.read_text(encoding="utf-8").replace("TS Client", "DG-Talk"), encoding="utf-8")

store = ROOT / (app + "java/com/tsrewrite/client/Store.kt")
source = store.read_text(encoding="utf-8")
start = source.index("        // Start with ts22.ir in the list")
stop = source.index("        if (_identities.value.isEmpty())", start)
source = source[:start] + '''        // One default Heerlen bookmark on a new DG-Talk installation.
        // Removing the bookmark is respected on subsequent launches.
        if (!prefs.getBoolean("seededDgTalkHeerlen", false)) {
            if (_bookmarks.value.none { it.address.trim().equals("heerlen.mijnts3.nl", ignoreCase = true) }) {
                upsertBookmark(Bookmark(label = "Heerlen", address = "heerlen.mijnts3.nl"))
            }
            prefs.edit().putBoolean("seededDgTalkHeerlen", true).apply()
        }
''' + source[stop:]
source = source.replace('    private fun isTs22(b: Bookmark) = b.address.trim().equals("ts22.ir", ignoreCase = true)\n', "")
source = source.replace('private const val TS22_NAME = "OmeGa | Ts22.IR"\n', "")
store.write_text(source, encoding="utf-8")

edit(app + "java/com/tsrewrite/client/Updater.kt",
     'private const val REPO = "NotMurPh/TeamSpeak-Android-TS3"',
     'private const val REPO = "michiel66/DG-Talk"')

edit(app + "res/values/colors.xml", "#253446", "#0B311B")

write(app + "res/mipmap-anydpi-v26/ic_launcher.xml", """<?xml version="1.0" encoding="utf-8"?>
<!-- DG-Talk's original walkie-talkie logo; keeps all application UI unchanged. -->
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/ic_launcher_background" />
    <foreground android:drawable="@drawable/dg_talk_launcher_foreground" />
    <monochrome android:drawable="@drawable/dg_talk_launcher_monochrome" />
</adaptive-icon>
""")
write(app + "res/drawable/dg_talk_launcher_foreground.xml", """<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp" android:height="108dp"
    android:viewportWidth="108" android:viewportHeight="108">
    <!-- Central radio body, antenna, top speaker and push-to-talk key. -->
    <path android:fillColor="#22C55E"
        android:pathData="M36,16 L42,14 L47,34 L41,36 Z" />
    <path android:fillColor="#FFFFFF"
        android:pathData="M42,30 L70,30 C74,30 77,33 77,37 L77,80 C77,84 74,87 70,87 L42,87 C38,87 35,84 35,80 L35,37 C35,33 38,30 42,30 Z" />
    <path android:fillColor="#14532D"
        android:pathData="M42,39 L70,39 L70,52 L42,52 Z" />
    <path android:fillColor="#22C55E"
        android:pathData="M47,44 L65,44 L65,47 L47,47 Z" />
    <path android:fillColor="#14532D"
        android:pathData="M48,58 L64,58 C67,58 69,60 69,63 L69,75 C69,78 67,80 64,80 L48,80 C45,80 43,78 43,75 L43,63 C43,60 45,58 48,58 Z" />
    <path android:fillColor="#22C55E"
        android:pathData="M52,62 L60,62 L60,76 L52,76 Z" />
    <path android:fillColor="#22C55E"
        android:pathData="M81,46 L85,41 L87,43 L83,49 Z M83,54 L90,54 L90,58 L83,58 Z M81,65 L85,70 L88,68 L83,62 Z" />
</vector>
""")
write(app + "res/drawable/dg_talk_launcher_monochrome.xml", """<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp" android:height="108dp"
    android:viewportWidth="108" android:viewportHeight="108">
    <path android:fillColor="#FFFFFF"
        android:pathData="M36,16 L42,14 L47,34 L41,36 Z M42,30 L70,30 C74,30 77,33 77,37 L77,80 C77,84 74,87 70,87 L42,87 C38,87 35,84 35,80 L35,37 C35,33 38,30 42,30 Z M81,46 L85,41 L87,43 L83,49 Z M83,54 L90,54 L90,58 L83,58 Z M81,65 L85,70 L88,68 L83,62 Z" />
</vector>
""")
edit("tools/release.ps1", '$applicationId = "com.tsrewrite.client"', '$applicationId = "nl.dg.talk"')
edit("tools/release.ps1", '$assetName = "teamspeak-android-arm64-v8a.apk"', '$assetName = "DG-Talk-arm64-v8a.apk"')
# The original source targets Android 8 / ARM64. DG-Talk supports Android 7 / ARMv7 too.
edit("android/app/build.gradle.kts", "minSdk = 26", "minSdk = 24")
edit("android/app/build.gradle.kts",
     'ndk { abiFilters += listOf("arm64-v8a") }',
     'ndk { abiFilters += listOf("arm64-v8a", "armeabi-v7a") }')
edit("android/settings.gradle.kts", 'rootProject.name = "TSClient"', 'rootProject.name = "DG-Talk"')
edit("rust-toolchain.toml", 'targets = ["aarch64-linux-android"]',
     'targets = ["aarch64-linux-android", "armv7-linux-androideabi"]')
edit("tools/build-core.ps1", 'param([string[]]$Abis = @("arm64-v8a"))',
     'param([string[]]$Abis = @("arm64-v8a", "armeabi-v7a"))')
edit("tools/build-core.ps1", 'cargo ndk @targets -P 26 -o',
     'cargo ndk @targets -P 24 -o')

connection = app + "java/com/tsrewrite/client/ConnectionService.kt"
edit(connection, 'return Notification.Builder(this, CHANNEL)',
     'val builder = if (android.os.Build.VERSION.SDK_INT >= 26) Notification.Builder(this, CHANNEL)\n'
     '            else Notification.Builder(this).setPriority(Notification.PRIORITY_LOW)\n'
     '        return builder')
edit(connection,
     'nm.createNotificationChannel(NotificationChannel(CHANNEL, "Connection", NotificationManager.IMPORTANCE_LOW))',
     'if (android.os.Build.VERSION.SDK_INT >= 26) {\n'
     '            nm.createNotificationChannel(NotificationChannel(CHANNEL, "Connection", NotificationManager.IMPORTANCE_LOW))\n'
     '        }')
edit(connection,
     'setContentDescription(R.id.notif_mute, if (mic) "Unmute microphone" else "Mute microphone")',
     'setCharSequence(R.id.notif_mute, "setContentDescription", if (mic) "Unmute microphone" else "Mute microphone")')
edit(connection,
     'setContentDescription(R.id.notif_deafen, if (sound) "Undeafen" else "Deafen")',
     'setCharSequence(R.id.notif_deafen, "setContentDescription", if (sound) "Undeafen" else "Deafen")')

notifier = app + "java/com/tsrewrite/client/Notifier.kt"
edit(notifier, 'nm.createNotificationChannel(NotificationChannel(CHANNEL, "Messages and pokes", NotificationManager.IMPORTANCE_HIGH))',
     'if (android.os.Build.VERSION.SDK_INT >= 26) {\n'
     '            nm.createNotificationChannel(NotificationChannel(CHANNEL, "Messages and pokes", NotificationManager.IMPORTANCE_HIGH))\n'
     '        }')
edit(notifier, 'nm.notify(nextId++, Notification.Builder(context, CHANNEL)',
     'val builder = if (android.os.Build.VERSION.SDK_INT >= 26) Notification.Builder(context, CHANNEL)\n'
     '            else Notification.Builder(context).setPriority(Notification.PRIORITY_HIGH)\n'
     '        nm.notify(nextId++, builder')

overlay = app + "java/com/tsrewrite/client/FloatingControls.kt"
edit(overlay,
     '    private fun overlayParams(w: Int, h: Int, touchable: Boolean) = WindowManager.LayoutParams(',
     '    @Suppress("DEPRECATION")\n'
     '    private fun overlayParams(w: Int, h: Int, touchable: Boolean) = WindowManager.LayoutParams(')
edit(overlay, 'w, h, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,',
     'w, h,\n'
     '        if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY\n'
     '        else WindowManager.LayoutParams.TYPE_PHONE,')

# README.md is intentionally maintained separately, without being overwritten by branding.

print("DG-Talk branding applied, original microphone/PTT and UI code retained.")
