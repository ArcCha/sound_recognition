/**
 * 
 */
package pl.krakow.v_lo.algosound;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
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

import org.apache.commons.math3.complex.Complex;

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
    databaseDir = new File("./.algosound");
    propertiesFile = new File(databaseDir, "database.properties");
    properties = new Properties();
    if (!databaseDir.exists())
    {
      databaseDir.mkdir();
      initProperties();
    }
    try
    {
      properties.load(new FileInputStream(propertiesFile));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void initProperties()
  {
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

  public void saveRawCommandBytes(String name, ByteArrayOutputStream stream)
  {
    String commandList = properties.getProperty("commandList");
    commandList += "," + name;
    properties.setProperty("commandList", commandList);
    if(!name.endsWith(".wav"))
      name += ".wav";
    File newCommandFile = new File(databaseDir, name);
    try
    {
      FileOutputStream newCommandStream = new FileOutputStream(newCommandFile);
      newCommandStream.write(stream.toByteArray());
      newCommandStream.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public void saveCommand(Command command, String sufix) throws IOException
  {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    List<Complex> commandData = command.getData();
    for(Complex complex : commandData)
    {
      double sample = complex.abs();
      short test = (short) sample;
      ByteBuffer bb = ByteBuffer.allocate(2);
      bb.order(ByteOrder.LITTLE_ENDIAN);
      bb.putShort(test);
      outputStream.write(bb.array());
    }
    saveRawCommandBytes(command.getName() + sufix, outputStream);
  }
  
  public void saveCurrentCommand(ByteArrayOutputStream stream)
  {
    saveRawCommandBytes("command", stream);
  }

//  public ArrayList<Command> getAllCommands()
//  {
//    File[] commands = databaseDir.listFiles(new FileFilter()
//    {
//      @Override
//      public boolean accept(File arg0)
//      {
//        if (!arg0.isDirectory())
//          return true;
//        return false;
//      }
//    });
//    ArrayList<Command> result = new ArrayList<Command>();
//    for (File file : commands)
//    {
//      result.add(new Command(file));
//    }
//    return result;
//  }
  
  public Command getCommand(String commandName)
  {
    final File commandFile = new File(databaseDir, commandName + ".wav");
    Command command = new Command();
    command.setName(commandName);
    Path path = Paths.get(commandFile.getAbsolutePath());
    byte[] bytes = null;
    try
    {
      bytes = Files.readAllBytes(path);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    bb.order(ByteOrder.LITTLE_ENDIAN);
    List<Complex> commandData = command.getData();
    while(bb.hasRemaining())
    {
      commandData.add(new Complex((double) bb.getShort(), 0));
    }
    return command;
  }
  
  public List<Command> getAllCommands()
  {
    String commandList = properties.getProperty("commandList");
    String [] commandNames = commandList.split(",");
    ArrayList<Command> allCommands = new ArrayList<Command>();
    for(String name : commandNames)
    {
      allCommands.add(getCommand(name));
    }
    return allCommands;
  }
}
