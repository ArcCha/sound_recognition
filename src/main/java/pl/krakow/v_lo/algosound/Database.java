/**
 * 
 */
package pl.krakow.v_lo.algosound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.math3.complex.Complex;

import pl.krakow.v_lo.algosound.sound.SoundRecorder;

/**
 * @author arccha Simple, stupid class used to manage command .wav files, which are used as texts.
 */
public class Database
{
  File       databaseDir;
  File       propertiesFile;
  Properties properties;

  public Database()
  {
    databaseDir = new File(".algosound/");
    propertiesFile = new File(databaseDir, "database.properties");
    properties = new Properties();
    try
    {
      if (!databaseDir.exists())
      {
        databaseDir.mkdir();
        initProperties();
      }
      properties.load(new FileInputStream(propertiesFile));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void initProperties() throws IOException
  {
    propertiesFile.createNewFile();
    properties.setProperty("commandList", "");
    saveProperties();
  }

  private void saveProperties()
  {
    try
    {
      properties.store(new FileOutputStream(propertiesFile), null);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void saveCommand(Command command)
  {
    String name = command.getName();
    if (!name.equals("command"))
    {
      String commandList = properties.getProperty("commandList");
      commandList += "," + name;
      properties.setProperty("commandList", commandList);
      saveProperties();
    }
    name += ".wav";
    try
    {
      ByteArrayOutputStream byteOutput = Command.convertComplexToByteArrayOutputStream(command.getData());
      ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
      long soundLenght = byteInput.available() / 2;
      AudioInputStream audioInput = new AudioInputStream(byteInput, SoundRecorder.AUDIO_FORMAT, soundLenght);
      File commandFile = new File(databaseDir, name);
      AudioSystem.write(audioInput, SoundRecorder.TARGET_TYPE, commandFile);
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public Command getCommand(String commandName)
  {
    final File commandFile = new File(databaseDir, commandName + ".wav");
    Command command = new Command();
    command.setName(commandName);
    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
    try
    {
      AudioInputStream audioInput = AudioSystem.getAudioInputStream(commandFile);
      final int frameSize = audioInput.getFormat().getFrameSize();
      final int buffSize = 1024 * frameSize;
      byte[] byteArray = new byte[buffSize];
      while (audioInput.available() > 0)
      {
        audioInput.read(byteArray);
        ByteBuffer buff = ByteBuffer.wrap(byteArray);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        byteOutput.write(buff.array());
      }
    }
    catch (UnsupportedAudioFileException | IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    List<Complex> data = Command.parseBytes(byteOutput);
    command.setData(data);
    return command;
  }

  public List<Command> getAllCommands()
  {
    String commandList = properties.getProperty("commandList");
    String[] commandNames = commandList.split(",");
    ArrayList<Command> allCommands = new ArrayList<Command>();
    for (String name : commandNames)
    {
      if (name.length() == 0)
        continue;
      allCommands.add(getCommand(name));
    }
    return allCommands;
  }
}
