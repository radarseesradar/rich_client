/*
 * ComponentP
 * Title:        Smart Client Framework
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Smart Client Framework inc.
 */
package model.test.server;



import smartClient.framework.*;
import java.util.Collection;
import model.Visitor;
import model.PersistenceAdapter;

/**
 * @author Steve McDaniel
 * @version 1.0
 **/
public class ComponentP extends PersistenceAdapter {
  private String name;
  private String alias;
  private ToOne parent;
  private ToMany playmates;
  private ToOne bestFriend;

  	{
    	this.parent = new ManyToOne( this, "parent", "model.test.server.CompositeP", "children" );
        this.playmates = new UnidirectionalToMany( this, "playmates", "model.test.server.ComponentP" );
    	this.bestFriend = new UnidirectionalToOne( this, "bestFriend", "model.test.server.ComponentP" );
  	}

  public ComponentP( String aName )
  {
    this.name = aName;
  }

	public ComponentP()
	{
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String aName)
	{
		this.name = aName;
	}

	public String getAlias()
	{
		return this.alias;
	}

	public void setAlias(String anAlias)
	{
		this.alias = anAlias;
	}


	public void addPlaymate( ComponentP aPlaymate )
	{
		this.playmates.add( aPlaymate );
	}

  public void removePlaymate( ComponentP aPlaymate )
  {
    this.playmates.remove( aPlaymate );
  }

  public Collection getPlaymates( )
  {
    return this.playmates.getAll();
  }

  	public void setParent( CompositeP theParent )
  	{
    	this.parent.set( theParent );
  	}



  	public CompositeP getParent()
  	{
    	return (CompositeP) this.parent.get();
  	}


  public ComponentP getBestFriend()
  {
    return (ComponentP) this.bestFriend.get();
  }

  public void setBestFriend( ComponentP theBestFriend)
  {
    this.bestFriend.set( theBestFriend );
  }

  public Persistable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (ComponentP) this );
    return this;
  }

  public String toString()
  {
    return super.toString() + " -- " + this.name + this.getScfoid();
  }
}

