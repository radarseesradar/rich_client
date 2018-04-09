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
public class TestManyToManyRepository extends TestCase
{
  private ManyToManyRepository aManyToManyRepository;
  private Female aFemale;
  private Male aMale;
  private RoleKey leafKeya1;
  private RoleKey leafKeya2;
  private RoleKey leafKeyb1;
  private RoleKey leafKeyb2;

  private Female aFemalex;
  private Male aMalex;
  private RoleKey leafKeyax1;
  private RoleKey leafKeyax2;
  private RoleKey leafKeybx1;
  private RoleKey leafKeybx2;

  public TestManyToManyRepository( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestManyToManyRepository.class );
  }

  public void setUp()
  {
    SmartClientFramework.getSingleton().establishCommunications();
    AssociationsCoordinator anAC = new AssociationsCoordinator();
    aManyToManyRepository = anAC.getManyToManyRepository();
    aFemale = PersistentObjectFactory.getSingleton().createFemale( "leafa" );
    leafKeya1 = new RoleKey( aFemale, "brothers" );
    leafKeya2 = new RoleKey( aFemale, "brothers" );
    aMale = PersistentObjectFactory.getSingleton().createMale( "leafb" );
    leafKeyb1 = new RoleKey( aMale, "sisters" );
    leafKeyb2 = new RoleKey( aMale, "sisters" );

    aFemalex = PersistentObjectFactory.getSingleton().createFemale( "leafax" );
    leafKeyax1 = new RoleKey( aFemalex, "brothers" );
    leafKeyax2 = new RoleKey( aFemalex, "brothers" );
    aMalex = PersistentObjectFactory.getSingleton().createMale( "leafbx" );
    leafKeybx1 = new RoleKey( aMalex, "sisters" );
    leafKeybx2 = new RoleKey( aMalex, "sisters" );
    aManyToManyRepository.associate( leafKeya1, leafKeyb1 );
    aManyToManyRepository.associate( leafKeya1, leafKeybx1 );
    aManyToManyRepository.associate( leafKeyax1, leafKeyb1 );
    aManyToManyRepository.associate( leafKeyax1, leafKeybx1 );
  }

  public void testDisassociate()
  {
    aManyToManyRepository.disassociate( leafKeyb2, leafKeya2 );
    Collection actualBrothers = aManyToManyRepository.getToMany( leafKeya2 );
    Collection actualSisters = aManyToManyRepository.getToMany( leafKeyb2 );
    Collection expectedBrothers = Collections.singleton( aMalex );
    Collection expectedSisters = Collections.singleton( aFemalex );
    assert( "aMale should have only the single sister aFemalex", expectedSisters.containsAll( actualSisters ) && actualSisters.containsAll( expectedSisters ) );
    assert( "aTVC should have only the single brother aMalex", expectedBrothers.containsAll( actualBrothers ) && actualBrothers.containsAll( expectedBrothers ) );
  }

  public void testGetBrothers()
  {
    Collection aCollection;
    aCollection = aManyToManyRepository.getToMany( leafKeya2 );
    Collection brothers = new ArrayList();
    brothers.add( aMale );
    brothers.add( aMalex );
    assert( "aFemale's brothers should be aMale and aMalex", brothers.containsAll( aCollection ) && aCollection.containsAll( brothers ) );
  }

  public void testGetBrothers2()
  {
    Collection aCollection;
    aCollection = aManyToManyRepository.getToMany( leafKeyax2 );
    Collection brothers = new ArrayList();
    brothers.add( aMale );
    brothers.add( aMalex );
    assert( "aFemalex's brothers should be aMale and aMalex", brothers.containsAll( aCollection ) && aCollection.containsAll( brothers ) );
  }

  public void testGetSisters()
  {
    Collection aCollection;
    aCollection = aManyToManyRepository.getToMany( leafKeyb2 );
    Collection brothers = new ArrayList();
    brothers.add( aFemale );
    brothers.add( aFemalex );
    assert( "aMale's sisters should be aFemale and aFemalex", brothers.containsAll( aCollection ) && aCollection.containsAll( brothers ) );
  }

  public void testGetSisters2()
  {
    Collection aCollection;
    aCollection = aManyToManyRepository.getToMany( leafKeybx2 );
    Collection brothers = new ArrayList();
    brothers.add( aFemale );
    brothers.add( aFemalex );
    assert( "aMalex's sisters should be aFemale and aFemalex", brothers.containsAll( aCollection ) && aCollection.containsAll( brothers ) );
  }

}