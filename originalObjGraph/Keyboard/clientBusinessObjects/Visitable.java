
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package Keyboard.clientBusinessObjects;

import java.io.*;

public interface Visitable extends Cloneable, Serializable, Comparable, StubInterface
{
  public Visitable postAccept( Visitor aVisitor );
  public Object clone();
  public String otherClassName();
  public void xsetKboid( long aKboid );
  public void setNewlyCreated( boolean trueOrFalse );
  public boolean isNewlyCreated();
  public void setModified( boolean trueOrFalse );
  public boolean isModified();
  public void incrementUpdateCounter();
  public void save() throws Exception;
  public int getUpdateCounter();
  public void setUpdateCounter( int counter );
  public void setAssociationsCoordinator( AssociationsCoordinator anAssociationsCoordinator );
  public AssociationsCoordinator getAssociationsCoordinator();
  public Visitable refresh() throws javax.transaction.SystemException, java.lang.Exception, java.rmi.RemoteException;
}