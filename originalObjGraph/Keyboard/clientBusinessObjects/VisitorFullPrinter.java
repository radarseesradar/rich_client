package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.io.*;

public class VisitorFullPrinter extends VisitorViaAllPaths
{
  private int depth;

  private void incrementDepth()
  {
    depth++;
  }

  protected void addPreviouslyTransitioned( Transition aTransition )
  {
    for( int i = 0; i < depth + 1; i++ )
    {
      System.out.print( " " );
    }
    System.out.println( aTransition.toString() );
    super.addPreviouslyTransitioned( aTransition );
  }

  protected Visitor propagationVisitor()
  {
    VisitorFullPrinter copyOfThis =  (VisitorFullPrinter) clone();
    copyOfThis.incrementDepth();
    return copyOfThis;
  }

  public static void main( String args[] )
  {
      TVComponent aComponent;

      try
      {
      aComponent = new TestVisitor( "dontCare" ).buildObjectGraph();

      System.out.println( "Built object graph" );
      System.out.println();

      aComponent.accept( new VisitorFullPrinter() );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
  }
}