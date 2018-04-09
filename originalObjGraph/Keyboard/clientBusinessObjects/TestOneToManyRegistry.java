package Keyboard.clientBusinessObjects;

import java.util.*;
import java.lang.reflect.*;
import java.lang.*;
import Keyboard.clientBusinessObjects.*;
import Keyboard.*;
import junit.framework.*;
import java.io.*;

public class TestOneToManyRegistry extends TestCase
{
  private OneToManyRegistry aOneToManyRegistry;
  private TVCa aTVCa;
  private TVCb aTVCb;
  private TVComposite aTVComposite;
  private RoleKey leafKeya1;
  private RoleKey leafKeya2;
  private RoleKey leafKeyb1;
  private RoleKey leafKeyb2;
  private RoleKey trunkKey1;
  private RoleKey trunkKey2;

  public TestOneToManyRegistry( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestOneToManyRegistry.class );
  }

  public void setUp()
  {
    MiddleWareFramework.getSingleton().establishCommunications();
    AssociationsCoordinator anAC = new AssociationsCoordinator();
    aOneToManyRegistry = anAC.getOneToManyRegistry();
    aTVCa = KBCPersistentObjectFactory.getSingleton().createTVCa( "leafa" );
    leafKeya1 = new RoleKey( aTVCa, "parent" );
    leafKeya2 = new RoleKey( aTVCa, "parent" );
    aTVCb = KBCPersistentObjectFactory.getSingleton().createTVCb( "leafb" );
    leafKeyb1 = new RoleKey( aTVCb, "parent" );
    leafKeyb2 = new RoleKey( aTVCb, "parent" );
    aTVComposite = KBCPersistentObjectFactory.getSingleton().createTVComposite( "mainTrunk" );
    trunkKey1 = new RoleKey( aTVComposite, "children" );
    trunkKey2 = new RoleKey( aTVComposite, "chil" + "dren" );
    aOneToManyRegistry.associate( trunkKey1, leafKeya1 );
    aOneToManyRegistry.associate( trunkKey1, leafKeyb1 );
  }

  public void testChangeAssociation()
  {
    TVComposite aTVCompositeb = new TVComposite( "mainTrunkb" );
    RoleKey trunkKeyb1 = new RoleKey( aTVCompositeb, "children" );
    RoleKey trunkKeyb2 = new RoleKey( aTVCompositeb, "chil" + "dren" );
    aOneToManyRegistry.associate( trunkKeyb1, leafKeyb2 );
    Object []leaves = { aTVCa };
    Object []leavesb = { aTVCb };
    Collection aCollection;
    aCollection = aOneToManyRegistry.getToMany( trunkKey2 );
    assert( "Should get back a list with only aTVCa.", Arrays.equals( aCollection.toArray(), leaves ) );
    aCollection = aOneToManyRegistry.getToMany( trunkKeyb2 );
    assert( "Should get back a list with only aTVCb", Arrays.equals( aCollection.toArray(), leavesb ) );
    assert( "should get aTVComposite", aTVComposite.equals( aOneToManyRegistry.getToOne( leafKeya2 ) ) );
    assert( "should get aTVCompositeb", aTVCompositeb.equals( aOneToManyRegistry.getToOne( leafKeyb2 ) ) );
  }

  public void testGetToOne()
  {
    assert( "should get aTVComposite", aTVComposite.equals( aOneToManyRegistry.getToOne( leafKeya2 ) ) );
    assert( "should get aTVComposite", aTVComposite.equals( aOneToManyRegistry.getToOne( leafKeyb2 ) ) );
  }

  public void testGetToMany()
  {
    Collection aCollection;
    aCollection = aOneToManyRegistry.getToMany( trunkKey2 );
    Object []leaves = { aTVCa, aTVCb };
    Collection expected = (Collection) new HashSet( (Collection) Arrays.asList( leaves ) );
    assert( "Should get back a collection with aTVCa and a aTVCb", expected.containsAll( aCollection ) && aCollection.containsAll( expected ));
  }


  public void testDisassociate()
  {
    Collection aCollection;
    aOneToManyRegistry.disassociate( trunkKey1, leafKeya2 );
    aCollection = aOneToManyRegistry.getToMany( trunkKey2 );
    Object []leaves = { aTVCb };
    assert( "Should get back a list with only aTVCb in it.", Arrays.equals( aCollection.toArray(), leaves ) );
  }


}