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

public class VisitorPostStorage extends VisitorViaAllPaths
{

  protected boolean wasPreviouslyTransitioned( Transition aTransition )
  {
    boolean wasPreviouslyTransitioned = getPreviouslyTransitioned().contains( aTransition );
    boolean isClamped = isClamped( aTransition );
    Clamp directClamp = null;
    Clamp inverseClamp = null;
    if( isClamped && !wasPreviouslyTransitioned )
    {
      directClamp = aTransition.toClamp();
      directClamp = (Clamp)ClampProvider.getSingleton().getStorageClamps().get( directClamp );
      directClamp.getChangeList().removeCommandsFromTo( aTransition.getFrom(), aTransition.getTo() );
      inverseClamp = directClamp.getInverseClamp();
      if( inverseClamp != null )
      {
        inverseClamp.getChangeList().removeCommandsFromTo( aTransition.getTo(), aTransition.getFrom() );
      }
    }
    return wasPreviouslyTransitioned || isClamped;
  }

  protected void addPreviouslyTransitioned( Transition aTransition )
  {
    UnidirectionalAssociation anAssociation = aTransition.getAssociation();
    anAssociation.getChangeList().clear();
    super.addPreviouslyTransitioned( aTransition );
  }

  protected boolean isClamped( Transition aTransition )
  {
    return ClampProvider.getSingleton().getStorageClamps().containsKey( aTransition.toClamp() );
  }

  public Visitable visit( Object anObject )
  {
    if( ((StubInterface) anObject ).isStub() )
      return super.visit( (Object) anObject);
    Visitable aVisitable = (Visitable) anObject;
    aVisitable.setNewlyCreated( false );
    aVisitable.setModified( false );
    return super.visit( (Object) anObject);
  }
}