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
import java.lang.reflect.*;
import java.lang.*;
import Keyboard.clientBusinessObjects.*;
import Keyboard.*;
import junit.framework.*;
import java.io.*;

public class TestOneToOne extends TestCase
{
  public TestOneToOne( String name)
  {
    super( name );
  }

  protected void setUp()
  throws Exception
  {
    MiddleWareFramework.getSingleton().establishCommunications();
  }

  public static Test suite( )
  {
    return new TestSuite( TestOneToOne.class );
  }

  public void testSetAndGet()
  {
    TVCa aWife = KBCPersistentObjectFactory.getSingleton().createTVCa( "wife" );
    TVCb aHusband = KBCPersistentObjectFactory.getSingleton().createTVCb( "husband" );
    aHusband.setWife( (TVCa) aWife );
    assert( "husband should hav a wife", aHusband.getWife().equals( aWife ) );
    assert( "wife should hav a husband", aWife.getHusband().equals( aHusband ) );
  }

}