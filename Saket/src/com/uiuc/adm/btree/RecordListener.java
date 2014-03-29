package com.uiuc.adm.btree;

import java.io.IOException;

/**
* An listener notifed when record is inserted, updated or removed.
* <p/>
* NOTE: this class was used in JDBM2 to support secondary indexes
* JDBM3 does not have a secondary indexes, so this class is not publicly exposed.
*
* @param <K> key type
* @param <V> value type
* @author Jan Kotek
*/
interface RecordListener<K, V> {

    void recordInserted(K key, V value) throws IOException;

    void recordUpdated(K key, V oldValue, V newValue) throws IOException;

    void recordRemoved(K key, V value) throws IOException;

}