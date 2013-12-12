/**
 * 
 */
package pl.krakow.v_lo.algosound.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.krakow.v_lo.algosound.AlgoSound;
import pl.krakow.v_lo.algosound.sound.SoundRecorder;

/**
 * @author arccha
 */
public class AddCommand extends JFrame
{
  private static final long      serialVersionUID = -6134477803769383832L;
  private static final Dimension frameDim         = new Dimension(250, 200);
  private static final String    prompt           = new String("Please type name of command...");
  private final AddCommand       THIS             = this;
  private AlgoSound              algoSound;

  public AddCommand(JFrame father, AlgoSound algoSound)
  {
    this.algoSound = algoSound;
    setTitle("Add command");
    setSize(frameDim);
    setLocationRelativeTo(father);

    initialize();
  }

  private void initialize()
  {
    GridLayout gridLayout = new GridLayout(2, 1);
    JPanel panel = new JPanel(gridLayout);

    final JTextField name = new JTextField(prompt);
    name.addKeyListener(new KeyListener()
    {
      @Override
      public void keyTyped(KeyEvent arg0)
      {
      }

      @Override
      public void keyReleased(KeyEvent arg0)
      {
      }

      @Override
      public void keyPressed(KeyEvent arg0)
      {
        if (name.getText().equals(prompt))
          name.selectAll();
      }
    });

    JButton record = new JButton("Record");
    record.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        String commandName = name.getText();
        if (commandName.equals(prompt))
        {
          JOptionPane.showMessageDialog(THIS, "You need to specify name of the command first.");
          return;
        }
//        if (!algoSound.getDatabase().isNameAvailable(commandName))
//        {
//          JOptionPane.showMessageDialog(THIS, "Command already exists. Choose other name.");
//          return;
//        }
        SoundRecorder soundRecorder = new SoundRecorder();
        soundRecorder.startRecording();
        try
        {
          Thread.sleep(1500);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
        ByteArrayOutputStream recorded = null;
        try
        {
          recorded = soundRecorder.stopRecording();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        algoSound.getDatabase().saveRawCommandBytes(commandName, recorded);
        THIS.setVisible(false);
        THIS.dispose();
      }
    });

    panel.add(name);
    panel.add(record);

    add(panel);
    pack();
  }
}
