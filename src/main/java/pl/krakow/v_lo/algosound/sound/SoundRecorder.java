/**
 * 
 */
package pl.krakow.v_lo.algosound.sound;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import pl.krakow.v_lo.algosound.Timer;

/**
 * @author arccha
 */
public class SoundRecorder
{
  public static final AudioFileFormat.Type TARGET_TYPE;
  public static final AudioFormat          AUDIO_FORMAT;

  static
  {
    AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 1, 2, 44100.0F, false);
    TARGET_TYPE = AudioFileFormat.Type.WAVE;
  }

  private TargetDataLine                    line;
  private AtomicBoolean                     stopped;
  private ByteArrayOutputStream             out;

  public SoundRecorder()
  {
    stopped = new AtomicBoolean(false);
  }

  public void startRecording()
  {
    prepareLine();
    record();
    cleanup();
  }

  private void record()
  {
    Timer timer = new Timer(stopped);
    out = new ByteArrayOutputStream();
    int numBytesRead = 0;
    byte[] data = new byte[line.getBufferSize() / 5];
    line.start();
    timer.start();
    while (!stopped.get())
    {
      numBytesRead = line.read(data, 0, data.length);
      out.write(data, 0, numBytesRead);
    }
  }

  private void cleanup()
  {
    line.flush();
    line.close();
    stopped.set(false);
  }

  private void prepareLine()
  {
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);
    try
    {
      line = (TargetDataLine) AudioSystem.getLine(info);
      line.open(AUDIO_FORMAT);
    }
    catch (LineUnavailableException e)
    {
      e.printStackTrace();
    }
  }

  public ByteArrayOutputStream getRecordedData()
  {
    return out;
  }
}
