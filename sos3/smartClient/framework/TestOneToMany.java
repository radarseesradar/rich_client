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
public class TestOneToMany extends TestCase
{
  public TestOneToMany( String name)
  {
    super( name );
  }

  protected void setUp()
  throws Exception
  {
    SmartClientFramework.getSingleton().establishCommunications();
  }

  public static Test suite( )
  {
    return new TestSuite( TestOneToMany.class );
  }

  public void testAddAndGet()
  {
  Component aLeaf = PersistentObjectFactory.getSingleton().createFemale("aLeaf");
  Composite aTrunk = PersistentObjectFactory.getSingleton().createComposite( "aTrunk" );
  aTrunk.addChild( aLeaf );
  assert( "Parent should equal aTrunk", aLeaf.getParent().equals( aTrunk ) );
  }

  public void testAddAndGetAll()
  {
  Component leafa = PersistentObjectFactory.getSingleton().createFemale("leafa");
  Component leafb = PersistentObjectFactory.getSingleton().createMale("leafb");
  Composite aTrunk = PersistentObjectFactory.getSingleton().createComposite( "trunka" );
  aTrunk.addChild( leafa );
  aTrunk.addChild( leafb );
  Object []leaves = { leafa, leafb };
  Collection expected = (Collection) new HashSet( (Collection) Arrays.asList( leaves ) );
  Collection actual = (Collection) aTrunk.getChildren();
  assert( "Should get back a collection containing leafa and leafb.", expected.containsAll( actual ) && actual.containsAll( expected ) );
  }

  public void testRemove()
  {
  Component leafa = PersistentObjectFactory.getSingleton().createFemale("leafa");
  Component leafb = PersistentObjectFactory.getSingleton().createMale("leafb");
  Component leafc = PersistentObjectFactory.getSingleton().createFemale("leafc");
  Composite aTrunk = PersistentObjectFactory.getSingleton().createComposite( "trunka" );
  aTrunk.addChild( leafa );
  aTrunk.addChild( leafb );
  aTrunk.addChild( leafc );
  Collection leaves = new ArrayList();
  leaves.add( leafa );
  leaves.add( leafb );
  leaves.add( leafc );
  assert( "Should get back a list with leafa, leafb, and leafc.",  aTrunk.getChildren().containsAll( leaves ) && leaves.containsAll( aTrunk.getChildren() ) );
  aTrunk.removeChild( leafb );
  leaves.remove( leafb );
  assert( "Should get back a list with leafa and leafc.", aTrunk.getChildren().containsAll( leaves ) && leaves.containsAll( aTrunk.getChildren() ) );
  }

}