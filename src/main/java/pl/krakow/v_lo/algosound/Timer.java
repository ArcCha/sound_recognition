package pl.krakow.v_lo.algosound;

import java.util.concurrent.atomic.AtomicBoolean;

public class Timer extends Thread
{
  private AtomicBoolean bool;
  
  public Timer(AtomicBoolean bool)
  {
    this.bool = bool;
  }
  
  @Override
  public void run()
  {
    try
    {
      Thread.sleep(1500);
      bool.set(true);
    }
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
