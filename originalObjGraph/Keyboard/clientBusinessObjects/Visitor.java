
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
import java.io.*;

public class Visitor implements Cloneable
{

  private Set previouslyVisited;
  private Set previouslyTransitioned;
  private PostVisitHelper postVisitHelper;

  public Visitor()
  {
    previouslyVisited = new HashSet();
    previouslyTransitioned = new HashSet();
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

  protected Set getPreviouslyTransitioned( )
  {
    return previouslyTransitioned;
  }

  protected void displayPreviousTransitions()
  {
    System.out.println( "Previously transitioned set is: ");
    for( Iterator anIterator = previouslyTransitioned.iterator(); anIterator.hasNext(); )
    {
      System.out.println( "     " + anIterator.next() );
    }
  }

  protected String getTransientPackageName()
  {
    return MiddleWareFramework.getSingleton().getTransientPackageName();
  }

  protected String getPersistentPackageName()
  {
    return MiddleWareFramework.getSingleton().getPersistentPackageName();
  }

  protected void addPreviouslyVisited( Object anObject )
  {
    previouslyVisited.add( anObject );
  }

  protected void addPreviouslyTransitioned( Transition aTransition )
  {
    previouslyTransitioned.add( aTransition );
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

  protected boolean wasPreviouslyVisited( Object anObject )
  {
    return previouslyVisited.contains( anObject );
  }

  protected boolean isClamped( Transition aTransition )
  {
    return ClampProvider.getSingleton().getDefaultClamps().containsKey( aTransition.toClamp() );
  }

  protected boolean wasPreviouslyTransitioned( Transition aTransition )
  {
    return previouslyTransitioned.contains( aTransition ) || isClamped( aTransition );
  }

  public Collection propagateToMany( Collection someDestinations )
  {
    Object aDestination;
    for( Iterator anIterator = someDestinations.iterator(); anIterator.hasNext(); )
    {
      aDestination = anIterator.next();
      propagateToOne( (Visitable) aDestination );
    }
    return someDestinations;
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
        else if( Visitable.class.isAssignableFrom( typeAtField ) )
        {
          objAtField = someFields[i].get( anObject );
          propagateToOne( (Visitable) objAtField );
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

  protected Visitor propagationVisitor()
  {
    return this;
  }

  public Collection propagateToMany( Object attributeOwner, String attributeName, Class elementType )
  {
    return (Collection) propagateToMany( attributeOwner,  attributeName );
  }

  public Collection propagateToManyBidirectional( Object attributeOwner, String attributeName, Class elementType, String inverseAttribute )
  {
    return  propagateToMany( attributeOwner,  attributeName, elementType );
  }

  public Collection propagateOneToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    return  propagateToMany( attributeOwner, attributeName, attributeType );
  }

  public Collection propagateManyToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    return  propagateToMany( attributeOwner, attributeName, attributeType );
  }

  public Collection propagateToManyUnidirectional( Object attributeOwner, String attributeName, Class elementType )
  {
    return  propagateToMany( attributeOwner,  attributeName, elementType );
  }

  protected AccessMethodNameGenerator getAccessMethodNameGenerator()
  {
    return AccessMethodNameGenerator.getSingleton();
  }

  protected Collection getToManyDestination( Object attributeOwner, String attributeName )
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

  public Collection propagateToMany( Object attributeOwner, String attributeName )
  {
    Collection destination;
    destination = getToManyDestination(  attributeOwner,  attributeName );
    propagateToMany( destination );
    return destination;
  }

  public Visitable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
    return propagateToOne( attributeOwner, attributeName );
  }

  protected void processPrimitive( Object attributeOwner, String attributeName, Class attributeType )
  {
  }

  protected void processUpdateCounter( Object attributeOwner )
  {
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

  protected Visitable getToOneDestination( Object attributeOwner, String attributeName )
  {
    Visitable destination = null;
    Class attributeOwnersClass = null;
    String getMessage = null;
    Method accessor = null;
    RoleKey aRoleKey = null;
    ToOne aToOne = null;
    if( MiddleWareFramework.getSingleton().isClient() )
    {
      aRoleKey = new RoleKey( attributeOwner, attributeName );
      aToOne = aRoleKey.getToOne();
      return (Visitable) aToOne.get();
    }
    try
    {
      attributeOwnersClass = ((StubInterface)attributeOwner).yourself().getClass();
      getMessage = getAccessMethodNameGenerator().getter( attributeName );
      accessor = attributeOwnersClass.getMethod( getMessage, new Class[0] );
      destination = (Visitable) accessor.invoke( ((StubInterface)attributeOwner).yourself(), new Object[0] );
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

  public Visitable propagateToOne( Object attributeOwner, String attributeName )
  {
      return propagateToOne( getToOneDestination( attributeOwner, attributeName ));
  }

  public Visitable propagateToOne( Visitable aDestination )
  {
    if( (aDestination != null) &&  ! wasPreviouslyVisited( aDestination ) )
    {
      addPreviouslyVisited( aDestination );
      return (Visitable) aDestination.accept( propagationVisitor() );
    }
    else
      return (Visitable) null;
  }

  public Visitable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return propagateToOne( attributeOwner, attributeName, attributeType );
  }

  public Visitable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return propagateToOne( attributeOwner, attributeName, attributeType );
  }

  // generic chain of responsibility catch all
  public Visitable visit( Object anObject )
  {
    addPreviouslyVisited( anObject );
    if( ((StubInterface) anObject ).isStub() )
      return (Visitable) null;
    return (Visitable) anObject;
  }

  // transient callbacks
  public Visitable visit( TVComposite aVisitableObject )
  {
    return visit( (Object) aVisitableObject );
  }

  public void postVisit( TVComposite aVisitableObject )
  {
    getPostVisitHelper().postVisit( (TVComposite) aVisitableObject );
  }

  public Visitable visit( TVCa aVisitableObject )
  {
    return visit( (Object) aVisitableObject );
  }

  public void postVisit( TVCa aVisitableObject )
  {
    getPostVisitHelper().postVisit( (TVCa) aVisitableObject );
  }

  public Visitable visit( TVCb aVisitableObject )
  {
    return visit( (Object) aVisitableObject );
  }

  public void postVisit( TVComponent aVisitableObject )
  {
    getPostVisitHelper().postVisit( (TVComponent) aVisitableObject );
  }

  public Visitable visit( TVComponent aVisitableObject )
  {
    return visit( (Object) aVisitableObject );
  }

  public void postVisit( TVCb aVisitableObject )
  {
    getPostVisitHelper().postVisit( (TVCb) aVisitableObject );
  }

  // persistent callbacks

  public Visitable visit( TVPComposite aVisitableObject )
  {
    return visit( (Object) aVisitableObject );
  }

  public void postVisit( TVPComposite aVisitableObject )
  {
    getPostVisitHelper().postVisit( (TVPComposite) aVisitableObject );
  }

  public Visitable visit( TVPCa aVisitableObject )
  {
    return visit( (Object) aVisitableObject );
  }

  public void postVisit( TVPCa aVisitableObject )
  {
    getPostVisitHelper().postVisit( (TVPCa) aVisitableObject );
  }

  public Visitable visit( TVPCb aVisitableObject )
  {
    return visit( (Object) aVisitableObject );
  }

  public void postVisit( TVPComponent aVisitableObject )
  {
    getPostVisitHelper().postVisit( (TVPComponent) aVisitableObject );
  }

  public Visitable visit( TVPComponent aVisitableObject )
  {
    return visit( (Object) aVisitableObject );
  }

  public void postVisit( TVPCb aVisitableObject )
  {
    getPostVisitHelper().postVisit( (TVPCb) aVisitableObject );
  }

}