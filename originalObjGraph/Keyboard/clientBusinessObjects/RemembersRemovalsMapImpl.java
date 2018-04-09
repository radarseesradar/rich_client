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

public class RemembersRemovalsMapImpl implements Map, Serializable
{
  private Map primaryMap = new HashMap();
  private Map rememberedRemovalsMap = new HashMap();

  private Map getCombinedMap()
  {
    Map combinedMap = (Map) new HashMap( primaryMap );
    combinedMap.putAll( rememberedRemovalsMap );
    return combinedMap;
  }

  public void clear()
  {
    primaryMap.clear();
  }

  public boolean containsKey( Object key )
  {
    return primaryMap.containsKey( key );
  }

  public boolean containsValue( Object value )
  {
    return primaryMap.containsValue( value );
  }

  public Set entrySet()
  {
    return primaryMap.entrySet();
  }

  public boolean equals( Object o )
  {
    return primaryMap.equals( o );
  }

  public Object get( Object key )
  {
    return primaryMap.get( key );
  }

  public int hashCode()
  {
    return primaryMap.hashCode();
  }

  public boolean isEmpty()
  {
    return primaryMap.isEmpty();
  }

  public Set keySet()
  {
    return primaryMap.keySet();
  }

  public Object put( Object key, Object value )
  {
    return primaryMap.put( key, value );
  }

  public void putAll( Map t )
  {
    primaryMap.putAll( t );
  }

  public Object remove( Object key )
  {
    rememberedRemovalsMap.put( key, key );
    return primaryMap.remove( key );
  }

  public int size()
  {
    return primaryMap.size();
  }

  public Collection values()
  {
    if( MiddleWareFramework.getSingleton().isIncludingRemovals() )
    {
      return getCombinedMap().values();
    }
    else
    {
      return primaryMap.values();
    }
  }

}