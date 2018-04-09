
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package Keyboard.clientBusinessObjects;

import java.io.*;

public class KBCPersistentObjectFactory
{
  private static KBCPersistentObjectFactory singleton = null;
  private ClientSideIDFactory idFactory = null;

  private KBCPersistentObjectFactory()
  {
    idFactory = new ClientSideIDFactory();
  }

  public static KBCPersistentObjectFactory getSingleton ()
  {
    if (singleton == null)
      singleton = new KBCPersistentObjectFactory();
    return singleton;
  }

  public Visitable initializeNewlyCreated( Visitable newlyCreated )
  {
    newlyCreated.xsetKboid( idFactory.nextId() );
    newlyCreated.setNewlyCreated( true );
    newlyCreated.setModified( true );
    return newlyCreated;
  }

  public TVComposite createTVComposite( String name )
  {
    return (TVComposite) initializeNewlyCreated( new TVComposite( name ) );
  }

  public TVCa createTVCa( String name )
  {
    return (TVCa) initializeNewlyCreated( new TVCa(  name ) );
  }

  public TVCb createTVCb( String name )
  {
    return (TVCb) initializeNewlyCreated( new TVCb(  name ) );
  }
}