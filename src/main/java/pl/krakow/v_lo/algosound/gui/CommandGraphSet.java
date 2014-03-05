package pl.krakow.v_lo.algosound.gui;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.math3.complex.Complex;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pl.krakow.v_lo.algosound.Command;
import pl.krakow.v_lo.algosound.Matcher;

public class CommandGraphSet extends JScrollPane implements Observer
{
  private static final long      serialVersionUID  = 1L;
  private static final String    NO_DATA_AVAILABLE = "No data available.";
  private static final Dimension CHART_DIMENSION   = new Dimension(256, 256);
  private JFreeChart             soundWaveChart;
  private XYPlot                 soundWavePlot;
  private JFreeChart             spectrumChart;
  private XYPlot                 spectrumPlot;

  CommandGraphSet()
  {
    super();
    initializeSoundWaveChart();
    initializeSpectrumChart();
    initializePane();
  }

  private void initializePane()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    ChartPanel soundChartPanel = new ChartPanel(soundWaveChart);
    soundChartPanel.setPreferredSize(CHART_DIMENSION);
    panel.add(soundChartPanel);
    ChartPanel spectrumChartPanel = new ChartPanel(spectrumChart);
    spectrumChartPanel.setPreferredSize(CHART_DIMENSION);
    panel.add(spectrumChartPanel);
    setViewportView(panel);
  }

  private void initializeSoundWaveChart()
  {
    soundWaveChart = ChartFactory.createXYLineChart("Sound Wave", // chart title
        "Sample", // x axis label
        "Value", // y axis label
        null, // data
        PlotOrientation.VERTICAL, false, // include legend
        false, // tooltips
        false // urls
        );
    soundWavePlot = (XYPlot) soundWaveChart.getPlot();
    soundWavePlot.setNoDataMessage(NO_DATA_AVAILABLE);
  }

  private void initializeSpectrumChart()
  {
    spectrumChart = ChartFactory.createXYLineChart("Spectrum", // chart title
        "Frequency", // x axis label
        "Amplitude", // y axis label
        null, // data
        PlotOrientation.VERTICAL, false, // include legend
        false, // tooltips
        false // urls
        );
    spectrumPlot = (XYPlot) spectrumChart.getPlot();
    spectrumPlot.setNoDataMessage(NO_DATA_AVAILABLE);
  }

  @Override
  public void update(Observable arg0, Object arg1)
  {
    Command command = (Command) arg0;
    XYDataset soundWaveDataset = createSoundWaveDataset(command);
    soundWavePlot.setDataset(soundWaveDataset);
    XYDataset spectrumDataset = createSpectrumDataset(command);
    spectrumPlot.setDataset(spectrumDataset);
  }

  private XYDataset createSoundWaveDataset(Command command)
  {
    final XYSeries series = new XYSeries("Amplitude - time");

    int xValue = 0;
    for (Double yValue : command.getAmplitudeData())
      series.add(xValue++, yValue);

    return new XYSeriesCollection(series);
  }

  private XYDataset createSpectrumDataset(Command command)
  {
    final XYSeries series = new XYSeries("Amplitude - frequency");

    int xValue = 0;
    for (Complex yValue : Matcher.computeSamplesFromCommand(command))
        series.add(xValue++, yValue.abs());
    
    return new XYSeriesCollection(series);
  }
}
