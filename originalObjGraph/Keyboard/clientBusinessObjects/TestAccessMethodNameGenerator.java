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

public class TestAccessMethodNameGenerator extends TestCase
{
  public TestAccessMethodNameGenerator( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestAccessMethodNameGenerator.class );
  }

  public void testGenerations()
  {
    AccessMethodNameGenerator generator = AccessMethodNameGenerator.getSingleton();
    assertEquals( generator.adder( "children" ), "addChild"  );
    assertEquals( generator.remover( "children" ), "removeChild"  );
    assertEquals( generator.getter( "children" ), "getChildren"  );
    assertEquals( generator.setter( "children" ), "setChildren"  );

    assertEquals( generator.adder( "teachers" ), "addTeacher"  );
    assertEquals( generator.remover( "teachers" ), "removeTeacher"  );
    assertEquals( generator.getter( "teachers" ), "getTeachers"  );
    assertEquals( generator.setter( "teachers" ), "setTeachers"  );
  }

}