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

## Nieuwe functies in DG-Talk 1.2

Onder **Instellingen** zijn drie afzonderlijke onderdelen toegevoegd:

1. **Thema kiezen:** een veegbare carrousel met miniaturen van de eerder ontworpen schermen **Standaard, Kerst, 27MC en Space**. De eerste installatie begint met Standaard. Een ander gekozen thema wordt opgeslagen; alle bestaande schermen en knoppen blijven op dezelfde plek. De alternatieve stijlen veranderen de kleuren en normale kanaalicoontjes.
2. **Externe PTT:** ingeschakeld met een schakelaar. Kies *Fysieke knop instellen* en druk op de zijknop van de Android-portofoon of een gekoppelde Bluetooth-knop. De toegewezen Android-toetscode wordt opgeslagen. Indrukken = TX; loslaten = RX. De gewone PTT op het scherm en bestaande volume-/headsetknoppen blijven bestaan.
3. **Rogerpiep – 3 tonen:** aan/uit en *Test 3-tonige piep in kanaal*. Bij de laatste PTT-loslating wordt via de originele 48-kHz capture-stream een signaal met drie tonen (990, 1320, 1620 Hz) verstuurd **terwijl de TeamSpeak-zender nog openstaat**. Andere deelnemers horen de piep; de gebruiker hoort niet noodzakelijk zijn eigen zendgeluid terug. De testknop vereist een actieve verbinding, PTT-modus en een niet-gedempte microfoon.

**Compatibiliteit externe PTT:** gewone `KeyEvent`-toetsen (in de voorgrond) en via de Android MediaSession afgeleverde Bluetooth-mediatoetsen zijn ondersteund. Sommige portofoons versturen een **fabrikantspecifiek PTT-broadcastintent** of laten toetsen uitsluitend aan hun eigen app zien. Die knoppen vereisen ondersteuning voor de specifieke fabrikant en zijn niet gegarandeerd. Met de schakelaar uit wordt geen externe toets overgenomen.

De rogerpiep is optioneel en standaard uit. Een extra veiligheidscontrole sluit TX als een audiostream tijdens de piep wegvalt. Voor een release moet de werking van audio, toetsen en achtergronddiensten nog op echte toestellen worden getest.

## APK downloaden en bouwen

Ga naar **Actions → DG-Talk Android APK**. Een geslaagde build levert bij *Artifacts* het bestand **DG-Talk-Android7-32-64-APK** op. Download het ZIP-bestand en pak de APK daaruit uit. De workflow controleert de Android-minimumversie en de spraakbibliotheken voor beide CPU-architecturen.

**Let op bij bestaande installaties:** de APK in Actions is een debug-build. Debug-ondertekening kan per build verschillen. Android kan een nieuwe APK daarom weigeren als update op een bestaande installatie. Bewaar eerst via Instellingen een back-up van je DG-Talk-gegevens voordat je eventueel de bestaande app verwijdert. Voor betrouwbare updates zonder herinstallatie moet een blijvende, privé bewaarde Android-ondertekeningssleutel worden ingesteld.

De wijzigingen voor oudere Android-telefoons zijn gericht op compatibiliteit; werking op een specifieke telefoon, inclusief microfoon, Bluetooth, overlay en notificaties, vraagt ook een praktijktest.

## Herkomst en licenties

Oorspronkelijke broncode: [NotMurPh/TeamSpeak-Android-TS3 – build-25](https://github.com/NotMurPh/TeamSpeak-Android-TS3/tree/dafbab92037b3ae135e494319dd4016fdcb1f2b9), commit `dafbab92037b3ae135e494319dd4016fdcb1f2b9`.

De oorspronkelijke `LICENSE`, `THIRD_PARTY.md` en overige licentiemeldingen blijven aanwezig. DG-Talk is geen officiële TeamSpeak-app en wordt niet door TeamSpeak ondersteund.
