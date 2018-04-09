
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
public class VisitorPrinter extends Visitor
{

  private int depth;

  private void incrementDepth()
  {
    depth++;
  }

  public Visitor propagationVisitor()
  {
    VisitorPrinter copyOfThis =  (VisitorPrinter) clone();
    copyOfThis.incrementDepth();
    return copyOfThis;
  }

  public Persistable visit( Object anObject )
  {
    for( int i = 0; i < depth; i++ )
    {
      System.out.print( "    " );
    }
    System.out.println( anObject.toString() );
    return super.visit( (Object) anObject);
  }

  public static void main( String[] args )
  {
      Component aComponent;

      try
      {
      aComponent = new TestVisitor( "dontCare" ).buildObjectGraph();

      System.out.println( "Built object graph" );
      System.out.println();

      aComponent.accept( new VisitorPrinter() );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
  }
}