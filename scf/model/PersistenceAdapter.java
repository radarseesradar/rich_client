/* Generated by Together */
/*
 * PersistenceAdapter
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 */

package model;
import smartClient.framework.SessionInterface;
import smartClient.framework.Identifiable;
import smartClient.framework.StubInterface;
import smartClient.framework.AssociationsCoordinator;
import smartClient.framework.Stub;
import smartClient.framework.Persistable;
import smartClient.framework.SmartClientFramework;

/**
 * @author Steve McDaniel
 * @version 1.0*/
public class PersistenceAdapter implements Persistable
{
	private long scfoid;
	private int updateCounter;
	private boolean newlyCreated;
	private boolean modified;
	private AssociationsCoordinator associationsCoordinator;

	public PersistenceAdapter()
	{
		this.setAssociationsCoordinator( SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator() );
	}
	public long getScfoid()
	{
		return this.scfoid;
	}

	public void setScfoid( long oid )
	{
		this.scfoid = oid;
	}

	public Persistable accept( Visitor aVisitor )
	{
		return aVisitor.visit( this ).postAccept( aVisitor );
	}

	public Persistable postAccept( Visitor aVisitor )
	{
		aVisitor.postVisit( (PersistenceAdapter) this );
		return this;
	}

	public boolean isStub()
	{
		return false;
	}

	public  String otherClassName()
	{
    	String thisClassName = "";
        String otherClassName = "";
        int separatorIndex = 0;
		thisClassName = getClass().getName();
        separatorIndex = thisClassName.lastIndexOf( '.' );
        if( separatorIndex != -1 )
        {
        	thisClassName = thisClassName.substring( separatorIndex + 1, thisClassName.length() );
        }
        if( thisClassName.endsWith( "P" ) )
        {
        	otherClassName = thisClassName.substring( 0, thisClassName.length() - 1 );
        }
        else
        {
        	otherClassName = thisClassName + "P";
        }
        return otherClassName;
	}

	public String otherPackageName()
	{
    	String thisPackageName = "";
        String replacement = "";
        StringBuffer otherPackageName = null;
        int separatorIndex = 0;
		thisPackageName = getClass().getPackage().getName();
        separatorIndex = thisPackageName.lastIndexOf( '.' );
        otherPackageName = new StringBuffer( thisPackageName );
        if( thisPackageName.endsWith( "client" ) )
        {
        	replacement = "server";
        }
        else
        {
        	replacement = "client";
        }
        otherPackageName.replace( separatorIndex + 1, thisPackageName.length(), replacement );
        return otherPackageName.toString();
 	}

	public Stub toStub()
    {
		if( SmartClientFramework.getSingleton().isServer() )
        {
			return new Stub( this.getScfoid(), getClass().getName() );
        }
        else
        {
			return new Stub( this.getScfoid(), this.otherClassName() );
        }
    }

	public StubInterface yourself()
	{
		return (StubInterface) this;
	}


    public void save() throws Exception
    {
		if( SmartClientFramework.getSingleton().isServer() )
        {
        	return;
        }
		SmartClientFramework.getSingleton().save( this );
    }

	public void incrementUpdateCounter()
	{
		this.updateCounter++;
	}

    public Persistable refresh()
	throws javax.transaction.SystemException, java.lang.Exception, java.rmi.RemoteException
    {
		if( SmartClientFramework.getSingleton().isServer() )
        {
	    	return this;
        }
		synchronized( getAssociationsCoordinator() )
		{
			Stub seedStub = this.toStub();
			Persistable result = null;
			
			result = SmartClientFramework.getSingleton().getSession().retrieveAllFromStub(  seedStub );
			
			SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().merge( result.getAssociationsCoordinator() );
			return result;
		}
    }

	public boolean isModified()
	{
		return this.modified;
	}

	public void markModified()
	{
		this.setModified( true );
	}

	public void setModified(boolean trueOrFalse)
	{
		if( SmartClientFramework.getSingleton().isServer()
        || SmartClientFramework.getSingleton().markModifyIsDisabled() )
        {
			return;
        }
		synchronized( this.getAssociationsCoordinator() )
		{
			this.modified = trueOrFalse;
			if( this.isModified() )
			{
				this.getAssociationsCoordinator().addToWorkingSet( this );
			}
		}
	}

	public int getUpdateCounter()
	{
		return this.updateCounter;
	}

	public void setUpdateCounter( int anUpdateCounter )
	{
		this.updateCounter = anUpdateCounter;
	}

	public boolean isNewlyCreated()
	{
		return this.newlyCreated;
	}

	public void setNewlyCreated( boolean trueOrFalse )
	{
		if( SmartClientFramework.getSingleton().isServer() )
        {
			return;
        }
		synchronized( getAssociationsCoordinator() )
		{
			this.newlyCreated = trueOrFalse;
			if( this.isNewlyCreated() )
			{
				this.getAssociationsCoordinator().addToWorkingSet( this );
			}
		}
	}

	public AssociationsCoordinator getAssociationsCoordinator()
	{
		return this.associationsCoordinator;
	}

	public void setAssociationsCoordinator( AssociationsCoordinator anAssociationsCoordinator )
	{
		this.associationsCoordinator = anAssociationsCoordinator;
	}

	public boolean equals( Object another )
	{
		return this.scfoid == ((StubInterface)another).getScfoid();
	}
	
	public int compareTo( Object another )
	{
		return (new Long( this.getScfoid() )).compareTo( new Long( ((Persistable)another).getScfoid() ));
	}
	
	public int hashCode()
	{
		return (int) this.scfoid;
	}

  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch( CloneNotSupportedException e )
    {
      // Cannot happen -- all PersistenceAdapters support  cloning
      throw new InternalError( e.toString() );
    }
  }

}
