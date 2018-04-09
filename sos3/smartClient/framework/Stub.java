package smartClient.framework;


import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import model.*;


/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
final public class Stub implements Serializable, StubInterface
{
  private long scfoid;
  private String className;

  public Stub( long oid, String _className )
  {
    this.scfoid = oid;
    className = _className;
  }

  public Stub toStub()
  {
    return this;
  }

  public long getScfoid()
  {
    return scfoid;
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
    return (int) scfoid;
  }

  public boolean equals( Object another )
  {
    return this.scfoid == ((StubInterface)another).getScfoid();
  }

  public int compareTo( Object another )
  {
    return (new Long( getScfoid() )).compareTo( new Long( ((StubInterface)another).getScfoid() ));
  }

  public Persistable findPersistentObject()
  {
  	return SmartClientFramework.getSingleton().getPersistencePolicy().findPersistentObject( this );
  }

  public StubInterface yourself()
  {
    return (StubInterface) this;
  }

  public Persistable accept( Visitor aVisitor )
  {
    return aVisitor.visit( (Object) this );
  }

  public Persistable toTransient( )
  {
    return (Persistable) SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getWorkingSetElementAt( this );
  }

  public String toString()
  {
    return "Stub for " + getClassName() + ":" + getScfoid();
  }

  public Persistable toModifiedTransientIfAny()
  {
    Persistable aTransient = toTransient();
    if( aTransient != null && aTransient.isModified() )
    {
      return aTransient;
    }
    else
    {
      return null;
    }
  }

  public Persistable toNewlyCreatedTransientIfAny()
  {
    Persistable aTransient = toTransient();
    if( aTransient != null && aTransient.isNewlyCreated() )
    {
      return aTransient;
    }
    else
    {
      return null;
    }
  }

  public void setScfoid( long anScfoid )
  {
    throw new UnsupportedOperationException( "Stub.setScfoid is not supported" );
  }

}