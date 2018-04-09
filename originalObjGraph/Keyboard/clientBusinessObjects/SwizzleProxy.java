package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.lang.reflect.*;
import Keyboard.*;
import secant.extreme.*;
import java.io.*;
import java.util.*;

public class SwizzleProxy implements InvocationHandler, Serializable
{
  private Object underlyingObject;
  private static Set identityMethodNames;

  static
  {
    identityMethodNames = new HashSet();
    identityMethodNames.add( "isStub" );
    identityMethodNames.add( "getKboid" );
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
                                   , new Class [] { StubInterface.class, ToOne.class, ToMany.class, Visitable.class }
                                   , new SwizzleProxy( ((Visitable) _obj).toStub() ) );

  }

  public boolean checkLastRetrievalCache( )
  {
    AssociationsCoordinator lastRetrieval = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getLastRetrieval();
    if( lastRetrieval != null && lastRetrieval.workingSetContains( underlyingObject ) )
    {
      underlyingObject = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getWorkingSetElementAt( underlyingObject );
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

    TVSessionBeanObject sessionBean = MiddleWareFramework.getSingleton().getTVSessionBeanObject();
    Client client = MiddleWareFramework.getSingleton().getClient();

    if( client == null || MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().isPreservingStubs() )
      return;

    byte [] byteArray;

    client.begin();
    byteArray = sessionBean.retrieveAllFromStub( (new Serializer()).serialize( underlyingObject ) );
    client.commit();

    Visitable retrievedObjGraph = (Visitable) (new Serializer()).deserialize( byteArray );

    underlyingObject = retrievedObjGraph;

    AssociationsCoordinator from = ((Visitable) underlyingObject).getAssociationsCoordinator();
    AssociationsCoordinator to = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator();
    to.merge( from );
    ((Visitable)underlyingObject).setAssociationsCoordinator( to );
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
  }
}