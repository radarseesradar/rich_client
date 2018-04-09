package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

public class KBClampFileIOException extends Error
{
  public KBClampFileIOException()
  {
  }

  public KBClampFileIOException( String reason )
  {
    super( reason );
  }
}