package com.uiuc.adm.btree;


import java.io.*;

/**
* Interface used to provide a serialization mechanism other than a class' normal
* serialization.
*
* @author Alex Boisvert
*/
public interface Serializer<A> {

    /**
* Serialize the content of an object into a byte array.
*
* @param out ObjectOutput to save object into
* @param obj Object to serialize
*/
    public void serialize(DataOutput out, A obj)
            throws IOException;


    /**
* Deserialize the content of an object from a byte array.
*
* @param in to read serialized data from
* @return deserialized object
* @throws IOException
* @throws ClassNotFoundException
*/
    public A deserialize(DataInput in)
            throws IOException, ClassNotFoundException;

}