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
public class TestManyToMany extends TestCase
{
  private Female sisterx;
  private Female sistery;
  private Male brotherx;
  private Male brothery;

  public void setUp()
  {
    SmartClientFramework.getSingleton().establishCommunications();
    sisterx = PersistentObjectFactory.getSingleton().createFemale( "sisterx" );
    sistery = PersistentObjectFactory.getSingleton().createFemale( "sistery" );
    brotherx = PersistentObjectFactory.getSingleton().createMale( "brotherx" );
    brothery = PersistentObjectFactory.getSingleton().createMale( "brothery" );
  }

  public TestManyToMany( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestManyToMany.class );
  }

  public void testAddAndGet()
  {
    Collection expectedBrothers = Collections.singleton( brotherx );
    Collection expectedSisters = Collections.singleton( sisterx );
    sisterx.addBrother( brotherx );
    Collection actualBrothers = sisterx.getBrothers(  );
    Collection actualSisters = brotherx.getSisters(  );
    assert( "sisterx should have only the single brother brotherx", expectedBrothers.containsAll( actualBrothers ) && actualBrothers.containsAll( expectedBrothers ) );
    assert( "brotherx should have only the single sister sisterx", expectedSisters.containsAll( actualSisters ) && actualSisters.containsAll( expectedSisters ) );
  }

  public void testRemove()
  {
    Collection expectedBrothers = Collections.singleton( brotherx );
    Collection expectedSisters = Collections.singleton( sisterx );
    sisterx.addBrother( brotherx );
    sisterx.addBrother( brothery );
    brotherx.addSister( sistery );
    brothery.addSister( sistery );
    sisterx.removeBrother( brothery );
    brotherx.removeSister( sistery );
    Collection actualBrothers = sisterx.getBrothers(  );
    Collection actualSisters = brotherx.getSisters(  );
    assert( "sisterx should have only the single brother brotherx", expectedBrothers.containsAll( actualBrothers ) && actualBrothers.containsAll( expectedBrothers ) );
    assert( "brotherx should have only the single sister sisterx", expectedSisters.containsAll( actualSisters ) && actualSisters.containsAll( expectedSisters ) );
  }
  public void testAddAndGetAll()
  {
    Collection expectedBrothers = new ArrayList();
    Collection expectedSisters = new ArrayList();
    expectedBrothers.add( brotherx );
    expectedBrothers.add( brothery );
    expectedSisters.add( sisterx );
    expectedSisters.add( sistery );
    sisterx.addBrother( brotherx );
    sisterx.addBrother( brothery );
    brotherx.addSister( sistery );
    brothery.addSister( sistery );
    Collection actualBrothers = sisterx.getBrothers(  );
    Collection actualSisters = brotherx.getSisters(  );
    assert( "sisterx should have a brotherx and a brothery", expectedBrothers.containsAll( actualBrothers ) && actualBrothers.containsAll( expectedBrothers ) );
    assert( "brotherx should have a sisterx and a sister y", expectedSisters.containsAll( actualSisters ) && actualSisters.containsAll( expectedSisters ) );
    actualBrothers = sistery.getBrothers(  );
    actualSisters = brothery.getSisters(  );
    assert( "sistery should have a brotherx and a brothery", expectedBrothers.containsAll( actualBrothers ) && actualBrothers.containsAll( expectedBrothers ) );
    assert( "brothery should have a sisterx and a sister y", expectedSisters.containsAll( actualSisters ) && actualSisters.containsAll( expectedSisters ) );
  }

}