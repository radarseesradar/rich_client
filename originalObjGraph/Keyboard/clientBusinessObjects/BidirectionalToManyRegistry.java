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

public interface BidirectionalToManyRegistry
{
  public void associate( RoleKey fromKey, RoleKey toKey );

  public void disassociate( RoleKey fromKey, RoleKey toKey );
  public Collection getToMany( RoleKey manyKey );
}