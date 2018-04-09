package smartClient.framework;


import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import model.*;

/**
 * @author Steve McDaniel 
 * <br>Copyright (c) Steve McDaniel
 */
public class VisitorSynchronizer extends VisitorViaAllPaths
{
  protected Persistable other;

  /**
   * @associates Object 
   */
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

  public Persistable getOther()
  {
    return other;
  }

  protected void setOther( Persistable _other )
  {
    other = _other;
  }

  public Persistable createOther( Object anObject )
  {
    try
    {
      if( wasPreviouslyCreated( anObject ) )
        return (Persistable) getPreviouslyCreated( anObject );

      long theScfoid = - ((Persistable) anObject).getScfoid();

      Persistable newlyCreated = (Persistable) anObject.getClass().newInstance();
      newlyCreated.setScfoid( theScfoid );
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


  // Probably not invoked, but overriden  to insure that we can't possibly stop propagating on previously visited.
  // Propogation should only terminate on previously transitioned.
  public Persistable propagateToOne( Persistable aDestination )
  {
    if( (aDestination != null) )
    {
      addPreviouslyVisited( aDestination );
      return (Persistable) aDestination.accept( propagationVisitor() );
    }
    else
      return (Persistable) null;
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
      if( wasPreviouslyTransitioned( new Transition( thisDestination, inverseAttributeName, attributeOwner ) ) )
        return null;
      addPreviouslyTransitioned( new Transition( thisDestination, inverseAttributeName, attributeOwner ) );

      otherDestination = (Persistable) createOther( thisDestination );

      // Assign the transient toOne companion to the current transient other
      // and return the persistent toOne.
      setToOneDestination( getOther(), attributeName, deriveOthersType( attributeType ), otherDestination );
      return (Persistable) thisDestination.accept( propagationVisitor( otherDestination ) );
  }

  public Persistable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType,
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
  	PropagatorSynchronizer aPropagator = new PropagatorSynchronizer( this, attributeOwner,  attributeName,  elementType,
   																	  inverseAttributeName,  inverseType );
    return aPropagator.propagateOneToMany();
  }

  public Collection propagateToMany( Object attributeOwner, String attributeName, Class elementType )
  {
  	PropagatorSynchronizer aPropagator = new PropagatorSynchronizer( this, attributeOwner,  attributeName,  elementType );
    return aPropagator.propagateToMany();
  }

  public Visitor propagationVisitor( Persistable _other )
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

  public Class deriveOthersType( Class attributesType )
  {
  	return SmartClientFramework.getSingleton().deriveOthersType( attributesType );
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
      e.printStackTrace();
      System.out.println( "elementType = " + elementType.getName() );
      System.out.println( "parameterType = " + parameterType.getName() );
      System.out.println( "setMessage = " + setMessage );
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass.getName() );
	  System.out.println( "attributeName = " + attributeName );
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