package smartClient.framework;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */

public class ClampFileIOException extends Error
{
  public ClampFileIOException()
  {
  }

  public ClampFileIOException( String reason )
  {
    super( reason );
  }
}