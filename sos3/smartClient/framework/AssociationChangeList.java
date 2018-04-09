package smartClient.framework;

import java.util.*;
import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class AssociationChangeList implements CommandInterface
{
  /**
   * @associates CommandInterface 
   */
  private List associationChangeList;

  public AssociationChangeList()
  {
    associationChangeList = (List) new LinkedList();
  }

  private AssociationChangeList( Collection that )
  {
    associationChangeList = (List) new LinkedList( that );
  }

  public AssociationChangeList getCommandsFromTo( Persistable receiver, Persistable arg )
  {
    Set result = new HashSet();
    Command nextCommand = null;
    for( Iterator anIterator = associationChangeList.iterator(); anIterator.hasNext(); )
    {
      nextCommand = (Command) anIterator.next();
      if( receiver.equals( nextCommand.getReceiver() ) && arg.equals( nextCommand.getArg() ) )
      {
        result.add( nextCommand );
      }
    }
    return new AssociationChangeList( result );
  }

  public void removeCommandsFromTo( Persistable receiver, Persistable arg )
  {
    removeAll( getCommandsFromTo( receiver, arg ) );
  }

  private void removeAll( AssociationChangeList subtractList )
  {
    associationChangeList.removeAll( subtractList.associationChangeList );
  }

  public Set getAllConflictingOperands()
  {
    Set result = new HashSet();
    Command nextCommand = null;
    for( Iterator anIterator = associationChangeList.iterator(); anIterator.hasNext(); )
    {
      nextCommand = (Command) anIterator.next();
      switch( nextCommand.getAssociationType() )
      {
        case UnidirectionalAssociation.AssociationTypes.ONE_TO_ONE:
          result.add( nextCommand.getReceiver() );
          result.add( nextCommand.getArg() );
          break;
        case UnidirectionalAssociation.AssociationTypes.MANY_TO_MANY:
          break;
        case UnidirectionalAssociation.AssociationTypes.ONE_TO_MANY:
          result.add( nextCommand.getArg() );
          break;
        case UnidirectionalAssociation.AssociationTypes.MANY_TO_ONE:
          result.add( nextCommand.getReceiver() );
          break;
        case UnidirectionalAssociation.AssociationTypes.TO_ONE:
          result.add( nextCommand.getArg() );
          break;
        case UnidirectionalAssociation.AssociationTypes.TO_MANY:
          result.add( nextCommand.getArg() );
          break;
      }
	  if( nextCommand.getReceiver().toTransient().isNewlyCreated() )
      {
        result.add( nextCommand.getReceiver() );
      }
	  if( nextCommand.getArg().toTransient().isNewlyCreated() )
      {
        result.add( nextCommand.getArg() );
      }
    }
    return result;
  }

  public void sort()
  {
    Collections.sort( associationChangeList );
  }

  public void add( CommandInterface aCommand )
  {
    associationChangeList.add( aCommand );
  }

  public void addAll( AssociationChangeList that )
  {
    associationChangeList.addAll( that.associationChangeList );
  }

  public void clear()
  {
    associationChangeList = (List) new LinkedList();
  }

  public void execute()
  {
    Iterator anIterator;

    for( anIterator = associationChangeList.iterator(); anIterator.hasNext(); )
    {
      ((CommandInterface)anIterator.next()).execute();
    }
  }

}