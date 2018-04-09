
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package Keyboard.clientBusinessObjects;

import java.util.*;
import java.lang.reflect.*;
import java.lang.*;
import Keyboard.clientBusinessObjects.*;
import Keyboard.*;
import junit.framework.*;
import java.io.*;

public class TestVisitor extends TestCase
{
  protected TVComposite branches[];
  protected TVComponent leaves[];

  public TVComponent [] getLeaves()
  {
    return leaves;
  }

  public TVComposite [] getBranches()
  {
    return branches;
  }

  public TestVisitor( String name)
  {
    super( name );
  }

  public TVComponent buildObjectGraph()
  {
      MiddleWareFramework.getSingleton().clear();
      branches = new TVComposite[3];
      leaves = new TVComponent[4];
      KBCPersistentObjectFactory creator = KBCPersistentObjectFactory.getSingleton();
      branches[0] = creator.createTVComposite( "branch_one" );
      branches[1] = creator.createTVComposite( "branch_two" );
      branches[2] = creator.createTVComposite( "branch_three" );
      leaves[0] = creator.createTVCa( "leaf_one" );
      leaves[1] = creator.createTVCb( "leaf_two" );
      leaves[2] = creator.createTVCa( "leaf_three" );
      leaves[3] = creator.createTVCb( "leaf_four" );

      branches[0].addChild( branches[2] );
      branches[0].addChild( leaves[0] );
      branches[0].addChild( leaves[1] );
//      branches[1].addChild( leaves[2] );
      leaves[2].setParent(branches[1]);
      branches[1].addChild( leaves[3] );
      branches[1].addChild( branches[0] );
      branches[2].addChild( branches[1] );
      ((TVCa)leaves[0]).setHusband( (TVCb) leaves[1] );
      ((TVCa)leaves[2]).setHusband( (TVCb) leaves[3] );
      ((TVCa)leaves[0]).addBrother( (TVCb) leaves[1] );
      ((TVCa)leaves[0]).addBrother( (TVCb) leaves[3] );
      ((TVCa)leaves[2]).addBrother( (TVCb) leaves[3] );
      ((TVCa)leaves[2]).addBrother( (TVCb) leaves[1] );

      return leaves[1];
  }

  public TVComponent buildCopyOfObjectGraph( TVComponent anObjectGraph )
  {
    TVComponent copyOfGraph;
    VisitorCopier aVisitorCopier = new VisitorCopier();
    anObjectGraph.accept( aVisitorCopier );
    copyOfGraph = (TVComponent) aVisitorCopier.getOther();

    // modify names in copy, so that we can distinguish it from original
    return (TVComponent) copyOfGraph.accept
    (
      new Visitor()
      {
        public Visitable visit( Object anObject )
        {
          TVComponent aComponent = (TVComponent) anObject;
          aComponent.setName( aComponent.getName() + ":cloned:" );
          return aComponent;
        }
      }
    );
  }

  protected void setUp()
  throws Exception
  {
    MiddleWareFramework.getSingleton().establishCommunications();
    buildObjectGraph();
  }

  public void testPrune()
  {
    VisitorPruner aPruner = new VisitorPruner();
    Visitable anObjectGraph = leaves[1];
    Visitable aPrunedObjectGraph = ((Visitable) anObjectGraph.clone()).accept( aPruner );
    TVCb aTVCb = (TVCb) aPrunedObjectGraph;
    assertEquals( "leaf_two", aTVCb.getName() );
    assert( "Expected empty collection", NullToMany.getSingleton().getAll().isEmpty() );
    assert( "Expected empty collection of sisters", aTVCb.getSisters().isEmpty() );
    assert( "Expected null parent", aTVCb.getParent() == null );
    assert( "Expected null wife", aTVCb.getWife() == null );
  }

  public void testEqualGraphs()
  {
    VisitorEqualityTester anEqualityTester = new VisitorEqualityTester( leaves[1] );
    Visitable result = leaves[1].accept(anEqualityTester);
    assert( "Object graphs should be equal", anEqualityTester.isEqual() );
  }

  public void testAssociationsCoordinatorMerge()
  {
    AssociationsCoordinator mergedAssociationsCoordinator = new AssociationsCoordinator();
    mergedAssociationsCoordinator.merge( MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator() );
    assert( "Association coordinators should be equal", MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().equals(mergedAssociationsCoordinator) );
  }

  public void testAssociationsCoordinatorMerge2()
  {
    Iterator anIterator = null;
    Visitable nextValue = null;
    AssociationsCoordinator mergedAssociationsCoordinator = new AssociationsCoordinator();
    mergedAssociationsCoordinator.merge( MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator() );
    anIterator = mergedAssociationsCoordinator.getWorkingSet().iterator();
    while( anIterator.hasNext() )
    {
      nextValue = (Visitable) anIterator.next();
      assert( "Associations coordinator should have been redirected"
      , nextValue.getAssociationsCoordinator() == mergedAssociationsCoordinator );
    }
  }

  public void testFullEqualGraphs()
  {
    VisitorFullEqualityTester anEqualityTester = new VisitorFullEqualityTester( leaves[1] );
    Visitable result = leaves[1].accept(anEqualityTester);
    assert( "Object graphs should be completely equal", anEqualityTester.isEqual() );
  }

  public void testEqualGraphsAfterSerialization() throws Exception
  {
    byte byteArray[];
    TVComponent deserializedCopyOfGraph;
    TVComponent copyOfGraph;
    VisitorCopier aCopier;
    aCopier = new VisitorCopier();
    leaves[1].accept( aCopier );
    copyOfGraph = (TVComponent) aCopier.getOther();
    System.out.println( "<10>TestVisitor.testEqualGraphsAfterSerialization  copy of graph is." );
    copyOfGraph.accept( new VisitorPrinter() );
    byteArray = (new Serializer()).serialize( copyOfGraph );
    deserializedCopyOfGraph = (TVComponent) (new Serializer()).deserialize( byteArray );
    System.out.println( "<20>TestVisitor.testEqualGraphsAfterSerialization deserialized copy of graph is." );
    deserializedCopyOfGraph.accept( new VisitorPrinter() );
    VisitorEqualityTester anEqualityTester = new VisitorEqualityTester( deserializedCopyOfGraph );
    Visitable result = leaves[1].accept(anEqualityTester);
    assert( "Object graphs should not be equal", ! anEqualityTester.isEqual() );
  }

  public void testEqualGraphsDifferentOrigins()
  {
    TVComponent copyOfGraph;
    copyOfGraph = (TVComponent) leaves[1].accept( new VisitorCopier() );
    VisitorEqualityTester anEqualityTester = new VisitorEqualityTester( copyOfGraph );
    Visitable result = leaves[2].accept(anEqualityTester);
    assert( "Object graphs should not test equal", ! anEqualityTester.isEqual() );
  }

  public void testFullEqualGraphsDifferentOrigins()
  {
    TVComponent copyOfGraph;
    copyOfGraph = (TVComponent) leaves[1].accept( new VisitorCopier() );
    VisitorFullEqualityTester anEqualityTester = new VisitorFullEqualityTester( copyOfGraph );
    Visitable result = leaves[2].accept(anEqualityTester);
    assert( "Object graphs should not test completely equal", ! anEqualityTester.isEqual() );
  }

  public void testUnequalGraphs()
  {
    TVComponent copyOfGraph;
    copyOfGraph = (TVComponent) leaves[1].accept( new VisitorCopier() );
    copyOfGraph.setName( copyOfGraph.getName() + ":cloned:" );
    VisitorEqualityTester anEqualityTester = new VisitorEqualityTester( copyOfGraph );
    Visitable result = leaves[2].accept(anEqualityTester);
    assert( "Object graphs should not be equal", ! anEqualityTester.isEqual() );
  }

  public void testFullUnequalGraphs()
  {
    TVComponent copyOfGraph;
    copyOfGraph = (TVComponent) leaves[1].accept( new VisitorCopier() );
    copyOfGraph.setName( copyOfGraph.getName() + ":cloned:" );
    VisitorFullEqualityTester anEqualityTester = new VisitorFullEqualityTester( copyOfGraph );
    Visitable result = leaves[2].accept(anEqualityTester);
    assert( "Object graphs should not be completely equal", ! anEqualityTester.isEqual() );
  }

  public void testWasTransitioned()
  {
    String to1 = "to";
    String to2 = "t" + "o";
    assert( "to1 should equal to2", to1.equals( to2 ) );
    assert( "to1 hash code should equal to2 hash code", to1.hashCode() == to2.hashCode() );
    Transition aTransition = new Transition( branches[0], "to", leaves[0] );
    Transition aTransition2 = new Transition( branches[0], "t" + "o", leaves[0] );
    assert( "aTransition should equal aTransition2", aTransition.equals( aTransition2 ) );
    assert( "aTransition's hashCode should equalt that of aTransition2", aTransition.hashCode() == aTransition2.hashCode() );
    Visitor aVisitor = new Visitor();
    aVisitor.addPreviouslyTransitioned( aTransition );
    Visitor aVisitorClone = (Visitor) aVisitor.clone();
    assert( "We should recognize that we've already taken this transition", aVisitor.wasPreviouslyTransitioned( aTransition2 ) );
  }

  public void testWasTransitioned2()
  {
    String to1 = "to";
    String to2 = "t" + "o";
    assert( "to1 should equal to2", to1.equals( to2 ) );
    assert( "to1 hash code should equal to2 hash code", to1.hashCode() == to2.hashCode() );
    Transition aTransition = new Transition( branches[0], "to", leaves[0] );
    Transition aTransition2 = new Transition( branches[0], "t" + "o", leaves[0] );
    assert( "aTransition should equal aTransition2", aTransition.equals( aTransition2 ) );
    assert( "aTransition's hashCode should equalt that of aTransition2", aTransition.hashCode() == aTransition2.hashCode() );
    Visitor aVisitor = new Visitor();
    aVisitor.addPreviouslyTransitioned( aTransition );
    aVisitor.addPreviouslyTransitioned( aTransition2 );
    assert( "We should recognize that we've already taken this transition", aVisitor.wasPreviouslyTransitioned( aTransition ) );
  }

  public void testWasTransitioned3()
  {
    String to1 = "to";
    String to2 = "t" + "o";
    assert( "to1 should equal to2", to1.equals( to2 ) );
    assert( "to1 hash code should equal to2 hash code", to1.hashCode() == to2.hashCode() );
    Transition aTransition = new Transition( branches[0], "to", leaves[0] );
    Transition aTransition2 = new Transition( branches[0], "t" + "o", leaves[0] );
    assert( "aTransition should equal aTransition2", aTransition.equals( aTransition2 ) );
    assert( "aTransition's hashCode should equalt that of aTransition2", aTransition.hashCode() == aTransition2.hashCode() );
    Visitor aVisitor = new Visitor();
    aVisitor.addPreviouslyTransitioned( aTransition );
    assert( "We should recognize that we've already taken this transition", aVisitor.wasPreviouslyTransitioned( aTransition ) );
    assert( "We should recognize that we've already taken this transition", aVisitor.wasPreviouslyTransitioned( aTransition ) );
  }

  public void testToClamp()
  {
    Set aSet = new HashSet();
    Transition aTransition = new Transition( branches[0], "children", leaves[0] );
    Transition aTransition2 = new Transition( branches[0], "child" + "ren", leaves[0] );
    aSet.add( aTransition.toClamp() );
    assertEquals( "Keyboard.clientBusinessObjects.TVComposite", aTransition.toClamp().getFromClassName() );
    assert( "We should be able to use transition2 to find clamp for transition in set", aSet.contains( aTransition2.toClamp() ));
  }

  public void testClamp()
  {
    Set aSet = new HashSet();
    Clamp clamp1 = new Clamp( "Person", "friend" );
    Clamp clamp2 = new Clamp( "Per" + "son", "fr" + "iend" );
    aSet.add( clamp1 );
    assert( "We should be able to use clamp2 to find clamp1 in set", aSet.contains( clamp2 ));
  }

  /*
  public void testProvider()
  throws Exception
  {
    Set expectedClamps = new HashSet();
    expectedClamps.add( new Clamp( "Keyboard.clientBusinessObjects.TVComposite", "children" ) );
    expectedClamps.add( new Clamp( "Keyboard.clientBusinessObjects.TVCb", "wife" ) );
    expectedClamps.add( new Clamp( "Keyboard.clientBusinessObjects.TVCb", "sister" ) );
    Set actualClamps = ClampProvider.getSingleton().getDefaultClamps();
    assert( "Actual clamps should be the same as expected clamps",
      actualClamps.containsAll( (Collection) expectedClamps )
      && expectedClamps.containsAll( (Collection) actualClamps ) );
  }

  */

  public void testDynamicAddToManyDestination()
  {
    Visitor aVisitor = new Visitor();
    TVComposite aParent = new TVComposite( "Branch" );
    TVCa aChild = new TVCa( "leaf" );
    dynamicAddToMany( aParent, "children", TVComponent.class, aChild );
    String childsName;
    childsName = ((TVComponent)aParent.getChildren().iterator().next()).getName();
    assert( "should have found childs name", childsName.equals(aChild.getName()));
  }

  public void dynamicAddToMany( Object attributeOwner, String attributeName, Class parameterType, Object parameterValue )
  {
    try
    {
      Class attributeOwnersClass = attributeOwner.getClass();
      String setMessage = AccessMethodNameGenerator.getSingleton().adder( attributeName );
      Class [] setParameterTypes = new Class[1];
      setParameterTypes[0] = parameterType;
      Method setMethod = attributeOwnersClass.getMethod(setMessage, setParameterTypes );
      Object [] setParameters = new Object[1];
      setParameters[0] = parameterValue;
      setMethod.invoke( attributeOwner, setParameters );
    }
    catch( NoSuchMethodException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( InvocationTargetException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( IllegalAccessException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }

  public static Test suite( )
  {
    return new TestSuite( TestVisitor.class );
  }

}