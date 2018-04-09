
package smartClient.framework;

import java.util.*;
import java.lang.reflect.*;
import java.security.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
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

  public Persistable getFrom()
  {
    return (Persistable) from;
  }

  public Persistable getTo()
  {
    return (Persistable) to;
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