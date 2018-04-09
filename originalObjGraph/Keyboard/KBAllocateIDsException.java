package Keyboard;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

public class KBAllocateIDsException extends Error
{
  public KBAllocateIDsException()
  {
  }

  public KBAllocateIDsException( String reason )
  {
    super( reason );
  }
}