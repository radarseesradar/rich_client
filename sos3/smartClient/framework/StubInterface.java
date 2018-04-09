package smartClient.framework;


import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public interface StubInterface extends Identifiable, Visitable
{
  public boolean isStub();
  public Stub toStub();
  public StubInterface yourself();
}