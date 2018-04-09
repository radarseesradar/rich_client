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

public interface StubInterface
{
  public boolean isStub();
  public long getKboid();
  public Stub toStub();
  public StubInterface yourself();
  public Visitable accept( Visitor aVisitor );
}