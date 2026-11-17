# DG-Talk

DG-Talk is een Android TeamSpeak 3-client, gebaseerd op de werkende **build-25** van [NotMurPh/TeamSpeak-Android-TS3](https://github.com/NotMurPh/TeamSpeak-Android-TS3). De oorspronkelijke interface en de TeamSpeak-audio/PTT-functionaliteit blijven behouden.

## DG-Talk

- **Appnaam:** DG-Talk
- **Server bij eerste installatie:** `heerlen.mijnts3.nl`
- **Pictogram:** groen portofoonicoon
- **Android-pakket:** `nl.dg.talk`
- **Minimale Android-versie:** Android 7.0 (API 24)
- **CPU:** 32-bit ARM (`armeabi-v7a`) en 64-bit ARM (`arm64-v8a`)

De installaties voor beide architecturen worden samen aangeboden in één **universele APK**. De native Rust-TeamSpeak-spraakengine en JNA moeten in beide architecturen in de APK aanwezig zijn. Een 32-bit-only toestel kan geen 64-bit-bibliotheek laden.

## APK downloaden en bouwen

Ga naar **Actions → DG-Talk Android APK**. Een geslaagde build levert bij *Artifacts* het bestand **DG-Talk-Android7-32-64-APK** op. Download het ZIP-bestand en pak de APK daaruit uit. De workflow controleert de Android-minimumversie en de spraakbibliotheken voor beide CPU-architecturen.

**Let op bij bestaande installaties:** de APK in Actions is een debug-build. Debug-ondertekening kan per build verschillen. Android kan een nieuwe APK daarom weigeren als update op een bestaande installatie. Bewaar eerst via Instellingen een back-up van je DG-Talk-gegevens voordat je eventueel de bestaande app verwijdert. Voor betrouwbare updates zonder herinstallatie moet een blijvende, privé bewaarde Android-ondertekeningssleutel worden ingesteld.

De wijzigingen voor oudere Android-telefoons zijn gericht op compatibiliteit; werking op een specifieke telefoon, inclusief microfoon, Bluetooth, overlay en notificaties, vraagt ook een praktijktest.

## Herkomst en licenties

Oorspronkelijke broncode: [NotMurPh/TeamSpeak-Android-TS3 – build-25](https://github.com/NotMurPh/TeamSpeak-Android-TS3/tree/dafbab92037b3ae135e494319dd4016fdcb1f2b9), commit `dafbab92037b3ae135e494319dd4016fdcb1f2b9`.

De oorspronkelijke `LICENSE`, `THIRD_PARTY.md` en overige licentiemeldingen blijven aanwezig. DG-Talk is geen officiële TeamSpeak-app en wordt niet door TeamSpeak ondersteund.
