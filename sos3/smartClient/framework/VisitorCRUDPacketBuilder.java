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

public class VisitorCRUDPacketBuilder extends VisitorViaAllPaths
{
  private CRUDPacket crudPacket;

  public VisitorCRUDPacketBuilder()
  {
    crudPacket = new CRUDPacket();
  }

  public boolean wasPreviouslyTransitioned( Transition aTransition )
  {
    boolean wasPreviouslyTransitioned = getPreviouslyTransitioned().contains( aTransition );
    boolean isClamped = isClamped( aTransition );
    Clamp directClamp = null;
    Clamp inverseClamp = null;
    int associationType = aTransition.getAssociation().getAssociationType();
    if( (associationType == UnidirectionalAssociation.AssociationTypes.TO_ONE)
    || (associationType == UnidirectionalAssociation.AssociationTypes.TO_MANY) )
    	return wasPreviouslyTransitioned;
    if( isClamped  && !wasPreviouslyTransitioned )
    {
      directClamp = (Clamp) ClampProvider.getSingleton().getStorageClamps().get( aTransition.toClamp() );
      processClampedChangeList( directClamp.getChangeList().getCommandsFromTo( aTransition.getFrom(), aTransition.getTo() ) );
      inverseClamp = directClamp.getInverseClamp();
      if( inverseClamp != null )
      {
        processClampedChangeList( inverseClamp.getChangeList().getCommandsFromTo( aTransition.getTo(), aTransition.getFrom() ) );
      }
    }
    return wasPreviouslyTransitioned || isClamped;
  }

  public boolean isClamped( Transition aTransition )
  {
    return ClampProvider.getSingleton().getStorageClamps().containsKey( aTransition.toClamp() );
  }

  private void processClampedChangeList( AssociationChangeList anAssociationChangeList )
  {
    processChangeList( anAssociationChangeList );
  }

  private void processChangeList( AssociationChangeList  anAssociationChangeList )
  {
    crudPacket.addAssociationChanges( anAssociationChangeList );
  }


  public void addPreviouslyTransitioned( Transition aTransition )
  {
    UnidirectionalAssociation anAssociation = aTransition.getAssociation();
    processChangeList( anAssociation.getChangeList() );
    super.addPreviouslyTransitioned( aTransition );
  }

  public CRUDPacket getCRUDPacket()
  {
    return crudPacket;
  }

  public Persistable visit( Object anObject )
  {
    if( ((StubInterface) anObject ).isStub() )
      return super.visit( (Object) anObject);
    Persistable workingSetObj = null;
    if( wasPreviouslyVisited( anObject ) )
      return super.visit( (Object) anObject );

    workingSetObj = (Persistable) SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getWorkingSetElementAt( anObject );

    if( workingSetObj != null && ((Persistable) workingSetObj).isNewlyCreated() )
    {
      crudPacket.addNewlyCreated( ((Persistable) ((Persistable) workingSetObj).clone()).accept( new VisitorPruner() ) );
    }
    else if( workingSetObj != null && ((Persistable) workingSetObj).isModified() )
    {
      crudPacket.addModified(  ((Persistable) ((Persistable) workingSetObj).clone()).accept( new VisitorPruner() ) );
    }

    return super.visit( (Object) anObject);
  }

}