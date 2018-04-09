package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

public class DataGenerator
{
  protected TVComposite branches[];
  protected TVComponent leaves[];

  public TVComponent buildBiObjectGraph()
  {
      MiddleWareFramework.getSingleton().clear();
      branches = new TVComposite[3];
      leaves = new TVComponent[4];
      KBCPersistentObjectFactory creator = KBCPersistentObjectFactory.getSingleton();
      branches[0] = creator.createTVComposite( "branch_one" );
      branches[1] = creator.createTVComposite( "branch_two" );
      branches[2] = creator.createTVComposite( "branch_three" );
      leaves[0] = creator.createTVCa( "leaf_one" );
      leaves[1] = creator.createTVCb( "leaf_two" );
      leaves[2] = creator.createTVCa( "leaf_three" );
      leaves[3] = creator.createTVCb( "leaf_four" );

      branches[0].addChild( branches[2] );
      branches[0].addChild( leaves[0] );
      branches[0].addChild( leaves[1] );
      branches[1].addChild( leaves[2] );
//      leaves[2].setParent(branches[1]);
      branches[1].addChild( leaves[3] );
      branches[1].addChild( branches[0] );
      branches[2].addChild( branches[1] );
      ((TVCa)leaves[0]).setHusband( (TVCb) leaves[1] );
      ((TVCa)leaves[2]).setHusband( (TVCb) leaves[3] );
      ((TVCa)leaves[0]).addBrother( (TVCb) leaves[1] );
      ((TVCa)leaves[0]).addBrother( (TVCb) leaves[3] );
      ((TVCa)leaves[2]).addBrother( (TVCb) leaves[3] );
      ((TVCa)leaves[2]).addBrother( (TVCb) leaves[1] );

      return leaves[1];
  }

  public TVComposite [] getBranches()
  {
    return branches;
  }

  public TVComponent [] getLeaves()
  {
    return leaves;
  }

}