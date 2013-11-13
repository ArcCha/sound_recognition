/**
 * 
 */
package pl.krakow.v_lo.algosound.maths;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

/**
 * @author arccha
 */
public class FastFourierTransform
{

  private List<Complex> data;

  public FastFourierTransform(List<Complex> data) throws IllegalArgumentException
  {
    if (!isPowerOfTwo(data.size()))
      throw new IllegalArgumentException("Input vector size must be a power of two.");
    this.data = data;
  }

  public List<Complex> transformForward()
  {
    data = transformRadix2(0, data.size(), 1, false);
    return data;
  }

  public List<Complex> transformBackward()
  {
    data = transformRadix2(0, data.size(), 1, true);
    return data;
  }

  private List<Complex> transformRadix2(int startingIdx, int size, int step, boolean reverse)
  {
    ArrayList<Complex> result = new ArrayList<Complex>(size);
    for (int i = 0; i < size; ++i)
    {
      result.add(new Complex(0));
    }
    if (size == 1)
    {
      result.set(0, data.get(startingIdx));
    }
    else
    {
      List<Complex> resultOfPart1 = transformRadix2(startingIdx, size / 2, step * 2, reverse);
      List<Complex> resultOfPart2 = transformRadix2(startingIdx + step, size / 2, step * 2, reverse);
      for (int i = 0; i < resultOfPart1.size(); ++i)
      {
        Complex omega;
        if (reverse)
          omega = new Complex(0, (2) * Math.PI * i / size);
        else
          omega = new Complex(0, (-2) * Math.PI * i / size);
        omega = omega.exp();

        Complex temp = resultOfPart1.get(i);
        result.set(i, temp.add(omega.multiply(resultOfPart2.get(i))));
        result.set(i + size / 2, temp.subtract(omega.multiply(resultOfPart2.get(i))));
        if (reverse)
        {
          result.set(i, result.get(i).divide(2));
          result.set(i + size / 2, result.get(i + size / 2).divide(2));
        }
      }
    }
    return result;
  }

  public List<Complex> getResult()
  {
    return data;
  }

  private boolean isPowerOfTwo(int number)
  {
    return ((number & (-number)) == number);
  }

}
