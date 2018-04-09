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

public class VisitorViaAllPaths extends Visitor
{

  public Visitable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
      // get toOne destination and create its transient companion
      Visitable thisDestination;
      if( attributeName == null  )
      {
        return (Visitable) attributeOwner;
      }

      thisDestination = (Visitable) getToOneDestination( attributeOwner, attributeName );


      if( thisDestination == null  )
        return null;
      if( wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) ) )
        return null;
      addPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) );

      // Return the toOne.
      return (Visitable) thisDestination.accept( propagationVisitor() );
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
    Collection thisDestinationCollection = getToManyDestination( attributeOwner, attributeName );
    Collection nonTransitionedMembers = (Collection) new ArrayList();


    // Select elements that haven't previously been transitioned.
    for( Iterator anIterator = thisDestinationCollection.iterator(); anIterator.hasNext(); )
    {
      nextThis = anIterator.next();
      if( (nextThis!= null)
      && !((StubInterface)nextThis).isStub()
      && ! wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, nextThis ) ) )
      {
        nonTransitionedMembers.add( nextThis );
      }
    }

    if( nonTransitionedMembers.isEmpty() )
      return nonTransitionedMembers;

    // Sort transitions
    Collections.sort( (List) nonTransitionedMembers );


    // Record their transitions.
    for( Iterator anIterator = nonTransitionedMembers.iterator(); anIterator.hasNext(); )
    {
      nextThis = anIterator.next();
      addPreviouslyTransitioned( new Transition( attributeOwner, attributeName, nextThis ) );
    }

    // Transition them.
    return propagateToMany( nonTransitionedMembers );
  }

  public Collection propagateToMany( Collection aCollection )
  {
    Object nextObj;
    for( Iterator anIterator = aCollection.iterator(); anIterator.hasNext(); )
    {
      nextObj = anIterator.next();
      propagateToOne( (Visitable) nextObj );
    }
    return aCollection;
  }

  public Visitable propagateToOne( Object receiver, String attributeName )
  {
    if( attributeName == null )
    {
      return (Visitable) receiver;
    }
    return propagateToOne( getToOneDestination( receiver, attributeName ) );
  }

  public Visitable propagateToOne( Visitable anAcceptor )
  {
    if( anAcceptor != null )
      return (Visitable) anAcceptor.accept( propagationVisitor() );
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
    thisPrimitive = getToOnePrimitive( attributeOwner, attributeName );
  }
}