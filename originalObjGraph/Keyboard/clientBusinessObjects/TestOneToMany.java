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

public class TestOneToMany extends TestCase
{
  public TestOneToMany( String name)
  {
    super( name );
  }

  protected void setUp()
  throws Exception
  {
    MiddleWareFramework.getSingleton().establishCommunications();
  }

  public static Test suite( )
  {
    return new TestSuite( TestOneToMany.class );
  }

  public void testAddAndGet()
  {
  TVComponent aLeaf = KBCPersistentObjectFactory.getSingleton().createTVCa("aLeaf");
  TVComposite aTrunk = KBCPersistentObjectFactory.getSingleton().createTVComposite( "aTrunk" );
  aTrunk.addChild( aLeaf );
  assert( "Parent should equal aTrunk", aLeaf.getParent().equals( aTrunk ) );
  }

  public void testAddAndGetAll()
  {
  TVComponent leafa = KBCPersistentObjectFactory.getSingleton().createTVCa("leafa");
  TVComponent leafb = KBCPersistentObjectFactory.getSingleton().createTVCb("leafb");
  TVComposite aTrunk = KBCPersistentObjectFactory.getSingleton().createTVComposite( "trunka" );
  aTrunk.addChild( leafa );
  aTrunk.addChild( leafb );
  Object []leaves = { leafa, leafb };
  Collection expected = (Collection) new HashSet( (Collection) Arrays.asList( leaves ) );
  Collection actual = (Collection) aTrunk.getChildren();
  assert( "Should get back a collection containing leafa and leafb.", expected.containsAll( actual ) && actual.containsAll( expected ) );
  }

  public void testRemove()
  {
  TVComponent leafa = KBCPersistentObjectFactory.getSingleton().createTVCa("leafa");
  TVComponent leafb = KBCPersistentObjectFactory.getSingleton().createTVCb("leafb");
  TVComponent leafc = KBCPersistentObjectFactory.getSingleton().createTVCa("leafc");
  TVComposite aTrunk = KBCPersistentObjectFactory.getSingleton().createTVComposite( "trunka" );
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