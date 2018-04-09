package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

public class VisitorFullPrinterIgnoreClamps extends VisitorFullPrinter
{
  protected boolean isClamped( Transition aTransition )
  {
    return false;
  }
}