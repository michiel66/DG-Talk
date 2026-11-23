package com.tsrewrite.client.ui

import androidx.compose.ui.graphics.Color

/**
 * Visual palettes for the optional Press2Talk radio console.
 * No session, microphone, server or PTT behaviour is changed by a skin.
 */
internal data class RadioSkin(
    val accent: Color,
    val secondary: Color,
    val connected: Color,
    val surface: Color,
    val outline: Color,
    val text: Color,
    val mutedText: Color,
    val bgTop: Color,
    val bgMiddle: Color,
    val bgBottom: Color,
    val cardTop: Color,
    val cardMiddle: Color,
    val cardBottom: Color,
    val cardOutline: Color,
    val avatarBackground: Color,
    val idleRing: Color,
    val buttonTop: Color,
    val buttonBottom: Color,
    val buttonOutline: Color,
    val pttTop: Color,
    val pttBottom: Color,
    val pttRing: Color,
    val idleBars: Color,
    val christmas: Boolean = false,
    val title: String = "DG-Talk",
)

/** Matching the previously released App 2 palette exactly for backwards compatibility. */
internal fun radioSkin(themeName: String): RadioSkin = when (themeName) {
    "responder" -> RadioSkin(
        accent = Color(0xFFFF334A), secondary = Color(0xFFFF5969),
        connected = Color(0xFF45E87C),
        surface = Color(0xFF25151B), outline = Color(0xFF913342),
        text = Color(0xFFF9F5F5), mutedText = Color(0xFFBCA8AD),
        bgTop = Color(0xFF211018), bgMiddle = Color(0xFF090B10),
        bgBottom = Color(0xFF160B13),
        cardTop = Color(0xFF38111C), cardMiddle = Color(0xFF170D14),
        cardBottom = Color(0xFF290C18), cardOutline = Color(0xFFCE3045),
        avatarBackground = Color(0xFF1A1018), idleRing = Color(0xFF81313E),
        buttonTop = Color(0xFF522530), buttonBottom = Color(0xFF1C0F16),
        buttonOutline = Color(0xFF080508),
        pttTop = Color(0xFFBA2038), pttBottom = Color(0xFF5F081A),
        pttRing = Color(0xFFFF4D58), idleBars = Color(0xFF702331),
        title = "DG-Talk",
    )
    "cyber" -> RadioSkin(
        accent = Color(0xFFC84BFF), secondary = Color(0xFF1BE3FF),
        connected = Color(0xFF29F5B0),
        surface = Color(0xFF21143D), outline = Color(0xFF794DD0),
        text = Color(0xFFF4F1FF), mutedText = Color(0xFFBEB1D9),
        bgTop = Color(0xFF180E37), bgMiddle = Color(0xFF08091E),
        bgBottom = Color(0xFF0A1D36),
        cardTop = Color(0xFF2B1249), cardMiddle = Color(0xFF151331),
        cardBottom = Color(0xFF0F2A43), cardOutline = Color(0xFFB851F0),
        avatarBackground = Color(0xFF151B39), idleRing = Color(0xFF366A9E),
        buttonTop = Color(0xFF37205F), buttonBottom = Color(0xFF0C2447),
        buttonOutline = Color(0xFF0D0A1A),
        pttTop = Color(0xFF671DB1), pttBottom = Color(0xFF0C5266),
        pttRing = Color(0xFF35E7FE), idleBars = Color(0xFF394F84),
        title = "DG-Talk",
    )
    "christmasradio" -> RadioSkin(
        accent = Color(0xFFFFCB5F), secondary = Color(0xFFFF5863),
        connected = Color(0xFF53E992),
        surface = Color(0xFF4F1021), outline = Color(0xFFC96D58),
        text = Color(0xFFFFF6E9), mutedText = Color(0xFFF1D9CC),
        bgTop = Color(0xFF450C1F), bgMiddle = Color(0xFF190915),
        bgBottom = Color(0xFF351021),
        cardTop = Color(0xFF4D102B), cardMiddle = Color(0xFF231029),
        cardBottom = Color(0xFF50132D), cardOutline = Color(0xFFFFCF65),
        avatarBackground = Color(0xFF301223), idleRing = Color(0xFFD26866),
        buttonTop = Color(0xFF823043), buttonBottom = Color(0xFF3B1025),
        buttonOutline = Color(0xFFFFC963),
        pttTop = Color(0xFFD52C43), pttBottom = Color(0xFF7B1028),
        pttRing = Color(0xFFFFCE69), idleBars = Color(0xFF8C3B52),
        christmas = true, title = "DG-Talk",
    )
    else -> RadioSkin(
        accent = Color(0xFF39B7FF), secondary = Color(0xFF39B7FF),
        connected = Color(0xFF00E8A5),
        surface = Color(0xFF1B2D47), outline = Color(0xFF3F5F83),
        text = Color(0xFFECF4FF), mutedText = Color(0xFFACBBD0),
        bgTop = Color(0xFF0D2545), bgMiddle = Color(0xFF071529),
        bgBottom = Color(0xFF081B33),
        cardTop = Color(0xFF17365C), cardMiddle = Color(0xFF122743),
        cardBottom = Color(0xFF192E50), cardOutline = Color(0xFF4679AC),
        avatarBackground = Color(0xFF13243D), idleRing = Color(0xFF54779B),
        buttonTop = Color(0xFF244568), buttonBottom = Color(0xFF10253D),
        buttonOutline = Color(0xFF030A13),
        pttTop = Color(0xFF174D83), pttBottom = Color(0xFF09203A),
        pttRing = Color(0xFF39B7FF), idleBars = Color(0xFF315779),
    )
}
