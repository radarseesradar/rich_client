
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

public class TVComposite extends TVComponent
{
  private ToMany children;

  {
    children = new OneToMany( this, "children", "TVComponent", "parent" );
  }

  public TVComposite()
  {
  }

  public TVComposite( String _name )
  {
    super( _name );
  }

  public String otherClassName()
  {
    return "TVPComposite";
  }

  public void addChild( TVComponent aChild )
  {
    children.add( aChild );
  }

  public void removeChild( TVComponent aChild )
  {
    children.remove( aChild );
  }

  public void addChildren( TVComponent aChild )
  {
    children.add( aChild );
  }

  public Collection getChildren( )
  {
    return children.getAll();
  }

  public Visitable accept( Visitor aVisitor )
  {
    return aVisitor.visit( (TVComposite) this ).postAccept( aVisitor );
  }

  public Visitable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (TVComposite) this );
    return this;
  }

}