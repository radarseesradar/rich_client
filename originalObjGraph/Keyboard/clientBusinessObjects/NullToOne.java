package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.io.*;

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