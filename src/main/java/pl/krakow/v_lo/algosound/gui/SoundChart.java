package pl.krakow.v_lo.algosound.gui;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import pl.krakow.v_lo.algosound.Command;

public abstract class SoundChart
{

  protected Command    command;
  protected JFreeChart chart;

  protected abstract JFreeChart createChart(Command command);

  protected abstract XYDataset createDataset(Command command);

  public void updateChart()
  {
    chart = createChart(command);
  }

  public Command getCommand()
  {
    return command;
  }

  public void setCommand(Command command)
  {
    this.command = command;
    updateChart();
  }

  public JFreeChart getChart()
  {
    return chart;
  }

}
