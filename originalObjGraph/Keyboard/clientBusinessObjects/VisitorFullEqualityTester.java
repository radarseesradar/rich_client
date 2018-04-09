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
import java.io.*;
import java.lang.reflect.*;

public class VisitorFullEqualityTester extends VisitorSynchronizer
{
  public VisitorFullEqualityTester( Visitable _other )
  {
    setOther( _other );
  }

  private class SharedBoolean
  {
    private boolean sharedEqual = true;

    private void setToEqual()
    {
      sharedEqual = true;
    }

    private void setToUnequal()
    {
      sharedEqual = false;
    }

    private boolean isEqual()
    {
      return sharedEqual;
    }
  }

  private SharedBoolean equal = new SharedBoolean();

  private void checkEquality( Object op1, Object op2 )
  {
    if( ! equal.isEqual() )
      return;
    if( op1 == null && op2 == null )
      return;
    if( op1 == null || op2 == null )
    {
      equal.setToUnequal();
    }
    else if( op1.getClass() == op2.getClass() && op1.equals( op2 ) )
    {
      equal.setToEqual();
    }
    else
    {
      equal.setToUnequal();
    }
  }


  public boolean isEqual()
  {
    return equal.isEqual();
  }

  public Visitable visit( Object anObject )
  {
    checkEquality( anObject, other );
    if( !equal.isEqual() )
      return (Visitable) anObject;
    return super.visit( (Object) anObject );
  }

  public Visitable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
      // get toOne destination and create its transient companion
      Visitable thisDestination;
      Visitable otherDestination;
      if( attributeName == null
      || ((StubInterface)attributeOwner).isStub()
      || ((StubInterface)other).isStub()
      || ( attributeOwner.getClass() != other.getClass() ) )
      {
        equal.setToUnequal();
        return (Visitable) attributeOwner;
      }

      thisDestination = (Visitable) getToOneDestination( attributeOwner, attributeName );
      otherDestination = (Visitable) getToOneDestination( getOther(), attributeName );

      if( !equal.isEqual() )
        return null;

      if( thisDestination == null  )
        return null;
      if( wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) ) )
        return null;
      addPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) );

      checkEquality( thisDestination, otherDestination );
      if( !equal.isEqual() )
        return null;

      // Return the toOne.
      return (Visitable) thisDestination.accept( propagationVisitor( otherDestination ) );
  }

  public Collection propagateManyToMany( Object attributeOwner, String attributeName, Class elementType,
  String inverseAttributeName, Class inverseType )
  {
    return propagateToMany( attributeOwner, attributeName, elementType );
  }

  public Collection propagateOneToMany( Object attributeOwner, String attributeName, Class elementType,
  String inverseAttributeName, Class inverseType )
  {
    return propagateToMany( attributeOwner, attributeName, elementType );
  }

  public Collection propagateToMany( Object attributeOwner, String attributeName, Class elementType )
  {
    Object nextThis;
    checkEquality( attributeOwner, getOther() );
    if( !equal.isEqual() )
      return null;
    Collection thisDestinationCollection = getToManyDestination( attributeOwner, attributeName );
    Collection nonTransitionedMembers = (Collection) new ArrayList();


    // Select elements that haven't previously been transitioned.
    for( Iterator anIterator = thisDestinationCollection.iterator(); anIterator.hasNext(); )
    {
      nextThis = anIterator.next();
      if( (nextThis!= null)
      && ! wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, nextThis ) ) )
      {
        nonTransitionedMembers.add( nextThis );
      }
    }

    if( nonTransitionedMembers.isEmpty() )
      return nonTransitionedMembers;


    // Record their transitions.
    for( Iterator anIterator = nonTransitionedMembers.iterator(); anIterator.hasNext(); )
    {
      nextThis = anIterator.next();
      addPreviouslyTransitioned( new Transition( attributeOwner, attributeName, nextThis ) );
    }

    Collection thatDestinationCollection = getToManyDestination( getOther(), attributeName );

    // Transition them.
    return propagateToMany( nonTransitionedMembers, thatDestinationCollection );
  }

  protected Collection propagateToMany( Collection aCollection, Collection otherCollection )
  {
    Object nextObj;
    Object companion;
    if( ! equal.isEqual() )
      return null;
    for( Iterator anIterator = aCollection.iterator(); anIterator.hasNext(); )
    {
      nextObj = anIterator.next();
      if( otherCollection.contains( nextObj ) )
      {
        companion = findCompanion( nextObj, otherCollection );
      }
      else
      {
        equal.setToUnequal();
        return null;
      }
      propagateToOne( (Visitable) nextObj, (Visitable) companion );
    }
    return aCollection;
  }

  public Visitable propagateToOne( Object receiver, String attributeName )
  {
    if( attributeName == null
    || ((StubInterface)receiver).isStub()
    || ((StubInterface)other).isStub()
    || ( receiver.getClass() != other.getClass() ) )
    {
      equal.setToUnequal();
      return (Visitable) receiver;
    }
    return propagateToOne( getToOneDestination( receiver, attributeName ), getToOneDestination( other, attributeName ) );
  }

  protected Visitable propagateToOne( Visitable anAcceptor, Visitable otherAcceptor )
  {
    checkEquality( anAcceptor, otherAcceptor );
    if( !equal.isEqual() )
      return null;
    if( anAcceptor != null )
      return (Visitable) anAcceptor.accept( propagationVisitor( otherAcceptor ) );
    else
      return (Visitable) null;
  }

  public Visitable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType,
  String inverseAttributeName, Class inverseType )
  {
    return propagateToOne( attributeOwner,  attributeName,  attributeType );
  }

  public Visitable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType,
  String inverseAttributeName, Class inverseType )
  {
    return propagateToOne( attributeOwner,  attributeName,  attributeType );
  }

  protected void processPrimitive( Object attributeOwner, String attributeName, Class attributeType )
  {
    Object thisPrimitive;
    Object thatPrimitive;
    thisPrimitive = getToOnePrimitive( attributeOwner, attributeName );
    thatPrimitive = getToOnePrimitive( getOther(), attributeName );
    checkEquality( thisPrimitive, thatPrimitive );
  }

}