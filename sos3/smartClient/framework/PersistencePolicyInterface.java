/* Generated by Together */

package smartClient.framework;

import java.io.*;

public interface PersistencePolicyInterface extends Serializable
{
	public Persistable findPersistentObject( Object anObj );
    public Persistable createPersistentObject( Persistable aShadow );
    public SessionInterface createSession() throws java.rmi.RemoteException;
}