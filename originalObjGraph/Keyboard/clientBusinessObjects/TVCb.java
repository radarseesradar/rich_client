
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

public class TVCb extends TVComponent
{

  private ToOne wife;
  private ToMany sisters;

  {
    wife = new OneToOne( this, "wife", "TVCa", "husband" );
    sisters = new ManyToMany( this, "sisters", "TVCa", "brothers" );
  }

  public TVCb()
  {
  }

  public TVCb( String _name )
  {
    super( _name );
  }

  public void addSister( TVCa aSister )
  {
    sisters.add( aSister );
  }

  public void removeSister( TVCa aSister )
  {
    sisters.remove( aSister );
  }

  public Collection getSisters()
  {
    return sisters.getAll();
  }

  public TVCa getWife()
  {
    return (TVCa) wife.get();
  }

  public void setWife( TVCa theWife)
  {
    wife.set( theWife);
  }

  public String otherClassName()
  {
    return "TVPCb";
  }

  public void setModified( boolean trueOrFalse )
  {
    super.setModified( trueOrFalse );
  }

  public Visitable accept( Visitor aVisitor )
  {
    return aVisitor.visit( (TVCb) this ).postAccept( aVisitor );
  }

  public Visitable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (TVCb) this );
    return this;
  }

}