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

public class VisitorFullEqualityTesterIgnoreClamps extends VisitorFullEqualityTester
{
  public VisitorFullEqualityTesterIgnoreClamps( Visitable _other )
  {
    super( _other );
  }

  protected boolean isClamped( Transition aTransition )
  {
    return false;
  }

}