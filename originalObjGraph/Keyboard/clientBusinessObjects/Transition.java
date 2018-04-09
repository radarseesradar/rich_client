
/**
 * Title:        Persistent Object Graph<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Steve McDaniel<p>
 * Company:      Salient<p>
 * @author Steve McDaniel
 * @version 1.0
 */
package Keyboard.clientBusinessObjects;

import java.util.*;
import java.lang.reflect.*;
import java.security.*;

public class Transition
{
  private Object from;
  private String over;
  private Object to;

  public Transition( Object _from, String _over, Object _to )
  {
    from = _from;
    over = _over;
    to = _to;
  }

  public Visitable getFrom()
  {
    return (Visitable) from;
  }

  public Visitable getTo()
  {
    return (Visitable) to;
  }

  public Clamp toClamp()
  {
    return toRoleKey().toClamp();
  }

  public UnidirectionalAssociation getAssociation()
  {
    return toRoleKey().getAssociation();
  }

  public RoleKey toRoleKey()
  {
    return new RoleKey( from, over );
  }

  public boolean equals( Object another )
  {
    return from.equals( ((Transition)another).from ) && over.equals(((Transition)another).over)  && to.equals( ((Transition)another).to );
  }

  public int hashCode()
  {
    return from.hashCode() ^ over.hashCode() ^ to.hashCode();
  }

  public String toString()
  {
    return "Transition: " + from + "  " + over + "  " + to;
  }

}