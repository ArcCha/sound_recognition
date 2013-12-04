package pl.krakow.v_lo.algosound.gui;

import org.apache.commons.math3.complex.Complex;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pl.krakow.v_lo.algosound.Command;

public class SoundWaveChart
{
  private static final String TITLE = "SoundChart";
  private Command             command;
  private JFreeChart          chart;

  public SoundWaveChart()
  {
    this.command = null;
  }

  public SoundWaveChart(Command command)
  {
    this.command = command;
    chart = createChart(command);
  }

  private JFreeChart createChart(Command command)
  {
    final JFreeChart chart = ChartFactory.createXYLineChart(TITLE, // chart title
        "Sample", // x axis label
        "Value", // y axis label
        createDataset(command), // data
        PlotOrientation.VERTICAL, false, // include legend
        false, // tooltips
        false // urls
        );
    return chart;
  }

  private XYDataset createDataset(Command command)
  {
    final XYSeries series = new XYSeries("Amplitude - time");

    int xValue = 0;
    for (Complex yValue : command.getData())
      series.add(xValue++, yValue.getReal());
    
    return new XYSeriesCollection(series);
  }

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
  }

  public JFreeChart getChart()
  {
    return chart;
  }
}
