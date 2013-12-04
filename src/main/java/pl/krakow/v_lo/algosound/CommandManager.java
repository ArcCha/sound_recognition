package pl.krakow.v_lo.algosound;

import java.util.Observable;

public class CommandManager extends Observable
{
  private Database database;
  private Command  current;
  private Command  matched;

  CommandManager(Database database)
  {
    this.database = database;
  }
}
