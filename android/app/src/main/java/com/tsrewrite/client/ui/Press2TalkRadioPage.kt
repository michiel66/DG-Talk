package com.tsrewrite.client.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Headset
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.People
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsrewrite.client.ChatKey
import com.tsrewrite.client.Session
import com.tsrewrite.client.Store
import kotlinx.coroutines.delay
import uniffi.tscore.Status
import uniffi.tscore.VoiceMode

/** Optional Press2Talk radio console. The existing TeamSpeak screen remains available in every theme. */
private val RadioBlue = Color(0xFF39B7FF)
private val RadioGreen = Color(0xFF00E8A5)
private val RadioSurface = Color(0xFF1B2D47)
private val RadioBackground = Color(0xFF071529)
private val RadioText = Color(0xFFECF4FF)
private val RadioDim = Color(0xFFACBBD0)

@Composable
fun Press2TalkRadioPage(
    session: Session,
    store: Store,
    onRecents: () -> Unit,
    onChannels: () -> Unit,
    onChat: () -> Unit,
    onSettings: () -> Unit,
    onClient: (UShort, String) -> Unit,
) {
    val server by session.server.collectAsState()
    val status by session.status.collectAsState()
    val talking by session.talking.collectAsState()
    val unread by session.unread.collectAsState()
    val settings by store.settings.collectAsState()
    val inputMuted by session.inputMuted.collectAsState()
    val outputMuted by session.outputMuted.collectAsState()
    val whisper by session.whisper.collectAsState()
    val avatars by session.icons.avatars.collectAsState()
    val route by session.route.collectAsState()
    var showUsers by remember { mutableStateOf(false) }
    var confirmLeave by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }
    var toggled by remember { mutableStateOf(false) }
    var ping by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            ping = runCatching { session.client.connectionStats().pingMs }.getOrDefault(0f)
            delay(3000)
        }
    }
    // Never leave TX latched when switching themes, disconnecting, or navigating away.
    DisposableEffect(Unit) {
        onDispose { session.setPushToTalk(false, "screen") }
    }

    val ownChannel = server?.ownChannelId
    val members = server?.clients.orEmpty().filter { it.channelId == ownChannel }
    val activeClient = members.firstOrNull { talking.containsKey(it.id) }
    LaunchedEffect(activeClient?.avatarHash) {
        activeClient?.let { session.icons.avatar(it.uid, it.avatarHash) }
    }
    val speaking = activeClient != null
    val myName = server?.clients?.firstOrNull { it.id == server?.ownClientId }?.name ?: "DG-Talk"
    val name = activeClient?.name ?: "Niemand spreekt"
    val channelName = server?.channels?.firstOrNull { it.id == ownChannel }?.name ?: "Kanaal laden..."
    val connected = status.first == Status.CONNECTED && server != null
    val ptt = settings.voiceMode == VoiceMode.PUSH_TO_TALK || whisper != null
    val tx = pressed || toggled || (server?.ownClientId?.let { talking.containsKey(it) } == true)
    val txAllowed = connected && ptt && !inputMuted
    val background = Brush.verticalGradient(listOf(Color(0xFF0D2545), RadioBackground, Color(0xFF081B33)))

    BoxWithConstraints(Modifier.fillMaxSize().background(background).statusBarsPadding().navigationBarsPadding()) {
        val compact = maxHeight < 685.dp
        val narrow = maxWidth < 345.dp
        val roundButton = if (narrow) 43.dp else 50.dp
        val pttHeight = if (compact) 96.dp else 128.dp
        Column(Modifier.fillMaxSize().padding(horizontal = 12.dp).padding(top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 12.dp)) {
            Row(Modifier.fillMaxWidth().height(if (compact) 44.dp else 54.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Icon(Icons.Rounded.AccountCircle, "Gebruikers", tint = RadioText,
                    modifier = Modifier.size(34.dp).clickable { showUsers = true })
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Rounded.GraphicEq, null, tint = RadioBlue, modifier = Modifier.size(30.dp))
                    Text("DG-Talk", color = RadioText, fontSize = if (compact) 24.sp else 28.sp, fontWeight = FontWeight.Bold)
                }
                Box(Modifier.size(14.dp).background(if (connected) RadioGreen else Ts.Red, CircleShape))
            }

            Row(Modifier.fillMaxWidth().height(if (compact) 65.dp else 77.dp)
                .clip(RoundedCornerShape(18.dp)).background(RadioSurface.copy(alpha = 0.90f))
                .border(1.dp, Color(0xFF3F5F83), RoundedCornerShape(18.dp))
                .clickable { onChannels() }.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Headset, null, tint = RadioBlue, modifier = Modifier.size(32.dp))
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(TsText.of(server?.name ?: "TS-HEERLEN"),
                        color = RadioText, fontWeight = FontWeight.Bold, maxLines = 1,
                        overflow = TextOverflow.Ellipsis, fontSize = if (compact) 17.sp else 20.sp)
                    Text((if (connected) "● Verbonden" else "● Verbinden...") +
                        "  |  Gebruikers: " + (server?.clients?.size ?: 0).toString() +
                        "  |  " + ping.toInt().toString() + " ms",
                        color = if (connected) RadioGreen else RadioDim, fontSize = if (narrow) 10.sp else 11.sp, maxLines = 1)
                }
                Icon(Icons.Rounded.People, "Gebruikers tonen", tint = RadioText,
                    modifier = Modifier.size(28.dp).clickable { showUsers = true })
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top) {
                RadioShortcut(Icons.Rounded.History, "Recents", roundButton, onRecents)
                RadioShortcut(Icons.Rounded.List, "Kanalen", roundButton, onChannels)
                RadioShortcut(Icons.Rounded.People, "Gebruikers", roundButton, { showUsers = true }, unread.isNotEmpty())
                RadioShortcut(Icons.Rounded.Bluetooth, "Audio", roundButton, { session.toggleOutput() })
                RadioShortcut(Icons.AutoMirrored.Rounded.Chat, "Chat", roundButton, onChat, unread.isNotEmpty())
                RadioShortcut(Icons.Rounded.Settings, "Instellingen", roundButton, onSettings)
            }

            // Live speaker card. Actual TeamSpeak speaker and avatar, never a hard-coded demonstration name.
            Column(Modifier.fillMaxWidth().weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF17365C), Color(0xFF122743), Color(0xFF192E50))))
                .border(1.dp, Color(0xFF4679AC), RoundedCornerShape(24.dp))
                .padding(if (compact) 10.dp else 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Icon(Icons.Rounded.Headset, null, tint = RadioDim, modifier = Modifier.size(25.dp))
                    Text("Wie praat er?", color = RadioText,
                        fontWeight = FontWeight.Bold, fontSize = if (compact) 20.sp else 23.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    AudioBars(speaking, 58.dp)
                    Box(Modifier.size(if (compact) 84.dp else 110.dp)
                        .border(3.dp, if (speaking) RadioBlue else Color(0xFF54779B), CircleShape)
                        .background(Color(0xFF13243D), CircleShape), contentAlignment = Alignment.Center) {
                        val av = activeClient?.let { avatars[it.avatarHash] }
                        if (av != null) Image(av, "Avatar van " + name,
                            Modifier.fillMaxSize().padding(4.dp).clip(CircleShape))
                        else Icon(Icons.Rounded.AccountCircle, null, tint = RadioDim,
                            modifier = Modifier.size(if (compact) 69.dp else 90.dp))
                    }
                    AudioBars(speaking, 58.dp)
                }
                Text(name, color = RadioText, fontSize = if (compact) 19.sp else 23.sp,
                    fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(channelName, color = RadioDim, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(if (speaking) "● Spreekt nu..." else "● Luisteren", color = if (speaking) RadioGreen else RadioBlue,
                    fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("Aangemeld als " + myName, color = RadioDim, fontSize = 10.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Row(Modifier.fillMaxWidth().height(pttHeight), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f).fillMaxSize()
                    .clip(RoundedCornerShape(65.dp))
                    .background(Brush.verticalGradient(
                        if (tx && txAllowed) listOf(Color(0xFFC92A33), Color(0xFF701B30))
                        else listOf(Color(0xFF174D83), Color(0xFF09203A))))
                    .border(3.dp, if (tx && txAllowed) Color(0xFFFF5F65) else RadioBlue, RoundedCornerShape(65.dp))
                    .then(if (txAllowed) Modifier.pointerInput(settings.pttToggle, inputMuted) {
                        detectTapGestures(onPress = {
                            if (settings.pttToggle) {
                                toggled = !toggled
                                session.setPushToTalk(toggled, "screen")
                            } else {
                                pressed = true
                                session.setPushToTalk(true, "screen")
                                try {
                                    tryAwaitRelease()
                                } finally {
                                    pressed = false
                                    session.setPushToTalk(false, "screen")
                                }
                            }
                        })
                    } else Modifier),
                    contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Icon(if (inputMuted) Icons.Rounded.MicOff else Icons.Rounded.Mic, null,
                            tint = RadioText, modifier = Modifier.size(if (compact) 33.dp else 44.dp))
                        Text(when {
                            !connected -> "Verbinden..."
                            inputMuted -> "Microfoon uit"
                            tx -> "ZENDEN • TX"
                            ptt -> "Push to talk"
                            else -> "Luisteren"
                        }, color = RadioText, fontWeight = FontWeight.Bold,
                            fontSize = if (compact) 17.sp else 20.sp)
                    }
                }
                Spacer(Modifier.size(8.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.size(44.dp).background(Color(0xFFC93740), CircleShape)
                        .border(2.dp, Color(0xFF562129), CircleShape).clickable { confirmLeave = true },
                        contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.CallEnd, "Verbinding verbreken", tint = Color.White,
                            modifier = Modifier.size(23.dp))
                    }
                    Icon(if (outputMuted) Icons.Rounded.Headset else Icons.Rounded.Mic, "Microfoon aan/uit",
                        tint = if (inputMuted) Ts.Red else RadioDim,
                        modifier = Modifier.size(22.dp).clickable { session.toggleInputMuted() })
                }
            }
        }
    }
    if (showUsers) AlertDialog(
        onDismissRequest = { showUsers = false },
        title = { Text("Online gebruikers (" + (server?.clients?.size ?: 0).toString() + ")") },
        text = {
            Column(Modifier.height(320.dp).verticalScroll(androidx.compose.foundation.rememberScrollState())) {
                server?.clients.orEmpty().forEach { user ->
                    Text(user.name, modifier = Modifier.fillMaxWidth()
                        .clickable { showUsers = false; onClient(user.id, user.name) }
                        .padding(vertical = 10.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        },
        confirmButton = { TextButton(onClick = { showUsers = false }) { Text("Sluiten") } },
    )
    if (confirmLeave) AlertDialog(
        onDismissRequest = { confirmLeave = false },
        title = { Text("Verbinding verbreken?") },
        text = { Text("Je verlaat het TeamSpeak-kanaal.") },
        confirmButton = { TextButton(onClick = { confirmLeave = false; session.disconnect() }) { Text("Verbreken") } },
        dismissButton = { TextButton(onClick = { confirmLeave = false }) { Text("Annuleren") } },
    )
}

@Composable
private fun RadioShortcut(icon: ImageVector, label: String, diameter: Dp, onClick: () -> Unit, notify: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(Modifier.size(diameter).clip(CircleShape)
            .background(Brush.verticalGradient(listOf(Color(0xFF244568), Color(0xFF10253D))))
            .border(2.dp, Color(0xFF030A13), CircleShape).clickable(onClick = onClick),
            contentAlignment = Alignment.Center) {
            Icon(icon, label, tint = RadioText, modifier = Modifier.size(25.dp))
            if (notify) Box(Modifier.align(Alignment.TopEnd).size(9.dp).background(Ts.Red, CircleShape))
        }
        Text(label, color = RadioText, fontSize = 9.sp, maxLines = 1)
    }
}

/** Animated only while audio is actually being received; no fake signal strength. */
@Composable
private fun AudioBars(active: Boolean, width: Dp) {
    var beat by remember { mutableStateOf(false) }
    LaunchedEffect(active) {
        beat = false
        while (active) { beat = !beat; delay(260) }
    }
    Canvas(Modifier.size(width, 55.dp)) {
        val count = 7
        val step = size.width / (count + 1)
        for (i in 0 until count) {
            val magnitude = if (active) {
                (if (beat) intArrayOf(0.25f, 0.50f, 0.80f, 1f, 0.72f, 0.48f, 0.26f)
                 else intArrayOf(0.52f, 0.30f, 0.65f, 0.85f, 1f, 0.60f, 0.30f))[i]
            } else 0.12f
            val h = size.height * magnitude
            val x = step * (i + 1)
            drawLine(color = if (active) RadioBlue else Color(0xFF315779),
                start = androidx.compose.ui.geometry.Offset(x, (size.height - h) / 2f),
                end = androidx.compose.ui.geometry.Offset(x, (size.height + h) / 2f),
                strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
        }
    }
}

/** A lightweight theme preview within the existing settings carousel. */
@Composable
fun Press2TalkThemePreview() {
    Column(Modifier.fillMaxSize().background(Color(0xFF071529)).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Text("DG-Talk", color = RadioText, fontSize = 21.sp, fontWeight = FontWeight.Bold)
        Box(Modifier.fillMaxWidth().height(35.dp).clip(RoundedCornerShape(9.dp))
            .background(RadioSurface), contentAlignment = Alignment.Center) {
            Text("● TS-HEERLEN • Verbonden", color = RadioGreen, fontSize = 10.sp)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf(Icons.Rounded.History, Icons.Rounded.List, Icons.Rounded.People,
                Icons.Rounded.Bluetooth, Icons.AutoMirrored.Rounded.Chat, Icons.Rounded.Settings).forEach { icon ->
                Box(Modifier.size(24.dp).background(Color(0xFF234263), CircleShape),
                    contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = RadioText, modifier = Modifier.size(15.dp))
                }
            }
        }
        Column(Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(12.dp))
            .background(RadioSurface).border(1.dp, RadioBlue, RoundedCornerShape(12.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {
            Text("Wie praat er?", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = RadioText)
            Icon(Icons.Rounded.AccountCircle, null, tint = RadioBlue, modifier = Modifier.size(48.dp))
            Text("Marcel (Android)", fontSize = 13.sp, color = RadioText)
            Text("● Spreekt nu...", fontSize = 10.sp, color = RadioGreen)
        }
        Box(Modifier.fillMaxWidth().height(44.dp).clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF174D83)).border(2.dp, RadioBlue, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center) {
            Text("🎙 Push to talk", color = RadioText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
