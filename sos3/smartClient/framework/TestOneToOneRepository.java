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
public class TestOneToOneRepository extends TestCase
{
  private OneToOneRepository aOneToOneRepository;
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

  public TestOneToOneRepository( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestOneToOneRepository.class );
  }

  public void setUp()
  {
    SmartClientFramework.getSingleton().establishCommunications();
    AssociationsCoordinator anAC = new AssociationsCoordinator();
    aOneToOneRepository = anAC.getOneToOneRepository();
    aFemale = PersistentObjectFactory.getSingleton().createFemale( "leafa" );
    leafKeya1 = new RoleKey( aFemale, "husband" );
    leafKeya2 = new RoleKey( aFemale, "husband" );
    aMale = PersistentObjectFactory.getSingleton().createMale( "leafb" );
    leafKeyb1 = new RoleKey( aMale, "wife" );
    leafKeyb2 = new RoleKey( aMale, "wife" );
    aOneToOneRepository.associate( leafKeya1, leafKeyb1 );

    aFemalex = PersistentObjectFactory.getSingleton().createFemale( "leafax" );
    leafKeyax1 = new RoleKey( aFemalex, "husband" );
    leafKeyax2 = new RoleKey( aFemalex, "husband" );
    aMalex = PersistentObjectFactory.getSingleton().createMale( "leafbx" );
    leafKeybx1 = new RoleKey( aMalex, "wife" );
    leafKeybx2 = new RoleKey( aMalex, "wife" );
    aOneToOneRepository.associate( leafKeyax1, leafKeybx1 );
  }

  public void testGetToOne()
  {
    assert( "should get back a wife", aFemale.equals( aOneToOneRepository.getToOne( leafKeyb2 ) ) );
    assert( "should get back a husband", aMale.equals( aOneToOneRepository.getToOne( leafKeya2 ) ) );
    assert( "should get back an x wife", aFemalex.equals( aOneToOneRepository.getToOne( leafKeybx2 ) ) );
    assert( "should get back an x husband", aMalex.equals( aOneToOneRepository.getToOne( leafKeyax2 ) ) );
  }

  public void testDisassociate()
  {
    aOneToOneRepository.disassociate( leafKeyb2, leafKeya2 );
    assert( "should get back a null wife", aOneToOneRepository.getToOne( leafKeyb2 ) == null );
    assert( "should get back a null husband", aOneToOneRepository.getToOne( leafKeya2 ) == null );
    assert( "should get back an x wife", aFemalex.equals( aOneToOneRepository.getToOne( leafKeybx2 ) ) );
    assert( "should get back an x husband", aMalex.equals( aOneToOneRepository.getToOne( leafKeyax2 ) ) );
  }

  public void testChangeAssociation()
  {
    aOneToOneRepository.associate( leafKeyb2, leafKeyax2 );
    assert( "should get back an x wife", aFemalex.equals( aOneToOneRepository.getToOne( leafKeyb2 ) ) );
    assert( "should get back a husband", aMale.equals( aOneToOneRepository.getToOne( leafKeyax2 ) ) );
    assert( "should get back a null wife", aOneToOneRepository.getToOne( leafKeybx2 ) == null );
    assert( "should get back a null husband", aOneToOneRepository.getToOne( leafKeya2 ) == null );
 }

}