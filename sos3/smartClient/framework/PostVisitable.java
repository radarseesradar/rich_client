
package smartClient.framework;

import java.io.*;
import model.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public interface PostVisitable extends Cloneable, Serializable, Comparable, Visitable
{
  public Persistable postAccept( Visitor aVisitor );
}