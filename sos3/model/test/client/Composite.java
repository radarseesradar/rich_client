/*
 * Composite
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 */

package model.test.client;


import smartClient.framework.*;
import java.util.Collection;
import model.Visitor;

/**
 * @author Steve McDaniel
 * @version 1.0
 **/
public class Composite extends Component
{
  private ToMany children = new OneToMany( this, "children", "model.test.client.Component", "parent" );

  public Composite()
  {
  }

  public Composite( String aName )
  {
    super( aName );
  }

  public void addChild( Component aChild )
  {
    this.children.add( aChild );
  }

  public void removeChild( Component aChild )
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
    aVisitor.postVisit( (Composite) this );
    return this;
  }

}