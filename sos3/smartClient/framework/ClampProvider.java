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
public class ClampProvider
{
  private static ClampProvider singleton = null;
  private Map retrievalClamps = null;
  private Map storageClamps = null;

  private ClampProvider()
  {
  }

  public void clear()
  {
  	retrievalClamps = null;
    storageClamps = null;
  }

  private String getRetrievalClampsFilename()
  {
  	return (String) SmartClientFramework.getSingleton().getProperties().get( "RETRIEVAL_CLAMPS" );
  }

  private String getStorageClampsFilename()
  {
  	return (String) SmartClientFramework.getSingleton().getProperties().get( "STORAGE_CLAMPS" );
  }

  public Map getRetrievalClamps()
  {
    if( retrievalClamps == null )
      retrievalClamps = readClampFile( getRetrievalClampsFilename() );
    return retrievalClamps;
  }

  public Map getStorageClamps()
  {
    if( storageClamps == null )
      storageClamps = readClampFile( getStorageClampsFilename() );
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
      throw new ClampFileIOException ( e.getMessage() );
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

}