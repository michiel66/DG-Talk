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

edit(app + "res/values/colors.xml", "#253446", "#132A47")

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
    <path android:fillColor="#31C9F6"
        android:pathData="M36,16 L42,14 L47,34 L41,36 Z" />
    <path android:fillColor="#FFFFFF"
        android:pathData="M42,30 L70,30 C74,30 77,33 77,37 L77,80 C77,84 74,87 70,87 L42,87 C38,87 35,84 35,80 L35,37 C35,33 38,30 42,30 Z" />
    <path android:fillColor="#173A5C"
        android:pathData="M42,39 L70,39 L70,52 L42,52 Z" />
    <path android:fillColor="#31C9F6"
        android:pathData="M47,44 L65,44 L65,47 L47,47 Z" />
    <path android:fillColor="#173A5C"
        android:pathData="M48,58 L64,58 C67,58 69,60 69,63 L69,75 C69,78 67,80 64,80 L48,80 C45,80 43,78 43,75 L43,63 C43,60 45,58 48,58 Z" />
    <path android:fillColor="#31C9F6"
        android:pathData="M52,62 L60,62 L60,76 L52,76 Z" />
    <path android:fillColor="#31C9F6"
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
write("README.md", """# DG-Talk

DG-Talk is een Android TeamSpeak 3-client met een **eigen appnaam, eigen radio-icoon en Heerlen als standaardserver**.

- **Naam:** DG-Talk
- **Standaardserver:** \`heerlen.mijnts3.nl\`
- **Android-pakket:** \`nl.dg.talk\` (installeerbaar naast de originele TS Client)
- **Basis:** [NotMurPh/TeamSpeak-Android-TS3](https://github.com/NotMurPh/TeamSpeak-Android-TS3), release **build-25** op commit \`dafbab92037b3ae135e494319dd4016fdcb1f2b9\`.
- **Interface, PTT, spraak, audiobediening en TeamSpeak 3-protocol:** origineel behouden.

## APK maken
Open de GitHub Actions-tab en voer **DG-Talk Android APK** uit. Download vervolgens het APK-artifact van de geslaagde workflowrun.

## Bronvermelding en licenties
De oorspronkelijke applicatiecode wordt gebruikt volgens de MIT-licentie. De originele \`LICENSE\` en \`THIRD_PARTY.md\` en overige meegeleverde licentiebestanden blijven in deze repository aanwezig. DG-Talk is geen officiële TeamSpeak-app en wordt niet ondersteund door TeamSpeak.

Bij een eigen distributie gelden ook de licenties en merkrechten voor alle meegeleverde afhankelijkheden en assets.
""")
print("DG-Talk branding applied, original microphone/PTT and UI code retained.")
