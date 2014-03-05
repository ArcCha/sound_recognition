package pl.krakow.v_lo.algosound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Command extends Observable
{
  private String        name;
  private List<Short> data;

  public Command()
  {
    name = "";
    data = new ArrayList<Short>();
  }
  
  public Command(String name, List<Short> data)
  {
    this.name = name;
    this.data = data;
  }

  public static List<Short> parseBytes(ByteArrayOutputStream stream)
  {
    List<Short> result = new ArrayList<Short>();
    byte[] byteArr = stream.toByteArray();
    ByteBuffer buff = ByteBuffer.wrap(byteArr);
  
    buff.order(ByteOrder.LITTLE_ENDIAN);
    while (buff.hasRemaining())
    {
      result.add(buff.getShort());
    }
    
    return result;
  }
  
  public static ByteArrayOutputStream convertShortToByteArrayOutputStream(List<Short> data) throws IOException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    for(Short value : data)
    {
      ByteBuffer buff = ByteBuffer.allocate(2);
      buff.order(ByteOrder.LITTLE_ENDIAN);
      buff.putShort(value);
      out.write(buff.array());
    }
    return out;
  }
  
  public List<Short> getData()
  {
    return data;
  }
  
  public List<Double> getAmplitudeData()
  {
    List<Double> amplitudeData = new ArrayList<Double>();
    for(Short value : data)
      amplitudeData.add(value.doubleValue() / (1 << 15));
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

  public void setData(List<Short> data)
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
