package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

public class AddCommand extends Command
{
  public AddCommand( Stub _receiver, String _roleName, Stub _arg, String _argTypeName, int _associationType )
  {
    super(  _receiver, _roleName, _arg, _argTypeName, _associationType );
  }

  protected String getCommandMessage()
  {
    return AccessMethodNameGenerator.getSingleton().adder( getRoleName() );
  }

}