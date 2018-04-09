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
import java.lang.reflect.*;
import java.lang.*;
import Keyboard.clientBusinessObjects.*;
import Keyboard.*;
import junit.framework.*;

public class TestUnidirectionalToManyRegistry extends TestToManyRegistry
{
  public TestUnidirectionalToManyRegistry( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestToManyRegistry.class );
  }

  protected ToManyRegistry createRegistry()
  {
    AssociationsCoordinator anAC = new AssociationsCoordinator();
    return (ToManyRegistry) anAC.getUnidirectionalToManyRegistry();
  }

}