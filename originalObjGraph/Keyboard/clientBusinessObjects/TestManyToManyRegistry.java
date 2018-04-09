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

public class TestManyToManyRegistry extends TestCase
{
  private ManyToManyRegistry aManyToManyRegistry;
  private TVCa aTVCa;
  private TVCb aTVCb;
  private RoleKey leafKeya1;
  private RoleKey leafKeya2;
  private RoleKey leafKeyb1;
  private RoleKey leafKeyb2;

  private TVCa aTVCax;
  private TVCb aTVCbx;
  private RoleKey leafKeyax1;
  private RoleKey leafKeyax2;
  private RoleKey leafKeybx1;
  private RoleKey leafKeybx2;

  public TestManyToManyRegistry( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestManyToManyRegistry.class );
  }

  public void setUp()
  {
    MiddleWareFramework.getSingleton().establishCommunications();
    AssociationsCoordinator anAC = new AssociationsCoordinator();
    aManyToManyRegistry = anAC.getManyToManyRegistry();
    aTVCa = KBCPersistentObjectFactory.getSingleton().createTVCa( "leafa" );
    leafKeya1 = new RoleKey( aTVCa, "brothers" );
    leafKeya2 = new RoleKey( aTVCa, "brothers" );
    aTVCb = KBCPersistentObjectFactory.getSingleton().createTVCb( "leafb" );
    leafKeyb1 = new RoleKey( aTVCb, "sisters" );
    leafKeyb2 = new RoleKey( aTVCb, "sisters" );

    aTVCax = KBCPersistentObjectFactory.getSingleton().createTVCa( "leafax" );
    leafKeyax1 = new RoleKey( aTVCax, "brothers" );
    leafKeyax2 = new RoleKey( aTVCax, "brothers" );
    aTVCbx = KBCPersistentObjectFactory.getSingleton().createTVCb( "leafbx" );
    leafKeybx1 = new RoleKey( aTVCbx, "sisters" );
    leafKeybx2 = new RoleKey( aTVCbx, "sisters" );
    aManyToManyRegistry.associate( leafKeya1, leafKeyb1 );
    aManyToManyRegistry.associate( leafKeya1, leafKeybx1 );
    aManyToManyRegistry.associate( leafKeyax1, leafKeyb1 );
    aManyToManyRegistry.associate( leafKeyax1, leafKeybx1 );
  }

  public void testDisassociate()
  {
    aManyToManyRegistry.disassociate( leafKeyb2, leafKeya2 );
    Collection actualBrothers = aManyToManyRegistry.getToMany( leafKeya2 );
    Collection actualSisters = aManyToManyRegistry.getToMany( leafKeyb2 );
    Collection expectedBrothers = Collections.singleton( aTVCbx );
    Collection expectedSisters = Collections.singleton( aTVCax );
    assert( "aTVCb should have only the single sister aTVCax", expectedSisters.containsAll( actualSisters ) && actualSisters.containsAll( expectedSisters ) );
    assert( "aTVC should have only the single brother aTVCbx", expectedBrothers.containsAll( actualBrothers ) && actualBrothers.containsAll( expectedBrothers ) );
  }

  public void testGetBrothers()
  {
    Collection aCollection;
    aCollection = aManyToManyRegistry.getToMany( leafKeya2 );
    Collection brothers = new ArrayList();
    brothers.add( aTVCb );
    brothers.add( aTVCbx );
    assert( "aTVCa's brothers should be aTVCb and aTVCbx", brothers.containsAll( aCollection ) && aCollection.containsAll( brothers ) );
  }

  public void testGetBrothers2()
  {
    Collection aCollection;
    aCollection = aManyToManyRegistry.getToMany( leafKeyax2 );
    Collection brothers = new ArrayList();
    brothers.add( aTVCb );
    brothers.add( aTVCbx );
    assert( "aTVCax's brothers should be aTVCb and aTVCbx", brothers.containsAll( aCollection ) && aCollection.containsAll( brothers ) );
  }

  public void testGetSisters()
  {
    Collection aCollection;
    aCollection = aManyToManyRegistry.getToMany( leafKeyb2 );
    Collection brothers = new ArrayList();
    brothers.add( aTVCa );
    brothers.add( aTVCax );
    assert( "aTVCb's sisters should be aTVCa and aTVCax", brothers.containsAll( aCollection ) && aCollection.containsAll( brothers ) );
  }

  public void testGetSisters2()
  {
    Collection aCollection;
    aCollection = aManyToManyRegistry.getToMany( leafKeybx2 );
    Collection brothers = new ArrayList();
    brothers.add( aTVCa );
    brothers.add( aTVCax );
    assert( "aTVCbx's sisters should be aTVCa and aTVCax", brothers.containsAll( aCollection ) && aCollection.containsAll( brothers ) );
  }

}