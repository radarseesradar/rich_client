
package smartClient.framework;

import java.io.*;
import model.test.client.*;
import model.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
abstract public class AbstractPersistentObjectFactory
{
  private static PersistentObjectFactory singleton = null;
  private IDFactoryInterface idFactory = new ClientSideIDFactory();

  protected AbstractPersistentObjectFactory()
  {
  }

  public static PersistentObjectFactory getSingleton ()
  {
    if (singleton == null)
      singleton = new PersistentObjectFactory ();
    return singleton;
  }

  protected Persistable initializeNewlyCreated( Persistable newlyCreated )
  {
    newlyCreated.setScfoid( idFactory.nextId() );
    newlyCreated.setNewlyCreated( true );
    newlyCreated.setModified( true );
    return newlyCreated;
  }

}