
package smartClient.framework;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

/**
 * @author Steve McDaniel 
 * <br>Copyright (c) Steve McDaniel
 */
public class VisitorPersister extends VisitorSynchronizer
{

  public VisitorPersister()
  {
  }

  public Persistable createOther( Object anObject )
  {
      if( wasPreviouslyCreated( anObject ) )
        return (Persistable) getPreviouslyCreated( anObject );

      Persistable newlyCreated = SmartClientFramework.getSingleton().getPersistencePolicy().createPersistentObject(
      												(Persistable) anObject );
      addPreviouslyCreated( (Object) newlyCreated );
      return newlyCreated;
  }

  public Persistable visit( Object anObject )
  {
      if( getOther() == null )
        setOther( createOther( anObject ) );
      return super.visit( (Object) anObject );
  }

  public static void main( String[] args )
  {
    try
    {
      TestVisitorPersistence.oneTimeSetUp();
      new TestVisitorPersistence("dontCare").testLoadData();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
}