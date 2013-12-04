package pl.krakow.v_lo.algosound;


public class CommandManager
{
  private Database database;
  private Command  current;
  private Command  matched;

  CommandManager(Database database)
  {
    this.database = database;
  }

  public Command getCurrent()
  {
    return current;
  }

  public void setCurrent(Command current)
  {
    this.current = current;
  }

  public Command getMatched()
  {
    return matched;
  }

  public void setMatched(Command matched)
  {
    this.matched = matched;
  }
}
