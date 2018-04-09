
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package Keyboard.clientBusinessObjects;

import Keyboard.*;
import secant.extreme.*;
import java.io.*;

public class TVComponent implements Visitable
{

  private long kboid;
  private boolean newlyCreated;
  private boolean modified;
  private String name;
  private String alias;
  private ToOne parent;
  private int updateCounter;
  private AssociationsCoordinator associationsCoordinator;

  {
    parent = new ManyToOne( this, "parent", "TVComposite", "children" );
  }

  public TVComponent()
  {
    setAssociationsCoordinator( MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator() );
  }

  public  String otherClassName()
  {
    return "TVPComponent";
  }

  public TVComponent( String _name )
  {
    this();
    name = _name;
  }

  public long getKboid()
  {
    return kboid;
  }

  public void xsetKboid( long _kboid )
  {
    kboid = _kboid;
  }

  public boolean getNewlyCreated()
  {
    return newlyCreated;
  }

  public boolean isNewlyCreated()
  {
    return newlyCreated;
  }

  public boolean isStub()
  {
    return false;
  }

  public void setNewlyCreated( boolean trueOrFalse )
  {
    if( MiddleWareFramework.getSingleton().isServer() )
      return;
    synchronized( getAssociationsCoordinator() )
    {
      newlyCreated = trueOrFalse;
      if( isNewlyCreated() )
        {
        getAssociationsCoordinator().addToWorkingSet( this );
        MiddleWareFramework.getSingleton().getGlobalCRUDRecorder().addNewlyCreatedObject( this );
        }
    }
  }

  public boolean getModified()
  {
    return modified;
  }

  public boolean isModified()
  {
    return modified;
  }

  public void setModified( boolean trueOrFalse )
  {
    if( MiddleWareFramework.getSingleton().isServer() || MiddleWareFramework.getSingleton().markModifyIsDisabled() )
      return;
    synchronized( this.getAssociationsCoordinator() )
    {
      modified = trueOrFalse;
      if( isModified() )
        {
        getAssociationsCoordinator().addToWorkingSet( this );
        MiddleWareFramework.getSingleton().getGlobalCRUDRecorder().addModifiedObject( this );
        }
    }
  }

  public void markModified()
  {
    setModified( true );
  }

  public String getName()
  {
    return name;
  }

  public void setName( String _name )
  {
    name = _name;
    markModified();
  }

  public String getAlias()
  {
    return alias;
  }

  public void setAlias( String _alias )
  {
    alias = _alias;
    markModified();
  }

  public boolean equals( Object another )
  {
    return kboid == ((StubInterface)another).getKboid();
  }

  public int compareTo( Object another )
  {
    return (new Long( getKboid() )).compareTo( new Long( ((Visitable)another).getKboid() ));
  }

  public int hashCode()
  {
    return (int) kboid;
  }

  public String toString()
  {
   /* return getClass().getName()
    + ":"
    + name
    + getKboid()
    + ":"
    + getUpdateCounter()
    + ":"
    + getAssociationsCoordinator()
    + (isModified() ? ":modified" : "")
    + (isNewlyCreated() ? ":newlyCreated" : "")
    ; */
    return name + ":" + getUpdateCounter();
  }

  public void setParent( TVComposite theParent )
  {
    parent.set( theParent );
  }

  public TVComposite getParent()
  {
    return (TVComposite) parent.get();
  }

  public int getUpdateCounter()
  {
    return updateCounter;
  }

  public void setUpdateCounter( int _updateCounter )
  {
    updateCounter = _updateCounter;
  }

  public Visitable accept( Visitor aVisitor )
  {
    return aVisitor.visit( (TVComponent) this ).postAccept( aVisitor );
  }

  public Visitable postAccept( Visitor aVisitor )
  {
    aVisitor.postVisit( (TVComponent) this );
    return this;
  }

  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch( CloneNotSupportedException e )
    {
      // Cannot happen -- all TVComponents support  cloning
      throw new InternalError( e.toString() );
    }
  }

  public Stub toStub()
  {
    return new Stub( getKboid(), otherClassName() );
  }

  public void incrementUpdateCounter()
  {
    updateCounter++;
  }

  public void setAssociationsCoordinator( AssociationsCoordinator _associationsCoordinator )
  {
    associationsCoordinator = _associationsCoordinator;
  }

  public AssociationsCoordinator getAssociationsCoordinator()
  {
    return associationsCoordinator;
  }

  public void save() throws Exception
  {
    MiddleWareFramework.getSingleton().getAssociationMode().save( this );
  }

  public StubInterface yourself()
  {
    return (StubInterface) this;
  }

  public Visitable refresh()
  throws javax.transaction.SystemException, java.lang.Exception, java.rmi.RemoteException
  {
    synchronized( getAssociationsCoordinator() )
    {
      Stub seedStub;
      byte [] byteArray;
      seedStub = toStub();
      Client aClient = MiddleWareFramework.getSingleton().getClient();
      Visitable result = null;

      aClient.begin();
      byteArray = MiddleWareFramework.getSingleton().getTVSessionBeanObject().retrieveAllFromStub( (new Serializer()).serialize( seedStub ) );
      aClient.commit();
      result = (Visitable) (new Serializer()).deserialize( byteArray );
      MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().merge( result.getAssociationsCoordinator() );
      return result;
    }
  }

}