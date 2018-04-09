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
public class TestOneToOne extends TestCase
{
  public TestOneToOne( String name)
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
    return new TestSuite( TestOneToOne.class );
  }

  public void testSetAndGet()
  {
    Female aWife = PersistentObjectFactory.getSingleton().createFemale( "wife" );
    Male aHusband = PersistentObjectFactory.getSingleton().createMale( "husband" );
    aHusband.setWife( (Female) aWife );
    assert( "husband should hav a wife", aHusband.getWife().equals( aWife ) );
    assert( "wife should hav a husband", aWife.getHusband().equals( aHusband ) );
  }

}