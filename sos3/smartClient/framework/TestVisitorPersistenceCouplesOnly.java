
package smartClient.framework;

import javax.rmi.*;
import junit.framework.*;
import junit.extensions.*;
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
public class TestVisitorPersistenceCouplesOnly extends TestCase
{
  public static SessionInterface tvObject;
  private DataGenerator aDataGenerator;

  public TestVisitorPersistenceCouplesOnly( String name)
  {
    super( name );
  }

  public static void oneTimeSetUp()
  {
  	try
    {
		SmartClientFramework.getSingleton().setPropertiesUsing( "C:\\sos3\\CouplesOnlyProperties.txt" );
        SmartClientFramework.getSingleton().establishCommunications();
        tvObject = SmartClientFramework.getSingleton().getSession();
        tvObject.setPropertiesUsing( "C:\\sos3\\CouplesOnlyProperties.txt" );
    }
    catch( java.rmi.RemoteException e )
    {
    	throw new InternalError( "RMI exception on junit onetime setup for TestVisitorPersistenceCouplesOnly" );
    }
  }

  public static Test allTests()
  {
    return new TestSuite( TestVisitorPersistenceOrphansOnly.class );
  }


  public static Test complexTests()
  {
    TestSuite aSuite = new TestSuite();
    aSuite.addTest( new TestVisitorPersistenceCouplesOnly( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistenceCouplesOnly( "testClearClientCache" ) );
    aSuite.addTest( new TestVisitorPersistenceCouplesOnly( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistenceCouplesOnly( "testRemoveChild" ) );
    aSuite.addTest( new TestVisitorPersistenceCouplesOnly( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistenceCouplesOnly( "testRemoveSister" ) );
    aSuite.addTest( new TestVisitorPersistenceCouplesOnly( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistenceCouplesOnly( "testChangeParent" ) );
    aSuite.addTest( new TestVisitorPersistenceCouplesOnly( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistenceCouplesOnly( "testSwapMates" ) );
    return aSuite;
  }

  public static Test suite( )
  {
    TestSuite aSuite;
//    aSuite = (TestSuite) allTests();
    aSuite = (TestSuite) complexTests();

    TestSetup wrapper = new TestSetup(aSuite)
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
      tvObject.clearDB();
  }

  private Component buildObjectGraph()
  {
    Component objGraph = (aDataGenerator = new DataGenerator()).buildBiObjectGraph();

    objGraph.accept
    (
      new Visitor()
      {
        public Persistable visit( Object anObject )
        {
          Component aComponent = (Component) anObject;
          aComponent.setAlias( aComponent.getName() + "" );
          return (Persistable) aComponent;
        }
      }
    );

    return (Component) objGraph;
  }

  public void testClearClientCache() throws Exception
  {
    Composite[] branches;
    Component[] leaves;

    Component expectedObjGraph =  (new DataGenerator()).buildExpectedObjGraphForCouplesOnly();

    Component objGraph = buildObjectGraph();
    leaves = aDataGenerator.getLeaves();

    objGraph.save();

    SmartClientFramework.getSingleton().clear();

    leaves[0].refresh();

	VisitorFullToStringIgnoreClamps expectedResultsCollector = new VisitorFullToStringIgnoreClamps();
    expectedObjGraph.accept( expectedResultsCollector );

	VisitorFullToStringIgnoreClamps actualResultsCollector = new VisitorFullToStringIgnoreClamps();
    leaves[1].accept( actualResultsCollector );

    assert( "Graphs should  be the same", expectedResultsCollector.equals( actualResultsCollector ) );
  }


  public void testRemoveChild() throws Exception
  {
    Composite[] branches;
    Component[] leaves;

    Component expectedObjGraph =  (new DataGenerator()).buildExpectedObjGraphForCouplesOnly2();


    Component objGraph = buildObjectGraph();

    objGraph.save();

    branches = aDataGenerator.getBranches();
    leaves = aDataGenerator.getLeaves();
    branches[0].removeChild( leaves[0] );

    objGraph.save();

    SmartClientFramework.getSingleton().clear();

    leaves[1].refresh();

	VisitorFullToStringIgnoreClamps expectedResultsCollector = new VisitorFullToStringIgnoreClamps();
    expectedObjGraph.accept( expectedResultsCollector );

	VisitorFullToStringIgnoreClamps actualResultsCollector = new VisitorFullToStringIgnoreClamps();
    leaves[1].accept( actualResultsCollector );

    assert( "Graphs should  be the same", expectedResultsCollector.equals( actualResultsCollector ) );
  }

  public void testChangeParent() throws Exception
  {
    Composite[] branches;
    Component[] leaves;

    Component expectedObjGraph =  (new DataGenerator()).buildExpectedObjGraphForCouplesOnly4();


    Component objGraph = buildObjectGraph();

    objGraph.save();

    branches = aDataGenerator.getBranches();
    leaves = aDataGenerator.getLeaves();
	leaves[0].setParent( branches[1] );

    objGraph.save();

    SmartClientFramework.getSingleton().clear();

    leaves[1].refresh();

	VisitorFullToStringIgnoreClamps expectedResultsCollector = new VisitorFullToStringIgnoreClamps();
    expectedObjGraph.accept( expectedResultsCollector );

	VisitorFullToStringIgnoreClamps actualResultsCollector = new VisitorFullToStringIgnoreClamps();
    leaves[1].accept( actualResultsCollector );

    assert( "Graphs should  be the same", expectedResultsCollector.equals( actualResultsCollector ) );
  }

  public void testSwapMates() throws Exception
  {
    Composite[] branches;
    Component[] leaves;

    Component expectedObjGraph =  (new DataGenerator()).buildExpectedObjGraphForCouplesOnly5();


    Component objGraph = buildObjectGraph();

    objGraph.save();

    branches = aDataGenerator.getBranches();
    leaves = aDataGenerator.getLeaves();

    // swap mates
    ((Female)leaves[0]).setHusband( (Male) leaves[3] );
    ((Female)leaves[2]).setHusband( (Male) leaves[1] );

    objGraph.save();

    SmartClientFramework.getSingleton().clear();

    leaves[1].refresh();

	VisitorFullToStringIgnoreClamps expectedResultsCollector = new VisitorFullToStringIgnoreClamps();
    expectedObjGraph.accept( expectedResultsCollector );

	VisitorFullToStringIgnoreClamps actualResultsCollector = new VisitorFullToStringIgnoreClamps();
    leaves[1].accept( actualResultsCollector );

    assert( "Graphs should  be the same", expectedResultsCollector.equals( actualResultsCollector ) );
  }

  public void testRemoveSister() throws Exception
  {
    Composite[] branches;
    Component[] leaves;

    Component expectedObjGraph =  (new DataGenerator()).buildExpectedObjGraphForCouplesOnly3();


    Component objGraph = buildObjectGraph();

    objGraph.save();

    branches = aDataGenerator.getBranches();
    leaves = aDataGenerator.getLeaves();
    ((Male)leaves[1]).removeSister( (Female) leaves[2] );

    objGraph.save();

    SmartClientFramework.getSingleton().clear();

    leaves[1].refresh();

	VisitorFullToStringIgnoreClamps expectedResultsCollector = new VisitorFullToStringIgnoreClamps();
    expectedObjGraph.accept( expectedResultsCollector );

	VisitorFullToStringIgnoreClamps actualResultsCollector = new VisitorFullToStringIgnoreClamps();
    leaves[1].accept( actualResultsCollector );

    assert( "Graphs should  be the same", expectedResultsCollector.equals( actualResultsCollector ) );
  }

  private void print( Persistable memberInGraph, String message )
  {
    memberInGraph.getAssociationsCoordinator().setPreservingStubs( true );
    System.out.println( message );
    memberInGraph.accept( new VisitorFullPrinterIgnoreClamps() );
    memberInGraph.getAssociationsCoordinator().setPreservingStubs( false );
  }

  public static void main( String[] args )
  {
    junit.textui.TestRunner.run( TestVisitorPersistenceCouplesOnly.class );
//    TestRunner( TestVisitorPersistenceCouplesOnly.class );
  }

}