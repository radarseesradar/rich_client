
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package Keyboard.clientBusinessObjects;

import java.util.*;
import Keyboard.*;
import java.io.*;

public class TVCa extends TVComponent
{

  private ToOne husband;
  private ToMany brothers;


  {
    husband = new OneToOne( this, "husband", "TVCb", "wife" );
    brothers = new ManyToMany( this, "brothers", "TVCb", "sisters" );
  }


  public void addBrother( TVCb aBrother )
  {
    brothers.add( aBrother );
  }

  public void removeBrother( TVCb aBrother )
  {
    brothers.remove( aBrother );
  }

  public Collection getBrothers()
  {
    return brothers.getAll();
  }

  public TVCa()
  {
  }

  public TVCa( String _name )
  {
    super( _name );
  }

  public String otherClassName()
  {
    return "TVPCa";
  }

  public TVCb getHusband()
  {
    return (TVCb) husband.get();
  }

  public void setHusband( TVCb theHusband )
  {
    husband.set( theHusband );
  }

  public Visitable accept( Visitor aVisitor )
  {
    return aVisitor.visit( (TVCa) this ).postAccept( aVisitor );
  }

  public Visitable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (TVCa) this );
    return this;
  }

}