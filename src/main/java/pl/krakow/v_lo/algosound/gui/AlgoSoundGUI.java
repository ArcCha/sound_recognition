package pl.krakow.v_lo.algosound.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pl.krakow.v_lo.algosound.AlgoSound;
import pl.krakow.v_lo.algosound.Command;
import pl.krakow.v_lo.algosound.CommandManager;
import pl.krakow.v_lo.algosound.Database;
import pl.krakow.v_lo.algosound.MatchedResult;
import pl.krakow.v_lo.algosound.Matcher;
import pl.krakow.v_lo.algosound.sound.SoundPlayer;
import pl.krakow.v_lo.algosound.sound.SoundRecorder;

public class AlgoSoundGUI extends JFrame
{
  /*
   * No idea what it is used for, but eclipse complains about it.
   */
  private static final long      serialVersionUID = -8821408889675820562L;
  private static final Dimension APP_DIMENSION     = new Dimension(1024, 768);
  private final AlgoSoundGUI     THIS             = this;
  private AlgoSound              algoSound;

  private CommandGraphSet        patternGraphs;
  private CommandGraphSet        matchedGraphs;

  private Database               database;
  private CommandManager         commandManager;

  public AlgoSoundGUI(AlgoSound algoSound)
  {
    this.algoSound = algoSound;
    database = algoSound.getDatabase();
    commandManager = algoSound.getCommandManager();
    initializeUI();
  }

  private void initializeUI()
  {
    setTitle("Algosound");
    setSize(APP_DIMENSION);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    initializeMenuBar();

    BorderLayout layout = new BorderLayout(10, 10);
    JPanel panel = new JPanel(layout);
    panel.setPreferredSize(APP_DIMENSION);

    initializeCenter(panel);
    initializeEast(panel);

    add(panel);
    pack();
  }

  private void initializeCenter(JPanel panel)
  {
    JPanel innerPanel = new JPanel();
    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
    patternGraphs = new CommandGraphSet();
    commandManager.getPattern().addObserver(patternGraphs);
    innerPanel.add(patternGraphs);
    matchedGraphs = new CommandGraphSet();
    commandManager.getMatched().addObserver(matchedGraphs);
    innerPanel.add(matchedGraphs);
    panel.add(innerPanel, BorderLayout.CENTER);
  }

  private void initializeEast(JPanel panel)
  {
    JPanel innerPanel = new JPanel();
    BoxLayout boxLayout = new BoxLayout(innerPanel, BoxLayout.Y_AXIS);
    innerPanel.setLayout(boxLayout);

    JButton record = new JButton("Record");
    record.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        SoundRecorder soundRecorder = new SoundRecorder();
        soundRecorder.startRecording();
        ByteArrayOutputStream output = soundRecorder.getRecordedData();
        Command command = new Command("command", Command.parseBytes(output));
        commandManager.setPattern(command);
        database.saveCommand(command);
      }
    });

    JButton match = new JButton("Match");
    match.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        Matcher matcher = new Matcher(database.getCommand("command"), algoSound.getDatabase());
        List<MatchedResult> matchResults = matcher.match();
        if (matchResults.size() > 0)
        {
          Command matchedSound = matchResults.get(0).getCommand();
          commandManager.setMatched(matchedSound);
        }
        System.out.println(matchResults);
      }
    });

    JButton playCommand = new JButton("Play command");
    playCommand.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        File soundFile = new File("./.algosound/command.wav");
        BufferedInputStream sound = null;
        try
        {
          FileInputStream tmp = new FileInputStream(soundFile);
          sound = new BufferedInputStream(tmp);
        }
        catch (FileNotFoundException e1)
        {
          JOptionPane.showMessageDialog(THIS, "You need to record command first.");
          return;
        }
        SoundPlayer soundPlayer = new SoundPlayer(sound);
        soundPlayer.playSound();
      }
    });

    JButton playMatched = new JButton("Play matched");
    playMatched.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (commandManager.getMatched() == null)
        {
          JOptionPane.showMessageDialog(THIS, "You need to match first");
          return;
        }
        File matchedSound = database.getCommandSoundFile(commandManager.getMatched().getName());
        BufferedInputStream sound = null;
        try
        {
          FileInputStream tmp = new FileInputStream(matchedSound);
          sound = new BufferedInputStream(tmp);
        }
        catch (FileNotFoundException e1)
        {
          JOptionPane.showMessageDialog(THIS, "You need to match first.");
          return;
        }
        SoundPlayer soundPlayer = new SoundPlayer(sound);
        soundPlayer.playSound();
      }
    });

    justifyButtonsAndAdd(innerPanel, record, playCommand, match, playMatched);

    panel.add(innerPanel, BorderLayout.EAST);
  }

  private void justifyButtonsAndAdd(JPanel panel, JButton... buttons)
  {
    Dimension dimension = new Dimension(140, 30);
    Component spacer = Box.createRigidArea(new Dimension(5, 5)); // TODO Don't work FIX it later.
    for (JButton button : buttons)
    {
      button.setMinimumSize(dimension);
      button.setPreferredSize(dimension);
      button.setMaximumSize(dimension);
      button.setSize(dimension);
      panel.add(button);
      panel.add(spacer);
    }
  }

  private void initializeMenuBar()
  {
    JMenuBar menubar = new JMenuBar();
    JMenu database = new JMenu("Database");
    initializeDatabaseMenu(database);
    menubar.add(database);

    setJMenuBar(menubar);
  }

  private void initializeDatabaseMenu(JMenu database)
  {
    JMenuItem addCommand = new JMenuItem("Add command");
    addCommand.setToolTipText("Add command to database.");
    addCommand.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        AddCommand addCommand = new AddCommand(THIS, algoSound);
        addCommand.setVisible(true);
      }
    });
    database.add(addCommand);
    
    JMenuItem showCommand = new JMenuItem("Show command");
    showCommand.setToolTipText("Show charts for selected command instead of matched");
    showCommand.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        ShowCommand showCommand = new ShowCommand(THIS, algoSound, commandManager);
        showCommand.setVisible(true);
      }
    });
    database.add(showCommand);
  }
}
