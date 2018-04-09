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

public class TestRoleKey extends TestCase
{
  public TestRoleKey( String name)
  {
    super( name );
  }

  public void testKeyExists()
  {
    HashSet aSet = new HashSet();
    TVCa aTVCa = KBCPersistentObjectFactory.getSingleton().createTVCa( "Hello" );
    RoleKey roleKey1 = new RoleKey( aTVCa, "parent" );
    RoleKey roleKey2 = new RoleKey( aTVCa, "par" + "ent" );
    aSet.add( roleKey1 );
    assert( "Should be able to find the role key", aSet.contains( roleKey2 ) );
  }

  protected void setUp()
  throws Exception
  {
    MiddleWareFramework.getSingleton().establishCommunications();
  }


  public static Test suite( )
  {
    return new TestSuite( TestRoleKey.class );
  }

  }