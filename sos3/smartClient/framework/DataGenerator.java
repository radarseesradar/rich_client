package smartClient.framework;


import model.test.client.*;
import model.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class DataGenerator
{
  protected Composite[] branches;
  protected Component[] leaves;
  private AssociationsCoordinator savedAssociationsCoordinator;

  public Component buildBiObjectGraph()
  {
      SmartClientFramework.getSingleton().clear();
      branches = new Composite[3];
      leaves = new Component[4];
      PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
      branches[0] = creator.createComposite( "branch_one" );
      branches[1] = creator.createComposite( "branch_two" );
      branches[2] = creator.createComposite( "branch_three" );
      leaves[0] = creator.createFemale( "Sarah" );
      leaves[1] = creator.createMale( "Abe" );
      leaves[2] = creator.createFemale( "Ruth" );
      leaves[3] = creator.createMale( "Noah" );

      branches[0].addChild( branches[2] );
      branches[0].addChild( leaves[0] );
      branches[0].addChild( leaves[1] );
      branches[1].addChild( leaves[2] );
//      leaves[2].setParent(branches[1]);
      branches[1].addChild( leaves[3] );
      branches[1].addChild( branches[0] );
      branches[2].addChild( branches[1] );
      ((Female)leaves[0]).setHusband( (Male) leaves[1] );
      ((Female)leaves[2]).setHusband( (Male) leaves[3] );
      ((Female)leaves[0]).addBrother( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[3] );
      ((Female)leaves[2]).addBrother( (Male) leaves[3] );
      ((Female)leaves[2]).addBrother( (Male) leaves[1] );

      return leaves[1];
  }

  public Component buildUniObjectGraph()
  {
      SmartClientFramework.getSingleton().clear();
      branches = new Composite[3];
      leaves = new Component[10];
      PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
      branches[0] = creator.createComposite( "branch_one" );
      branches[1] = creator.createComposite( "branch_two" );
      branches[2] = creator.createComposite( "branch_three" );
      leaves[0] = creator.createFemale( "Sarah" );
      leaves[1] = creator.createMale( "Abe" );
      leaves[2] = creator.createFemale( "Ruth" );
      leaves[3] = creator.createMale( "Noah" );
      leaves[4] = creator.createMale( "Tom" );
      leaves[5] = creator.createMale( "Dick" );
      leaves[6] = creator.createMale( "Harry" );
      leaves[7] = creator.createFemale( "Thelma" );
      leaves[8] = creator.createFemale( "Louise" );
      leaves[9] = creator.createFemale( "Sue" );

      branches[0].addChild( branches[2] );
      branches[0].addChild( leaves[0] );
      branches[0].addChild( leaves[1] );
      branches[1].addChild( leaves[2] );
//      leaves[2].setParent(branches[1]);
      branches[1].addChild( leaves[3] );
      branches[1].addChild( branches[0] );
      branches[2].addChild( branches[1] );
      ((Female)leaves[0]).setHusband( (Male) leaves[1] );
      ((Female)leaves[2]).setHusband( (Male) leaves[3] );
      ((Female)leaves[0]).addBrother( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[3] );
      ((Female)leaves[2]).addBrother( (Male) leaves[3] );
      ((Female)leaves[2]).addBrother( (Male) leaves[1] );

	  leaves[1].addPlaymate( leaves[4] );
	  leaves[1].addPlaymate( leaves[5] );
	  leaves[1].addPlaymate( leaves[6] );
      leaves[1].setBestFriend( leaves[5] );
      leaves[5].addPlaymate( leaves[7] );
      leaves[5].addPlaymate( leaves[8] );
      leaves[5].addPlaymate( leaves[9] );
      leaves[5].setBestFriend( leaves[9] );

	  initializeAlias( leaves[1] );

      return leaves[1];
  }

  private void saveAssociationsCoordinator(  Persistable objGraph  )
  {
    savedAssociationsCoordinator = new AssociationsCoordinator();
    savedAssociationsCoordinator.merge( objGraph.getAssociationsCoordinator() );
  }

  public Component buildExpectedObjGraphForCouplesOnly()
  {
      SmartClientFramework.getSingleton().clear();
      branches = new Composite[3];
      leaves = new Component[4];
      PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
      branches[0] = creator.createComposite( "branch_one" );
      leaves[0] = creator.createFemale( "Sarah" );
      leaves[1] = creator.createMale( "Abe" );
      leaves[2] = creator.createFemale( "Ruth" );
      leaves[3] = creator.createMale( "Noah" );

      branches[0].addChild( leaves[0] );
      branches[0].addChild( leaves[1] );
      ((Female)leaves[0]).setHusband( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[3] );
      ((Female)leaves[2]).addBrother( (Male) leaves[1] );

	  initializeAlias( leaves[1] );
      incrementUpdateCounter( leaves[1] );

	  saveAssociationsCoordinator( leaves[1] );

      return leaves[1];
  }

  public Component buildExpectedObjGraphForCouplesOnly2()
  {
      SmartClientFramework.getSingleton().clear();
      branches = new Composite[3];
      leaves = new Component[4];
      PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
      branches[0] = creator.createComposite( "branch_one" );
      leaves[0] = creator.createFemale( "Sarah" );
      leaves[1] = creator.createMale( "Abe" );
      leaves[2] = creator.createFemale( "Ruth" );
      leaves[3] = creator.createMale( "Noah" );

      branches[0].addChild( leaves[1] );
      ((Female)leaves[0]).setHusband( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[3] );
      ((Female)leaves[2]).addBrother( (Male) leaves[1] );

	  initializeAlias( leaves[1] );
      incrementUpdateCounter( leaves[0] );

	  saveAssociationsCoordinator( leaves[1] );
      leaves[0].incrementUpdateCounter();

      return leaves[1];
  }

  public Component buildExpectedObjGraphForCouplesOnly3()
  {
      SmartClientFramework.getSingleton().clear();
      branches = new Composite[3];
      leaves = new Component[4];
      PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
      branches[0] = creator.createComposite( "branch_one" );
      leaves[0] = creator.createFemale( "Sarah" );
      leaves[1] = creator.createMale( "Abe" );
      leaves[2] = creator.createFemale( "Ruth" );
      leaves[3] = creator.createMale( "Noah" );

      branches[0].addChild( leaves[0] );
      branches[0].addChild( leaves[1] );
      ((Female)leaves[0]).setHusband( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[3] );

	  initializeAlias( leaves[1] );
      incrementUpdateCounter( leaves[1] );

	  saveAssociationsCoordinator( leaves[1] );

      return leaves[1];
  }

  public Component buildExpectedObjGraphForCouplesOnly4()
  {
      SmartClientFramework.getSingleton().clear();
      branches = new Composite[3];
      leaves = new Component[4];
      PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
      branches[0] = creator.createComposite( "branch_one" );
      branches[1] = creator.createComposite( "branch_two" );
      leaves[0] = creator.createFemale( "Sarah" );
      leaves[1] = creator.createMale( "Abe" );
      leaves[2] = creator.createFemale( "Ruth" );
      leaves[3] = creator.createMale( "Noah" );

      branches[0].addChild( leaves[1] );
      leaves[0].setParent( branches[1] );
      ((Female)leaves[0]).setHusband( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[3] );
      ((Female)leaves[2]).addBrother( (Male) leaves[1] );

	  initializeAlias( leaves[1] );
      incrementUpdateCounter( leaves[0] );

	  saveAssociationsCoordinator( leaves[1] );
      leaves[0].incrementUpdateCounter();

      return leaves[1];
  }

  public Component buildExpectedObjGraphForCouplesOnly5()
  {
      SmartClientFramework.getSingleton().clear();
      branches = new Composite[3];
      leaves = new Component[4];
      PersistentObjectFactory creator = PersistentObjectFactory.getSingleton();
      branches[0] = creator.createComposite( "branch_one" );
      branches[1] = creator.createComposite( "branch_two" );
      leaves[0] = creator.createFemale( "Sarah" );
      leaves[1] = creator.createMale( "Abe" );
      leaves[2] = creator.createFemale( "Ruth" );
      leaves[3] = creator.createMale( "Noah" );

      branches[0].addChild( leaves[0] );
      branches[0].addChild( leaves[1] );
      branches[1].addChild( leaves[2] );
      ((Female)leaves[2]).setHusband( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[1] );
      ((Female)leaves[0]).addBrother( (Male) leaves[3] );
      ((Female)leaves[2]).addBrother( (Male) leaves[1] );
      ((Female)leaves[2]).addBrother( (Male) leaves[3] );

	  initializeAlias( leaves[1] );
      incrementUpdateCounter( leaves[0] );

	  saveAssociationsCoordinator( leaves[1] );

      leaves[1].incrementUpdateCounter();
      leaves[2].incrementUpdateCounter();
      leaves[3].incrementUpdateCounter();

      return leaves[1];
  }

  private void incrementUpdateCounter( Persistable objGraph )
  {
    objGraph.accept
    (
      new Visitor()
      {
        public Persistable visit( Object anObject )
        {
          Persistable aPersistable = (Persistable) anObject;
          aPersistable.incrementUpdateCounter();
		  return super.visit( (Object) anObject);
        }
      }
    );
  }

  private void initializeAlias( Component objGraph )
  {
    objGraph.accept
    (
      new Visitor()
      {
        public Persistable visit( Object anObject )
        {
          Component aComponent = (Component) anObject;
          aComponent.setAlias( aComponent.getName() + "" );
          return super.visit( (Object) anObject);
        }
      }
    );
  }

  public Composite[] getBranches()
  {
    return branches;
  }

  public Component[] getLeaves()
  {
    return leaves;
  }

}