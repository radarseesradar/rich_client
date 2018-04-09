package smartClient.framework;

import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class VisitorEqualityTesterIgnoreClamps extends VisitorEqualityTester
{
  public VisitorEqualityTesterIgnoreClamps( Persistable _other )
  {
    super( _other );
  }

  public boolean isClamped( Transition aTransition )
  {
    return false;
  }
}