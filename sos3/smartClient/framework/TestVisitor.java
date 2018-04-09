
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
public class TestVisitor extends TestCase
{
  protected Composite[] branches;
  protected Component[] leaves;

  public Component[] getLeaves()
  {
    return leaves;
  }

  public Composite[] getBranches()
  {
    return branches;
  }

  public TestVisitor( String name)
  {
    super( name );
  }

  public Component buildObjectGraph()
  {
      SmartClientFramework.getSingleton().clear();
      branches = new Composite[3];
      leaves = new Component[4];
      PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
      branches[0] = creator.createComposite( "branch_one" );
      branches[1] = creator.createComposite( "branch_two" );
      branches[2] = creator.createComposite( "branch_three" );
      leaves[0] = creator.createFemale( "Sarah" );
      leaves[1] = creator.createMale( "Abe" );
      leaves[2] = creator.createFemale( "Ruth" );
      leaves[3] = creator.createMale( "Noah" );

      branches[0].addChild( branches[2] );
      branches[0].addChild( leaves[0] );
      branches[0].addChild( leaves[1] );
//      branches[1].addChild( leaves[2] );
      leaves[2].setParent(branches[1]);
      branches[1].addChild( leaves[3] );
      branches[1].addChild( branches[0] );
      branches[2].addChild( branches[1] );
      ((Female)leaves[0]).setHusband( (Male) leaves[1] );
      ((Female)leaves[2]).setHusband( (Male) leaves[3] );
      ((Female)leaves[0]).addBrother( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[3] );
      ((Female)leaves[2]).addBrother( (Male) leaves[3] );
      ((Female)leaves[2]).addBrother( (Male) leaves[1] );

      return leaves[1];
  }

  public Component buildCopyOfObjectGraph( Component anObjectGraph )
  {
    Component copyOfGraph;
    VisitorCopier aVisitorCopier = new VisitorCopier();
    anObjectGraph.accept( aVisitorCopier );
    copyOfGraph = (Component) aVisitorCopier.getOther();

    // modify names in copy, so that we can distinguish it from original
    return (Component) copyOfGraph.accept
    (
      new Visitor()
      {
        public Persistable visit( Object anObject )
        {
          Component aComponent = (Component) anObject;
          aComponent.setName( aComponent.getName() + ":cloned:" );
          return aComponent;
        }
      }
    );
  }

  protected void setUp()
  throws Exception
  {
	SmartClientFramework.getSingleton().setPropertiesUsing( "C:\\sos3\\SOSProperties.txt" );
    SmartClientFramework.getSingleton().establishCommunications();
    buildObjectGraph();
  }

  public void testPrune()
  {
    VisitorPruner aPruner = new VisitorPruner();
    Persistable anObjectGraph = leaves[1];
    Persistable aPrunedObjectGraph = ((Persistable) anObjectGraph.clone()).accept( aPruner );
    Male aMale = (Male) aPrunedObjectGraph;
    assertEquals( "Abe", aMale.getName() );
    assert( "Expected empty collection", NullToMany.getSingleton().getAll().isEmpty() );
    assert( "Expected empty collection of sisters", aMale.getSisters().isEmpty() );
    assert( "Expected null parent", aMale.getParent() == null );
    assert( "Expected null wife", aMale.getWife() == null );
  }

  public void testEqualGraphs()
  {
    VisitorEqualityTester anEqualityTester = new VisitorEqualityTester( leaves[1] );
    Persistable result = leaves[1].accept(anEqualityTester);
    assert( "Object graphs should be equal", anEqualityTester.isEqual() );
  }

  public void testAssociationsCoordinatorMerge()
  {
    AssociationsCoordinator mergedAssociationsCoordinator = new AssociationsCoordinator();
    mergedAssociationsCoordinator.merge( SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator() );
    assert( "Association coordinators should be equal", SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().equals(mergedAssociationsCoordinator) );
  }

  public void testAssociationsCoordinatorMerge2()
  {
    Iterator anIterator = null;
    Persistable nextValue = null;
    AssociationsCoordinator mergedAssociationsCoordinator = new AssociationsCoordinator();
    mergedAssociationsCoordinator.merge( SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator() );
    anIterator = mergedAssociationsCoordinator.getWorkingSet().iterator();
    while( anIterator.hasNext() )
    {
      nextValue = (Persistable) anIterator.next();
      assert( "Associations coordinator should have been redirected"
      , nextValue.getAssociationsCoordinator() == mergedAssociationsCoordinator );
    }
  }

  public void testFullEqualGraphs()
  {
    VisitorFullEqualityTester anEqualityTester = new VisitorFullEqualityTester( leaves[1] );
    Persistable result = leaves[1].accept(anEqualityTester);
    assert( "Object graphs should be completely equal", anEqualityTester.isEqual() );
  }

  public void testEqualGraphsAfterSerialization() throws Exception
  {
    byte[] byteArray;
    Component deserializedCopyOfGraph;
    Component copyOfGraph;
    VisitorCopier aCopier;
    aCopier = new VisitorCopier();
    leaves[1].accept( aCopier );
    copyOfGraph = (Component) aCopier.getOther();
    byteArray = (new Serializer()).serialize( copyOfGraph );
    deserializedCopyOfGraph = (Component) (new Serializer()).deserialize( byteArray );
    VisitorEqualityTester anEqualityTester = new VisitorEqualityTester( deserializedCopyOfGraph );
    Persistable result = leaves[1].accept(anEqualityTester);
    assert( "Object graphs should not be equal", ! anEqualityTester.isEqual() );
  }

  public void testEqualGraphsDifferentOrigins()
  {
    Component copyOfGraph;
    copyOfGraph = (Component) leaves[1].accept( new VisitorCopier() );
    VisitorEqualityTester anEqualityTester = new VisitorEqualityTester( copyOfGraph );
    Persistable result = leaves[2].accept(anEqualityTester);
    assert( "Object graphs should not test equal", ! anEqualityTester.isEqual() );
  }

  public void testFullEqualGraphsDifferentOrigins()
  {
    Component copyOfGraph;
    copyOfGraph = (Component) leaves[1].accept( new VisitorCopier() );
    VisitorFullEqualityTester anEqualityTester = new VisitorFullEqualityTester( copyOfGraph );
    Persistable result = leaves[2].accept(anEqualityTester);
    assert( "Object graphs should not test completely equal", ! anEqualityTester.isEqual() );
  }

  public void testUnequalGraphs()
  {
    Component copyOfGraph;
    copyOfGraph = (Component) leaves[1].accept( new VisitorCopier() );
    copyOfGraph.setName( copyOfGraph.getName() + ":cloned:" );
    VisitorEqualityTester anEqualityTester = new VisitorEqualityTester( copyOfGraph );
    Persistable result = leaves[2].accept(anEqualityTester);
    assert( "Object graphs should not be equal", ! anEqualityTester.isEqual() );
  }

  public void testFullUnequalGraphs()
  {
    Component copyOfGraph;
    copyOfGraph = (Component) leaves[1].accept( new VisitorCopier() );
    copyOfGraph.setName( copyOfGraph.getName() + ":cloned:" );
    VisitorFullEqualityTester anEqualityTester = new VisitorFullEqualityTester( copyOfGraph );
    Persistable result = leaves[2].accept(anEqualityTester);
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
    Visitor aVisitor = new VisitorViaAllPaths();
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
    Visitor aVisitor = new VisitorViaAllPaths();
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
    Visitor aVisitor = new VisitorViaAllPaths();
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
    assertEquals( "model.test.client.Composite", aTransition.toClamp().getFromClassName() );
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
    expectedClamps.add( new Clamp( "Keyboard.clientBusinessObjects.Composite", "children" ) );
    expectedClamps.add( new Clamp( "Keyboard.clientBusinessObjects.Male", "wife" ) );
    expectedClamps.add( new Clamp( "Keyboard.clientBusinessObjects.Male", "sister" ) );
    Set actualClamps = ClampProvider.getSingleton().getRetrievalClamps();
    assert( "Actual clamps should be the same as expected clamps",
      actualClamps.containsAll( (Collection) expectedClamps )
      && expectedClamps.containsAll( (Collection) actualClamps ) );
  }

  */

  public void testDynamicAddToManyDestination()
  {
    Visitor aVisitor = new Visitor();
    Composite aParent = new Composite ( "Branch" );
    Female aChild = new Female ( "leaf" );
    dynamicAddToMany( aParent, "children", Component.class, aChild );
    String childsName;
    childsName = ((Component)aParent.getChildren().iterator().next()).getName();
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