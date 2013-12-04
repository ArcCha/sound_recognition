package pl.krakow.v_lo.algosound.gui;

import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pl.krakow.v_lo.algosound.Command;
import pl.krakow.v_lo.algosound.Matcher;

public class SpectrumChart extends SoundChart
{
  private static final String TITLE = "SpectrumChart";

  public SpectrumChart()
  {
    this.command = null;
  }

  public SpectrumChart(Command command)
  {
    this.command = command;
    chart = createChart(command);
  }

  protected JFreeChart createChart(Command command)
  {
    final JFreeChart chart = ChartFactory.createXYLineChart(TITLE, // chart title
        "Frequency", // x axis label
        "Amplitude", // y axis label
        createDataset(command), // data
        PlotOrientation.VERTICAL, false, // include legend
        false, // tooltips
        false // urls
        );

    return chart;
  }

  protected XYDataset createDataset(Command command)
  {
    final XYSeries series = new XYSeries("Amplitude - frequency");

    int xValue = 0;
    for (List<Complex> samples : Matcher.computeSamplesFromCommand(command))
    {
      for (Complex yValue : samples)
        series.add(xValue++, yValue.abs());
    }
    return new XYSeriesCollection(series);
  }
}
