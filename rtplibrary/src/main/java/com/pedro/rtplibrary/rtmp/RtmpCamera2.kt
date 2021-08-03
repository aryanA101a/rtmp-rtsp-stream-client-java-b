package com.pedro.rtplibrary.rtmp

import android.content.Context
import android.os.Build
import com.pedro.rtplibrary.base.Camera2Base
import net.ossrs.rtmp.SrsFlvMuxer
import android.view.SurfaceView
import net.ossrs.rtmp.ConnectCheckerRtmp
import android.view.TextureView
import com.pedro.rtplibrary.view.OpenGlView
import com.pedro.rtplibrary.view.LightOpenGlView
import android.media.MediaCodec
import androidx.annotation.RequiresApi
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.util.ArrayList

/**
 * More documentation see:
 * [com.pedro.rtplibrary.base.Camera2Base]
 *
 * Created by pedro on 6/07/17.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class RtmpCamera2 : Camera2Base {
    private val srsFlvMuxer = ArrayList<SrsFlvMuxer>()
    var noOfStreams: Int

    @Deprecated(
        """This view produce rotations problems and could be unsupported in future versions.
    Use {@link Camera2Base#Camera2Base(OpenGlView)} or {@link Camera2Base#Camera2Base(LightOpenGlView)}
    instead."""
    )
    constructor(
        surfaceView: SurfaceView?,
        connectChecker: ConnectCheckerRtmp?,
        noOfStreams: Int
    ) : super(surfaceView) {
        this.noOfStreams = noOfStreams
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer.add(SrsFlvMuxer(connectChecker))
        }
    }

    @Deprecated(
        """This view produce rotations problems and could be unsupported in future versions.
    Use {@link Camera2Base#Camera2Base(OpenGlView)} or {@link Camera2Base#Camera2Base(LightOpenGlView)}
    instead."""
    )
    constructor(
        textureView: TextureView?,
        connectChecker: ConnectCheckerRtmp?,
        noOfStreams: Int
    ) : super(textureView) {
        this.noOfStreams = noOfStreams
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer.add(SrsFlvMuxer(connectChecker))
        }
    }

    constructor(
        openGlView: OpenGlView?,
        connectChecker: ConnectCheckerRtmp?,
        noOfStreams: Int
    ) : super(openGlView) {
        this.noOfStreams = noOfStreams
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer.add(SrsFlvMuxer(connectChecker))
        }
    }

    constructor(
        lightOpenGlView: LightOpenGlView?,
        connectChecker: ConnectCheckerRtmp?,
        noOfStreams: Int
    ) : super(lightOpenGlView) {
        this.noOfStreams = noOfStreams
        println(noOfStreams)
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer.add(SrsFlvMuxer(connectChecker))
        }
    }

    constructor(
        context: Context?,
        useOpengl: Boolean,
        connectChecker: ConnectCheckerRtmp?,
        noOfStreams: Int
    ) : super(context, useOpengl) {
        this.noOfStreams = noOfStreams
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer.add(SrsFlvMuxer(connectChecker))
        }
    }

    /**
     * H264 profile.
     *
     * @param profileIop Could be ProfileIop.BASELINE or ProfileIop.CONSTRAINED
     */
    fun setProfileIop(profileIop: Byte) {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].setProfileIop(profileIop)
        }
    }

    @Throws(RuntimeException::class)
    override fun resizeCache(newSize: Int) {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].resizeFlvTagCache(newSize)
        }
    }

    override fun getCacheSize(): Int {
        return srsFlvMuxer[0].flvTagCacheSize
    }

    override fun getSentAudioFrames(): Long {
        var saf: Long = 0
        for (i in 0 until noOfStreams - 1) {
            saf = +srsFlvMuxer[i].sentAudioFrames
        }
        return saf
    }

    override fun getSentVideoFrames(): Long {
        var svf: Long = 0
        for (i in 0 until noOfStreams - 1) {
            svf = +srsFlvMuxer[i].sentVideoFrames
        }
        return svf
    }

    override fun getDroppedAudioFrames(): Long {
        var daf: Long = 0
        for (i in 0 until noOfStreams - 1) {
            daf = +srsFlvMuxer[i].droppedAudioFrames
        }
        return daf
    }

    override fun getDroppedVideoFrames(): Long {
        var dvf: Long = 0
        for (i in 0 until noOfStreams - 1) {
            dvf = +srsFlvMuxer[i].droppedVideoFrames
        }
        return dvf
    }

    override fun resetSentAudioFrames() {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].resetSentAudioFrames()
        }
    }

    override fun resetSentVideoFrames() {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].resetSentVideoFrames()
        }
    }

    override fun resetDroppedAudioFrames() {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].resetDroppedAudioFrames()
        }
    }

    override fun resetDroppedVideoFrames() {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].resetDroppedVideoFrames()
        }
    }

    override fun setAuthorization(user: String, password: String) {
        srsFlvMuxer[0].setAuthorization(user, password)
    }

    /**
     * Some Livestream hosts use Akamai auth that requires RTMP packets to be sent with increasing
     * timestamp order regardless of packet type.
     * Necessary with Servers like Dacast.
     * More info here:
     * https://learn.akamai.com/en-us/webhelp/media-services-live/media-services-live-encoder-compatibility-testing-and-qualification-guide-v4.0/GUID-F941C88B-9128-4BF4-A81B-C2E5CFD35BBF.html
     */
    fun forceAkamaiTs(enabled: Boolean) {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].forceAkamaiTs(enabled)
        }
    }

    override fun prepareAudioRtp(isStereo: Boolean, sampleRate: Int) {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].setIsStereo(isStereo)
            srsFlvMuxer[i].setSampleRate(sampleRate)
        }
    }

    override fun startStreamRtp(url: List<String>) {
        println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvXXXXXXXXXXXXXXXXXXXXXXXvvvvvvvvvv")
        println("startStreamRtp")
        println(noOfStreams)
        if (videoEncoder.rotation == 90 || videoEncoder.rotation == 270) {
            for (i in 0 until noOfStreams - 1) {
                srsFlvMuxer[i].setVideoResolution(videoEncoder.height, videoEncoder.width)
            }
        } else {
            for (i in 0 until noOfStreams - 1) {
                srsFlvMuxer[i].setVideoResolution(videoEncoder.width, videoEncoder.height)
            }
        }
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].start(url[i])
            println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvXXXXXXXXXXXXXXXXXXXXXXXvvvvvvvvvv")
            println("startSrsflvmuxer")
            println(noOfStreams)
        }
    }

    override fun stopStreamRtp() {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].stop()
        }
    }

    override fun setReTries(reTries: Int) {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].setReTries(reTries)
        }
    }

    override fun shouldRetry(reason: String): Boolean {
        return srsFlvMuxer[0].shouldRetry(reason)
    }

    public override fun reConnect(delay: Long) {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].reConnect(delay)
        }
    }

    override fun hasCongestion(): Boolean {
        return srsFlvMuxer[0].hasCongestion()
    }

    override fun getAacDataRtp(aacBuffer: ByteBuffer, info: MediaCodec.BufferInfo) {
        if (noOfStreams == 1) {
            srsFlvMuxer[0].sendAudio(aacBuffer, info)
        } else {
            for (i in 0 until noOfStreams - 1) {
                srsFlvMuxer[i].sendAudio(aacBuffer.duplicate(), info)
            }
            srsFlvMuxer[noOfStreams - 1].sendAudio(aacBuffer, info)
        }
    }

    override fun onSpsPpsVpsRtp(sps: ByteBuffer, pps: ByteBuffer, vps: ByteBuffer) {
        if (noOfStreams == 1) {
            srsFlvMuxer[0].setSpsPPs(sps, pps)
        } else {
            for (i in 0 until noOfStreams - 1) {
                srsFlvMuxer[i].setSpsPPs(sps.duplicate(), pps.duplicate())
            }
            srsFlvMuxer[noOfStreams - 1].setSpsPPs(sps, pps)
        }
    }

    override fun getH264DataRtp(h264Buffer: ByteBuffer, info: MediaCodec.BufferInfo) {
        if (noOfStreams == 1) {
            srsFlvMuxer[0].sendVideo(h264Buffer, info)
        } else {
            for (i in 0 until noOfStreams - 1) {
                srsFlvMuxer[i].sendVideo(h264Buffer.duplicate(), info)
            }
            srsFlvMuxer[noOfStreams - 1].sendVideo(h264Buffer, info)
        }
    }

    override fun setLogs(enable: Boolean) {
        for (i in 0 until noOfStreams - 1) {
            srsFlvMuxer[i].setLogs(enable)
        }
    }
}