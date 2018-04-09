package Keyboard;
import Keyboard.clientBusinessObjects.*;
import secant.extreme.*;
import java.io.*;
import javax.rmi.*;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

public class TestMemoryLeak
{

  private static TVComponent buildObjectGraph()
  {
    TVComponent objGraph = ( new TestVisitor( "dontCare" )).buildObjectGraph();

    objGraph.accept
    (
      new Visitor()
      {
        public Visitable visit( Object anObject )
        {
          TVComponent aComponent = (TVComponent) anObject;
          aComponent.setAlias( aComponent.getName() + "" );
          return (Visitable) aComponent;
        }
      }
    );

    return (TVComponent) objGraph;
  }

  public static void main( String []args )
  {
    try
    {
      MiddleWareFramework.getSingleton().establishCommunications();
      TVSessionBeanObject tvObject = MiddleWareFramework.getSingleton().getTVSessionBeanObject();
      Client client = MiddleWareFramework.getSingleton().getClient();
      DataInputStream dis =  new DataInputStream( System.in );
      client.begin();
        tvObject.clearDB();
      client.commit();

      TVComponent objGraph = buildObjectGraph();

      System.out.println( "About to save object graph. Hit <CR> to continue. " );
      dis.readLine();

      objGraph.save();

      System.out.println( "About to retrieve object graph. Hit <CR> to continue. " );
      dis.readLine();

      objGraph.refresh();

      tvObject.remove();

      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
  }
}