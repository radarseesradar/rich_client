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
public class TestToManyRepository extends TestCase
{

  private ToManyRepository aToManyRepository;
  private Female aFemale;
  private Male aMale;
  private Composite aComposite;
  private RoleKey roleKey1;
  private RoleKey roleKey2;

  public TestToManyRepository( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestToManyRepository.class );
  }

  protected ToManyRepository createRepository()
  {
    AssociationsCoordinator anAC = new AssociationsCoordinator();
    return new ToManyRepository( anAC );
  }

  public void setUp()
  {
    SmartClientFramework.getSingleton().establishCommunications();
    aToManyRepository = createRepository();
    aFemale = PersistentObjectFactory.getSingleton().createFemale( "leafa" );
    aMale = PersistentObjectFactory.getSingleton().createMale( "leafb" );
    aComposite = PersistentObjectFactory.getSingleton().createComposite( "mainTrunk" );
    roleKey1 = new RoleKey( aComposite, "children" );
    roleKey2 = new RoleKey( aComposite, "chil" + "dren" );
    aToManyRepository.put( roleKey1, aFemale );
    aToManyRepository.put( roleKey1, aMale );
  }

  public void testRemove()
  {
    aToManyRepository.remove( roleKey2, aFemale );
    Collection toMany = aToManyRepository.get( roleKey2 );
    assert( "Should not be able to find leafa", ! toMany.contains( aFemale ) );
    assert( "Should be able to find leafb", toMany.contains( aMale ) );
  }


  public void testToManysExist()
  {
    Collection toMany = aToManyRepository.get( roleKey2 );
    assert( "Should be able to find leafa", toMany.contains( aFemale ) );
    assert( "Should be able to find leafb", toMany.contains( aMale ) );
  }

}