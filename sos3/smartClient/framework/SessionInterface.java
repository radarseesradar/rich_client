/* Generated by Together */

package smartClient.framework;

import java.rmi.*;
import java.util.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public interface SessionInterface extends Remote
{
	public String greetings( ) throws RemoteException;
	public long allocateIDs( int quantity ) throws RemoteException;
    public void clearDB() throws RemoteException;
    public void dumpDB( Stub seedForFlood, String message ) throws RemoteException;
    public Persistable retrieveAllFromStub( Stub seedStub ) throws RemoteException;
    public Persistable retrieveAllFromStubs( List seedStubs ) throws RemoteException;
    public void execute( CommandInterface command ) throws RemoteException;
    public void setPropertiesUsing( String filename ) throws RemoteException;
}
