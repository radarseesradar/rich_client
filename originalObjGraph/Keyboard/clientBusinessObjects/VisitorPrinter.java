
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package Keyboard.clientBusinessObjects;

import java.io.*;

public class VisitorPrinter extends Visitor
{

  private int depth;

  private void incrementDepth()
  {
    depth++;
  }

  protected Visitor propagationVisitor()
  {
    VisitorPrinter copyOfThis =  (VisitorPrinter) clone();
    copyOfThis.incrementDepth();
    return copyOfThis;
  }

  public Visitable visit( Object anObject )
  {
    for( int i = 0; i < depth; i++ )
    {
      System.out.print( "    " );
    }
    System.out.println( anObject.toString() );
    return super.visit( (Object) anObject);
  }

  public static void main( String args[] )
  {
      TVComponent aComponent;

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