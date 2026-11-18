# DG-Talk overstap van debug build #32 naar vaste APK-sleutel

**Huidige gebruikers:** GitHub Actions `DG-Talk Android APK #32`, commit `df456ef`, appversie `1.2.0`, versiecode `1030`. Dit is een debug-APK, ondertekend op een tijdelijke GitHub Actions-runner.

## Cruciale waarschuwing

De oorspronkelijke **privésleutel** van de debug-APK is niet beschikbaar uit het APK-bestand. Een nieuw ondertekende APK kan daarom waarschijnlijk **niet over de huidige debug-installatie** geïnstalleerd worden. Gebruikers moeten **eenmalig** hun gegevens exporteren, de oude app verwijderen en daarna de officieel ondertekende versie installeren. Vanaf dat moment kunnen latere builds die dezelfde permanente sleutel gebruiken wél in-place worden bijgewerkt.

**Verwijder DG-Talk nooit vóór het maken van een back-up.** In de huidige app: Instellingen > Export backup; bewaar het JSON-bestand buiten DG-Talk. De back-up bevat instellingen, bookmarks en TeamSpeak-identiteiten. Een lokaal geïmporteerde avatarfoto kan mogelijk opnieuw moeten worden gekozen.

## Nieuwe sleutel en testbuild

- De eigenaar maakt één permanente release-ondertekeningssleutel (PKCS12 keystore).
- **Nooit in GitHub-code, issues, Releases of Actions-logs opslaan.**
- Bewaar een offline reservekopie; bij sleutelverlies kunnen gebruikers wederom niet in-place updaten.
- Maak vier GitHub Actions repository secrets aan via [Settings > Secrets and variables > Actions](https://github.com/michiel66/DG-Talk/settings/secrets/actions):
  - `ANDROID_KEYSTORE_BASE64`: de complete keystore als Base64.
  - `ANDROID_KEYSTORE_PASSWORD`: het wachtwoord van de keystore.
  - `ANDROID_KEY_ALIAS`: de sleutelalias.
  - `ANDROID_KEY_PASSWORD`: het sleutelwachtwoord.
- Start **handmatig** [DG-Talk Vaste Sleutel Test APK](https://github.com/michiel66/DG-Talk/actions/workflows/dg-talk-signed-test.yml).
- Download de test-APK uit de artifact-download, controleer het toestel en importeer de back-up na de eenmalige overstap.
- Dit testproces publiceert **geen** GitHub Release en stuurt **geen** updates naar gebruikers.

## Pas na de test: publiceren

[DG-Talk Goedgekeurde Update Publiceren](https://github.com/michiel66/DG-Talk/actions/workflows/dg-talk-publish-update.yml) vereist handmatige toestemming en het runnummer van de goedgekeurde testbuild. Dit proces downloadt **exact de geteste APK** en controleert package ID, versie, Android-minimum, SHA-256 en **dezelfde vaste ondertekeningssleutel**. Alleen dan publiceert het een GitHub Release met `update.json`. De app zoekt `releases/latest/download/update.json`.

De APK ondersteunt Android API 24+ en ARM32/ARM64. De update-metadata bevat `abis: ["arm64-v8a", "armeabi-v7a"]` voor compatibiliteit met beide.

## Later

Voor toekomstige officiële versies moet het releaseversienummer en `versionCode` stijgen, dezelfde `applicationId` `nl.dg.talk` behouden blijven en **dezelfde vaste keystore** gebruikt worden. De build en publicatieworkflows zijn voor eerste versie `1.2.1` ingericht en moeten voor een volgende versie samen aangepast of geparametriseerd worden. Doe dit pas na de test.

Het bestaande debug-buildproces is onafhankelijk van de permanente releasesleutel. Download voor officiële updates alleen APK's uit de geteste signed workflow of een goedgekeurde GitHub Release, **niet** uit `DG-Talk Android APK` debug-artifacts.
