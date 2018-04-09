/* Secant Object Implementation for TVPCb (safe).
 * generated by emit_jbo(Dec 21 2000) on 05/03/01 at 10:28:25 AM
 */
package Keyboard;
import secant.util.Money;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.*;
import secant.portable.persistence.*;



// begin_safe_imports
import Keyboard.clientBusinessObjects.*;
import java.io.*;
// end_safe_imports

public class TVPCb extends Keyboard.TVPComponent
	// begin_safe_additionalInterfaces
	// end_safe_additionalInterfaces
{
	private AssociationReference wife;
	private AssociationReference sisters;
	private DirtyBits _dirtyBits_;
	// begin_safe_members
	// end_safe_members

	public TVPCb()
	{
		super();
		PersistenceService _ps = PersistenceService.getInstance();
		_dirtyBits_ = _ps.createDirtyBits(2);
		wife = _ps.createAssociationReference(this, "wife");
		sisters = _ps.createAssociationReference(this, "sisters");
		// begin_safe_constructNoArgs
		// end_safe_constructNoArgs
	}

	// factory method(s)
	public static TVPCb createTVPCb(long kboid, String _loc) throws PersistenceException
	{
		TVPCb _obj = new TVPCb();
		_obj.setKboid(kboid);
		PersistenceService.getInstance().activateNew(_obj, _loc);
		// begin_safe_factory
		// end_safe_factory
		return _obj;
	}

	public static TVPCb createTVPCb(long kboid) throws PersistenceException
	{
		return createTVPCb(kboid, null);
	}

	// begin_safe_constructors
	// end_safe_constructors

	// attribute accessor methods


	// begin_safe_getWife_javadoc_comment
	// end_safe_getWife_javadoc_comment
	public Keyboard.TVPCa getWife() throws PersistenceException
	{
		// begin_safe_getWife
		// end_safe_getWife
		return (Keyboard.TVPCa)wife.get();
	}

	// begin_safe_setWife_javadoc_comment
	// end_safe_setWife_javadoc_comment
	public void setWife(Keyboard.TVPCa _obj) throws PersistenceException
	{
		// begin_safe_setWife

                try
                {
                  if( _obj == null )
                  {
                    return;
                  }
                  Visitable old = getWife();
                  if( old != null && ! old.equals( _obj ) )
                  {
                    wife.disassociate( old );
                  }
                  wife.associate( _obj );
                }
                catch( PersistenceException exc )
                {
                  exc.printStackTrace();
                  throw exc;
                }
		// end_safe_setWife
	}

	// begin_safe_getSisters_javadoc_comment
	// end_safe_getSisters_javadoc_comment
	public java.util.LinkedList getSisters() throws PersistenceException
	{
		// begin_safe_getSisters
		// end_safe_getSisters
		return (java.util.LinkedList)sisters.get();
	}

	// begin_safe_addSister_javadoc_comment
	// end_safe_addSister_javadoc_comment
	public void addSister(Keyboard.TVPCa _obj) throws PersistenceException
	{
		// begin_safe_addSister
		sisters.associate(_obj);
		// end_safe_addSister
	}

	// begin_safe_removeSister_javadoc_comment
	// end_safe_removeSister_javadoc_comment
	public void removeSister(Keyboard.TVPCa _obj) throws PersistenceException
	{
		// begin_safe_removeSister
		sisters.disassociate(_obj);
		// end_safe_removeSister
	}

	// operations

	// begin_safe_additional_methods

  public Visitable accept( Visitor aVisitor )
  {
    return aVisitor.visit( (TVPCb) this ).postAccept( aVisitor );
  }

  public Visitable postAccept( Visitor aVisitor )
  {
    super.postAccept(aVisitor);
    aVisitor.postVisit( (TVPCb) this );
    return this;
  }

  public String otherClassName()
  {
    return "TVCb";
  }

	// end_safe_additional_methods
}

// begin_safe_other
// end_safe_other


/*    -- begin 
// begin_safe_ORPHANED_SAFE_BLOCKS
// begin_safe_addWife
		wife.associate(_obj);
// end_safe_addWife
// begin_safe_addSisters
		sisters.associate(_obj);
// end_safe_addSisters
// begin_safe_addBrother
		sisters.associate(_obj);
// end_safe_addBrother
// begin_safe_removeBrother
		sisters.disassociate(_obj);
// end_safe_removeBrother
// begin_safe_setFk_2883928
		Fk_2883928 = _zz;
// end_safe_setFk_2883928
// begin_safe_setFk_14680530
		Fk_14680530 = _zz;
// end_safe_setFk_14680530
// begin_safe_setFk_2883928
		Fk_2883928 = _zz;
// end_safe_setFk_2883928
// end_safe_ORPHANED_SAFE_BLOCKS
*/ // -- end 