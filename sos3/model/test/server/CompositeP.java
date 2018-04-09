/*
 * CompositeP
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 */
package model.test.server;
import smartClient.framework.*;
import java.util.Collection;
import model.Visitor;

/**
 * @author Steve McDaniel
 * @version 1.0
 **/
public class CompositeP extends ComponentP
{
  private ToMany children = new OneToMany( this, "children", "model.test.server.ComponentP", "parent" );


  public CompositeP()
  {
  }

  public CompositeP( String aName )
  {
    super( aName );
  }

  public void addChild( ComponentP aChild )
  {
    this.children.add( aChild );
  }

  public void removeChild( ComponentP aChild )
  {
    this.children.remove( aChild );
  }

  public Collection getChildren( )
  {
    return this.children.getAll();
  }

  public Persistable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (CompositeP) this );
    return this;
  }

}