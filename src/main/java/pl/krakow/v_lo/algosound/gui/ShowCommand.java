package pl.krakow.v_lo.algosound.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pl.krakow.v_lo.algosound.AlgoSound;
import pl.krakow.v_lo.algosound.Command;
import pl.krakow.v_lo.algosound.CommandManager;
import pl.krakow.v_lo.algosound.Database;

public class ShowCommand extends JFrame
{
  private static final long serialVersionUID = 1L;
  private Database     database;
  private AlgoSoundGUI mainFrame;
  private CommandManager commandManager;

  public ShowCommand(AlgoSoundGUI mainFrame, AlgoSound algoSound, CommandManager commandManager)
  {
    this.database = algoSound.getDatabase();
    this.mainFrame = mainFrame;
    this.commandManager = commandManager;
    setTitle("Show command");
    setPreferredSize(new Dimension(310, 120));
    setLocationRelativeTo(mainFrame);

    initialize();
  }

  private void initialize()
  {
    Dimension buttonDimension = new Dimension(150, 30);
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    for (final Command command : database.getAllCommands())
    {
      if (command.getName().equals("command.wav"))
        continue;
      JButton commandButton = new JButton(command.getName());
      commandButton.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          commandManager.setMatched(command);
        }
      });
      commandButton.setPreferredSize(buttonDimension);
      panel.add(commandButton);
    }
    add(panel);
    pack();
  }
}
