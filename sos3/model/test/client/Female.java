/*
 * Female
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
public class Female extends Component
{

  private ToOne husband = new OneToOne( this, "husband", "model.test.client.Male", "wife" );
  private ToMany brothers = new ManyToMany( this, "brothers", "model.test.client.Male", "sisters" );



  public Female()
  {
  }

  public Female( String aName )
  {
    super( aName );
  }

  public void addBrother( Male aBrother )
  {
    this.brothers.add( aBrother );
  }

  public void removeBrother( Male aBrother )
  {
    this.brothers.remove( aBrother );
  }

  public Collection getBrothers()
  {
    return this.brothers.getAll();
  }

  public Male getHusband()
  {
    return (Male) this.husband.get();
  }

  public void setHusband( Male theHusband )
  {
    this.husband.set( theHusband );
  }

  public Persistable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (Female) this );
    return this;
  }

}