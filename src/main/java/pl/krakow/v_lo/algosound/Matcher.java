package pl.krakow.v_lo.algosound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import pl.krakow.v_lo.algosound.maths.FastFourierTransform;;

public class Matcher
{
  private Command             pattern;
  private Database            database;
  private List<Complex> patternSamples;
  private static final int    matchingSampleSize = 1024;

  public Matcher(Command pattern, Database database)
  {
    this.pattern = pattern;
    this.database = database;
    patternSamples = new ArrayList<Complex>();
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
    
    for (Command command : database.getAllCommands())
    {
      if (command.getName().equals("command.wav"))
        continue;
      
      System.out.println("### Matching " + command.getName() + "...");
      
      MatchedResult matchedResult = match(command);
      result.add(matchedResult);
      
      System.out.println("### Matching rate: " + matchedResult.getMatchingRate());
    }
    Collections.sort(result);
    return result;
  }

  private MatchedResult match(Command command)
  {
    MatchedResult result = new MatchedResult(command, 1e60);
    List<Complex> textSamples = computeSamplesFromCommand(command);
    double matchingRate = 0;
    final int patternEnd = 8;
    final int textEnd = 8;
    // ucinaj wzorzec od przodu i przesuwaj
    for (int patternBegin = 0; patternBegin < patternEnd; ++patternBegin)
      // przesuwaj wzorzec względem porównywanego tekstu (ucinaj tył tekstu)
      for (int textBegin = 0; textBegin < textEnd; ++textBegin)
      {
        matchingRate = matchSamples(patternSamples, patternBegin, textSamples, textBegin);
        if(matchingRate < result.getMatchingRate())
        {
//          System.out.println("matching rate (" + patternBegin + ", " + textBegin + "): " + matchingRate);
          result.setMatchingRate(matchingRate);
        }
      }
    return result;
  }

  private double matchSamples(List<Complex> patternSamples, int patternBegin, 
                              List<Complex> textSamples, int textBegin)
  {
    double matchingRate = 0;
    int pattern_i = matchingSampleSize * patternBegin;
    int text_i = matchingSampleSize * textBegin;
    while(text_i < textSamples.size() && pattern_i < patternSamples.size())
    {
      double textVal = textSamples.get(text_i).abs();
      double patternVal = patternSamples.get(pattern_i).abs();
      matchingRate += Math.pow((textVal - patternVal), 2);
      ++text_i;
      ++pattern_i;
    }
    return matchingRate;
  }

  public static List<Complex> computeSamplesFromCommand(Command command)
  {
    return computeSamplesFromCommand(command, matchingSampleSize);
  }

  public static List<Complex> computeSamplesFromCommand(Command command, int matchingSampleSize)
  {
    List<Complex> result = new ArrayList<Complex>();
    List<Complex> rawData = command.getRawData();
    int idx = 0;
    while (idx + matchingSampleSize - 1 < rawData.size())
    {
      List<Complex> sample = rawData.subList(idx, idx + matchingSampleSize);
      FastFourierTransform fft = new FastFourierTransform(sample);
      result.addAll(fft.transformForward());
      idx += matchingSampleSize;
    }
    return result;
  }
}
