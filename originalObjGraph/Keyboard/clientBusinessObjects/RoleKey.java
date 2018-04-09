package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.security.*;

public class RoleKey implements Serializable
{

  private Object manipulator;
  private String roleName;
  private boolean removed;

  public RoleKey( Object _manipulator, String _roleName )
  {
    manipulator = _manipulator;
    roleName = _roleName;
    removed = false;
  }

  public boolean wasRemoved()
  {
    return removed;
  }

  public void remove()
  {
    removed = true;
  }

  public void restore()
  {
    removed = false;
  }

  public Object getManipulator()
  {
    return manipulator;
  }

  public Clamp toClamp()
  {
    return new Clamp( getDeclaredFromClass().getName(), getRoleName() );
  }

  public String getRoleName()
  {
    return roleName;
  }

  public boolean equals( Object another )
  {
    return  getManipulator().equals( ((RoleKey) another).getManipulator() )
            && roleName.equals( ((RoleKey) another).roleName ) ;
  }

  public int hashCode()
  {
    return getManipulator().hashCode() ^ roleName.hashCode();
  }

  public Field getField()
  {
    return getField( getManipulator(), getManipulator().getClass(), getRoleName() );
  }

  private Field getField( Object attributeOwner, Class attributeOwnersClass, String attributeName )
  {
    Field aField = null;
    Class theSuperClass = null;
    try
    {
      aField = attributeOwnersClass.getDeclaredField( attributeName );
      return aField;
    }
    catch ( NoSuchFieldException e )
    {
      theSuperClass = attributeOwnersClass.getSuperclass();
      if( theSuperClass == null )
      {
        e.printStackTrace();
        System.out.println( "attributeOwner = " + attributeOwner );
        System.out.println( "attributeOwnersClass = " + attributeOwnersClass );
        System.out.println( "attributeName = " + attributeName );
        // This would indicate a programming bug.
        throw new InternalError( e.toString() );
      }
      else
      {
        return getField( attributeOwner, theSuperClass, attributeName );
      }
    }
    catch( SecurityException e )
    {
      e.printStackTrace();
      System.out.println();
      System.out.println( "attributeOwner = " + attributeOwner );
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass );
      System.out.println( "attributeName = " + attributeName );
      System.out.println( "Security environment must be set, such that ReflectPermission(suppressAccessChecks) is established." );
      // Security environment must be set, such that access control is not enforced to run this application.
      throw new InternalError( e.toString() );
    }
  }

  public Class getDeclaredFromClass()
  {
    return getField().getDeclaringClass();
  }

  public Object getFieldsValue()
  {
    Field aField = null;
    try
    {
      aField = getField();
      aField.setAccessible( true );
      return aField.get( getManipulator() );
    }
    catch( IllegalAccessException e )
    {
      e.printStackTrace();
      System.out.println();
      System.out.println( "aField = " + aField );
      // Security environment must be set, such that access control is not enforced to run this application.
      throw new InternalError( e.toString() );
    }
  }

  public UnidirectionalAssociation getAssociation()
  {
    return (UnidirectionalAssociation) getFieldsValue();
  }

  public ToOne getToOne()
  {
    return (ToOne) getFieldsValue();
  }

  public ToMany getToMany()
  {
    return (ToMany) getFieldsValue();
  }

  public String toString()
  {
    return getManipulator().toString() + "   " + getRoleName();
  }

}