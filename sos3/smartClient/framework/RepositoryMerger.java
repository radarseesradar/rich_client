package smartClient.framework;


import java.io.*;
import java.util.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class RepositoryMerger implements Serializable
{
  private AssociationsCoordinator associationsCoordinator;

  public RepositoryMerger( AssociationsCoordinator _associationsCoordinator )
  {
    associationsCoordinator = _associationsCoordinator;
  }

  protected AssociationsCoordinator getAssociationsCoordinator()
  {
    return associationsCoordinator;
  }

  protected StubInterface getWorkingSetElementIfAnyAt( Object anObject )
  {
    return getAssociationsCoordinator().getWorkingSetElementIfAnyAt( anObject );
  }

  protected void addToWorkingSet( Object anObject )
  {
    getAssociationsCoordinator().addToWorkingSet( (StubInterface) anObject );
  }

  public boolean isConsistent( AssociationsCoordinator anAssociationsCoordinator, Map toOnes )
  {
    Map.Entry toOneEntry = null;
    StubInterface from = null;
    StubInterface to = null;
    String validAssoc = anAssociationsCoordinator.toString();
    for( Iterator toOnesIterator = toOnes.entrySet().iterator(); toOnesIterator.hasNext(); )
    {
      toOneEntry = (Map.Entry) toOnesIterator.next();
      from = (StubInterface) ((RoleKey)toOneEntry.getKey()).getManipulator();
      if( !from.isStub() && ! ((Persistable) from).getAssociationsCoordinator().toString().equals( validAssoc ) )
        return false;
      to = (StubInterface) toOneEntry.getValue();
      if( !to.isStub() && ! ((Persistable) to).getAssociationsCoordinator().toString().equals( validAssoc ) )
        return false;
    }
    return true;
  }

  protected void merge( Map from, Map to )
  {
    Map.Entry fromEntry = null;
    Persistable toValue = null;
    RoleKey fromKey = null;
    Persistable fromValue = null;

    for( Iterator fromIterator = from.entrySet().iterator(); fromIterator.hasNext(); )
    {
      fromEntry = (Map.Entry) fromIterator.next();
      fromKey = (RoleKey) fromEntry.getKey();
      fromValue = (Persistable) fromEntry.getValue();

      addToWorkingSet( fromKey.getManipulator() );
      addToWorkingSet( fromValue );

      if( ! to.containsKey( fromKey ) )
      {
        to.put( fromKey, getWorkingSetElementIfAnyAt( fromValue )  );
        continue;
      }
      else if( ((Persistable) to.get( fromKey )).isStub() && ! fromValue.isStub() )
      {
        to.put( fromKey, getWorkingSetElementIfAnyAt( fromValue ) );
      }
    }
  }
}