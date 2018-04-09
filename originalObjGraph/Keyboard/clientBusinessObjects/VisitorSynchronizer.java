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
import java.lang.reflect.*;
import java.lang.*;
import Keyboard.*;
import java.io.*;

public class VisitorSynchronizer extends Visitor
{
  protected Visitable other;
  protected HashMap previouslyCreated;

  public VisitorSynchronizer()
  {
    previouslyCreated = new HashMap();
  }

  protected boolean wasPreviouslyCreated( Object anObject )
  {
    return previouslyCreated.containsKey( anObject );
  }

  protected void addPreviouslyCreated( Object anObject )
  {
    previouslyCreated.put( anObject, anObject );
  }

  protected Object getPreviouslyCreated( Object anObject )
  {
    return previouslyCreated.get( anObject );
  }

  public Visitable getOther()
  {
    return other;
  }

  protected void setOther( Visitable _other )
  {
    other = _other;
  }

  protected Visitable createOther( Object anObject )
  {
    try
    {
      if( wasPreviouslyCreated( anObject ) )
        return (Visitable) getPreviouslyCreated( anObject );

      long theKboid = - ((Visitable) anObject).getKboid();

      Visitable newlyCreated = (Visitable) anObject.getClass().newInstance();
      newlyCreated.xsetKboid( theKboid );
      addPreviouslyCreated( (Object) newlyCreated );
      return newlyCreated;
    }
    catch( IllegalAccessException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( InstantiationException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }

  public Visitable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
      // get toOne destination and create its transient companion
      Visitable thisDestination;
      Visitable otherDestination;
      thisDestination = (Visitable) getToOneDestination( attributeOwner, attributeName );
      if( thisDestination == null  )
        return null;
      if( wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) ) )
        return null;
      addPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) );

      otherDestination = (Visitable) createOther( thisDestination );

      // Assign the transient toOne companion to the current transient other
      // and return the persistent toOne.
      setToOneDestination( getOther(), attributeName, deriveOthersType( attributeType ), otherDestination );
      return (Visitable) thisDestination.accept( propagationVisitor( otherDestination ) );
  }


  // Probably not invoked, but overriden  to insure that we can't possibly stop propagating on previously visited.
  // Propogation should only terminate on previously transitioned.
  public Visitable propagateToOne( Visitable aDestination )
  {
    if( (aDestination != null) )
    {
      addPreviouslyVisited( aDestination );
      return (Visitable) aDestination.accept( propagationVisitor() );
    }
    else
      return (Visitable) null;
  }

  public Visitable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType,
  String inverseAttributeName, Class inverseType )
  {
      // get toOne destination and create its transient companion
      Visitable otherDestination;
      Visitable thisDestination;
      thisDestination = (Visitable) getToOneDestination( attributeOwner, attributeName );
      if( thisDestination == null  )
        return null;
      if( wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) ) )
        return null;
      addPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) );
      if( wasPreviouslyTransitioned( new Transition( thisDestination, inverseAttributeName, attributeOwner ) ) )
        return null;
      addPreviouslyTransitioned( new Transition( thisDestination, inverseAttributeName, attributeOwner ) );

      otherDestination = (Visitable) createOther( thisDestination );

      // Assign the transient toOne companion to the current transient other
      // and return the persistent toOne.
      setToOneDestination( getOther(), attributeName, deriveOthersType( attributeType ), otherDestination );
      return (Visitable) thisDestination.accept( propagationVisitor( otherDestination ) );
  }

  public Visitable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType,
  String inverseAttributeName, Class inverseType )
  {
    return propagateManyToOne( attributeOwner, attributeName, attributeType, inverseAttributeName, inverseType );
  }

  public Collection propagateManyToMany( Object attributeOwner, String attributeName, Class elementType,
  String inverseAttributeName, Class inverseType )
  {
    return propagateOneToMany( attributeOwner, attributeName, elementType, inverseAttributeName, inverseType );
  }

  public Collection propagateOneToMany( Object attributeOwner, String attributeName, Class elementType,
  String inverseAttributeName, Class inverseType )
  {
    Collection thisDestinationCollection = getToManyDestination( attributeOwner, attributeName );
    Visitable nextOther;
    Object nextThis;
    Object companion;
    Collection nonTransitionedMembers = (Collection) new ArrayList();


    // Select elements that haven't previously been transitioned.
    for( Iterator anIterator = thisDestinationCollection.iterator(); anIterator.hasNext(); )
    {
      nextThis = anIterator.next();
      if( (nextThis!= null)
      && ! ( wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, nextThis ) )
      || wasPreviouslyTransitioned( new Transition( nextThis, inverseAttributeName, attributeOwner ) ) ) )
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
      addPreviouslyTransitioned( new Transition( nextThis, inverseAttributeName, attributeOwner ) );
    }

    // Transition them.
    for( Iterator anIterator = nonTransitionedMembers.iterator(); anIterator.hasNext(); )
    {
      nextThis = anIterator.next();
      nextOther = createOther( nextThis );
      addToManyDestination( getOther(), attributeName, nextOther, deriveOthersType( elementType ) );
      ((Visitable) nextThis ).accept( propagationVisitor( (Visitable) nextOther ) );
    }
    return nonTransitionedMembers;
  }

  public Collection propagateToMany( Object attributeOwner, String attributeName, Class elementType )
  {
    Collection thisDestinationCollection = getToManyDestination( attributeOwner, attributeName );
    Visitable nextOther;
    Object nextThis;
    Object companion;
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

    // Transition them.
    for( Iterator anIterator = nonTransitionedMembers.iterator(); anIterator.hasNext(); )
    {
      nextThis = anIterator.next();
      nextOther = createOther( nextThis );
      addToManyDestination( getOther(), attributeName, nextOther, deriveOthersType( elementType ) );
      ((Visitable) nextThis ).accept( propagationVisitor( (Visitable) nextOther ) );
    }
    return nonTransitionedMembers;
  }

  protected Visitor propagationVisitor( Visitable _other )
  {
    VisitorSynchronizer copyOfThis =  (VisitorSynchronizer) clone();
    copyOfThis.setOther( _other );
    return copyOfThis;
  }

  protected void processPrimitive( Object attributeOwner, String attributeName, Class attributeType )
  {
    Object fromValue;
    Object toValue;
    String fromString;
    String toString;
    fromValue = getToOnePrimitive( attributeOwner, attributeName );
    toValue = getToOnePrimitive( getOther(), attributeName );
    fromString = "" + fromValue;
    toString = "" + toValue;
    if( fromString.equals( toString ) )
      return;
    setToOneDestination( getOther(), attributeName, attributeType, fromValue );
  }

  protected String getOthersPackageName()
  {
    return null;
  }

  protected Class deriveOthersType( Class attributesType )
  {
    try
    {
      Visitable attributesObj = (Visitable) attributesType.newInstance();
      String othersPackageName = getOthersPackageName();
      if( othersPackageName == null )
        return Class.forName( attributesObj.otherClassName() );
      return Class.forName( othersPackageName + "." + attributesObj.otherClassName() );
    }
    catch( IllegalAccessException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( ClassNotFoundException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( InstantiationException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }

  protected void addToManyDestination( Object attributeOwner, String attributeName, Object parameterValue, Class elementType )
  {
      Class attributeOwnersClass = null;
      Class parameterType = null;
      String setMessage = "";
      Class [] setParameterTypes = new Class[1];
      Method setMethod = null;
      Object [] setParameters = new Object[1];
    try
    {
      attributeOwnersClass = attributeOwner.getClass();
      parameterType = Class.forName( elementType.getName() );
      setMessage = getAccessMethodNameGenerator().adder( attributeName );
      setParameterTypes[0] = parameterType;
      setMethod = attributeOwnersClass.getMethod(setMessage, setParameterTypes );
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
    catch( ClassNotFoundException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }

}