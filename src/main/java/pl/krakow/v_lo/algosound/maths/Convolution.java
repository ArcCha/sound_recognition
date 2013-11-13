package pl.krakow.v_lo.algosound.maths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

public class Convolution
{  
  public Convolution()
  {
    
  }
  
  public List<Double> countSumOfDiffSquares(List<Complex> pattern, List<Complex> text)
  {
    System.out.println("Text: " + text);
    System.out.println("Pattern: " + pattern);
    
    final int patternSize = pattern.size();
    final int textSize = text.size();
    final int resultSize = nextPowerOf2(pattern.size() + text.size());
    List<Double> convolutionResult = new ArrayList<Double>(pattern.size() + text.size());
    // sum [a;b) = prefixesSum[b] - prefixesSum[a]
    
    double[] textPrefixesSum = countPrefixesSum(text, pattern.size());
    double[] patternSquaresSum = countSquaresSum(pattern);
    
    Collections.reverse(pattern);
    pattern = resize(pattern, resultSize);
    text = resize(text, resultSize);
    
    FastFourierTransform fft = new FastFourierTransform(pattern);
    List<Complex> pattern_fft = fft.transformForward();
    
    fft = new FastFourierTransform(text);
    List<Complex> text_fft = fft.transformForward();
    
    List<Complex> convolution = new ArrayList<Complex>(resultSize);
    for (int i = 0; i < resultSize; ++i)
      convolution.add( pattern_fft.get(i).multiply(text_fft.get(i)) );
    
    fft = new FastFourierTransform(convolution);
    convolution = fft.transformBackward();
    
    System.out.println("Convolution: " + convolution);
    double sum;
    for (int i = 0; i < textSize + patternSize - 1; ++i)
    {
      sum = patternSquaresSum[i] + (textPrefixesSum[i+1] - textPrefixesSum[Math.max(i-patternSize+1, 0)]);
      sum -= 2 * convolution.get(i).abs();
      convolutionResult.add(sum);
    }
    System.out.println("Result: " + convolutionResult);
    return convolutionResult;
  }
  
  private List<Complex> resize(List<Complex> list, int newSize)
  {
    List<Complex> newList = new ArrayList<Complex>(newSize);
    for (int i = 0; i < list.size(); ++i)
      newList.add(list.get(i));
    for (int i = list.size(); i < newSize; ++i)
      newList.add(new Complex(0));
    return newList;
  }
  
  private double[] countPrefixesSum(List<Complex> list, int extendSize)
  {
    final int size = list.size() + extendSize;
    double[] prefixesSum = new double[size];
    prefixesSum[0] = 0;
    for (int i = 0; i < list.size(); ++i)
      prefixesSum[i+1] = prefixesSum[i] + Math.pow(list.get(i).abs(), 2);
    
    for (int i = list.size() + 1; i < size; ++i)
      prefixesSum[i] = prefixesSum[i-1];
    return prefixesSum;
  }
  
  private double[] countSquaresSum(List<Complex> list)
  {
    double[] squaresSum = new double[2*list.size() + 1];
    int i = 0;
    double sum = 0;
  
    for (int j = list.size()-1; j >= 0; --j, ++i)
    {
      sum += Math.pow(list.get(j).abs(), 2);
      squaresSum[i] = sum;
      System.out.print("("+i+") " + sum + " ");
    }
    for (int j = list.size()-1; j >= 0; --j, ++i)
    {
      sum -= Math.pow(list.get(j).abs(), 2);
      squaresSum[i] = sum;
      System.out.print("("+i+") " + sum + " ");
    }
    System.out.println("");
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
