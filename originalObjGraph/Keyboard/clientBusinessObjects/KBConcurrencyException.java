package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

public class KBConcurrencyException extends Error
{
  public KBConcurrencyException()
  {
  }

  public KBConcurrencyException( String reason )
  {
    super( reason );
  }
}