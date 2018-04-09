
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

public class TestVisitorPersistenceOrphansOnly extends TestCase
{
  protected static TVSessionBeanHome tvHome;
  protected static TVSessionBeanObject tvObject;
  protected static Client client;
  private TestVisitor aTestVisitor;

  public TestVisitorPersistenceOrphansOnly( String name)
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
    return new TestSuite( TestVisitorPersistenceOrphansOnly.class );
  }


  public static Test complexTests()
  {
    TestSuite suite = new TestSuite();
    suite.addTest( new TestVisitorPersistenceOrphansOnly( "testClearDB" ) );
    suite.addTest( new TestVisitorPersistenceOrphansOnly( "testProxy" ) );
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



  public void testProxy() throws Exception
  {
    TVComponent objGraph = buildObjectGraph();

    objGraph.save();

    proxyRetrieveAndCompare( objGraph );
  }

  private void proxyRetrieveAndCompare( Visitable objGraph ) throws Exception
  {
    TVComposite branches[] = aTestVisitor.getBranches();
    TVComponent leaves[] = aTestVisitor.getLeaves();
    TVComposite aBranch = null;
    Visitable leavesObjGraph = null;
    AssociationsCoordinator savedOriginal = new AssociationsCoordinator();
    savedOriginal.merge( leaves[1].getAssociationsCoordinator() );
    MiddleWareFramework.getSingleton().clear();

    System.out.println( "<<<About to print leaves[1]>>" );
    leaves[1].accept( new VisitorPrinterIgnoreClamps() );

    leavesObjGraph = leaves[1].refresh();
    System.out.println( "<<<About to print leavesObjGraph after initial retrieval>>" );
    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( true );
    leavesObjGraph.accept( new VisitorPrinterIgnoreClamps() );

    leavesObjGraph.getAssociationsCoordinator().printString();

    VisitorEqualityTesterIgnoreClamps aTester = new VisitorEqualityTesterIgnoreClamps( leavesObjGraph );
    leaves[1].accept( aTester );
    assert( "Graphs should not be the same", ! aTester.isEqual() );

    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( true );
    System.out.println( "<<<About to print leavesObjGraph after comparing for inequality>>" );
    leavesObjGraph.accept( new VisitorPrinterIgnoreClamps() );

    // fault in branch_one and branch_three
    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( false );
    aBranch = ((TVComponent)leavesObjGraph).getParent();
    aBranch.getChildren();

    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( true );
    System.out.println( "<<<About to print leavesObjGraph after faulting in branch_one and branch_three>>" );
    leavesObjGraph.accept( new VisitorPrinterIgnoreClamps() );

    aTester = new VisitorEqualityTesterIgnoreClamps( leavesObjGraph );
    leaves[1].accept( aTester );
    assert( "Graphs should not be the same", ! aTester.isEqual() );

    // fault in branch_two and remainder of graph
    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( false );
    aBranch = aBranch.getParent();
    aBranch.getChildren();
    aBranch.getParent();

    leavesObjGraph.getAssociationsCoordinator().setPreservingStubs( false );
    System.out.println( "<<<About to print leavesObjGraph after faulting in branch_two and remainder of graph>>" );
    leavesObjGraph.accept( new VisitorPrinterIgnoreClamps() );

    aTester = new VisitorEqualityTesterIgnoreClamps( leavesObjGraph );
    leaves[1].accept( aTester );
    assert( "Graphs should  be the same", aTester.isEqual() );

    leavesObjGraph.getAssociationsCoordinator().printString();

    assert( "Associations coordinators should be equal", leavesObjGraph.getAssociationsCoordinator().equals( savedOriginal ) );

    assert( "Association coordinator should be consistent", leavesObjGraph.getAssociationsCoordinator().isConsistent() );

  }

  public static void main( String args[] )
  {
    junit.textui.TestRunner.run( TestVisitorPersistenceOrphansOnly.class );
//    TestRunner( TestVisitorPersistenceOrphansOnly.class );
  }

}