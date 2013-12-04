package pl.krakow.v_lo.algosound.gui;

import org.apache.commons.math3.complex.Complex;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pl.krakow.v_lo.algosound.Command;

public class SoundWaveChart extends SoundChart
{
  static final String TITLE = "SoundChart";
  
  public SoundWaveChart()
  {
    this.command = null;
  }

  public SoundWaveChart(Command command)
  {
    this.command = command;
    chart = createChart(command);
  }

  @Override
  protected JFreeChart createChart(Command command)
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

  protected XYDataset createDataset(Command command)
  {
    final XYSeries series = new XYSeries("Amplitude - time");

    int xValue = 0;
    for (Complex yValue : command.getData())
      series.add(xValue++, yValue.getReal());
    
    return new XYSeriesCollection(series);
  }
}
