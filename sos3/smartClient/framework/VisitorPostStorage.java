package smartClient.framework;

import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */

public class VisitorPostStorage extends VisitorViaAllPaths
{

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

  public void addPreviouslyTransitioned( Transition aTransition )
  {
    UnidirectionalAssociation anAssociation = aTransition.getAssociation();
    anAssociation.getChangeList().clear();
    super.addPreviouslyTransitioned( aTransition );
  }

  public boolean isClamped( Transition aTransition )
  {
    return ClampProvider.getSingleton().getStorageClamps().containsKey( aTransition.toClamp() );
  }

  public Persistable visit( Object anObject )
  {
    if( ((StubInterface) anObject ).isStub() )
      return super.visit( (Object) anObject);
    Persistable aPersistable = (Persistable) anObject;
    aPersistable.setNewlyCreated( false );
    aPersistable.setModified( false );
    return super.visit( (Object) anObject);
  }
}