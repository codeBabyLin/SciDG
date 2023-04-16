//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.neo4j.graphdb;

//import cn.DynamicGraph.graphdb.TransactionVersionCommit;
//import cn.DynamicGraph.kernel.impl.store.DbVersionStore;
//import scala.Function2;

public interface Transaction extends AutoCloseable {



    void terminate();

    void failure();

    void success();

    void close();

    Lock acquireWriteLock(PropertyContainer var1);

    Lock acquireReadLock(PropertyContainer var1);
}
