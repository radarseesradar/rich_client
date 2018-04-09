/*
 * Component
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Smart Client Framework inc.
 */

package model.test.client;


import smartClient.framework.*;
import java.util.Collection;
import model.Visitor;
import model.PersistenceAdapter;

/**
 * @author Steve McDaniel
 * @version 1.0
 **/
public class Component extends PersistenceAdapter {

  private String name;
  private String alias;
  private ToOne parent = new ManyToOne( this, "parent", "model.test.client.Composite", "children" );
  private ToMany playmates = new UnidirectionalToMany( this, "playmates", "model.test.client.Component" );
  private ToOne bestFriend = new UnidirectionalToOne( this, "bestFriend", "model.test.client.Component" );


  public Component()
  {
  }

  public Component( String aName )
  {
    this.name = aName;
  }

  public Component getBestFriend()
  {
    return (Component) this.bestFriend.get();
  }

  public void setBestFriend( Component theBestFriend)
  {
    this.bestFriend.set( theBestFriend );
  }

  public void addPlaymate( Component aPlaymate )
  {
    this.playmates.add( aPlaymate );
  }

  public void removePlaymate( Component aPlaymate )
  {
    this.playmates.remove( aPlaymate );
  }

  public Collection getPlaymates( )
  {
    return this.playmates.getAll();
  }

  public String getName()
  {
    return this.name;
  }

  public void setName( String aName )
  {
    this.name = aName;
    markModified();
  }

  public String getAlias()
  {
    return this.alias;
  }

  public void setAlias( String anAlias )
  {
    this.alias = anAlias;
    markModified();
  }

  public String toString()
  {
   /* return getClass().getName()
    + ":"
    + name
    + getScfoid()
    + ":"
    + getUpdateCounter()
    + ":"
    + getAssociationsCoordinator()
    + (isModified() ? ":modified" : "")
    + (isNewlyCreated() ? ":newlyCreated" : "")
    ; */
    return this.name + ":" + this.getUpdateCounter();
  }

  public void setParent( Composite theParent )
  {
    this.parent.set( theParent );
  }

  public Composite getParent()
  {
    return (Composite) this.parent.get();
  }

  public Persistable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (Component) this );
    return this;
  }

}