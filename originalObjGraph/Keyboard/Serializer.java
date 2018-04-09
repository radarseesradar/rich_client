
/**
 * Title:        Persistent Object Graph<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Steve McDaniel<p>
 * Company:      Salient<p>
 * @author Steve McDaniel
 * @version 1.0
 */
package Keyboard;

import java.io.*;

public class Serializer
{

  public Serializer()
  {
  }


  public byte[] serialize (Object obj) throws Exception
  {
    // Serialize the array
    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
    java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
    oos.writeObject(obj);

    // return the byte array
    return baos.toByteArray();
    }

  public Object deserialize (byte[] data) throws Exception
  {
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ObjectInputStream ois = new ObjectInputStream(bais);
    Object obj = ois.readObject();
    return obj;
    }
}