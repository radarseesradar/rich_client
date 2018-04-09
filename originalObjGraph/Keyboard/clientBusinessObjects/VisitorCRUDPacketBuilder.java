package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.io.*;
import java.util.*;

public class VisitorCRUDPacketBuilder extends VisitorViaAllPaths
{
  private CRUDPacket crudPacket;

  public VisitorCRUDPacketBuilder()
  {
    crudPacket = new CRUDPacket();
  }

  protected boolean wasPreviouslyTransitioned( Transition aTransition )
  {
    boolean wasPreviouslyTransitioned = getPreviouslyTransitioned().contains( aTransition );
    boolean isClamped = isClamped( aTransition );
    Clamp directClamp = null;
    Clamp inverseClamp = null;
    if( isClamped && !wasPreviouslyTransitioned )
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

  protected boolean isClamped( Transition aTransition )
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


  protected void addPreviouslyTransitioned( Transition aTransition )
  {
    UnidirectionalAssociation anAssociation = aTransition.getAssociation();
    processChangeList( anAssociation.getChangeList() );
    super.addPreviouslyTransitioned( aTransition );
  }

  public CRUDPacket getCRUDPacket()
  {
    return crudPacket;
  }

  public Visitable visit( Object anObject )
  {
    if( ((StubInterface) anObject ).isStub() )
      return super.visit( (Object) anObject);
    Visitable workingSetObj = null;
    if( wasPreviouslyVisited( anObject ) )
      return super.visit( (Object) anObject );

    workingSetObj = (Visitable) MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getWorkingSetElementAt( anObject );

    if( workingSetObj != null && ((Visitable) workingSetObj).isNewlyCreated() )
    {
      crudPacket.addNewlyCreated( ((Visitable) ((Visitable) workingSetObj).clone()).accept( new VisitorPruner() ) );
    }
    else if( workingSetObj != null && ((Visitable) workingSetObj).isModified() )
    {
      crudPacket.addModified(  ((Visitable) ((Visitable) workingSetObj).clone()).accept( new VisitorPruner() ) );
    }

    return super.visit( (Object) anObject);
  }

}