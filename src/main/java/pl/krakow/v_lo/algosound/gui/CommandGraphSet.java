package pl.krakow.v_lo.algosound.gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import pl.krakow.v_lo.algosound.Command;

public class CommandGraphSet extends JComponent implements Observer
{
  private SoundWaveChart           soundChart;
  private SpectrumChart        spectrumChart;
  private ColoredSpectrumChart coloredSpectrumChart;

  CommandGraphSet()
  {
    soundChart = null;
    spectrumChart = null;
    coloredSpectrumChart = null;
  }
  
  CommandGraphSet(Command command)
  {
    soundChart = new SoundWaveChart(command);
    spectrumChart = new SpectrumChart(command);
  }

  @Override
  public void update(Observable arg0, Object arg1)
  {
    // TODO Auto-generated method stub

  }
}
