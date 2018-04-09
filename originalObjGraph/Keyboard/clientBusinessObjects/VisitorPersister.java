
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
import Keyboard.*;
import java.io.*;

public class VisitorPersister extends VisitorSynchronizer
{

  public VisitorPersister()
  {
  }

  protected String getOthersPackageName()
  {
    return getPersistentPackageName();
  }

  protected Visitable createOther( Object anObject )
  {
      String persistentClassName = "";
      Class persistentClass = null;
      String persistentCreationMessage = "";
      long theKboid = 0;
    try
    {
      if( wasPreviouslyCreated( anObject ) )
        return (Visitable) getPreviouslyCreated( anObject );
      persistentClassName = ((Visitable) anObject).otherClassName();
      persistentClass = Class.forName( getPersistentPackageName() + "." + persistentClassName);
      persistentCreationMessage = "create" + persistentClassName;
      theKboid = ((Visitable) anObject).getKboid();


      Class [] createParameterTypes = new Class[1];
      createParameterTypes[0] = long.class;

      Method createMethod = persistentClass.getMethod(persistentCreationMessage, createParameterTypes );
      Object [] createParameters = new Object[1];
      createParameters[0] = new Long( theKboid );
      Visitable newlyCreated = (Visitable) createMethod.invoke( persistentClass, createParameters );
      addPreviouslyCreated( (Object) newlyCreated );
      return newlyCreated;
    }
    catch( NoSuchMethodException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( InvocationTargetException e )
    {
      e.printStackTrace();
      System.out.println( "persistentClassName = " + persistentClassName );
      System.out.println( "persistentClass = " + persistentClass.getName() );
      System.out.println( "persistentCreationMessage = " + persistentCreationMessage );
      System.out.println( "theKboid = " + theKboid );
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

  public Visitable visit( Object anObject )
  {
      if( getOther() == null )
        setOther( createOther( anObject ) );
      return super.visit( (Object) anObject );
  }

  public static void main( String args[] )
  {
    try
    {
      TestVisitorPersistence.oneTimeSetUp();
      new TestVisitorPersistence("dontCare").testLoadData();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
}