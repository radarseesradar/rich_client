/*
 * Visitor
 * Copyright (c) Steve McDaniel
 */

package model;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import smartClient.framework.*;

/**
 * @author Steve McDaniel 
 * @version 1.0
 **/
public class Visitor implements Cloneable
{
  /**
   * @associates Object 
   */
  private Set previouslyVisited;
  private PostVisitHelper postVisitHelper;

  public Visitor()
  {
    previouslyVisited = new HashSet();
    setPostVisitHelper( new PostVisitHelper( this ) );
  }

  private void setPostVisitHelper( PostVisitHelper _postVisitHelper )
  {
    postVisitHelper = _postVisitHelper;
  }

  private PostVisitHelper getPostVisitHelper( )
  {
    return postVisitHelper;
  }

  public Visitor propagationVisitor()
  {
    return this;
  }

  protected void addPreviouslyVisited( Object anObject )
  {
    previouslyVisited.add( anObject );
  }

  protected boolean wasPreviouslyVisited( Object anObject )
  {
    return previouslyVisited.contains( anObject );
  }

  protected AccessMethodNameGenerator getAccessMethodNameGenerator()
  {
    return AccessMethodNameGenerator.getSingleton();
  }

  protected Object findCompanion( Object anObject, Collection aCollection )
  {
    Object nextObj;

    for( Iterator anIterator = aCollection.iterator(); anIterator.hasNext(); )
    {
      nextObj = anIterator.next();
      if( anObject.equals( nextObj ) )
        return nextObj;
    }
    throw new InternalError( "IMPOSSIBLE: Failed to find companion after passing containment check." );
  }

  public Object clone( )
  {
    try
    {
      Object theClone;
      theClone = super.clone();
      ( (Visitor) theClone).setPostVisitHelper( new PostVisitHelper( (Visitor) theClone ) );
      return theClone;
    }
    catch( CloneNotSupportedException e )
    {
      // Cannot happen -- all visitors support  cloning
      throw new InternalError( e.toString() );
    }
  }

  public void addPreviouslyTransitioned( Transition aTransition )
  {
  	throw new InternalError( "Subclasses responsibility" );
  }

  public boolean wasPreviouslyTransitioned( Transition aTransition )
  {
  	throw new InternalError( "Subclasses responsibility" );
  }

  protected Set getPreviouslyTransitioned( )
  {
  	throw new InternalError( "Subclasses responsibility" );
  }

  protected void displayPreviousTransitions()
  {
  }

  protected void processPrimitive( Object attributeOwner, String attributeName, Class attributeType )
  {
  }

  protected void addPreviouslyCreated( Object anObject )
  {
  	throw new InternalError( "Subclasses responsibility" );
  }

  protected boolean wasPreviouslyCreated( Object anObject )
  {
  	throw new InternalError( "Subclasses responsibility" );
  }

  protected Object getPreviouslyCreated( Object anObject )
  {
  	throw new InternalError( "Subclasses responsibility" );
  }

  public Class deriveOthersType( Class attributesType )
  {
  	throw new InternalError( "Subclasses responsibility" );
  }

  public Persistable getOther()
  {
  	throw new InternalError( "Subclasses responsibility" );
  }

  protected void setOther( Persistable _other )
  {
  	throw new InternalError( "Subclasses responsibility" );
  }

  public Persistable createOther( Object anObject )
  {
  	throw new InternalError( "Subclasses responsibility" );
  }

  public boolean isClamped( Transition aTransition )
  {
    return ClampProvider.getSingleton().getRetrievalClamps().containsKey( aTransition.toClamp() );
  }

  // This method unexpectedly thows an IllegalAccessException.  Don't use it until it is fixed.
/*
  public void propagateDeclaredFields( Class aType, Object anObject )
  {
    try
    {
      Object objAtField;
      Class typeAtField;
      Field someFields[] = aType.getDeclaredFields();
      for( int i = 0; i < someFields.length; i++ )
      {
        typeAtField = someFields[i].getType();
        if( typeAtField.isPrimitive() )
          continue;
        else if( AbstractCollection.class.isAssignableFrom( typeAtField ) )
        {
          objAtField = someFields[i].get( anObject );
          propagateToMany( (Collection) objAtField );
        }
        else if( Persistable.class.isAssignableFrom( typeAtField ) )
        {
          objAtField = someFields[i].get( anObject );
          propagateToOne( (Persistable) objAtField );
        }
      }
    }
    catch( IllegalAccessException e )
    {
      // Shouldnot happen if we control the security environment
      throw new InternalError( e.toString() );
    }
  }
*/

  protected void processUpdateCounter( Object attributeOwner )
  {
  }

  protected Persistable getToOneDestination( Object attributeOwner, String attributeName )
  {
    Persistable destination = null;
    Class attributeOwnersClass = null;
    String getMessage = null;
    Method accessor = null;
    RoleKey aRoleKey = null;
    ToOne aToOne = null;
    if( SmartClientFramework.getSingleton().isClient() )
    {
      aRoleKey = new RoleKey( attributeOwner, attributeName );
      aToOne = aRoleKey.getToOne();
      return (Persistable) aToOne.get();
    }
    try
    {
      attributeOwnersClass = ((StubInterface)attributeOwner).yourself().getClass();
      getMessage = getAccessMethodNameGenerator().getter( attributeName );
      accessor = attributeOwnersClass.getMethod( getMessage, new Class[0] );
      destination = (Persistable) accessor.invoke( ((StubInterface)attributeOwner).yourself(), new Object[0] );
      return  destination;
    }
    catch( NoSuchMethodException e )
    {
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass.getName() );
      System.out.println( "getMessage = " + getMessage );
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( InvocationTargetException e )
    {
      e.printStackTrace();
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass.getName() );
      System.out.println( "getMessage = " + getMessage );
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( IllegalAccessException e )
    {
      e.printStackTrace();
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass.getName() );
      System.out.println( "getMessage = " + getMessage );
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }

  protected void setToOneDestination( Object attributeOwner, String attributeName, Class parameterType, Object parameterValue )
  {
      Class attributeOwnersClass = null;
      String setMessage = null;
      Class [] setParameterTypes = null;
      Method setMethod = null;
      Object [] setParameters = null;
    try
    {
      attributeOwnersClass = attributeOwner.getClass();
      setMessage = getAccessMethodNameGenerator().setter( attributeName );
      setParameterTypes = new Class[1];
      setParameterTypes[0] = parameterType;
      setMethod = attributeOwnersClass.getMethod(setMessage, setParameterTypes );
      setParameters = new Object[1];
      setParameters[0] = parameterValue;
      setMethod.invoke( attributeOwner, setParameters );
    }
    catch( NoSuchMethodException e )
    {
      e.printStackTrace();
      System.out.println( "attributeOwner = " + attributeOwner );
      System.out.println( "attributeName = " + attributeName );
      System.out.println( "parameterType = " + parameterType );
      System.out.println( "parameterValue = " + parameterValue );
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass );
      System.out.println( "setMessage = " + setMessage );
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( InvocationTargetException e )
    {
      e.printStackTrace();
      System.out.println( "attributeOwner = " + attributeOwner );
      System.out.println( "attributeName = " + attributeName );
      System.out.println( "parameterType = " + parameterType );
      System.out.println( "parameterValue = " + parameterValue );
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass );
      System.out.println( "setMessage = " + setMessage );
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( IllegalAccessException e )
    {
      e.printStackTrace();
      System.out.println( "attributeOwner = " + attributeOwner );
      System.out.println( "attributeName = " + attributeName );
      System.out.println( "parameterType = " + parameterType );
      System.out.println( "parameterValue = " + parameterValue );
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass );
      System.out.println( "setMessage = " + setMessage );
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( IllegalArgumentException e )
    {
      e.printStackTrace();
      System.out.println( "attributeOwner = " + attributeOwner );
      System.out.println( "attributeName = " + attributeName );
      System.out.println( "parameterType = " + parameterType );
      System.out.println( "parameterValue = " + parameterValue );
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass );
      System.out.println( "setMessage = " + setMessage );
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }

  }

  public Collection getToManyDestination( Object attributeOwner, String attributeName )
  {
    String getMessage = null;
    try
    {
      Collection destination;
      Class attributeOwnersClass = ((StubInterface)attributeOwner).yourself().getClass();
      getMessage = getAccessMethodNameGenerator().getter( attributeName );
      Method accessor = attributeOwnersClass.getMethod( getMessage, new Class[0] );
      destination = (Collection) accessor.invoke( ((StubInterface)attributeOwner).yourself(), new Object[0] );
      return destination;
    }
    catch( NoSuchMethodException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( InvocationTargetException e )
    {
      e.printStackTrace();
      System.out.println( "getMessage = " + getMessage );
      System.out.println( "attributeOwner = " + attributeOwner );
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( IllegalAccessException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }

  protected Object getToOnePrimitive( Object attributeOwner, String attributeName )
  {
    try
    {
      Object result;
      Class attributeOwnersClass = attributeOwner.getClass();
      String getMessage = getAccessMethodNameGenerator().getter( attributeName );
      Method accessor = attributeOwnersClass.getMethod( getMessage, new Class[0] );
      result =  accessor.invoke( attributeOwner, new Object[0] );
      return  result;
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

  public Collection propagateManyToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    return  propagateToMany( attributeOwner, attributeName, attributeType );
  }

  public Collection propagateOneToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    return  propagateToMany( attributeOwner, attributeName, attributeType );
  }

  public Collection propagateToMany( Object attributeOwner, String attributeName, Class elementType )
  {
    return (Collection) propagateToMany( attributeOwner,  attributeName );
  }

  public Collection propagateToMany( Object attributeOwner, String attributeName )
  {
    Collection destination;
    destination = getToManyDestination(  attributeOwner,  attributeName );
    propagateToMany( destination );
    return destination;
  }

  public Collection propagateToMany( Collection someDestinations )
  {
    Object aDestination;
    for( Iterator anIterator = someDestinations.iterator(); anIterator.hasNext(); )
    {
      aDestination = anIterator.next();
      propagateToOne( (StubInterface) aDestination );
    }
    return someDestinations;
  }

  public Persistable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return propagateToOne( attributeOwner, attributeName, attributeType );
  }

  public Persistable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return propagateToOne( attributeOwner, attributeName, attributeType );
  }

  public Persistable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
    return propagateToOne( attributeOwner, attributeName );
  }

  public Persistable propagateToOne( Object attributeOwner, String attributeName )
  {
      return propagateToOne( getToOneDestination( attributeOwner, attributeName ));
  }

  public Persistable propagateToOne( StubInterface aDestination )
  {
    if( (aDestination != null) &&  ! wasPreviouslyVisited( aDestination ) )
    {
      addPreviouslyVisited( aDestination );
      return (Persistable) aDestination.accept(  propagationVisitor() );
    }
    else
      return (Persistable) null;
  }

  // One visit serves everybody
  public Persistable visit( Object anObject )
  {
    addPreviouslyVisited( anObject );
    if( ((StubInterface) anObject ).isStub() )
      return (Persistable) null;
    return (Persistable) anObject;
  }

  public void postVisit( PersistenceAdapter aPersistableObject )
  {
    getPostVisitHelper().postVisit( aPersistableObject );
  }

  // transient callbacks
  public void postVisit( model.test.client.Composite aPersistableObject )
  {
    getPostVisitHelper().postVisit(  aPersistableObject );
  }

  public void postVisit( model.test.client.Female aPersistableObject )
  {
    getPostVisitHelper().postVisit(  aPersistableObject );
  }

  public void postVisit( model.test.client.Component aPersistableObject )
  {
    getPostVisitHelper().postVisit(  aPersistableObject );
  }

  public void postVisit( model.test.client.Male aPersistableObject )
  {
    getPostVisitHelper().postVisit(  aPersistableObject );
  }

  // persistent callbacks

  public void postVisit( model.test.server.CompositeP aPersistableObject )
  {
    getPostVisitHelper().postVisit(  aPersistableObject );
  }

  public void postVisit( model.test.server.FemaleP aPersistableObject )
  {
    getPostVisitHelper().postVisit(  aPersistableObject );
  }

  public void postVisit( model.test.server.ComponentP aPersistableObject )
  {
    getPostVisitHelper().postVisit(  aPersistableObject );
  }

  public void postVisit( model.test.server.MaleP aPersistableObject )
  {
    getPostVisitHelper().postVisit(  aPersistableObject );
  }

}