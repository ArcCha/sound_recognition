/**
 * 
 */
package pl.krakow.v_lo.algosound.sound;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * @author arccha
 */
public class SoundRecorder extends Thread
{
  private TargetDataLine                    line;
  private AudioInputStream                  audioInputStream;
  private File                              tmpFile;
  private static final AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;

  public SoundRecorder()
  {
    try
    {
      tmpFile = File.createTempFile("wav", "tmp");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void startRecording()
  {
    setUpRecorder();
    line.start();
    super.start();
  }

  public ByteArrayOutputStream stopRecording() throws IOException
  {
    line.stop();
    line.close();
    Path tmpFilePath = Paths.get(tmpFile.getAbsolutePath(), "");
    byte[] bytes = Files.readAllBytes(tmpFilePath);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write(bytes);
    return outputStream;
  }

  @Override
  public void run()
  {
    try
    {
      AudioSystem.write(audioInputStream, targetType, tmpFile);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void setUpRecorder()
  {
    AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 1, 2, 44100.0F, false);
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
    try
    {
      line = (TargetDataLine) AudioSystem.getLine(info);
      line.open(audioFormat);
    }
    catch (LineUnavailableException e)
    {
      e.printStackTrace();
    }
    this.audioInputStream = new AudioInputStream(line);
  }

}
