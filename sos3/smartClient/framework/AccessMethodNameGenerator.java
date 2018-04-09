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
public class AccessMethodNameGenerator
{
  private static AccessMethodNameGenerator singleton = null;
  private Map specialPlurals;

  public static AccessMethodNameGenerator getSingleton ()
  {
    if (singleton == null)
      singleton = new AccessMethodNameGenerator();
    return singleton;
  }

  private AccessMethodNameGenerator()
  {
  }

  public boolean specialPluralsAreInitialized()
  {
  	return !( specialPlurals == null );
  }

  public void setSpecialPluralsUsing( String filename )
  {
    FileInputStream fis = null;
  	try
    {
  	  specialPlurals = (Map) new Properties();
      fis = new FileInputStream( filename );
      ((Properties)specialPlurals).load( fis );
    }
    catch ( IOException ioe )
    {
      System.out.println("Error in Reading properties File :" + ioe);
    }
    finally
    {
      try
      {
        if( fis != null )
          fis.close();
      }
      catch( IOException e )
      {
      }
    }
  }

  public String getSingular( String _aRoleName )
  {
    String aRoleName = unCapitalize( _aRoleName );
    if( ! this.specialPluralsAreInitialized() )
    	this.setSpecialPluralsUsing( (String) SmartClientFramework.getSingleton().getProperties().get( "SPECIAL_PLURALS" ) );
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