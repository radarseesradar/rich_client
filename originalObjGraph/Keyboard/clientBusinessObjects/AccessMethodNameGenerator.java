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

public class AccessMethodNameGenerator
{
  private static AccessMethodNameGenerator singleton = null;
  private Map specialPlurals = new HashMap();

  public static AccessMethodNameGenerator getSingleton ()
  {
    if (singleton == null)
      singleton = new AccessMethodNameGenerator();
    return singleton;
  }

  private AccessMethodNameGenerator()
  {
    specialPlurals.put( "children", "child" );
  }

  public String getSingular( String _aRoleName )
  {
    String aRoleName = unCapitalize( _aRoleName );
    if( specialPlurals.containsKey( aRoleName ) )
      return (String) specialPlurals.get( aRoleName );
    if( aRoleName.endsWith( "s" ) )
    {
      return aRoleName.substring( 0, aRoleName.length() - 1 );
    }
    return _aRoleName;
  }

  public String unCapitalize( String aString )
  {
    StringBuffer sbuf = new StringBuffer( aString );
    sbuf.setCharAt( 0, Character.toLowerCase( sbuf.charAt(0) ) );
    return sbuf.toString();
  }

  public String capitalize( String aString )
  {
    StringBuffer sbuf = new StringBuffer( aString );
    sbuf.setCharAt( 0, Character.toUpperCase( sbuf.charAt(0) ) );
    return sbuf.toString();
  }

  public String getter( String aRoleName )
  {
    return "get" + capitalize( aRoleName );
  }

  public String setter( String aRoleName )
  {
    return "set" + capitalize( aRoleName );
  }

  public String adder( String aRoleName )
  {
    return "add" + capitalize( getSingular( aRoleName ) );
  }

  public String remover( String aRoleName )
  {
    return "remove" + capitalize( getSingular( aRoleName ) );
  }
}