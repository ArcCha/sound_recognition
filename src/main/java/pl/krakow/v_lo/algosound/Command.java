package pl.krakow.v_lo.algosound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    name = "";
    data = new ArrayList<Complex>();
  }
  
  public Command(String name, List<Complex> data)
  {
    this.name = name;
    this.data = data;
  }

  public static List<Complex> parseBytes(ByteArrayOutputStream stream)
  {
    List<Complex> result = new ArrayList<Complex>();
    byte[] byteArr = stream.toByteArray();
    ByteBuffer buff = ByteBuffer.wrap(byteArr);
  
    buff.order(ByteOrder.LITTLE_ENDIAN);
    while (buff.hasRemaining())
    {
      result.add(new Complex((double) buff.getShort(), 0));
    }
    
    return result;
  }
  
  public static ByteArrayOutputStream convertComplexToByteArrayOutputStream(List<Complex> data) throws IOException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    for(Complex complex : data)
    {
      ByteBuffer buff = ByteBuffer.allocate(2);
      buff.order(ByteOrder.LITTLE_ENDIAN);
      buff.putShort((short) complex.getReal());
      out.write(buff.array());
    }
    return out;
  }
  
  public List<Complex> getData()
  {
    return data;
  }
  
  public List<Complex> getAmplitudeData()
  {
    List<Complex> amplitudeData = new ArrayList<Complex>();
    for(Complex value : data)
      amplitudeData.add(new Complex(value.getReal() / 32678.0));
    return amplitudeData;
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

  public void replicate(Command command)
  {
    name = command.getName();
    data = command.getData();
    setChanged();
  }
}
