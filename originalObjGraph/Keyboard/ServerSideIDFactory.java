package Keyboard;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.io.*;

public class ServerSideIDFactory
{
  private static ServerSideIDFactory singleton = null;
  public static final String idFilename = "id.txt";

  private ServerSideIDFactory()
  {
  }

  public static ServerSideIDFactory getSingleton ()
  {
    if (singleton == null)
      singleton = new ServerSideIDFactory();
    return singleton;
  }

  public synchronized long allocateIDs( int quantity )
  {
    RandomAccessFile rafs = null;
    long beginning = 0;
    long nextBeginning = 0;
    try
    {
      rafs = new RandomAccessFile( idFilename, "rw" );
      beginning = Long.parseLong( rafs.readLine() );
      nextBeginning = beginning + quantity;
      rafs.setLength( 0L );
      rafs.writeBytes( Long.toString( nextBeginning ) );
      rafs.close();
    }
    catch( IOException e )
    {
      throw new KBAllocateIDsException( e.getMessage() );
    }
    return beginning;
  }

  public static void main(String[] args)
  {
    ServerSideIDFactory anIDFactory = ServerSideIDFactory.getSingleton();
    long []gather = new long[7];
    for( int i=0; i < 7; i++ )
    {
        gather[i] = anIDFactory.allocateIDs( 100 );
    }
    for( int i=0; i < 7; i++ )
    {
      System.out.println( gather[i] );
    }
  }
}