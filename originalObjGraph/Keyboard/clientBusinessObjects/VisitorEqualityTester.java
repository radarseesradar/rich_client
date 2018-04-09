
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
import java.io.*;

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

  private Visitable other;
  private SharedBoolean equal = new SharedBoolean();

  public VisitorEqualityTester( Visitable _other )
  {
    other = _other;
  }

  private void setOther( Visitable _other )
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
      propagateToOne( (Visitable) nextObj, (Visitable) companion );
    }
    return aCollection;
  }

  public Visitable propagateToOne( Object receiver, String attributeName )
  {
    if( attributeName == null
    || ((StubInterface)receiver).isStub()
    || ((StubInterface)other).isStub()
    || ( receiver.getClass() != other.getClass() ) )
    {
      equal.setToUnequal();
      return (Visitable) receiver;
    }
    return propagateToOne( getToOneDestination( receiver, attributeName ), getToOneDestination( other, attributeName ) );
  }

  protected Visitable propagateToOne( Visitable anAcceptor, Visitable otherAcceptor )
  {
    checkEquality( anAcceptor, otherAcceptor );
    if( !equal.isEqual() )
      return null;
    if( anAcceptor != null && ! wasPreviouslyVisited( anAcceptor ) )
      return (Visitable) anAcceptor.accept( propagationVisitor( otherAcceptor ) );
    else
      return (Visitable) null;
  }

  protected Visitor propagationVisitor( Visitable _other )
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


  public Visitable visit( Object anObject )
  {
    checkEquality( anObject, other );
    if( !equal.isEqual() )
      return (Visitable) anObject;
    return super.visit( (Object) anObject );
  }
}