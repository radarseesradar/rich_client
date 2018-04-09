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

public class ClampProvider
{
  private static ClampProvider singleton = null;
  public static final String defaultClampsFilename = "defaultClamps.txt";
  public static final String storageClampsFilename = "storageClamps.txt";
  private Map defaultClamps = null;
  private Map storageClamps = null;

  private ClampProvider()
  {
  }

  public Map getDefaultClamps()
  {
    if( defaultClamps == null )
      defaultClamps = readClampFile( defaultClampsFilename );
    return defaultClamps;
  }

  public Map getStorageClamps()
  {
    if( storageClamps == null )
      storageClamps = readClampFile( storageClampsFilename );
    return storageClamps;
  }

  public void clearStorageClampsCommandLists()
  {
    for( Iterator anIterator = getStorageClamps().keySet().iterator(); anIterator.hasNext(); )
    {
      ((Clamp) anIterator.next()).clearChangeList();
    }
  }

  public static ClampProvider getSingleton ()
  {
    if (singleton == null)
      singleton = new ClampProvider();
    return singleton;
  }

  private Map readClampFile( String clampFilename )
  {
    Map aMapOfClamps = new HashMap();
    FileInputStream fis = null;
    try
    {
      fis = new FileInputStream( clampFilename );
      InputStreamReader isr = new InputStreamReader( fis );
      BufferedReader br = new BufferedReader( isr );
      String nextLine = null;
      String fromClassName = null;
      String toRoleName = null;
      StringTokenizer aTokenizer = null;
      Clamp nextClamp = null;
      while( (nextLine = br.readLine() ) != null )
      {
        if( nextLine.startsWith( "#" ) )
          continue;
        if( nextLine.equals( "" ) )
          continue;
        aTokenizer = new StringTokenizer( nextLine );
        fromClassName = aTokenizer.nextToken();
        toRoleName = aTokenizer.nextToken();
        nextClamp = new Clamp( fromClassName, toRoleName );
        aMapOfClamps.put( nextClamp, nextClamp );
      }
    }
    catch( IOException e )
    {
      throw new KBClampFileIOException( e.getMessage() );
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

    return aMapOfClamps;
  }

  public static void main( String []args )
  {
    ClampProvider.getSingleton().readClampFile( defaultClampsFilename );
  }

}