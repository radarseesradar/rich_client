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

public class TestOneToOneRegistry extends TestCase
{
  private OneToOneRegistry aOneToOneRegistry;
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

  public TestOneToOneRegistry( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestOneToOneRegistry.class );
  }

  public void setUp()
  {
    MiddleWareFramework.getSingleton().establishCommunications();
    AssociationsCoordinator anAC = new AssociationsCoordinator();
    aOneToOneRegistry = anAC.getOneToOneRegistry();
    aTVCa = KBCPersistentObjectFactory.getSingleton().createTVCa( "leafa" );
    leafKeya1 = new RoleKey( aTVCa, "husband" );
    leafKeya2 = new RoleKey( aTVCa, "husband" );
    aTVCb = KBCPersistentObjectFactory.getSingleton().createTVCb( "leafb" );
    leafKeyb1 = new RoleKey( aTVCb, "wife" );
    leafKeyb2 = new RoleKey( aTVCb, "wife" );
    aOneToOneRegistry.associate( leafKeya1, leafKeyb1 );

    aTVCax = KBCPersistentObjectFactory.getSingleton().createTVCa( "leafax" );
    leafKeyax1 = new RoleKey( aTVCax, "husband" );
    leafKeyax2 = new RoleKey( aTVCax, "husband" );
    aTVCbx = KBCPersistentObjectFactory.getSingleton().createTVCb( "leafbx" );
    leafKeybx1 = new RoleKey( aTVCbx, "wife" );
    leafKeybx2 = new RoleKey( aTVCbx, "wife" );
    aOneToOneRegistry.associate( leafKeyax1, leafKeybx1 );
  }

  public void testGetToOne()
  {
    assert( "should get back a wife", aTVCa.equals( aOneToOneRegistry.getToOne( leafKeyb2 ) ) );
    assert( "should get back a husband", aTVCb.equals( aOneToOneRegistry.getToOne( leafKeya2 ) ) );
    assert( "should get back an x wife", aTVCax.equals( aOneToOneRegistry.getToOne( leafKeybx2 ) ) );
    assert( "should get back an x husband", aTVCbx.equals( aOneToOneRegistry.getToOne( leafKeyax2 ) ) );
  }

  public void testDisassociate()
  {
    aOneToOneRegistry.disassociate( leafKeyb2, leafKeya2 );
    assert( "should get back a null wife", aOneToOneRegistry.getToOne( leafKeyb2 ) == null );
    assert( "should get back a null husband", aOneToOneRegistry.getToOne( leafKeya2 ) == null );
    assert( "should get back an x wife", aTVCax.equals( aOneToOneRegistry.getToOne( leafKeybx2 ) ) );
    assert( "should get back an x husband", aTVCbx.equals( aOneToOneRegistry.getToOne( leafKeyax2 ) ) );
  }

  public void testChangeAssociation()
  {
    aOneToOneRegistry.associate( leafKeyb2, leafKeyax2 );
    assert( "should get back an x wife", aTVCax.equals( aOneToOneRegistry.getToOne( leafKeyb2 ) ) );
    assert( "should get back a husband", aTVCb.equals( aOneToOneRegistry.getToOne( leafKeyax2 ) ) );
    assert( "should get back a null wife", aOneToOneRegistry.getToOne( leafKeybx2 ) == null );
    assert( "should get back a null husband", aOneToOneRegistry.getToOne( leafKeya2 ) == null );
 }

}