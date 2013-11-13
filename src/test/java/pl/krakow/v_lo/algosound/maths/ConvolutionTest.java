package pl.krakow.v_lo.algosound.maths;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.math3.complex.Complex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ConvolutionTest
{
  final double DELTA = 1e-4;
  List<Complex> pattern;
  List<Complex> text;
  List<Double> expectedOutput;
  
  public ConvolutionTest(List<Complex> pattern, List<Complex> text, List<Double> expectedOutput)
  {
    this.pattern = pattern;
    this.text = text;
    this.expectedOutput = expectedOutput;
  }
  
  @Parameters
  public static Collection<Object[]> generateData()
  {
    InputStream in = FastFourierTransform.class.getClassLoader().getResourceAsStream("convolutionTests.txt");
    Scanner scanner = new Scanner(in);
    scanner.useLocale(Locale.US);
    final int numberOfTestCases = scanner.nextInt();
    scanner.nextLine();
    Object[][] inputData = new Object[numberOfTestCases][3];
    for(int i = 0; i < numberOfTestCases; ++i)
    {
      String line = scanner.nextLine();
      String[] splitted = line.split("\\|"); 
      
      ArrayList<Complex> patternInput = new ArrayList<Complex>();
      Scanner patternScanner = new Scanner(splitted[0]);
      patternScanner.useLocale(Locale.US);
      while(patternScanner.hasNextDouble())
      {
        double real = patternScanner.nextDouble();
        double imag = patternScanner.nextDouble();
        patternInput.add(new Complex(real, imag));
      }
      patternScanner.close();
      inputData[i][0] = patternInput;
      
      ArrayList<Complex> textInput = new ArrayList<Complex>();
      Scanner textScanner = new Scanner(splitted[1]);
      textScanner.useLocale(Locale.US);
      while(textScanner.hasNextDouble())
      {
        double real = textScanner.nextDouble();
        double imag = textScanner.nextDouble();
        textInput.add(new Complex(real, imag));
      }
      textScanner.close();
      inputData[i][1] = textInput;
      
      List<Double> result = new ArrayList<Double>();
      Scanner resultScanner = new Scanner(splitted[2]);
      resultScanner.useLocale(Locale.US);
      while(resultScanner.hasNextDouble())
      {
        double val = resultScanner.nextDouble();
        result.add(val);
      }
      resultScanner.close();
      inputData[i][2] = result;
    }
    scanner.close();
    return Arrays.asList(inputData);
  }
  
  @Test
  public void testResultOfConvolution()
  {
    Convolution convolution = new Convolution();
    List<Double> result = convolution.countSumOfDiffSquares(pattern, text);
    for (int i = 0; i < expectedOutput.size(); ++i)
    {
      assertEquals(expectedOutput.get(i).doubleValue(), result.get(i).doubleValue(), DELTA);
    }
  }
}
