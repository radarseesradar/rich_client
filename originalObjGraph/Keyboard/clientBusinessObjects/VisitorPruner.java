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
import java.security.*;

public class VisitorPruner extends Visitor
{
  private Object currentAttributeOwner;
  private Object currentNullAssociation;
  private Field currentField;

  private void setToAnyToNull( Object attributeOwner, Class attributeOwnersClass, String attributeName, Object nullAssociation )
  {
    Field aField = null;
    Class theSuperClass = null;
    try
    {
      aField = attributeOwnersClass.getDeclaredField( attributeName );
      aField.setAccessible( true );
      aField.set( attributeOwner, nullAssociation );
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
        setToAnyToNull( attributeOwner, theSuperClass, attributeName, nullAssociation );
      }
    }
    catch( IllegalAccessException e )
    {
      e.printStackTrace();
      System.out.println();
      System.out.println( "attributeOwner = " + attributeOwner );
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass );
      System.out.println( "attributeName = " + attributeName );
      System.out.println( "Security environment must be set, such that access control is not enforced to run this application." );
      // Security environment must be set, such that access control is not enforced to run this application.
      throw new InternalError( e.toString() );
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

  private void setToManyToNull( Object attributeOwner, String attributeName )
  {
    setToAnyToNull( attributeOwner, attributeOwner.getClass(), attributeName, NullToMany.getSingleton() );
  }

  private void setToOneToNull( Object attributeOwner, String attributeName )
  {
    setToAnyToNull( attributeOwner, attributeOwner.getClass(), attributeName, NullToOne.getSingleton() );
  }

  public Collection propagateToMany( Object attributeOwner, String attributeName, Class attributeType )
  {
    setToManyToNull( attributeOwner, attributeName );
    return  null;
  }

  public Visitable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
    setToOneToNull( attributeOwner, attributeName );
    return null;
  }

  protected void processPrimitive( Object attributeOwner, String attributeName, Class attributeType )
  {
  }

  public Visitable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
    setToOneToNull( attributeOwner, attributeName );
    return null;
  }

  public Visitable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
    setToOneToNull( attributeOwner, attributeName );
      return null;
  }

  public Collection propagateOneToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    setToManyToNull( attributeOwner, attributeName );
    return  null;
  }

  public Collection propagateManyToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    setToManyToNull( attributeOwner, attributeName );
    return  null;
  }

  public static void main( String args[] )
  {
    try
    {
    VisitorPruner aPruner = new VisitorPruner();
    VisitorPrinter aPrinter = new VisitorPrinter();
    VisitorFullPrinter aFullPrinter = new VisitorFullPrinter();
    Visitable anObjectGraph = new TestVisitor( "dont care" ).buildObjectGraph();
    Visitable aPrunedObjectGraph = null;
    System.out.println( "---java.home = " + System.getProperty( "java.home" ) );
    System.out.println( "---user.home = " + System.getProperty( "user.home" ) );
    System.out.println( "---user.dir = " + System.getProperty( "user.dir" ) );
    System.out.println( "---java.ext.dirs = " + System.getProperty( "java.ext.dirs" ) );
    System.out.println( "<<< Printing original object graph >>>");
    anObjectGraph.accept( new VisitorPrinter() );
    System.out.println( "<<< Printing pruned object graph >>>");
    aPrunedObjectGraph = ((Visitable) anObjectGraph.clone()).accept( aPruner );
    aPrunedObjectGraph.accept( new VisitorPrinter() );
    System.out.println( "<<< Fully printing pruned object graph >>>");
    aPrunedObjectGraph.accept( new VisitorFullPrinter() );
    System.out.println( "<<< Reprinting original object graph >>>");
    anObjectGraph.accept( new VisitorPrinter() );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

}