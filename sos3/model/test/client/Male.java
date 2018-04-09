/*
 * Male
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
public class Male extends Component
{

  private ToOne wife = new OneToOne( this, "wife", "model.test.client.Female", "husband" );
  private ToMany sisters = new ManyToMany( this, "sisters", "model.test.client.Female", "brothers" );

  public Male()
  {
  }

  public Male( String aName )
  {
    super( aName );
  }

  public void addSister( Female aSister )
  {
    this.sisters.add( aSister );
  }

  public void removeSister( Female aSister )
  {
    this.sisters.remove( aSister );
  }

  public Collection getSisters()
  {
    return this.sisters.getAll();
  }

  public Female getWife()
  {
    return (Female) this.wife.get();
  }

  public void setWife( Female theWife)
  {
    this.wife.set( theWife);
  }

  public Persistable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (Male) this );
    return this;
  }

}