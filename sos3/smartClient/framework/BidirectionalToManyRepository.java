package smartClient.framework;


import java.util.*;
import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public interface BidirectionalToManyRepository
{
  public void associate( RoleKey fromKey, RoleKey toKey );

  public void disassociate( RoleKey fromKey, RoleKey toKey );
  public Collection getToMany( RoleKey manyKey );
}