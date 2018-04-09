/*
 * FemaleP
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
public class FemaleP extends ComponentP
{

  private ToOne husband = new OneToOne( this, "husband", "model.test.server.MaleP", "wife" );
  private ToMany brothers = new ManyToMany( this, "brothers", "model.test.server.MaleP", "sisters" );


  public FemaleP()
  {
  }

  public FemaleP( String aName )
  {
    super( aName );
  }

  public void addBrother( MaleP aBrother )
  {
    this.brothers.add( aBrother );
  }

  public void removeBrother( MaleP aBrother )
  {
    this.brothers.remove( aBrother );
  }

  public Collection getBrothers()
  {
    return this.brothers.getAll();
  }

  public MaleP getHusband()
  {
    return (MaleP) this.husband.get();
  }

  public void setHusband( MaleP theHusband )
  {
    this.husband.set( theHusband );
  }

  public Persistable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (FemaleP) this );
    return this;
  }

}