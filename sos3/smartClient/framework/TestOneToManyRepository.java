package smartClient.framework;

import java.util.*;
import java.lang.reflect.*;
import junit.framework.*;
import java.io.*;
import model.test.client.*;
import model.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class TestOneToManyRepository extends TestCase
{
  private OneToManyRepository aOneToManyRepository;
  private Female aFemale;
  private Male aMale;
  private Composite aComposite;
  private RoleKey leafKeya1;
  private RoleKey leafKeya2;
  private RoleKey leafKeyb1;
  private RoleKey leafKeyb2;
  private RoleKey trunkKey1;
  private RoleKey trunkKey2;

  public TestOneToManyRepository( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestOneToManyRepository.class );
  }

  public void setUp()
  {
    SmartClientFramework.getSingleton().establishCommunications();
    AssociationsCoordinator anAC = new AssociationsCoordinator();
    aOneToManyRepository = anAC.getOneToManyRepository();
    aFemale = PersistentObjectFactory.getSingleton().createFemale( "leafa" );
    leafKeya1 = new RoleKey( aFemale, "parent" );
    leafKeya2 = new RoleKey( aFemale, "parent" );
    aMale = PersistentObjectFactory.getSingleton().createMale( "leafb" );
    leafKeyb1 = new RoleKey( aMale, "parent" );
    leafKeyb2 = new RoleKey( aMale, "parent" );
    aComposite = PersistentObjectFactory.getSingleton().createComposite( "mainTrunk" );
    trunkKey1 = new RoleKey( aComposite, "children" );
    trunkKey2 = new RoleKey( aComposite, "chil" + "dren" );
    aOneToManyRepository.associate( trunkKey1, leafKeya1 );
    aOneToManyRepository.associate( trunkKey1, leafKeyb1 );
  }

  public void testChangeAssociation()
  {
    Composite aCompositeb = new Composite ( "mainTrunkb" );
    RoleKey trunkKeyb1 = new RoleKey( aCompositeb, "children" );
    RoleKey trunkKeyb2 = new RoleKey( aCompositeb, "chil" + "dren" );
    aOneToManyRepository.associate( trunkKeyb1, leafKeyb2 );
    Object []leaves = { aFemale };
    Object []leavesb = { aMale };
    Collection aCollection;
    aCollection = aOneToManyRepository.getToMany( trunkKey2 );
    assert( "Should get back a list with only aFemale.", Arrays.equals( aCollection.toArray(), leaves ) );
    aCollection = aOneToManyRepository.getToMany( trunkKeyb2 );
    assert( "Should get back a list with only aMale", Arrays.equals( aCollection.toArray(), leavesb ) );
    assert( "should get aComposite", aComposite.equals( aOneToManyRepository.getToOne( leafKeya2 ) ) );
    assert( "should get aCompositeb", aCompositeb.equals( aOneToManyRepository.getToOne( leafKeyb2 ) ) );
  }

  public void testGetToOne()
  {
    assert( "should get aComposite", aComposite.equals( aOneToManyRepository.getToOne( leafKeya2 ) ) );
    assert( "should get aComposite", aComposite.equals( aOneToManyRepository.getToOne( leafKeyb2 ) ) );
  }

  public void testGetToMany()
  {
    Collection aCollection;
    aCollection = aOneToManyRepository.getToMany( trunkKey2 );
    Object []leaves = { aFemale, aMale };
    Collection expected = (Collection) new HashSet( (Collection) Arrays.asList( leaves ) );
    assert( "Should get back a collection with aFemale and a aMale", expected.containsAll( aCollection ) && aCollection.containsAll( expected ));
  }


  public void testDisassociate()
  {
    Collection aCollection;
    aOneToManyRepository.disassociate( trunkKey1, leafKeya2 );
    aCollection = aOneToManyRepository.getToMany( trunkKey2 );
    Object []leaves = { aMale };
    assert( "Should get back a list with only aMale in it.", Arrays.equals( aCollection.toArray(), leaves ) );
  }


}