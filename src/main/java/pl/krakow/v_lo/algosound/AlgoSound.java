package pl.krakow.v_lo.algosound;

import javax.swing.SwingUtilities;

import pl.krakow.v_lo.algosound.gui.AlgoSoundGUI;

/**
 * 
 */

/**
 * @author arccha
 */
public class AlgoSound
{
  private Database       database;
  private CommandManager commandManager;

  public AlgoSound()
  {
    database = new Database();
    commandManager = new CommandManager();
  }

  public static void main(String[] args)
  {
    final AlgoSound algoSound = new AlgoSound();

    SwingUtilities.invokeLater(new Runnable()
    {

      @Override
      public void run()
      {
        AlgoSoundGUI algoSoundGUI = new AlgoSoundGUI(algoSound);
        algoSoundGUI.setVisible(true);
      }
    });
  }

  public CommandManager getCommandManager()
  {
    return commandManager;
  }

  public Database getDatabase()
  {
    return database;
  }
}
