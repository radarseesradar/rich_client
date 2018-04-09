package smartClient.framework;


import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class NullToOne implements ToOne, Serializable
{
  private static NullToOne singleton = null;

  private NullToOne()
  {
  }

  public static NullToOne getSingleton ()
  {
    if (singleton == null)
      singleton = new NullToOne();
    return singleton;
  }

  public Object get()
  {
    return null;
  }

  public void set( Object toObject )
  {
  }
}