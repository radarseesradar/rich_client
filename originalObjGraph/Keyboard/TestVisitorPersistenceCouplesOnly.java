
/**
 * Title:        Persistent Object Graph<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Steve McDaniel<p>
 * Company:      Salient<p>
 * @author Steve McDaniel
 * @version 1.0
 */
package Keyboard;

import secant.extreme.*;
import javax.rmi.*;
import junit.framework.*;
import junit.extensions.*;
import Keyboard.clientBusinessObjects.*;
import java.io.*;

public class TestVisitorPersistenceCouplesOnly extends TestCase
{
  protected static TVSessionBeanObject tvObject;
  protected static Client client;
  private DataGenerator aDataGenerator;

  public TestVisitorPersistenceCouplesOnly( String name)
  {
    super( name );
  }

  public static void oneTimeSetUp()
  {
    try
    {
      MiddleWareFramework.getSingleton().establishCommunications();
      client = MiddleWareFramework.getSingleton().getClient();
      tvObject = MiddleWareFramework.getSingleton().getTVSessionBeanObject();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static Test allTests()
  {
    return new TestSuite( TestVisitorPersistenceOrphansOnly.class );
  }


  public static Test complexTests()
  {
    TestSuite suite = new TestSuite();
    suite.addTest( new TestVisitorPersistenceCouplesOnly( "testClearDB" ) );
    suite.addTest( new TestVisitorPersistenceCouplesOnly( "testRemoveSister" ) );
    return suite;
  }

  public static Test suite( )
  {
    TestSuite suite;
//    suite = (TestSuite) allTests();
    suite = (TestSuite) complexTests();

    TestSetup wrapper = new TestSetup(suite)
    {
      public void setUp()
      {
        oneTimeSetUp();
      }
    };
    return wrapper;
  }



  public void testClearDB() throws Exception
  {
    client.begin();
      tvObject.clearDB();
    client.commit();
  }

  private TVComponent buildObjectGraph()
  {
    TVComponent objGraph = (aDataGenerator = new DataGenerator()).buildBiObjectGraph();

    objGraph.accept
    (
      new Visitor()
      {
        public Visitable visit( Object anObject )
        {
          TVComponent aComponent = (TVComponent) anObject;
          aComponent.setAlias( aComponent.getName() + "" );
          return (Visitable) aComponent;
        }
      }
    );

    return (TVComponent) objGraph;
  }

  public void testClearClientCache() throws Exception
  {
    TVComposite branches[];
    TVComponent leaves[];
    TVComponent objGraph = buildObjectGraph();
    leaves = aDataGenerator.getLeaves();

    objGraph.save();

    print( objGraph, "Object graph after initial save:" );

    MiddleWareFramework.getSingleton().clear();

    leaves[0].refresh();

    print( objGraph, "Object graph after clearing client side cache and refreshing from leaf_one." );

  }

  public void testLoadData() throws Exception
  {
    TVComposite branches[];
    TVComponent leaves[];
    TVComponent objGraph = buildObjectGraph();

    objGraph.save();

    print( objGraph, "Object graph after initial save:" );
  }


  public void testRemoveChild() throws Exception
  {
    TVComposite branches[];
    TVComponent leaves[];
    TVComponent objGraph = buildObjectGraph();

    objGraph.save();

    print( objGraph, "Object graph after initial save:" );

    branches = aDataGenerator.getBranches();
    leaves = aDataGenerator.getLeaves();
    branches[0].removeChild( leaves[0] );

    print( objGraph, "Object graph after removal and before second save:" );

    objGraph.save();

    print( objGraph, "Object graph after removal and after second save:" );
  }

  public void testRemoveSister() throws Exception
  {
    TVComposite branches[];
    TVComponent leaves[];
    TVComponent objGraph = buildObjectGraph();

    objGraph.save();

    print( objGraph, "Object graph after initial save:" );

    leaves = aDataGenerator.getLeaves();
    ((TVCb)leaves[1]).removeSister( (TVCa) leaves[2] );

    print( objGraph, "Object graph after removing sister and before second save:" );

    objGraph.save();

    print( objGraph, "Object graph after removing sister and after second save:" );
  }

  private void print( Visitable memberInGraph, String message )
  {
    memberInGraph.getAssociationsCoordinator().setPreservingStubs( true );
    System.out.println( message );
    memberInGraph.accept( new VisitorFullPrinterIgnoreClamps() );
    memberInGraph.getAssociationsCoordinator().setPreservingStubs( false );
  }

  public static void main( String args[] )
  {
    junit.textui.TestRunner.run( TestVisitorPersistenceCouplesOnly.class );
//    TestRunner( TestVisitorPersistenceCouplesOnly.class );
  }

}