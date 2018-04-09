package smartClient.framework;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */

public class VisitorRetriever extends VisitorSynchronizer
{
  private AssociationsCoordinator associationsCoordinator;

  public VisitorRetriever( )
  {
    associationsCoordinator = new AssociationsCoordinator();
  }

  public void resetOther()
  {
  	setOther( null );
  }

  protected void processUpdateCounter( Object attributeOwner )
  {
    int fromValue;
    fromValue = ((Persistable)attributeOwner).getUpdateCounter();
    ((Persistable)getOther()).setUpdateCounter( fromValue );
  }

  public AssociationsCoordinator getAssociationsCoordinator()
  {
    return associationsCoordinator;
  }

  public Persistable createOther( Object anObject )
  {
    Class transientClass = null;
    try
    {
      if( wasPreviouslyCreated( anObject ) )
        return (Persistable) getPreviouslyCreated( anObject );

      String transientClassName = ((Persistable) anObject).otherClassName();
      String transientPackageName = ((Persistable) anObject).otherPackageName();
      if( transientPackageName == null || transientPackageName.equals("" ) )
      	transientClass = Class.forName( transientClassName);
      else
      	transientClass = Class.forName( transientPackageName + "." + transientClassName);

      long theScfoid = ((Persistable) anObject).getScfoid();

      Persistable newlyCreated = (Persistable) transientClass.newInstance();
      newlyCreated.setAssociationsCoordinator( getAssociationsCoordinator() );
      newlyCreated.setScfoid( theScfoid );
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

  public Persistable visit( Object anObject )
  {
      if( getOther() == null )
        setOther( createOther( anObject ) );
      ((Persistable) getOther()).getAssociationsCoordinator().addToWorkingSet( (StubInterface) getOther() );
      return super.visit( (Object) anObject );
  }

  public boolean wasPreviouslyTransitioned( Transition aTransition )
  {
    return getPreviouslyTransitioned().contains( aTransition );
  }

  public Persistable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType,
  String inverseAttributeName, Class inverseType )
  {
      // get toOne destination and create its transient companion
      Persistable otherDestination;
      Persistable thisDestination;
      thisDestination = (Persistable) getToOneDestination( attributeOwner, attributeName );
      if( thisDestination == null  )
        return null;
      if( wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) ) )
        return null;
      addPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) );

      if( ! isClamped( new Transition( attributeOwner, attributeName, thisDestination ) ) )
        addPreviouslyTransitioned( new Transition( thisDestination, inverseAttributeName, attributeOwner ) );

      otherDestination = (Persistable) createOther( thisDestination );

      if( isClamped( new Transition( attributeOwner, attributeName, thisDestination ) ) )
      {
        setToOneDestination( getOther(), attributeName, deriveOthersType( attributeType )
                             , SwizzleProxy.createStubbedProxyFor( otherDestination ) );
        return null;
      }

      // Assign the transient toOne companion to the current transient other
      // and return the persistent toOne.
      setToOneDestination( getOther(), attributeName, deriveOthersType( attributeType ), otherDestination );
      return (Persistable) thisDestination.accept( propagationVisitor( otherDestination ) );
  }


  public Collection propagateOneToMany( Object attributeOwner, String attributeName, Class elementType,
  String inverseAttributeName, Class inverseType )
  {
  	PropagatorRetriever aPropagator = new PropagatorRetriever( this, attributeOwner,  attributeName,  elementType,
   																	  inverseAttributeName,  inverseType );
    return aPropagator.propagateOneToMany();
  }


  public Collection propagateToMany( Object attributeOwner, String attributeName, Class elementType,
  String inverseAttributeName, Class inverseType )
  {
  	PropagatorRetriever aPropagator = new PropagatorRetriever( this, attributeOwner,  attributeName,  elementType );
    return aPropagator.propagateToMany();
  }


  public Persistable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
      // get toOne destination and create its transient companion
      Persistable thisDestination;
      Persistable otherDestination;
      thisDestination = (Persistable) getToOneDestination( attributeOwner, attributeName );
      if( thisDestination == null  )
        return null;
      if( wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) ) )
        return null;
      addPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) );

      otherDestination = (Persistable) createOther( thisDestination );

      // Assign the transient toOne companion to the current transient other
      // and return the persistent toOne.
      setToOneDestination( getOther(), attributeName, deriveOthersType( attributeType ), otherDestination );
      return (Persistable) thisDestination.accept( propagationVisitor( otherDestination ) );
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