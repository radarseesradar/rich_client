package smartClient.framework;


import java.lang.reflect.*;
import java.io.*;
import java.util.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class SwizzleProxy implements InvocationHandler, Serializable
{
  private Object underlyingObject;

  /**
   * @associates String 
   */
  private static Set identityMethodNames;

  static
  {
    identityMethodNames = new HashSet();
    identityMethodNames.add( "isStub" );
    identityMethodNames.add( "getScfoid" );
    identityMethodNames.add( "equals" );
    identityMethodNames.add( "hashCode" );
    identityMethodNames.add( "toString" );
    identityMethodNames.add( "accept" );
  }

  private SwizzleProxy( Object _obj )
  {
    underlyingObject = _obj;
  }

  public synchronized static Object createStubbedProxyFor( Object _obj )
  {
    Class objClass = _obj.getClass();

    return Proxy.newProxyInstance( objClass.getClassLoader()
                                   , new Class [] { StubInterface.class, ToOne.class, ToMany.class, Persistable.class }
                                   , new SwizzleProxy( ((Persistable) _obj).toStub() ) );

  }

  public boolean checkLastRetrievalCache( )
  {
    AssociationsCoordinator lastRetrieval = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getLastRetrieval();
    if( lastRetrieval != null && lastRetrieval.workingSetContains( underlyingObject ) )
    {
      underlyingObject = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getWorkingSetElementAt( underlyingObject );
      stripRedundantProxies();
      return true;
    }
    return false;
  }

  public void stripRedundantProxies(  )
  {
    underlyingObject = getTrueUnderlyingObject( underlyingObject );
  }

  public Object getUnderlyingObject()
  {
    return underlyingObject;
  }

  public void setUnderlyingObject( Object anObject )
  {
  	underlyingObject = anObject;
  }

  public Object getTrueUnderlyingObject()
  {
  	stripRedundantProxies();
    return getUnderlyingObject();
  }

  private Object getTrueUnderlyingObject(  Object _underlyingObject )
  {
    SwizzleProxy anInvocationHandler = null;
    if( Proxy.isProxyClass( _underlyingObject.getClass() ) )
    {
      anInvocationHandler = (SwizzleProxy) Proxy.getInvocationHandler( _underlyingObject );
      return getTrueUnderlyingObject( anInvocationHandler.getUnderlyingObject() );
    }
    return _underlyingObject;
  }

  private void unStub(  )
  throws Exception
  {

	SmartClientFramework framework = SmartClientFramework.getSingleton();

	SessionInterface session = framework.getSession();

    if( framework.isServer() || framework.getGlobalAssociationsCoordinator().isPreservingStubs() )
      return;

    Persistable retrievedObjGraph = session.retrieveAllFromStub(  ((StubInterface)underlyingObject).toStub() );

    underlyingObject = retrievedObjGraph;

    AssociationsCoordinator from = ((Persistable) underlyingObject).getAssociationsCoordinator();
    AssociationsCoordinator to = framework.getGlobalAssociationsCoordinator();
    to.merge( from );
    ((Persistable)underlyingObject).setAssociationsCoordinator( to );
  }

  public Object invoke( Object proxy, Method method, Object[] args )
  throws InvocationTargetException, Exception
  {
    String methodName = null;
    try
    {
      methodName = method.getName();

      if( ((StubInterface) underlyingObject).isStub()
      && !identityMethodNames.contains( methodName ) )
        unStub( );

      if( methodName.equals( "hashCode" ) )
        return new Integer(underlyingObject.hashCode());

      if( methodName.equals( "equals" ) )
        return new Boolean( underlyingObject.equals( args[0] ) );

      return method.invoke( underlyingObject, args );
    }
    catch( IllegalAccessException e )
    {
      // This would indicate a programming bug.
      e.printStackTrace();
      System.out.println( "methodName = " + methodName );
      throw new InternalError( e.toString() );
    }
    catch( java.lang.reflect.InvocationTargetException e )
    {
      // This would indicate a programming bug.
      e.printStackTrace();
      throw new InternalError( e.toString() );
    }
  }
}