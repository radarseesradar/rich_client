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

public class VisitorRetriever extends VisitorSynchronizer
{
  private AssociationsCoordinator associationsCoordinator;

  public VisitorRetriever( )
  {
    associationsCoordinator = new AssociationsCoordinator();
  }

  protected void processUpdateCounter( Object attributeOwner )
  {
    int fromValue;
    fromValue = ((Visitable)attributeOwner).getUpdateCounter();
    ((Visitable)getOther()).setUpdateCounter( fromValue );
  }

  private AssociationsCoordinator getAssociationsCoordinator()
  {
    return associationsCoordinator;
  }

  protected Visitable createOther( Object anObject )
  {
    try
    {
      if( wasPreviouslyCreated( anObject ) )
        return (Visitable) getPreviouslyCreated( anObject );

      String transientClassName = ((Visitable) anObject).otherClassName();
      Class transientClass = Class.forName( getTransientPackageName() + "." + transientClassName);

      long theKboid = ((Visitable) anObject).getKboid();

      Visitable newlyCreated = (Visitable) transientClass.newInstance();
      newlyCreated.setAssociationsCoordinator( getAssociationsCoordinator() );
      newlyCreated.xsetKboid( theKboid );
      addPreviouslyCreated( (Object) newlyCreated );
      return newlyCreated;
    }
    catch( IllegalAccessException e )
    {
      // This would indicate a programming bug.
      e.printStackTrace();
      throw new InternalError( e.toString() );
    }
    catch( ClassNotFoundException e )
    {
      // This would indicate a programming bug.
      e.printStackTrace();
      throw new InternalError( e.toString() );
    }
    catch( InstantiationException e )
    {
      // This would indicate a programming bug.
      e.printStackTrace();
      throw new InternalError( e.toString() );
    }
  }

  protected String getOthersPackageName()
  {
    return getTransientPackageName();
  }

  public Visitable visit( Object anObject )
  {
      if( getOther() == null )
        setOther( createOther( anObject ) );
      ((Visitable) getOther()).getAssociationsCoordinator().addToWorkingSet( (StubInterface) getOther() );
      return super.visit( (Object) anObject );
  }

  protected boolean wasPreviouslyTransitioned( Transition aTransition )
  {
    return getPreviouslyTransitioned().contains( aTransition );
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

      if( ! isClamped( new Transition( attributeOwner, attributeName, thisDestination ) ) )
        addPreviouslyTransitioned( new Transition( thisDestination, inverseAttributeName, attributeOwner ) );

      otherDestination = (Visitable) createOther( thisDestination );

      if( isClamped( new Transition( attributeOwner, attributeName, thisDestination ) ) )
      {
        setToOneDestination( getOther(), attributeName, deriveOthersType( attributeType )
                             , SwizzleProxy.createStubbedProxyFor( otherDestination ) );
        return null;
      }

      // Assign the transient toOne companion to the current transient other
      // and return the persistent toOne.
      setToOneDestination( getOther(), attributeName, deriveOthersType( attributeType ), otherDestination );
      return (Visitable) thisDestination.accept( propagationVisitor( otherDestination ) );
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
      && ! ( wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, nextThis ) ) ) )
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
      if( ! isClamped( new Transition( attributeOwner, attributeName, nextThis ) ) )
        addPreviouslyTransitioned( new Transition( nextThis, inverseAttributeName, attributeOwner ) );
    }

    // Transition them.
    for( Iterator anIterator = nonTransitionedMembers.iterator(); anIterator.hasNext(); )
    {
      nextThis = anIterator.next();
      nextOther = createOther( nextThis );
      if( isClamped( new Transition( attributeOwner, attributeName, nextThis ) ) )
      {
        addToManyDestination( getOther(), attributeName
                              , SwizzleProxy.createStubbedProxyFor( nextOther ), deriveOthersType( elementType ) );
      }
      else
      {
        addToManyDestination( getOther(), attributeName, nextOther, deriveOthersType( elementType ) );
        ((Visitable) nextThis ).accept( propagationVisitor( (Visitable) nextOther ) );
      }
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
      if( isClamped( new Transition( attributeOwner, attributeName, nextThis ) ) )
      {
        addToManyDestination( getOther(), attributeName
                              , SwizzleProxy.createStubbedProxyFor( nextOther ), deriveOthersType( elementType ) );
      }
      else
      {
        addToManyDestination( getOther(), attributeName, nextOther, deriveOthersType( elementType ) );
        ((Visitable) nextThis ).accept( propagationVisitor( (Visitable) nextOther ) );
      }
    }
    return nonTransitionedMembers;
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

      if( isClamped( new Transition( attributeOwner, attributeName, thisDestination ) ) )
      {
        setToOneDestination( getOther(), attributeName, deriveOthersType( attributeType )
                             , SwizzleProxy.createStubbedProxyFor( otherDestination ) );
        return null;
      }

      // Assign the transient toOne companion to the current transient other
      // and return the persistent toOne.
      setToOneDestination( getOther(), attributeName, deriveOthersType( attributeType ), otherDestination );
      return (Visitable) thisDestination.accept( propagationVisitor( otherDestination ) );
  }

  protected void setToOneDestination( Object attributeOwner, String attributeName, Class parameterType, Object parameterValue )
  {
    RoleKey aRoleKey = null;
    ToOne aToOne = null;
    if( Proxy.isProxyClass( parameterValue.getClass() ) )
    {
      aRoleKey = new RoleKey( attributeOwner, attributeName );
      aToOne = aRoleKey.getToOne();
      aToOne.set( parameterValue );
    }
    else
    {
      super.setToOneDestination(  attributeOwner,  attributeName,  parameterType,  parameterValue );
    }
  }

  protected void addToManyDestination( Object attributeOwner, String attributeName, Object parameterValue, Class elementType )
  {
    RoleKey aRoleKey = null;
    ToMany aToMany = null;
    if( Proxy.isProxyClass( parameterValue.getClass() ) )
    {
      aRoleKey = new RoleKey( attributeOwner, attributeName );
      aToMany = aRoleKey.getToMany();
      aToMany.add( parameterValue );
    }
    else
    {
      super.addToManyDestination( attributeOwner, attributeName, parameterValue, elementType );
    }
  }

}