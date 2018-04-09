package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

public class KBKeyboardException extends Exception
{
  public KBKeyboardException()
  {
  }

  public KBKeyboardException( String reason )
  {
    super( reason );
  }
}