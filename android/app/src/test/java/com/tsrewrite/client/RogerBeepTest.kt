package com.tsrewrite.client

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RogerBeepTest {
    @Test fun threeAudibleTonesAndSingleCompletion() {
        val beep = RogerBeep()
        var finished = 0
        beep.start { finished++ }
        val frames = (0..24).map { beep.inject(ByteArray(1_920)) }
        assertTrue("tone 1 has audible PCM", frames[0].any { it != 0.toByte() })
        assertTrue("tone 2 has audible PCM", frames[8].any { it != 0.toByte() })
        assertTrue("tone 3 has audible PCM", frames[16].any { it != 0.toByte() })
        assertEquals("callback must fire exactly once", 1, finished)
    }

    @Test fun cancelLeavesNormalMicrophoneSamplesUntouched() {
        val beep = RogerBeep()
        var completed = false
        beep.start { completed = true }
        beep.cancel()
        val original = ByteArray(1_920) { (it % 111).toByte() }
        assertArrayEquals(original, beep.inject(original))
        assertTrue(!completed)
    }

    @Test fun disabledFilterLeavesMicUntouched() {
        val beep = RogerBeep()
        val mic = ByteArray(1_920) { 7.toByte() }
        assertArrayEquals(mic, beep.inject(mic))
    }
}
