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


public class VisitorUpdater extends VisitorPersister
{

  public VisitorUpdater()
  {
  }

  protected void processUpdateCounter( Object attributeOwner )
  {
    int fromValue;
    int toValue;
    fromValue = ((Visitable)attributeOwner).getUpdateCounter();
    toValue = ((Visitable)getOther()).getUpdateCounter();
    if( fromValue != toValue )
     throw new KBConcurrencyException();
    ((Visitable)getOther()).incrementUpdateCounter();
  }

  protected Visitable createOther( Object anObject )
  {
    Visitable aVisitable = (Visitable) anObject;
    if( aVisitable.isNewlyCreated() )
    {
      aVisitable.setNewlyCreated( false );
      return super.createOther( anObject );
    }
    else
    {
      try
      {
        PersistenceService ps = PersistenceService.getInstance();
        String persistentClassName = ((Visitable) anObject).otherClassName();
        Class persistentClass = Class.forName( getPersistentPackageName() + "." + persistentClassName);
        Visitable aPersistentVisitable = (Visitable) ps.readObject( persistentClass, new Long( aVisitable.getKboid()), null );
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
  }
}