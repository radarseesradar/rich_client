
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package model;

import java.io.*;
import model.test.client.*;
import smartClient.framework.*;

public class PersistentObjectFactory extends AbstractPersistentObjectFactory
{

  public Composite createComposite( String name )
  {
    return (Composite) initializeNewlyCreated( new Composite ( name ) );
  }

  public Female createFemale( String name )
  {
    return (Female) initializeNewlyCreated( new Female (  name ) );
  }

  public Male createMale( String name )
  {
    return (Male) initializeNewlyCreated( new Male (  name ) );
  }
}