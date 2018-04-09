package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.util.*;
import java.io.*;

public interface ToMany
{
  public void add( Object toObject );
  public Collection getAll();
  public void remove( Object toObject );
  public boolean addAll( Collection aCollection );
}