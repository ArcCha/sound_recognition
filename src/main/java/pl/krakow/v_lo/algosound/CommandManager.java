package pl.krakow.v_lo.algosound;


public class CommandManager
{
  private Command  pattern;
  private Command  matched;

  CommandManager()
  {
    this.pattern = new Command();
    this.matched = new Command();
  }

  public Command getPattern()
  {
    return pattern;
  }

  public void setPattern(Command pattern)
  {
    this.pattern.replicate(pattern);
    this.pattern.notifyObservers();
  }

  public Command getMatched()
  {
    return matched;
  }

  public void setMatched(Command matched)
  {
    this.matched.replicate(matched);
    this.matched.notifyObservers();
  }
}
