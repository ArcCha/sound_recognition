package pl.krakow.v_lo.algosound.maths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

public class FFTBasedAlgorithms
{
  public FFTBasedAlgorithms()
  {

  }

  public List<Double> countSquaredError(List<Complex> patternOrig, List<Complex> textOrig)
  {
    List<Complex> pattern = new ArrayList<Complex>(patternOrig);
    List<Complex> text = new ArrayList<Complex>(textOrig);
    // System.out.println("pattern: " + pattern + "\ntext: " + text);
    final int patternSize = pattern.size();
    final int textSize = text.size();
    final int size = patternSize + textSize;
    final int resultSize = nextPowerOf2(size);
    List<Double> squaredError = new ArrayList<Double>(size);

    // sum [a;b) = prefixesSum[b] - prefixesSum[a]
    List<Complex> textPrefixesSum = countPrefixesSum(text, size);
    // System.out.println("TextPrefixesSum: " + textPrefixesSum);
    List<Complex> patternSquaresSum = countSquaresSum(pattern, size, textSize);
    // System.out.println("PatternSquaresSum: " + patternSquaresSum);

    Collections.reverse(pattern);
    resize(pattern, resultSize);
    resize(text, resultSize);

    FastFourierTransform fft = new FastFourierTransform(pattern);
    List<Complex> pattern_fft = fft.transformForward();

    fft = new FastFourierTransform(text);
    List<Complex> text_fft = fft.transformForward();

    List<Complex> convolution = new ArrayList<Complex>(resultSize);
    for (int i = 0; i < resultSize; ++i)
      convolution.add(pattern_fft.get(i).multiply(text_fft.get(i)));

    fft = new FastFourierTransform(convolution);
    convolution = fft.transformBackward();

    // System.out.println("Convolution: " + convolution);
    Complex sum;
    for (int i = 0; i < textSize + patternSize - 1; ++i)
    {
      sum = patternSquaresSum.get(i);
      sum = sum.add(textPrefixesSum.get(i + 1).subtract(textPrefixesSum.get(Math.max(i - patternSize + 1, 0))));
      sum = sum.subtract(convolution.get(i).multiply(2));
      squaredError.add(sum.abs());
    }
    // System.out.println("Result: " + convolutionResult);
    return squaredError;
  }

  private void resize(List<Complex> list, int newSize)
  {
    for (int i = list.size(); i < newSize; ++i)
      list.add(new Complex(0));
  }

  private List<Complex> countPrefixesSum(List<Complex> list, int size)
  {
    List<Complex> prefixesSum = new ArrayList<Complex>(size);
    Complex tempVal = new Complex(0);
    prefixesSum.add(tempVal);
    for (int i = 0; i < list.size(); ++i)
    {
      tempVal = list.get(i).multiply(list.get(i)); // list[i]^2
      prefixesSum.add(prefixesSum.get(i).add(tempVal)); // add(prefix[i] + list[i]^2)
    }
    for (int i = list.size() + 1; i < size; ++i)
      prefixesSum.add(prefixesSum.get(i - 1));
    return prefixesSum;
  }

  private List<Complex> countSquaresSum(List<Complex> list, int size, int textSize)
  {
    List<Complex> squaresSum = new ArrayList<Complex>(size);
    int i = 0;
    Complex currentSum = new Complex(0);

    for (int j = list.size() - 1; j >= 0; --j, ++i)
    {
      currentSum = currentSum.add(list.get(j).multiply(list.get(j))); // currentSum += list[j]^2
      squaresSum.add(currentSum);
    }
    // System.out.println("");
    for (int j = textSize - list.size() - 1; j >= 0; --j, ++i)
      squaresSum.add(squaresSum.get(i - 1)); // sum[i] = sum[i-1]
    for (int j = list.size() - 1; j >= 0; --j, ++i)
    {
      currentSum = currentSum.subtract(list.get(j).multiply(list.get(j))); // currentSum -= list[j]^2
      squaresSum.add(currentSum);
    }
    return squaresSum;
  }

  private int nextPowerOf2(int n)
  {
    --n;
    n |= n >> 1;
    n |= n >> 2;
    n |= n >> 4;
    n |= n >> 8;
    n |= n >> 16;
    return n + 1;
  }
}
