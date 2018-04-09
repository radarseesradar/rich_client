
package smartClient.framework;

import java.util.*;
import java.io.*;
import model.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class VisitorEqualityTester extends Visitor
{
  private class SharedBoolean
  {
    private boolean sharedEqual = true;

    private void setToEqual()
    {
      sharedEqual = true;
    }

    private void setToUnequal()
    {
      sharedEqual = false;
    }

    private boolean isEqual()
    {
      return sharedEqual;
    }
  }

  private Persistable other;
  private SharedBoolean equal = new SharedBoolean();

  public VisitorEqualityTester( Persistable _other )
  {
    other = _other;
  }

  protected void setOther( Persistable _other )
  {
    other = _other;
  }

  public boolean isEqual()
  {
    return equal.isEqual();
  }

  public Collection propagateToMany( Object receiver, String attributeName )
  {
    checkEquality( receiver, other );
    if( !equal.isEqual() )
      return null;
    return propagateToMany(  getToManyDestination( receiver, attributeName ),  getToManyDestination( other, attributeName ) );
  }

  protected Collection propagateToMany( Collection aCollection, Collection otherCollection )
  {
    Object nextObj;
    Object companion;
    if( ! equal.isEqual() )
      return null;
    for( Iterator anIterator = aCollection.iterator(); anIterator.hasNext(); )
    {
      nextObj = anIterator.next();
      if( otherCollection.contains( nextObj ) )
      {
        companion = findCompanion( nextObj, otherCollection );
        if( ((StubInterface)companion).isStub() != ((StubInterface)nextObj).isStub())
        {
          equal.setToUnequal();
          return null;
        }
      }
      else
      {
        equal.setToUnequal();
        return null;
      }
      propagateToOne( (Persistable) nextObj, (Persistable) companion );
    }
    return aCollection;
  }

  public Persistable propagateToOne( Object receiver, String attributeName )
  {
    if( attributeName == null
    || ((StubInterface)receiver).isStub()
    || ((StubInterface)other).isStub()
    || ( receiver.getClass() != other.getClass() ) )
    {
      equal.setToUnequal();
      return (Persistable) receiver;
    }
    return propagateToOne( getToOneDestination( receiver, attributeName ), getToOneDestination( other, attributeName ) );
  }

  protected Persistable propagateToOne( Persistable anAcceptor, Persistable otherAcceptor )
  {
    checkEquality( anAcceptor, otherAcceptor );
    if( !equal.isEqual() )
      return null;
    if( anAcceptor != null && ! wasPreviouslyVisited( anAcceptor ) )
      return (Persistable) anAcceptor.accept( propagationVisitor( otherAcceptor ) );
    else
      return (Persistable) null;
  }

  public Visitor propagationVisitor( Persistable _other )
  {
    VisitorEqualityTester copyOfThis =  (VisitorEqualityTester) clone();
    copyOfThis.setOther( _other );
    return copyOfThis;
  }

  private void checkEquality( Object op1, Object op2 )
  {
    if( ! equal.isEqual() )
      return;
    if( op1 == null && op2 == null )
      return;
    if( op1 == null || op2 == null )
    {
      equal.setToUnequal();
    }
    else if( op1.getClass() == op2.getClass() && op1.equals( op2 ) )
    {
      equal.setToEqual();
    }
    else
    {
      equal.setToUnequal();
    }
  }


  public Persistable visit( Object anObject )
  {
    checkEquality( anObject, other );
    if( !equal.isEqual() )
      return (Persistable) anObject;
    return super.visit( (Object) anObject );
  }
}