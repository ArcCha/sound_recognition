/**
 * 
 */
package pl.krakow.v_lo.algosound;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.apache.commons.math3.complex.Complex;

/**
 * @author arccha
 */
public class Command extends Observable
{
  private String        name;
  private List<Complex> data;

  public Command()
  {
    name = null;
    data = new ArrayList<Complex>();
  }

  public List<Complex> getData()
  {
    return data;
  }

  public String getName()
  {
    return name;
  }

  @Override
  public String toString()
  {
    return "Command [name=" + name + "]";
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setData(List<Complex> data)
  {
    this.data = data;
  }
}
