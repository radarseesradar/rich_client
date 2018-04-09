/*
 * MaleP
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
public class MaleP extends ComponentP
{

  private ToOne wife = new OneToOne( this, "wife", "model.test.server.FemaleP", "husband" );
  private ToMany sisters = new ManyToMany( this, "sisters", "model.test.server.FemaleP", "brothers" );

  public MaleP()
  {
  }

  public MaleP( String aName )
  {
    super( aName );
  }

  public void addSister( FemaleP aSister )
  {
    this.sisters.add( aSister );
  }

  public void removeSister( FemaleP aSister )
  {
    this.sisters.remove( aSister );
  }

  public Collection getSisters()
  {
    return this.sisters.getAll();
  }

  public FemaleP getWife()
  {
    return (FemaleP) this.wife.get();
  }

  public void setWife( FemaleP theWife)
  {
    this.wife.set( theWife);
  }

  public Persistable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (MaleP) this );
    return this;
  }

}