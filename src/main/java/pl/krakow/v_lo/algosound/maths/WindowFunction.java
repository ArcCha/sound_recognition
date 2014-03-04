package pl.krakow.v_lo.algosound.maths;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

public class WindowFunction
{
  private List<Complex> data;
  private double[] coefficients;
  private int len;
  
  public WindowFunction()
  {
    setData(new ArrayList<Complex>());
  }
  
  public WindowFunction(List<Complex> data)
  {
    setData(data);
  }
  
  public void setData(List<Complex> data)
  {
    this.data = data;
    len = data.size();
    coefficients = new double[data.size()];
  }
  
  public List<Complex> computeRectangularWindow()
  {
    for(int i = 0; i < len; ++i)
      coefficients[i] = 1.0;
    return multiply();
  }
  
  public List<Complex> computeTriangularWindow()
  {
    for(int i = 0; i < len; ++i)
      coefficients[i] = 1.0 - 2.0 * Math.abs(i - (len-1.0)/2.0) / len;
    return multiply();
  }
  
  public List<Complex> computeWelchWindow()
  {
    for(int i = 0; i < len; ++i)
      coefficients[i] = 1.0 - Math.pow((2.0*i - len + 1.0) / (len+1.0), 2);
    return multiply();
  }
  
  public List<Complex> computeHannWindow()
  {
    for(int i = 0; i < len; ++i)
      coefficients[i] = 0.5 * (1.0 - Math.cos(2.0 * Math.PI * i / (len - 1.0)));
    return multiply();
  }
  
  public List<Complex> computeHammingWindow()
  {
    for(int i = 0; i < len; ++i)
      coefficients[i] = 0.54 + 0.46 * Math.cos(2.0 * Math.PI * i / (len - 1.0));
    return multiply();
  }
  
  public List<Complex> computeBlackmanWindow()
  {
    for(int i = 0; i < len; ++i)
    {
      double val = 2.0 * Math.PI * i / (len - 1.0);
      coefficients[i] = 0.42659 - 0.49656 * Math.cos(val) + 0.076849 * Math.cos(2.0*val);
    }
    return multiply();
  }
  
  public List<Complex> computeNuttallWindow()
  {
    for(int i = 0; i < len; ++i)
    {
      double val = 2.0 * Math.PI * i / (len - 1.0);
      coefficients[i] = 0.355768 - 0.487396 * Math.cos(val) 
                                 + 0.144232 * Math.cos(2.0*val)
                                 - 0.012604 * Math.cos(3.0*val);
    }
    return multiply();
  }
  
  public List<Complex> computeFlatTopWindow()
  {
    for(int i = 0; i < len; ++i)
    {
      double val = 2.0 * Math.PI * i / (len - 1.0);
      coefficients[i] = 1.0 - 1.93 * Math.cos(val) 
                            + 1.29 * Math.cos(2.0*val)
                            - 0.388 * Math.cos(3.0*val)
                            + 0.028 * Math.cos(4.0*val);
    }
    return multiply();
  }
  
  public List<Complex> computeGaussianWindow()
  {
    for(int i = 0; i < len; ++i)
    {
      double val = (len-1.0) / 2.0;
      double exp = -0.5 * Math.pow((i - val) / (0.4 * val), 2); 
      coefficients[i] = Math.pow(Math.E, exp);
    }
    return multiply();
  }
  
  private List<Complex> multiply()
  {
    List<Complex> result = new ArrayList<Complex>();
    for(int i = 0; i < data.size(); ++i)
    {
      double value = data.get(i).getReal() * coefficients[i];
      result.add(new Complex(value));
    }
    return result;
  }
}
