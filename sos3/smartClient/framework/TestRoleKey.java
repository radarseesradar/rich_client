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
public class TestRoleKey extends TestCase
{
  public TestRoleKey( String name)
  {
    super( name );
  }

  public void testKeyExists()
  {
    HashSet aSet = new HashSet();
    Female aFemale = PersistentObjectFactory.getSingleton().createFemale( "Hello" );
    RoleKey roleKey1 = new RoleKey( aFemale, "parent" );
    RoleKey roleKey2 = new RoleKey( aFemale, "par" + "ent" );
    aSet.add( roleKey1 );
    assert( "Should be able to find the role key", aSet.contains( roleKey2 ) );
  }

  protected void setUp()
  throws Exception
  {
    SmartClientFramework.getSingleton().establishCommunications();
  }


  public static Test suite( )
  {
    return new TestSuite( TestRoleKey.class );
  }

  }