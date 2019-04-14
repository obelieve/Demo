/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\admin\\Desktop\\Demo\\app\\src\\main\\aidl\\com\\zxy\\demo\\aidl\\IPersonManager.aidl
 */
package com.zxy.demo.aidl;
public interface IPersonManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.zxy.demo.aidl.IPersonManager
{
private static final String DESCRIPTOR = "com.zxy.demo.aidl.IPersonManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.zxy.demo.aidl.IPersonManager interface,
 * generating a proxy if needed.
 */
public static com.zxy.demo.aidl.IPersonManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.zxy.demo.aidl.IPersonManager))) {
return ((com.zxy.demo.aidl.IPersonManager)iin);
}
return new com.zxy.demo.aidl.IPersonManager.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getPersonList:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.zxy.demo.aidl.Person> _result = this.getPersonList();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_addPerson:
{
data.enforceInterface(DESCRIPTOR);
com.zxy.demo.aidl.Person _arg0;
if ((0!=data.readInt())) {
_arg0 = com.zxy.demo.aidl.Person.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
boolean _result = this.addPerson(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.zxy.demo.aidl.IPersonManager
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.util.List<com.zxy.demo.aidl.Person> getPersonList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.zxy.demo.aidl.Person> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPersonList, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.zxy.demo.aidl.Person.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean addPerson(com.zxy.demo.aidl.Person per) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((per!=null)) {
_data.writeInt(1);
per.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_addPerson, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getPersonList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_addPerson = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public java.util.List<com.zxy.demo.aidl.Person> getPersonList() throws android.os.RemoteException;
public boolean addPerson(com.zxy.demo.aidl.Person per) throws android.os.RemoteException;
}
