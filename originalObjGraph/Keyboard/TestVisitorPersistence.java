
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

public class TestVisitorPersistence extends TestCase
{
  protected static TVSessionBeanHome tvHome;
  protected static TVSessionBeanObject tvObject;
  protected static Client client;
  private TestVisitor aTestVisitor;

  public TestVisitorPersistence( String name)
  {
    super( name );
  }

  public static void oneTimeSetUp()
  {
    try
    {
      client = new Client("//kbch");
      client.connectUser("secuser", "secuser");
      javax.naming.Context homes = new javax.naming.InitialContext();

      Object h = homes.lookup("TCService" + "/TVSessionBean");
      tvHome = (TVSessionBeanHome) PortableRemoteObject.narrow(h,TVSessionBeanHome.class);
      tvObject = tvHome.create();
      MiddleWareFramework.getSingleton().setTVSessionBeanObject( tvObject );
      MiddleWareFramework.getSingleton().setClient( client );

    }
    catch (Exception e)
    {
    e.printStackTrace();
    }
  }

  public static Test allTests()
  {
    return new TestSuite( TestVisitorPersistence.class );
  }

  public static Test simpleTests()
  {
    TestSuite suite = new TestSuite();
    suite.addTest( new TestVisitorPersistence( "testGreeting" ) );
    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
//    suite.addTest( new TestVisitorPersistence( "testCreateOne" ) );
//    suite.addTest( new TestVisitorPersistence( "testCreateAll" ) );
    return suite;
  }

  public static Test complexTests()
  {
    TestSuite suite = new TestSuite();
//    suite.addTest( new TestVisitorPersistence( "testGreeting" ) );
    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    suite.addTest( new TestVisitorPersistence( "testLoadUpdateAndRetrieveData" ) );
    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    suite.addTest( new TestVisitorPersistence( "testLoadRemoveAndRetrieveData3" ) );
    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    suite.addTest( new TestVisitorPersistence( "testNewToOne" ) );
    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    suite.addTest( new TestVisitorPersistence( "testSwapParents" ) ); // passes
    suite.addTest( new TestVisitorPersistence( "testRemoveSessionBean" ) );
//    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
//    suite.addTest( new TestVisitorPersistence( "testSwapMates" ) ); // fails because of secant
//    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
//    suite.addTest( new TestVisitorPersistence( "testSwapToOne" ) ); // fails because of secant
    return suite;
  }

  public static Test optimisticLockingTests()
  {
    TestSuite suite = new TestSuite();
    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    suite.addTest( new TestVisitorPersistence( "testOptimisticLocking1" ) );
//    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
//    suite.addTest( new TestVisitorPersistence( "testOptimisticLocking2" ) );
//    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
//    suite.addTest( new TestVisitorPersistence( "testNewHusband" ) );
    return suite;
  }

  public static Test minimalTests()
  {
    TestSuite suite = new TestSuite();
    suite.addTest( new TestVisitorPersistence( "testClearDB" ) );
    suite.addTest( new TestVisitorPersistence( "testLoadData" ) );
    return suite;
  }

  public static Test suite( )
  {
    TestSuite suite;
//    suite = (TestSuite) allTests();
//    suite = (TestSuite) simpleTests();
    suite = (TestSuite) complexTests();
//    suite = (TestSuite) optimisticLockingTests();
//      suite = (TestSuite) minimalTests();

    TestSetup wrapper = new TestSetup(suite)
    {
      public void setUp()
      {
        oneTimeSetUp();
      }
    };
    return wrapper;
  }

  public void testRemoveSessionBean()
  throws javax.ejb.RemoveException, java.rmi.RemoteException
  {
    tvObject.remove();
  }

  public void testGreeting() throws Exception
  {
    client.begin();
      assert( "Should have returned the string 'Hello World'", tvObject.greeting().equals( "Hello World" ) );
    client.commit();
  }

  public void testCreateOne() throws Exception
  {
    tvObject.testCreateOne();
  }

  public void testCreateAll() throws Exception
  {
    tvObject.testCreateAll();
  }

  public void testLoadData() throws Exception
  {
    TVComponent objGraph = buildObjectGraph();

    objGraph.save();
  }

  public void testClearDB() throws Exception
  {
    client.begin();
      tvObject.clearDB();
    client.commit();
  }

  public void testOptimisticLocking1() throws Exception
  {
    TVComponent objGraph = buildObjectGraph();

    objGraph.save();

    retrieveFullPrintAndCompare( objGraph );
  }

  public void testOptimisticLocking2() throws Exception
  {
    KBCPersistentObjectFactory creator = KBCPersistentObjectFactory.getSingleton();
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    TVComponent objGraph = objGraphBuilder.buildObjectGraph();
    TVComponent [] leaves = objGraphBuilder.getLeaves();
    TVComposite [] branches = objGraphBuilder.getBranches();
    TVComponent leafOneX = creator.createTVCa( "leaf_one_x" );
    TVComponent leafTwoX = creator.createTVCb( "leaf_two_x" );
    TVComponent leafFourX = creator.createTVCb( "leaf_four_x" );

    adjustObjGraph( objGraph );

    leafOneX.setAlias( leafOneX.getName() + "" );
    leafTwoX.setAlias( leafTwoX.getName() + "" );
    leafFourX.setAlias( leafFourX.getName() + "" );

    branches[0].addChild( leafOneX );
    branches[0].addChild( leafTwoX );
    branches[0].addChild( leafFourX );

    objGraph.save();

//    objGraph.setName( "zz" ); // passed optimistic
//    branches[0].addChild( leaves[2] ); // passed optimistic

//    leaves[3].setParent( branches[0] );  // passed optimistic
//    ((TVCb)leaves[3]).setWife( (TVCa) leafOneX ); // passed optimistic

//    branches[1].removeChild( leaves[2] ); // passed optimistic
//    ((TVCa)leaves[0]).removeBrother( (TVCb) leaves[1] ); // passed optimistic
//    ((TVCa)leafOneX).addBrother( (TVCb) leafTwoX ); // passed optimistic

    objGraph.save();

    retrievePrintAndCompare( objGraph );
  }

  private TVComponent buildObjectGraph()
  {
    TVComponent objGraph = (aTestVisitor = new TestVisitor( "dontCare" )).buildObjectGraph();

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

  public void testLoadUpdateAndRetrieveData() throws Exception
  {
    TVComponent objGraph = buildObjectGraph();

    objGraph.save();

//    objGraph.setAlias( objGraph.getAlias() + "_modified" );
    objGraph.setName( "zz" );

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testLoadAndRetrieve() throws Exception
  {
    TVComponent objGraph = buildObjectGraph();

    objGraph.save();

    retrieveAndCompare( objGraph );
  }


  private void retrieveAndCompare( Visitable objGraph ) throws Exception
  {
    Stub seedStub;
    byte [] byteArray;
    seedStub = objGraph.toStub();

    client.begin();
    byteArray = tvObject.retrieveAllFromStub( (new Serializer()).serialize( seedStub ) );
    client.commit();

    Visitable retrievedObjGraph = (Visitable) (new Serializer()).deserialize( byteArray );
    VisitorFullEqualityTester aTester = new VisitorFullEqualityTester( retrievedObjGraph );
    objGraph.accept( aTester );
    assert( "Graphs should be the same", aTester.isEqual() );
  }

  private Visitable retrieveObjectGraph( Visitable aVisitable ) throws Exception
  {
    return aVisitable.refresh();
  }

  private void retrieveFullPrintAndCompare( Visitable objGraph ) throws Exception
  {
    Stub seedStub;
    byte [] byteArray;
    seedStub = objGraph.toStub();

    client.begin();
    byteArray = tvObject.retrieveAllFromStub( (new Serializer()).serialize( seedStub ) );
    client.commit();

    Visitable retrievedObjGraph = (Visitable) (new Serializer()).deserialize( byteArray );
    System.out.println( "<<<About to print objGraph>>" );
    objGraph.accept( new VisitorFullPrinter() );
    System.out.println( "<<<About to print retrievedObjGraph>>" );
    retrievedObjGraph.accept( new VisitorFullPrinter() );
    VisitorFullEqualityTester aTester = new VisitorFullEqualityTester( retrievedObjGraph );
    objGraph.accept( aTester );
    assert( "Graphs should be the same", aTester.isEqual() );
  }

  private void retrievePrintAndCompare( Visitable objGraph ) throws Exception
  {
    Stub seedStub;
    byte [] byteArray;
    seedStub = objGraph.toStub();

    client.begin();
    byteArray = tvObject.retrieveAllFromStub( (new Serializer()).serialize( seedStub ) );
    client.commit();

    Visitable retrievedObjGraph = (Visitable) (new Serializer()).deserialize( byteArray );
    System.out.println( "<<<About to print objGraph>>" );
    objGraph.accept( new VisitorPrinter() );
    System.out.println( "<<<About to print retrievedObjGraph>>" );
    retrievedObjGraph.accept( new VisitorPrinter() );
    VisitorFullEqualityTester aTester = new VisitorFullEqualityTester( retrievedObjGraph );
    objGraph.accept( aTester );
    assert( "Graphs should be the same", aTester.isEqual() );
  }

  private void adjustObjGraph( TVComponent objGraph )
  {
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
  }

  public void testLoadRemoveAndRetrieveData() throws Exception
  {
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    TVComponent objGraph = objGraphBuilder.buildObjectGraph();
    TVComponent [] leaves = objGraphBuilder.getLeaves();

    adjustObjGraph( objGraph );

    objGraph.save();

    // remove in transient space
    ((TVCa)leaves[0]).removeBrother( (TVCb) leaves[1] );

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testSwapToOne() throws Exception
  {
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    TVComponent objGraph = objGraphBuilder.buildObjectGraph();
    TVComponent [] leaves = objGraphBuilder.getLeaves();
    TVComposite [] branches = objGraphBuilder.getBranches();

    adjustObjGraph( objGraph );

    // Initial persistent save of object graph
    objGraph.save();

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testSwapParents() throws Exception
  {
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    TVComponent objGraph = objGraphBuilder.buildObjectGraph();
    TVComponent [] leaves = objGraphBuilder.getLeaves();
    TVComposite [] branches = objGraphBuilder.getBranches();

    adjustObjGraph( objGraph );


    // Initial persistent save of object graph
    objGraph.save();

    // reset parents
    leaves[0].setParent( branches[1] );
    leaves[1].setParent( branches[1] );
    leaves[2].setParent( branches[0] );
    leaves[3].setParent( branches[0] );

    objGraph.save();

    retrieveFullPrintAndCompare( objGraph );
  }

  public void testSwapMates() throws Exception
  {
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    TVComponent objGraph = objGraphBuilder.buildObjectGraph();
    TVComponent [] leaves = objGraphBuilder.getLeaves();
    TVComposite [] branches = objGraphBuilder.getBranches();

    adjustObjGraph( objGraph );

    // Initial persistent save of object graph
    objGraph.save();

    // swap mates
    ((TVCa)leaves[0]).setHusband( (TVCb) leaves[3] );
    ((TVCa)leaves[2]).setHusband( (TVCb) leaves[1] );

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testNewHusband() throws Exception
  {
    KBCPersistentObjectFactory creator = KBCPersistentObjectFactory.getSingleton();
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    TVComponent objGraph = objGraphBuilder.buildObjectGraph();
    TVComponent [] leaves = objGraphBuilder.getLeaves();
    TVComposite [] branches = objGraphBuilder.getBranches();
    TVComponent leafTwoX = creator.createTVCb( "leaf_two_x" );
    TVComponent leafFourX = creator.createTVCb( "leaf_four_x" );

    adjustObjGraph( objGraph );


    leafTwoX.setAlias( leafTwoX.getName() + "" );
    leafFourX.setAlias( leafFourX.getName() + "" );

    branches[0].addChild( leafTwoX );
    branches[0].addChild( leafFourX );

    // Initial persistent save of object graph
    objGraph.save();

    // Change to new husband
    ((TVCa)leaves[0]).setHusband( (TVCb) leafTwoX );

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testLoadRemoveAndRetrieveData3() throws Exception
  {
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    TVComponent objGraph = objGraphBuilder.buildObjectGraph();
    TVComponent [] leaves = objGraphBuilder.getLeaves();
    TVComposite [] branches = objGraphBuilder.getBranches();

    adjustObjGraph( objGraph );

    objGraph.save();

    // remove in transient space
    ((TVCa)leaves[0]).removeBrother( (TVCb) leaves[1] );  // passes

    // reset parent on many to one
    ((TVCb)leaves[1]).setParent( branches[1] ); // passes
    ((TVCa)leaves[0]).setParent( branches[1] ); // passes

    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public void testNewToOne() throws Exception
  {
    KBCPersistentObjectFactory creator = KBCPersistentObjectFactory.getSingleton();
    TestVisitor objGraphBuilder = new TestVisitor( "dontCare" );
    TVComponent objGraph = objGraphBuilder.buildObjectGraph();
    TVComponent [] leaves = objGraphBuilder.getLeaves();
    TVComposite [] branches = objGraphBuilder.getBranches();
    TVComponent leafTwoX = creator.createTVCb( "leaf_two_x" );
    TVComponent leafFourX = creator.createTVCb( "leaf_four_x" );

    adjustObjGraph( objGraph );


    leafTwoX.setAlias( leafTwoX.getName() + "" );
    leafFourX.setAlias( leafFourX.getName() + "" );


    // Initial persistent save of object graph
    objGraph.save();

    retrieveAndCompare( objGraph );
  }

  public static void main( String args[] )
  {
    junit.textui.TestRunner.run( TestVisitorPersistence.class );
//    TestRunner( TestVisitorPersistence.class );
  }

}