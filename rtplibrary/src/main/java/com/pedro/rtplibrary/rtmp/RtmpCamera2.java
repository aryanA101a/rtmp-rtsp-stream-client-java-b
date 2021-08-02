package com.pedro.rtplibrary.rtmp;

import android.content.Context;
import android.media.MediaCodec;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.SurfaceView;
import android.view.TextureView;

import com.pedro.rtplibrary.base.Camera2Base;

import com.pedro.rtplibrary.view.LightOpenGlView;
import com.pedro.rtplibrary.view.OpenGlView;
import net.ossrs.rtmp.ConnectCheckerRtmp;
import net.ossrs.rtmp.SrsFlvMuxer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * More documentation see:
 * {@link com.pedro.rtplibrary.base.Camera2Base}
 *
 * Created by pedro on 6/07/17.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RtmpCamera2 extends Camera2Base {

  private ArrayList<SrsFlvMuxer> srsFlvMuxer ;
  int noOfStreams;
  /**
   * @deprecated This view produce rotations problems and could be unsupported in future versions.
   * Use {@link Camera2Base#Camera2Base(OpenGlView)} or {@link Camera2Base#Camera2Base(LightOpenGlView)}
   * instead.
   */
  @Deprecated
  public RtmpCamera2(SurfaceView surfaceView, ConnectCheckerRtmp connectChecker) {
    super(surfaceView);
    srsFlvMuxer.set(1, new SrsFlvMuxer(connectChecker));
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.set(i, new SrsFlvMuxer((ConnectCheckerRtmp) this));
    }
  }

  /**
   * @deprecated This view produce rotations problems and could be unsupported in future versions.
   * Use {@link Camera2Base#Camera2Base(OpenGlView)} or {@link Camera2Base#Camera2Base(LightOpenGlView)}
   * instead.
   */
  @Deprecated
  public RtmpCamera2(TextureView textureView, ConnectCheckerRtmp connectChecker) {
    super(textureView);

    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.set(i, new SrsFlvMuxer(connectChecker));
    }


  }

  public RtmpCamera2(OpenGlView openGlView, ConnectCheckerRtmp connectChecker) {
    super(openGlView);

    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.set(i, new SrsFlvMuxer(connectChecker));
    }
  }

  public RtmpCamera2(LightOpenGlView lightOpenGlView, ConnectCheckerRtmp connectChecker) {
    super(lightOpenGlView);

    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.set(i, new SrsFlvMuxer(connectChecker));
    }
  }

  public RtmpCamera2(Context context, boolean useOpengl, ConnectCheckerRtmp connectChecker) {
    super(context, useOpengl);

    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.set(i, new SrsFlvMuxer(connectChecker));


    }
  }

  /**
   * H264 profile.
   *
   * @param profileIop Could be ProfileIop.BASELINE or ProfileIop.CONSTRAINED
   */
  public void setProfileIop(byte profileIop) {
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).setProfileIop(profileIop);
    }

  }

  @Override
  public void resizeCache(int newSize) throws RuntimeException {
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).resizeFlvTagCache(newSize);
    }


  }

  @Override
  public int getCacheSize() {
    return srsFlvMuxer.get(0).getFlvTagCacheSize();
  }

  @Override
  public long getSentAudioFrames() {
    long saf=0;
    for(int i=0;i<noOfStreams;i++){
      saf=+srsFlvMuxer.get(i).getSentAudioFrames();
    }
    return saf;
  }

  @Override
  public long getSentVideoFrames() {
    long svf=0;
    for(int i=0;i<noOfStreams;i++){
      svf=+srsFlvMuxer.get(i).getSentVideoFrames();
    }
    return svf;

  }

  @Override
  public long getDroppedAudioFrames() {
    long daf=0;
    for(int i=0;i<noOfStreams;i++){
      daf=+srsFlvMuxer.get(i).getDroppedAudioFrames();
    }
    return daf;

  }

  @Override
  public long getDroppedVideoFrames() {
    long dvf=0;
    for(int i=0;i<noOfStreams;i++){
      dvf=+srsFlvMuxer.get(i).getDroppedVideoFrames();
    }
    return dvf;

  }

  @Override
  public void resetSentAudioFrames() {

    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).resetSentAudioFrames();
    }


  }

  @Override
  public void resetSentVideoFrames() {
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).resetSentVideoFrames();
    }

  }

  @Override
  public void resetDroppedAudioFrames() {
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).resetDroppedAudioFrames();
    }

  }

  @Override
  public void resetDroppedVideoFrames() {
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).resetDroppedVideoFrames();
    }

  }

  @Override
  public void setAuthorization(String user, String password) {
    srsFlvMuxer.get(0).setAuthorization(user, password);
  }

  /**
   * Some Livestream hosts use Akamai auth that requires RTMP packets to be sent with increasing
   * timestamp order regardless of packet type.
   * Necessary with Servers like Dacast.
   * More info here:
   * https://learn.akamai.com/en-us/webhelp/media-services-live/media-services-live-encoder-compatibility-testing-and-qualification-guide-v4.0/GUID-F941C88B-9128-4BF4-A81B-C2E5CFD35BBF.html
   */
  public void forceAkamaiTs(boolean enabled) {
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).forceAkamaiTs(enabled);
    }

  }

  @Override
  protected void prepareAudioRtp(boolean isStereo, int sampleRate) {
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).setIsStereo(isStereo);
      srsFlvMuxer.get(i).setSampleRate(sampleRate);
    }

  }

  @Override
  protected void startStreamRtp(List<String> url) {
    if (videoEncoder.getRotation() == 90 || videoEncoder.getRotation() == 270) {
      for(int i=0;i<noOfStreams;i++){
        srsFlvMuxer.get(i).setVideoResolution(videoEncoder.getHeight(), videoEncoder.getWidth());
      }

    } else {
      for(int i=0;i<noOfStreams;i++){
        srsFlvMuxer.get(i).setVideoResolution(videoEncoder.getWidth(), videoEncoder.getHeight());
      }

    }
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).start(url.get(i));
    }

  }

  @Override
  protected void stopStreamRtp() {
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).stop();
    }

  }

  @Override
  public void setReTries(int reTries) {
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).setReTries(reTries);
    }

  }

  @Override
  public boolean shouldRetry(String reason) {

    return srsFlvMuxer.get(0).shouldRetry(reason);


  }

  @Override
  public void reConnect(long delay) {
    for(int i=0;i<noOfStreams;i++){
      srsFlvMuxer.get(i).reConnect(delay);
    }

  }

  @Override
  public boolean hasCongestion() {

    return srsFlvMuxer.get(0).hasCongestion();
  }

  @Override
  protected void getAacDataRtp(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
    if(noOfStreams==1){
      srsFlvMuxer.get(0).sendAudio(aacBuffer, info);
    }
    else{
      for(int i=0;i<noOfStreams;i++){
        srsFlvMuxer.get(i).sendAudio(aacBuffer.duplicate(), info);
      }

      srsFlvMuxer.get(noOfStreams-1).sendAudio(aacBuffer, info);

    }


  }

  @Override
  protected void onSpsPpsVpsRtp(ByteBuffer sps, ByteBuffer pps, ByteBuffer vps) {
    if(noOfStreams==1){
      srsFlvMuxer.get(0).setSpsPPs(sps, pps);
    }
    else{
      for(int i=0;i<noOfStreams;i++){
        srsFlvMuxer.get(i).setSpsPPs(sps.duplicate(), pps.duplicate());
      }

      srsFlvMuxer.get(noOfStreams-1).setSpsPPs(sps, pps);

    }

  }

  @Override
  protected void getH264DataRtp(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
    if(noOfStreams==1){
      srsFlvMuxer.get(0).sendVideo(h264Buffer, info);
    }
    else{
      for(int i=0;i<noOfStreams;i++){
        srsFlvMuxer.get(i).sendVideo(h264Buffer.duplicate(), info);
      }

      srsFlvMuxer.get(noOfStreams-1).sendVideo(h264Buffer, info);

    }

  }

  @Override
  public void setLogs(boolean enable) {
    for (int i = 0; i < noOfStreams; i++) {
      srsFlvMuxer.get(i).setLogs(enable);
    }
  }}