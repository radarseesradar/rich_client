package smartClient.framework;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */

public class UnidirectionalToManyRepository extends ToManyRepository
{

  public UnidirectionalToManyRepository( AssociationsCoordinator _associationsCoordinator )
  {
    super( _associationsCoordinator );
  }

  public void printString( int depth )
  {
    String indent = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    System.out.println( indent + "<<<Printing UnidirectionalToManyRepository>>>");
    super.printString( depth + 1 );
  }


}