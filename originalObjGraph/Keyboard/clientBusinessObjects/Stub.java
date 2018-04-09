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
import secant.portable.persistence.*;
import java.io.*;


public class Stub implements Serializable, StubInterface
{
  private long kboid;
  private String className;

  public Stub( long _kboid, String _className )
  {
    kboid = _kboid;
    className = _className;
  }

  public Stub toStub()
  {
    return this;
  }

  public long getKboid()
  {
    return kboid;
  }

  public String getClassName()
  {
    return className;
  }

  public boolean isStub()
  {
    return true;
  }

  public int hashCode()
  {
    return (int) kboid;
  }

  public boolean equals( Object another )
  {
    return kboid == ((StubInterface)another).getKboid();
  }

  public int compareTo( Object another )
  {
    return (new Long( getKboid() )).compareTo( new Long( ((StubInterface)another).getKboid() ));
  }

  public Visitable findPersistentObject()
  {
      try
      {
        PersistenceService ps = PersistenceService.getInstance();
        String persistentClassName = getClassName();
        Class persistentClass = Class.forName( MiddleWareFramework.getSingleton().getPersistentPackageName() + "." + persistentClassName);
        Visitable aPersistentVisitable = (Visitable) ps.readObject( persistentClass, new Long( getKboid()), null );
        return aPersistentVisitable;
      }
      catch( ClassNotFoundException e )
      {
        throw new InternalError( e.toString() );
      }
      catch( PersistenceException e )
      {
        throw new InternalError( e.toString() );
      }
  }

  public StubInterface yourself()
  {
    return (StubInterface) this;
  }

  public Visitable accept( Visitor aVisitor )
  {
    return aVisitor.visit( (Object) this );
  }

  public Visitable toTransient( )
  {
    return (Visitable) MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getWorkingSetElementAt( this );
  }

  public String toString()
  {
    return "Stub for " + getClassName() + ":" + getKboid();
  }

  public Visitable toModifiedTransientIfAny()
  {
    Visitable aTransient = toTransient();
    if( aTransient != null && MiddleWareFramework.getSingleton().getGlobalCRUDRecorder().conatainsModifiedObject( aTransient ) )
    {
      return aTransient;
    }
    else
    {
      return null;
    }
  }

  public Visitable toNewlyCreatedTransientIfAny()
  {
    Visitable aTransient = toTransient();
    if( aTransient != null && MiddleWareFramework.getSingleton().getGlobalCRUDRecorder().conatainsNewlyCreatedObject( aTransient ) )
    {
      return aTransient;
    }
    else
    {
      return null;
    }
  }

}