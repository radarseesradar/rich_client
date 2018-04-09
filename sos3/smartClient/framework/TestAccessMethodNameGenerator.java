package smartClient.framework;


import java.util.*;
import java.lang.reflect.*;
import junit.framework.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
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
    if( ! generator.getSingleton().specialPluralsAreInitialized() )
    	generator.getSingleton().setSpecialPluralsUsing( "c:\\sos3\\specialPluralsProperties.txt" );
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