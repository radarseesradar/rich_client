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
public class VisitorFullPrinter extends VisitorViaAllPaths
{
  private int depth;

  protected void outputString( String pieceOfGraph )
  {
  	System.out.print( pieceOfGraph );
  }

  private void incrementDepth()
  {
    depth++;
  }

  public void addPreviouslyTransitioned( Transition aTransition )
  {
    for( int i = 0; i < depth + 1; i++ )
    {
      outputString( " " );
    }
    outputString( aTransition.toString() );
    outputString( "\n" );
    super.addPreviouslyTransitioned( aTransition );
  }

  public Visitor propagationVisitor()
  {
    VisitorFullPrinter copyOfThis =  (VisitorFullPrinter) clone();
    copyOfThis.incrementDepth();
    return copyOfThis;
  }

  public static void main( String[] args )
  {
      Component aComponent;

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