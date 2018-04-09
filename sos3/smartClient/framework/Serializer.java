
package smartClient.framework;

import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class Serializer
{

  public Serializer()
  {
  }


  public byte[] serialize (Object obj)
  {
  	try
    {
	    // Serialize the array
	    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	    java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
	    oos.writeObject(obj);
	
	    // return the byte array
	    return baos.toByteArray();
    }
    catch( Exception  e )
    {
    	e.printStackTrace();
        throw new InternalError( "Problem serializing" + obj );
    }
  }

  public Object deserialize (byte[] data)
  {
  	try
    {
	    ByteArrayInputStream bais = new ByteArrayInputStream(data);
	    ObjectInputStream ois = new ObjectInputStream(bais);
	    Object obj = ois.readObject();
	    return obj;
    }
    catch( Exception  e )
    {
    	e.printStackTrace();
        throw new InternalError( "Problem deserializing a byte array" );
    }
  }
}