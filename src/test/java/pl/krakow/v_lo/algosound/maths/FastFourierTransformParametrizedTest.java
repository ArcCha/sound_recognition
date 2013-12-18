/**
 * 
 */
package pl.krakow.v_lo.algosound.maths;

import static org.junit.Assert.assertEquals;

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

/**
 * @author arccha
 */
@RunWith(Parameterized.class)
public class FastFourierTransformParametrizedTest
{

  public static final double DELTA = 1e-2;
  
  List<Complex> inputData;
  List<Complex> expectedOutput;

  public FastFourierTransformParametrizedTest(List<Complex> inputData, List<Complex> expectedOutput)
  {
    this.inputData = inputData;
    this.expectedOutput = expectedOutput;
  }

  @Parameters
  public static Collection<Object[]> generateData()
  {
    InputStream in = FastFourierTransform.class.getClassLoader().getResourceAsStream("fftTests.txt");
    Scanner scanner = new Scanner(in);
    scanner.useLocale(Locale.US);
    final int numberOfTestCases = scanner.nextInt();
    scanner.nextLine();
    Object[][] inputData = new Object[numberOfTestCases][2];
    for(int i = 0; i < numberOfTestCases; ++i)
    {
      String line = scanner.nextLine();
      String[] splitted = line.split("\\|"); 
      
      ArrayList<Complex> input = new ArrayList<Complex>();
      Scanner inputScanner = new Scanner(splitted[0]);
      while(inputScanner.hasNextDouble())
      {
        double real = inputScanner.nextDouble();
        double imag = inputScanner.nextDouble();
        input.add(new Complex(real, imag));
      }
      inputScanner.close();
      inputData[i][0] = input;
      
      ArrayList<Complex> result = new ArrayList<Complex>();
      Scanner resultScanner = new Scanner(splitted[1]);
      resultScanner.useLocale(Locale.US);
      while(resultScanner.hasNextDouble())
      {
        double real = resultScanner.nextDouble();
        double imag = resultScanner.nextDouble();
        result.add(new Complex(real, imag));
      }
      resultScanner.close();
      inputData[i][1] = result;
    }
    scanner.close();
    return Arrays.asList(inputData);
  }

  @Test
  public void testForwardBackwardEquality()
  {
    FastFourierTransform transform = new FastFourierTransform(inputData);
    transform.transformForward();
    transform.transformBackward();
    List<Complex> result = transform.getResult();
    for(int i = 0; i < expectedOutput.size(); ++i)
    {
      assertEquals(inputData.get(i).getReal(), result.get(i).getReal(), DELTA);
      assertEquals(inputData.get(i).getImaginary(), result.get(i).getImaginary(), DELTA);
    }
  }
  
  @Test
  public void testResultOfTransform()
  {
    FastFourierTransform transform = new FastFourierTransform(inputData);
    transform.transformForward();
    List<Complex> result = transform.getResult();
    for(int i = 0; i < result.size(); ++i)
    {
      assertEquals(expectedOutput.get(i).getReal(), result.get(i).getReal(), DELTA);
      assertEquals(expectedOutput.get(i).getImaginary(), result.get(i).getImaginary(), DELTA);
    }
  }
}
