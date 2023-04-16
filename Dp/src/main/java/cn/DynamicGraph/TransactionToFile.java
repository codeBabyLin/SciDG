package cn.DynamicGraph;

import org.neo4j.graphdb.Transaction;

public class TransactionToFile {
    Transaction tx;
    String fileName;
    public TransactionToFile(Transaction tx, String fileName){
        this.tx = tx;
        this.fileName = fileName;
    }

}
