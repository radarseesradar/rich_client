package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.util.*;
import java.io.*;

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

  public boolean addAll( Collection aCollection )
  {
    return true;
  }
}