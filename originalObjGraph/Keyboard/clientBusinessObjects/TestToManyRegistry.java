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

public class TestToManyRegistry extends TestCase
{

  private ToManyRegistry aToManyRegistry;
  private TVCa aTVCa;
  private TVCb aTVCb;
  private TVComposite aTVComposite;
  private RoleKey roleKey1;
  private RoleKey roleKey2;

  public TestToManyRegistry( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestToManyRegistry.class );
  }

  protected ToManyRegistry createRegistry()
  {
    AssociationsCoordinator anAC = new AssociationsCoordinator();
    return new ToManyRegistry( anAC );
  }

  public void setUp()
  {
    MiddleWareFramework.getSingleton().establishCommunications();
    aToManyRegistry = createRegistry();
    aTVCa = KBCPersistentObjectFactory.getSingleton().createTVCa( "leafa" );
    aTVCb = KBCPersistentObjectFactory.getSingleton().createTVCb( "leafb" );
    aTVComposite = KBCPersistentObjectFactory.getSingleton().createTVComposite( "mainTrunk" );
    roleKey1 = new RoleKey( aTVComposite, "children" );
    roleKey2 = new RoleKey( aTVComposite, "chil" + "dren" );
    aToManyRegistry.put( roleKey1, aTVCa );
    aToManyRegistry.put( roleKey1, aTVCb );
  }

  public void testRemove()
  {
    aToManyRegistry.remove( roleKey2, aTVCa );
    Collection toMany = aToManyRegistry.get( roleKey2 );
    assert( "Should not be able to find leafa", ! toMany.contains( aTVCa ) );
    assert( "Should be able to find leafb", toMany.contains( aTVCb ) );
  }


  public void testToManysExist()
  {
    Collection toMany = aToManyRegistry.get( roleKey2 );
    assert( "Should be able to find leafa", toMany.contains( aTVCa ) );
    assert( "Should be able to find leafb", toMany.contains( aTVCb ) );
  }

}