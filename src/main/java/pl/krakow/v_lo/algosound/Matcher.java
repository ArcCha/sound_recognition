package pl.krakow.v_lo.algosound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import pl.krakow.v_lo.algosound.maths.FFTBasedAlgorithms;
import pl.krakow.v_lo.algosound.maths.FastFourierTransform;
import pl.krakow.v_lo.algosound.maths.WindowFunction;

public class Matcher
{
  private Command            pattern;
  private Database           database;
  private List<Complex>      patternSamples;
  private List<Complex>      absPattern;
  private List<Complex>      absText;
  private FFTBasedAlgorithms fftAlg;
  private static final int   matchingSampleSize = 1024;
  private final int          comparisonRange    = 12 * matchingSampleSize;

  public Matcher(Command pattern, Database database)
  {
    this.pattern = pattern;
    this.database = database;
    patternSamples = new ArrayList<Complex>();
    fftAlg = new FFTBasedAlgorithms();
  }

  public List<Complex> getPatternSamples()
  {
    return patternSamples;
  }

  public List<MatchedResult> match()
  {
    System.out.println("Starting matching.");
    
    List<MatchedResult> result = new ArrayList<MatchedResult>();
    patternSamples = computeSamplesFromCommand(pattern);
    absPattern = getAbsValues(patternSamples);

    for (Command command : database.getAllCommands())
    {
      if (command.getName().equals("command"))
        continue;

      System.out.print("### Matching " + command.getName() + "... ");

      MatchedResult matchedResult = match(command);
      result.add(matchedResult);

      System.out.println(matchedResult.getMatchingRate());
    }
    Collections.sort(result);
    return result;
  }

  private MatchedResult match(Command command)
  {
    absText = getAbsValues(computeSamplesFromCommand(command));

    List<Double> squaredErrorAll = fftAlg.countSquaredError(absPattern, absText);
    final int equalPlaced = patternSamples.size() - 1;

    MatchedResult result = new MatchedResult(command, 1e60);
    int i = equalPlaced - comparisonRange;
    for (; i < equalPlaced + comparisonRange; i += matchingSampleSize)
    {
      double matchingRate = squaredErrorAll.get(i);
      if (matchingRate < result.getMatchingRate())
        result.setMatchingRate(matchingRate);
    }
    return result;
  }

  private List<Complex> getAbsValues(List<Complex> list)
  {
    List<Complex> absList = new ArrayList<Complex>(list.size());
    for (Complex value : list)
      absList.add(new Complex(value.abs(), 0));
    return absList;
  }

  public static List<Complex> computeSamplesFromCommand(Command command)
  {
    return computeSamplesFromCommand(command, matchingSampleSize);
  }

  public static List<Complex> computeSamplesFromCommand(Command command, int matchingSampleSize)
  {
    WindowFunction windowFunction = new WindowFunction();
    List<Complex> rawData = new ArrayList<Complex>();
    for (Double value : command.getAmplitudeData())
      rawData.add(new Complex(value));
    List<Complex> result = new ArrayList<Complex>(rawData.size());
    
    int idx = 0;
    while (idx + matchingSampleSize - 1 < rawData.size())
    {
      List<Complex> sample = rawData.subList(idx, idx + matchingSampleSize);
      
      windowFunction.setData(sample);
      sample = windowFunction.computeFlatTopWindow();
      
      FastFourierTransform fft = new FastFourierTransform(sample);
      result.addAll(fft.transformForward());
      idx += matchingSampleSize;
    }
    return result;
  }
}
