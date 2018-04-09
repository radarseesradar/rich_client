
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
public class TestVisitorPersistence extends TestCase
{
  public static SessionInterface tvObject;
  private DataGenerator aDataGenerator;

  public TestVisitorPersistence( String name)
  {
    super( name );
  }

  public static void oneTimeSetUp()
  {
		SmartClientFramework.getSingleton().setPropertiesUsing( "C:\\sos3\\SOSProperties.txt" );
        SmartClientFramework.getSingleton().establishCommunications();
        tvObject = SmartClientFramework.getSingleton().getSession();
  }

  public static Test allTests()
  {
    return new TestSuite( TestVisitorPersistence.class );
  }

  public static Test simpleTests()
  {
    TestSuite aSuite = new TestSuite();
    aSuite.addTest( new TestVisitorPersistence( "testGreeting" ) );
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
//    aSuite.addTest( new TestVisitorPersistence( "testCreateAll" ) );
    return aSuite;
  }

  public static Test complexTests()
  {
    TestSuite aSuite = new TestSuite();
    aSuite.addTest( new TestVisitorPersistence( "testGreeting" ) );
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistence( "testLoadAndRetrieve" ) );
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistence( "testLoadUpdateAndRetrieveData" ) );
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistence( "testLoadRemoveAndRetrieveData" ) );
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistence( "testLoadRemoveAndRetrieveData3" ) );
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistence( "testSwapParents" ) );
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistence( "testSwapMates" ) );
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistence( "testNewHusband" ) );
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistence( "testOptimisticLocking2" ) );
    return aSuite;
  }

  public static Test optimisticLockingTests()
  {
    TestSuite aSuite = new TestSuite();
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    aSuite.addTest( new TestVisitorPersistence( "testOptimisticLocking1" ) );
//    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
//    aSuite.addTest( new TestVisitorPersistence( "testOptimisticLocking2" ) );
//    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
//    aSuite.addTest( new TestVisitorPersistence( "testNewHusband" ) );
    return aSuite;
  }

  public static Test minimalTests()
  {
    TestSuite aSuite = new TestSuite();
    aSuite.addTest( new TestVisitorPersistence( "testGreeting" ) );
    aSuite.addTest( new TestVisitorPersistence( "testClearDB" ) );
//    aSuite.addTest( new TestVisitorPersistence( "testLoadData" ) );
    aSuite.addTest( new TestVisitorPersistence( "testLoadAndRetrieve" ) );
    return aSuite;
  }

  public static Test suite( )
  {
    TestSuite aSuite;
//    aSuite = (TestSuite) allTests();
//    aSuite = (TestSuite) simpleTests();
    aSuite = (TestSuite) complexTests();
//    aSuite = (TestSuite) optimisticLockingTests();
//      aSuite = (TestSuite) minimalTests();

    TestSetup wrapper = new TestSetup(aSuite)
    {
      public void setUp()
      {
        oneTimeSetUp();
      }
    };
    return wrapper;
  }

  public void testGreeting() throws Exception
  {
      assert( "Should have returned the string <<Hello. I'm a Session. My middleware framework thinks it's on the server.>>",
      			tvObject.greetings().equals( "Hello. I'm a Session. My middleware framework thinks it's on the server." ) );
  }

  public void testLoadData() throws Exception
  {
    Component objGraph = buildObjectGraph();

    objGraph.save();
  }

  public void testClearDB() throws Exception
  {
      tvObject.clearDB();
  }

  public void testOptimisticLocking1() throws Exception
  {
    Component objGraph = buildObjectGraph();

    objGraph.save();

    retrieveFullPrintAndCompare( objGraph );
  }

  public void testOptimisticLocking2() throws Exception
  {
    PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
    Component objGraph = buildObjectGraph();
    Component[] leaves = aDataGenerator.getLeaves();
    Composite[] branches = aDataGenerator.getBranches();
    Component leafOneX = creator.createFemale( "leaf_one_x" );
    Component leafTwoX = creator.createMale( "leaf_two_x" );
    Component leafFourX = creator.createMale( "leaf_four_x" );

    adjustObjGraph( objGraph );

    leafOneX.setAlias( leafOneX.getName() + "" );
    leafTwoX.setAlias( leafTwoX.getName() + "" );
    leafFourX.setAlias( leafFourX.getName() + "" );

    branches[0].addChild( leafOneX );
    branches[0].addChild( leafTwoX );
    branches[0].addChild( leafFourX );

    objGraph.save();

    objGraph.setName( "zz" ); // passed optimistic
    branches[0].addChild( leaves[2] ); // passed optimistic

    leaves[3].setParent( branches[0] );  // passed optimistic
    ((Male)leaves[3]).setWife( (Female) leafOneX ); // passed optimistic

    branches[1].removeChild( leaves[2] ); // passed optimistic
    ((Female)leaves[0]).removeBrother( (Male) leaves[1] ); // passed optimistic
    ((Female)leafOneX).addBrother( (Male) leafTwoX ); // passed optimistic

    objGraph.save();

    retrieveAndCompare( objGraph );
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

  public void testLoadUpdateAndRetrieveData() throws Exception
  {
    Component objGraph = buildObjectGraph();

    objGraph.save();

//    objGraph.setAlias( objGraph.getAlias() + "_modified" );
    objGraph.setName( "zz" );

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testLoadAndRetrieve() throws Exception
  {
//    Component objGraph = buildObjectGraph();
    Component objGraph =  (new DataGenerator()).buildUniObjectGraph();

    objGraph.save();

    retrieveAndCompare( objGraph );
  }


  private void retrieveAndCompare( Persistable objGraph ) throws Exception
  {
    Stub seedStub;
    seedStub = objGraph.toStub();

    Persistable retrievedObjGraph = tvObject.retrieveAllFromStub(  seedStub );

    VisitorFullEqualityTester aTester = new VisitorFullEqualityTester( retrievedObjGraph );
    objGraph.accept( aTester );
    assert( "Graphs should be the same", aTester.isEqual() );
  }

  private Persistable retrieveObjectGraph( Persistable aPersistable ) throws Exception
  {
    return aPersistable.refresh();
  }

  private void retrieveFullPrintAndCompare( Persistable objGraph ) throws Exception
  {
    Stub seedStub;
    seedStub = objGraph.toStub();

    Persistable retrievedObjGraph = tvObject.retrieveAllFromStub(  seedStub );

    System.out.println( "<<<About to print objGraph>>" );
    objGraph.accept( new VisitorFullPrinter() );
    System.out.println( "<<<About to print retrievedObjGraph>>" );
    retrievedObjGraph.accept( new VisitorFullPrinter() );
    VisitorFullEqualityTester aTester = new VisitorFullEqualityTester( retrievedObjGraph );
    objGraph.accept( aTester );
    assert( "Graphs should be the same", aTester.isEqual() );
  }

  private void retrievePrintAndCompare( Persistable objGraph ) throws Exception
  {
    Stub seedStub;
    seedStub = objGraph.toStub();

    Persistable retrievedObjGraph = tvObject.retrieveAllFromStub( seedStub );

    System.out.println( "<<<About to print objGraph>>" );
    objGraph.accept( new VisitorPrinter() );
    System.out.println( "<<<About to print retrievedObjGraph>>" );
    retrievedObjGraph.accept( new VisitorPrinter() );
    VisitorFullEqualityTester aTester = new VisitorFullEqualityTester( retrievedObjGraph );
    objGraph.accept( aTester );
    assert( "Graphs should be the same", aTester.isEqual() );
  }

  private void adjustObjGraph( Component objGraph )
  {
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
  }

  public void testLoadRemoveAndRetrieveData() throws Exception
  {
    Component objGraph = buildObjectGraph();
    Component[] leaves = aDataGenerator.getLeaves();

    adjustObjGraph( objGraph );

    objGraph.save();

    // remove in transient space
    ((Female)leaves[0]).removeBrother( (Male) leaves[1] );

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testSwapToOne() throws Exception
  {
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    Component objGraph = objGraphBuilder.buildObjectGraph();
    Component[] leaves = objGraphBuilder.getLeaves();
    Composite[] branches = objGraphBuilder.getBranches();

    adjustObjGraph( objGraph );

    // Initial persistent save of object graph
    objGraph.save();

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testSwapParents() throws Exception
  {
    Component objGraph = buildObjectGraph();
    Component[] leaves = aDataGenerator.getLeaves();
    Composite[] branches = aDataGenerator.getBranches();

    adjustObjGraph( objGraph );


    // Initial persistent save of object graph
    objGraph.save();

    // reset parents
    leaves[0].setParent( branches[1] );
    leaves[1].setParent( branches[1] );
    leaves[2].setParent( branches[0] );
    leaves[3].setParent( branches[0] );

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testSwapMates() throws Exception
  {
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    Component objGraph = buildObjectGraph();
    Component[] leaves = aDataGenerator.getLeaves();
    Composite[] branches = aDataGenerator.getBranches();

    adjustObjGraph( objGraph );

    // Initial persistent save of object graph
    objGraph.save();

    // swap mates
    ((Female)leaves[0]).setHusband( (Male) leaves[3] );
    ((Female)leaves[2]).setHusband( (Male) leaves[1] );

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testNewHusband() throws Exception
  {
    PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
    Component objGraph = buildObjectGraph();
    Component[] leaves = aDataGenerator.getLeaves();
    Composite[] branches = aDataGenerator.getBranches();
    Component leafTwoX = creator.createMale( "leaf_two_x" );
    Component leafFourX = creator.createMale( "leaf_four_x" );

    adjustObjGraph( objGraph );


    leafTwoX.setAlias( leafTwoX.getName() + "" );
    leafFourX.setAlias( leafFourX.getName() + "" );

    branches[0].addChild( leafTwoX );
    branches[0].addChild( leafFourX );

    // Initial persistent save of object graph
    objGraph.save();

    // Change to new husband
    ((Female)leaves[0]).setHusband( (Male) leafTwoX );

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testLoadRemoveAndRetrieveData3() throws Exception
  {
    Component objGraph = buildObjectGraph();
    Component[] leaves = aDataGenerator.getLeaves();
    Composite[] branches = aDataGenerator.getBranches();

    adjustObjGraph( objGraph );

    objGraph.save();

    // remove in transient space
    ((Female)leaves[0]).removeBrother( (Male) leaves[1] );  // passes

    // reset parent on many to one
    ((Male)leaves[1]).setParent( branches[1] ); // passes
    ((Female)leaves[0]).setParent( branches[1] ); // passes

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testNewToOne() throws Exception
  {
    PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    Component objGraph = objGraphBuilder.buildObjectGraph();
    Component[] leaves = objGraphBuilder.getLeaves();
    Composite[] branches = objGraphBuilder.getBranches();
    Component leafTwoX = creator.createMale( "leaf_two_x" );
    Component leafFourX = creator.createMale( "leaf_four_x" );

    adjustObjGraph( objGraph );


    leafTwoX.setAlias( leafTwoX.getName() + "" );
    leafFourX.setAlias( leafFourX.getName() + "" );


    // Initial persistent save of object graph
    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public static void main( String[] args )
  {
    junit.textui.TestRunner.run( TestVisitorPersistence.class );
//    TestRunner( TestVisitorPersistence.class );
  }

}