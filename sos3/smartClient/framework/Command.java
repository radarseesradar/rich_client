package smartClient.framework;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public abstract class Command implements CommandInterface, Comparable
{
  private Stub receiver;
  private String roleName;
  private Stub arg;
  private String argTypeName;
  private int mySequenceNumber;
  private int associationType;
  private static int nextSequenceNumber;

  public Command( Stub _receiver, String _roleName, Stub _arg, String _argTypeName, int _associationType )
  {
    receiver = _receiver;
    roleName = _roleName;
    arg = _arg;
    argTypeName = _argTypeName;
    associationType = _associationType;
    mySequenceNumber = ++nextSequenceNumber;
  }

  public int getAssociationType()
  {
    return associationType;
  }

  public Stub getArg()
  {
    return arg;
  }

  private int getMySequenceNumber()
  {
    return mySequenceNumber;
  }

  public boolean equals( Command another )
  {
    return getMySequenceNumber() == another.getMySequenceNumber();
  }

  public int compareTo( Object another )
  {
    return (new Integer( getMySequenceNumber() )).compareTo( new Integer( ((Command)another).getMySequenceNumber() ));
  }

  public int hashCode()
  {
    return  getMySequenceNumber();
  }

  public Stub getReceiver()
  {
    return receiver;
  }

  protected String getRoleName()
  {
    return roleName;
  }

  private String getArgTypeName()
  {
    return argTypeName;
  }

  abstract protected String getCommandMessage();

  public void execute()
  {
    Object attributeOwner = null;
    Object parameterValue = null;
    Class parameterType = null;
    Class attributeOwnersClass = null;
    String commandMessage = null;
    Class [] commandParameterTypes = new Class[1];
    Method commandMethod = null;
    Object [] commandParameters = new Object[1];
    try
    {
      attributeOwner = getReceiver().findPersistentObject();
      parameterValue = getArg().findPersistentObject();
      parameterType =   Class.forName( getArgTypeName() );
      attributeOwnersClass = attributeOwner.getClass();
      commandMessage = getCommandMessage();
      commandParameterTypes[0] = parameterType;
      commandMethod = attributeOwnersClass.getMethod(commandMessage, commandParameterTypes );
      commandParameters[0] = parameterValue;
      commandMethod.invoke( attributeOwner, commandParameters );
    }
    catch( NoSuchMethodException e )
    {
      e.printStackTrace();
      System.out.println( "attributeOwner = " + attributeOwner );
      System.out.println( "parameterValue = " + parameterValue );
      System.out.println( "parameterType = " + parameterType );
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass );
      System.out.println( "commandMessage = " + commandMessage );
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( InvocationTargetException e )
    {
      e.printStackTrace();
      System.out.println( "attributeOwner = " + attributeOwner );
      System.out.println( "parameterValue = " + parameterValue );
      System.out.println( "parameterType = " + parameterType );
      System.out.println( "attributeOwnersClass = " + attributeOwnersClass );
      System.out.println( "commandMessage = " + commandMessage );
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( IllegalAccessException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( ClassNotFoundException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }
}