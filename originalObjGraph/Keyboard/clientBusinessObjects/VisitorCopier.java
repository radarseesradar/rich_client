
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package Keyboard.clientBusinessObjects;

import java.util.*;
import java.lang.reflect.*;
import java.lang.*;
import Keyboard.*;
import java.io.*;

public class VisitorCopier extends VisitorSynchronizer
{

  public Visitable visit( Object anObject )
  {
      if( getOther() == null )
        setOther( createOther( anObject ) );
      return super.visit( (Object) anObject );
  }

  protected Class deriveOthersType( Class attributeType )
  {
    return attributeType;
  }

  public static void main( String args[] )
  {
      TVComponent originalObjGraph;
      TVComponent copyOfObjGraph;
      TVComponent aComponent;
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

      copyOfObjGraph = (TVComponent) aTestVisitor.buildCopyOfObjectGraph( originalObjGraph );
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