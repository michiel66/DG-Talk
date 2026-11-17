package com.tsrewrite.client

import kotlin.math.PI
import kotlin.math.sin

/**
 * 48 kHz, mono, 16-bit PCM roger sign-off. Mixed into the EXISTING microphone-to-TS3
 * capture stream while the Rust client still has PTT open. Other channel users hear it.
 * Nothing is sent to the channel when disabled.
 */
class RogerBeep {
    private val lock = Any()
    private var samplePosition = 0
    private var active = false
    private var done: (() -> Unit)? = null

    fun start(onFinished: () -> Unit) = synchronized(lock) {
        samplePosition = 0
        done = onFinished
        active = true
    }

    fun cancel() = synchronized(lock) {
        active = false
        done = null
        samplePosition = 0
    }

    /** Called only from the dedicated 20-ms microphone thread. */
    fun inject(pcm: ByteArray): ByteArray {
        var finished: (() -> Unit)? = null
        val result = synchronized(lock) {
            if (!active) return pcm
            val output = ByteArray(pcm.size)
            for (i in 0 until pcm.size / 2) {
                val sample = sampleAt(samplePosition++)
                output[i * 2] = (sample.toInt() and 0xff).toByte()
                output[i * 2 + 1] = (sample.toInt() shr 8).toByte()
            }
            if (samplePosition >= TOTAL_SAMPLES) {
                active = false
                finished = done
                done = null
            }
            output
        }
        finished?.invoke()
        return result
    }

    companion object {
        private const val SAMPLE_RATE = 48_000
        private const val TONE_SAMPLES = 5_760 // 120 ms
        private const val GAP_SAMPLES = 1_440  // 30 ms
        private const val STEP = TONE_SAMPLES + GAP_SAMPLES
        private const val TOTAL_SAMPLES = 3 * STEP
        private val FREQUENCIES = doubleArrayOf(990.0, 1320.0, 1620.0)

        private fun sampleAt(index: Int): Short {
            val tone = index / STEP
            val local = index % STEP
            if (tone !in 0..2 || local >= TONE_SAMPLES) return 0
            // An 8-ms envelope prevents clicks when each tone switches.
            val ramp = minOf(1.0, local / 384.0, (TONE_SAMPLES - local) / 384.0).coerceAtLeast(0.0)
            val value = sin(2.0 * PI * FREQUENCIES[tone] * local / SAMPLE_RATE)
            return (value * 6500.0 * ramp).toInt().toShort()
        }
    }
}
