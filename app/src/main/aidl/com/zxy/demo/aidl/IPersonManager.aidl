// IPersonManager.aidl
package com.zxy.demo.aidl;

// Declare any non-default types here with import statements
import com.zxy.demo.aidl.Person;
interface IPersonManager {

    List<Person> getPersonList();

    boolean addPerson(in Person per);
}
