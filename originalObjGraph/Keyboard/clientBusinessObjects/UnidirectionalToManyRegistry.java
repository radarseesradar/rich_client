package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

public class UnidirectionalToManyRegistry extends ToManyRegistry
{

  public UnidirectionalToManyRegistry( AssociationsCoordinator _associationsCoordinator )
  {
    super( _associationsCoordinator );
  }

  public void printString( int depth )
  {
    String indent = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    System.out.println( indent + "<<<Printing UnidirectionalToManyRegistry>>>");
    super.printString( depth++ );
  }


}