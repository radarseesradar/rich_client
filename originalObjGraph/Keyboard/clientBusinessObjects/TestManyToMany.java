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

public class TestManyToMany extends TestCase
{
  private TVCa sisterx;
  private TVCa sistery;
  private TVCb brotherx;
  private TVCb brothery;

  public void setUp()
  {
    MiddleWareFramework.getSingleton().establishCommunications();
    sisterx = KBCPersistentObjectFactory.getSingleton().createTVCa( "sisterx" );
    sistery = KBCPersistentObjectFactory.getSingleton().createTVCa( "sistery" );
    brotherx = KBCPersistentObjectFactory.getSingleton().createTVCb( "brotherx" );
    brothery = KBCPersistentObjectFactory.getSingleton().createTVCb( "brothery" );
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