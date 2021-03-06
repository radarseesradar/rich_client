/* generated by emit_jrmi(Dec 21 2000) on 04/19/01 at 09:56:08 AM from Keyboard.odl
*/

package Keyboard;

public class _TVSessionBeanObject_TVSessionBean_Tie extends org.omg.stub.javax.ejb._EJBObjectImpl_Tie {

	private Keyboard.TVSessionBeanObject _target = null;

	public java.lang.String[] _ids() {
		return _type_ids;
	}

	public void setTarget(java.rmi.Remote target) {
		_target = (Keyboard.TVSessionBeanObject) target;
		super.setTarget(target);
	}
	private static final java.lang.String[] _type_ids = {
		"RMI:Keyboard.TVSessionBeanObject:0000000000000000",
		"RMI:javax.ejb.EJBObject:0000000000000000"
	};

	public org.omg.CORBA.portable.OutputStream  _invoke(java.lang.String _method, org.omg.CORBA.portable.InputStream _in, org.omg.CORBA.portable.ResponseHandler _reply) throws org.omg.CORBA.SystemException {
		try {
			switch(_method.length()) {
			case 7:
				if (_method.equals("execute")) {
					byte[] command = _ByteArray_read(_in);
					try {
						_target.execute(command);
					} catch(javax.ejb.EJBException ex) {
						org.omg.CORBA.portable.OutputStream _out = _reply.createExceptionReply();
						_out.write_string("IDL:javax/ejb/EJBEx:1.0");
						((org.omg.CORBA_2_3.portable.OutputStream)_out).write_value(ex, javax.ejb.EJBException.class);
						return _out;
					}
					return _reply.createReply();
				} else if (_method.equals("clearDB")) {
					try {
						_target.clearDB();
					} catch(javax.ejb.EJBException ex) {
						org.omg.CORBA.portable.OutputStream _out = _reply.createExceptionReply();
						_out.write_string("IDL:javax/ejb/EJBEx:1.0");
						((org.omg.CORBA_2_3.portable.OutputStream)_out).write_value(ex, javax.ejb.EJBException.class);
						return _out;
					}
					return _reply.createReply();
				}
			case 8:
				if (_method.equals("storeAll")) {
					byte[] objectGraph = _ByteArray_read(_in);
					try {
						_target.storeAll(objectGraph);
					} catch(javax.ejb.EJBException ex) {
						org.omg.CORBA.portable.OutputStream _out = _reply.createExceptionReply();
						_out.write_string("IDL:javax/ejb/EJBEx:1.0");
						((org.omg.CORBA_2_3.portable.OutputStream)_out).write_value(ex, javax.ejb.EJBException.class);
						return _out;
					}
					return _reply.createReply();
				} else if (_method.equals("greeting")) {
					String _zz;
					try {
						_zz = _target.greeting();
					} catch(javax.ejb.EJBException ex) {
						org.omg.CORBA.portable.OutputStream _out = _reply.createExceptionReply();
						_out.write_string("IDL:javax/ejb/EJBEx:1.0");
						((org.omg.CORBA_2_3.portable.OutputStream)_out).write_value(ex, javax.ejb.EJBException.class);
						return _out;
					}
					org.omg.CORBA.portable.OutputStream _out = _reply.createReply();
					_out.write_string(_zz);
					return _out;
				}
			case 11:
				if (_method.equals("retrieveAll")) {
					long seedID = _in.read_longlong();
					String seedClass = _in.read_string();
					byte[] _zz;
					try {
						_zz = _target.retrieveAll(seedID, seedClass);
					} catch(javax.ejb.EJBException ex) {
						org.omg.CORBA.portable.OutputStream _out = _reply.createExceptionReply();
						_out.write_string("IDL:javax/ejb/EJBEx:1.0");
						((org.omg.CORBA_2_3.portable.OutputStream)_out).write_value(ex, javax.ejb.EJBException.class);
						return _out;
					}
					org.omg.CORBA.portable.OutputStream _out = _reply.createReply();
					_ByteArray_write(_out, _zz);
					return _out;
				} else if (_method.equals("allocateIDs")) {
					int quantity = _in.read_long();
					long _zz;
					try {
						_zz = _target.allocateIDs(quantity);
					} catch(javax.ejb.EJBException ex) {
						org.omg.CORBA.portable.OutputStream _out = _reply.createExceptionReply();
						_out.write_string("IDL:javax/ejb/EJBEx:1.0");
						((org.omg.CORBA_2_3.portable.OutputStream)_out).write_value(ex, javax.ejb.EJBException.class);
						return _out;
					}
					org.omg.CORBA.portable.OutputStream _out = _reply.createReply();
					_out.write_longlong(_zz);
					return _out;
				}
			case 13:
				if (_method.equals("testCreateOne")) {
					try {
						_target.testCreateOne();
					} catch(javax.ejb.EJBException ex) {
						org.omg.CORBA.portable.OutputStream _out = _reply.createExceptionReply();
						_out.write_string("IDL:javax/ejb/EJBEx:1.0");
						((org.omg.CORBA_2_3.portable.OutputStream)_out).write_value(ex, javax.ejb.EJBException.class);
						return _out;
					}
					return _reply.createReply();
				} else if (_method.equals("testCreateAll")) {
					try {
						_target.testCreateAll();
					} catch(javax.ejb.EJBException ex) {
						org.omg.CORBA.portable.OutputStream _out = _reply.createExceptionReply();
						_out.write_string("IDL:javax/ejb/EJBEx:1.0");
						((org.omg.CORBA_2_3.portable.OutputStream)_out).write_value(ex, javax.ejb.EJBException.class);
						return _out;
					}
					return _reply.createReply();
				}
			case 19:
				if (_method.equals("retrieveAllFromStub")) {
					byte[] stub = _ByteArray_read(_in);
					byte[] _zz;
					try {
						_zz = _target.retrieveAllFromStub(stub);
					} catch(javax.ejb.EJBException ex) {
						org.omg.CORBA.portable.OutputStream _out = _reply.createExceptionReply();
						_out.write_string("IDL:javax/ejb/EJBEx:1.0");
						((org.omg.CORBA_2_3.portable.OutputStream)_out).write_value(ex, javax.ejb.EJBException.class);
						return _out;
					}
					org.omg.CORBA.portable.OutputStream _out = _reply.createReply();
					_ByteArray_write(_out, _zz);
					return _out;
				}
			}
		} catch (org.omg.CORBA.SystemException ex) {
			throw ex;
		} catch (java.lang.Throwable ex) {
			throw new org.omg.CORBA.portable.UnknownException(ex);
		}
		return super._invoke(_method, _in, _reply);
	}
	static private byte[] _ByteArray_read(org.omg.CORBA.portable.InputStream _in)
	{
		int _zz = _in.read_long();
		if(_zz > 1000000) {
			throw new org.omg.CORBA.BAD_PARAM("ByteArray exceeded bound");
		}
		byte[]  _value = new byte[_zz];
		for(int _i = 0; _i < _zz; ++_i) {
			_value[_i] = _in.read_octet();
		}
		return _value;
	}
	static private void _ByteArray_write(org.omg.CORBA.portable.OutputStream _out, byte[] _value)
	{
		if(_value.length > 1000000) {
			throw new org.omg.CORBA.BAD_PARAM("ByteArray exceeded bound");
		}
		_out.write_long(_value.length);
		for(int _i = 0; _i < _value.length; ++_i) {
			_out.write_octet(_value[_i]);
		}
	}
}
