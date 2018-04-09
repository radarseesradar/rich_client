
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
public class TestVisitorPersistenceOrphansOnly extends TestCase
{
  public static SessionInterface tvObject;
  private DataGenerator aDataGenerator;

  public TestVisitorPersistenceOrphansOnly( String name)
  {
    super( name );
  }

  public static void oneTimeSetUp()
  {
  	try
    {
		SmartClientFramework.getSingleton().setPropertiesUsing( "C:\\sos3\\OrphansOnlyProperties.txt" );
        SmartClientFramework.getSingleton().establishCommunications();
        tvObject = SmartClientFramework.getSingleton().getSession();
        tvObject.setPropertiesUsing( "C:\\sos3\\OrphansOnlyProperties.txt" );
    }
    catch( java.rmi.RemoteException e )
    {
    	throw new InternalError( "RMI exception on junit onetime setup for TestVisitorPersistenceOrphansOnly" );
    }
  }


  public static Test allTests()
  {
    return new TestSuite( TestVisitorPersistenceOrphansOnly.class );
  }


  public static Test complexTests()
  {
    TestSuite aSuite = new TestSuite();
    aSuite.addTest( new TestVisitorPersistenceOrphansOnly( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistenceOrphansOnly( "testProxy" ) );
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



  public void testProxy() throws Exception
  {
    Component objGraph = buildObjectGraph();

    objGraph.save();

    proxyRetrieveAndCompare( objGraph );
  }

  private void proxyRetrieveAndCompare( Persistable objGraph ) throws Exception
  {
    Composite[] branches = aDataGenerator.getBranches();
    Component[] leaves = aDataGenerator.getLeaves();
    Composite aBranch = null;
    Persistable leavesObjGraph = null;
    AssociationsCoordinator savedOriginal = new AssociationsCoordinator();
    savedOriginal.merge( leaves[1].getAssociationsCoordinator() );
    SmartClientFramework.getSingleton().clear();

    leavesObjGraph = leaves[1].refresh();
    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( true );

    VisitorEqualityTesterIgnoreClamps aTester = new VisitorEqualityTesterIgnoreClamps( leavesObjGraph );
    leaves[1].accept( aTester );
    assert( "Graphs should not be the same", ! aTester.isEqual() );

    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( true );

    // fault in branch_one and branch_three
    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( false );
    aBranch = ((Component)leavesObjGraph).getParent();
    aBranch.getChildren();

    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( true );

    aTester = new VisitorEqualityTesterIgnoreClamps( leavesObjGraph );
    leaves[1].accept( aTester );
    assert( "Graphs should not be the same", ! aTester.isEqual() );

    // fault in branch_two and remainder of graph
    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( false );
    aBranch = aBranch.getParent();
    aBranch.getChildren();
    aBranch.getParent();

    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( false );

    String actualResult = null;
	String expectedResult = null;

	VisitorFullToStringIgnoreClamps actualResultCollector = new VisitorFullToStringIgnoreClamps();
	VisitorFullToStringIgnoreClamps expectedResultCollector = new VisitorFullToStringIgnoreClamps();

	leavesObjGraph.accept(  actualResultCollector );

	leaves[1].accept(  expectedResultCollector );

	actualResult = actualResultCollector.toString();

	expectedResult = expectedResultCollector.toString();
	assert( "Graphs should be the same", actualResult.equals( expectedResult ) );

    assert( "Associations coordinators should be equal", leavesObjGraph.getAssociationsCoordinator().equals( savedOriginal ) );

    assert( "Association coordinator should be consistent", leavesObjGraph.getAssociationsCoordinator().isConsistent() );

  }

  public static void main( String[] args )
  {
    junit.textui.TestRunner.run( TestVisitorPersistenceOrphansOnly.class );
//    TestRunner( TestVisitorPersistenceOrphansOnly.class );
  }

}