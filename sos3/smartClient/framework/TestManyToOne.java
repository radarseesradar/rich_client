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
public class TestManyToOne extends TestCase
{
  public TestManyToOne( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestManyToOne.class );
  }

  protected void setUp()
  throws Exception
  {
    SmartClientFramework.getSingleton().establishCommunications();
  }

  public void testSetAndGet()
  {
  Component aLeaf = PersistentObjectFactory.getSingleton().createFemale( "leafa" );
  Composite aTrunk = PersistentObjectFactory.getSingleton().createComposite( "trunka" );
  aLeaf.setParent( aTrunk );
  assert( "Parent should equal aTrunk", aLeaf.getParent().equals( aTrunk ) );
  }

}