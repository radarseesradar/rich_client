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

public class TestManyToOne extends TestCase
{
  public TestManyToOne( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestManyToOne.class );
  }

  protected void setUp()
  throws Exception
  {
    MiddleWareFramework.getSingleton().establishCommunications();
  }

  public void testSetAndGet()
  {
  TVComponent aLeaf = KBCPersistentObjectFactory.getSingleton().createTVCa( "leafa" );
  TVComposite aTrunk = KBCPersistentObjectFactory.getSingleton().createTVComposite( "trunka" );
  aLeaf.setParent( aTrunk );
  assert( "Parent should equal aTrunk", aLeaf.getParent().equals( aTrunk ) );
  }

}