
package smartClient.framework;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import model.test.client.*;

/**
 * @author Steve McDaniel 
 * <br>Copyright (c) Steve McDaniel
 */

public class VisitorCopier extends VisitorSynchronizer
{

  public Persistable visit( Object anObject )
  {
      if( getOther() == null )
        setOther( createOther( anObject ) );
      return super.visit( (Object) anObject );
  }

  public Class deriveOthersType( Class attributeType )
  {
    return attributeType;
  }

  public static void main( String[] args )
  {
      Component originalObjGraph;
      Component copyOfObjGraph;
      Component aComponent;
      TestVisitor aTestVisitor;

      try
      {

      aTestVisitor = new TestVisitor( "dontCare" );

      originalObjGraph = aTestVisitor.buildObjectGraph();

      System.out.println( "Built object graph" );
      System.out.println();

      System.out.println( "Original object graph" );
      System.out.println();

      originalObjGraph.accept( new VisitorPrinter() );

      copyOfObjGraph = (Component) aTestVisitor.buildCopyOfObjectGraph( originalObjGraph );
      System.out.println( "Made copy of object graph and changed the names in the copy." );
      System.out.println();

      System.out.println( "Copy of object graph" );
      System.out.println();
      copyOfObjGraph.accept( new VisitorPrinter() );

      System.out.println( "Original object graph" );
      System.out.println();

      originalObjGraph.accept( new VisitorPrinter() );

      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
  }
}