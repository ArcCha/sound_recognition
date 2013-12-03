package pl.krakow.v_lo.algosound.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.apache.commons.math3.complex.Complex;

import pl.krakow.v_lo.algosound.Command;
import pl.krakow.v_lo.algosound.Matcher;

public class ColoredSpectrum extends JComponent
{
  private List<Double> data;
  private final int    sampleSize = 1024;

  public ColoredSpectrum(Dimension dimension, Command command)
  {
    createDataset(command);
    setSize(dimension);
    repaint();
    setVisible(true);
  }

  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    final int width = 3;
    final int height = 1;
    final int numberOfSamplesAverage = 2;
    for (int i = 0, x = 0; i < data.size(); i += sampleSize)
    {
      int y = sampleSize / numberOfSamplesAverage / 2;
      float val = 0;
      for (int j = i; j < i + sampleSize; ++j)
      {
        val += data.get(j).floatValue();
        if ((j + 1) % numberOfSamplesAverage == 0)
        {
          val = Math.min(val, 1f);
          g2d.setColor(new Color(val, val, val));
          g2d.fillRect(x, y, width, height);
          val = 0;
          y -= height;
        }
      }
      x += width;
    }
  }

  private void createDataset(Command command)
  {
    data = new ArrayList<Double>();
    for (Complex yValue : Matcher.computeSamplesFromCommand(command))
      data.add(yValue.abs());
  }

  public void updateSpectrum(Command command)
  {
    createDataset(command);
    repaint();
  }

  public void hideIt()
  {
    setVisible(false);
  }

  public void unhideIt()
  {
    setVisible(true);
  }
}
