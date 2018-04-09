package smartClient.framework;


import java.util.*;
import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class NullToMany implements ToMany, Serializable
{
  private static NullToMany singleton = null;

  private NullToMany()
  {
  }

  public static NullToMany getSingleton ()
  {
    if (singleton == null)
      singleton = new NullToMany();
    return singleton;
  }

  public void add( Object toObject )
  {
  }

  public Collection getAll()
  {
    return (Collection) Collections.EMPTY_LIST;
  }

  public void remove( Object toObject )
  {
  }

}