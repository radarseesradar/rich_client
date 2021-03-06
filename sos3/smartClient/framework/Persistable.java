/* Generated by Together */

package smartClient.framework;

import javax.transaction.SystemException;
import java.rmi.RemoteException;
/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public interface Persistable extends PostVisitable, StubInterface
{
    public void save() throws Exception;

    public void incrementUpdateCounter();

    public Persistable refresh() throws SystemException, Exception, RemoteException;

    public String otherClassName();

    public String otherPackageName();

    public Object clone();

  public boolean isModified();

  public void setModified(boolean modified);

  public int getUpdateCounter();

  public void setUpdateCounter(int updateCounter);

  public boolean isNewlyCreated();

  public void setNewlyCreated(boolean newlyCreated);

  public AssociationsCoordinator getAssociationsCoordinator();

  public void setAssociationsCoordinator(AssociationsCoordinator associationsCoordinator);

  public void markModified();

}
