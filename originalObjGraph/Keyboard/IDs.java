package Keyboard;

import secant.pos.*;

public class IDs implements PIDFactory
{
  static long previousTime = 0;
  static long nextTime = 0;

  public static Long nextId()
  {
    while( previousTime == nextTime )
    {
      nextTime = System.currentTimeMillis();
    }
    previousTime = nextTime;
    return new Long(nextTime);
  }

  public void createId( ObjID id )
  {
    id.assignKey( nextId() );
  }

  public static void main(String[] args)
  {
    long []gather = new long[7];
    for( int i=0; i < 7; i++ )
    {
      gather[i] = nextId().longValue();
    }
    for( int i=0; i < 7; i++ )
    {
      System.out.println( gather[i] );
    }
  }
}


