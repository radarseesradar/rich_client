package smartClient.framework;


import java.util.*;
import java.lang.reflect.*;
import java.io.*;


/**
 * @author Steve McDaniel 
 * <br>Copyright (c) Steve McDaniel
 */
public class VisitorUpdater extends VisitorPersister
{

  public VisitorUpdater()
  {
  }

  protected void processUpdateCounter( Object attributeOwner )
  {
    int fromValue;
    int toValue;
    fromValue = ((Persistable)attributeOwner).getUpdateCounter();
    toValue = ((Persistable)getOther()).getUpdateCounter();
    if( fromValue != toValue )
     throw new ConcurrencyException ();
    ((Persistable)getOther()).incrementUpdateCounter();
  }

  public Persistable createOther( Object anObject )
  {
    Persistable aPersistable = (Persistable) anObject;
    if( aPersistable.isNewlyCreated() )
    {
      aPersistable.setNewlyCreated( false );
      return super.createOther( anObject );
    }
    else
    {
  	  return (Persistable) SmartClientFramework.getSingleton().getPersistencePolicy().findPersistentObject(
      				anObject );
    }
  }
}