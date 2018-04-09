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
public class TestUnidirectionalToManyRepository extends TestToManyRepository
{
  public TestUnidirectionalToManyRepository( String name)
  {
    super( name );
  }

  public static Test suite( )
  {
    return new TestSuite( TestToManyRepository.class );
  }

  protected ToManyRepository createRepository()
  {
    AssociationsCoordinator anAC = new AssociationsCoordinator();
    return (ToManyRepository) anAC.getUnidirectionalToManyRepository();
  }

}