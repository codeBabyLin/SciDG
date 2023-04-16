package OperationImpl;

import operation.VersionGraphStore;
import org.apache.commons.lang3.tuple.Pair;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import sampleGraph.SampleGraph;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class VersionGraphStoreImplSP implements VersionGraphStore {

    private String storePath;
    HashMap<Integer,String> version_path;
    public VersionGraphStoreImplSP(String storePath){
        this.storePath = storePath;
        this.version_path = new HashMap<>();
    }
    public void delfile(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f: files){
                delfile(f);
            }
        }
        file.delete();
    }
    void writeVersionPath(HashMap<Integer,String> version_path,String dirPath){
        String fileNameDelta = new File(dirPath,"version_path.del").getAbsolutePath();
        System.out.println(fileNameDelta);
        //String fileNameDel = new File(path,"del.del").getName();
        try {
            RandomAccessFile fp = new RandomAccessFile(fileNameDelta, "rw");
            int size = version_path.size();
            fp.writeInt(size);
            for(Map.Entry map: version_path.entrySet()){
                int key = (int) map.getKey();
                String value = (String) map.getValue();
                fp.writeInt(key);
                fp.writeUTF(value);
            }
        }catch (Exception e){

        }
    }


    @Override
    public void begin() {
        delfile(new File(this.storePath));
    }

    @Override
    public void storeGraph(SampleGraph sampleGraph, int version) {
        String dataBasePath = String.format("%s//server%d//server",this.storePath, version);
        version_path.put(version,dataBasePath);
        System.out.println(String.format("version: %d   dataPath:%s",version,dataBasePath));

        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(dataBasePath));
        HashMap<Integer,Integer> nodeIds = new HashMap<>();

        Transaction tx = graphDb.beginTx();
        int count = 0;
        for(Integer e: sampleGraph.getNodes()){
            count = count + 1;
            Node n = graphDb.createNode();
            String label = sampleGraph.getNodeLabel(e);
            HashMap<String,Object> nodeProperty = sampleGraph.getNodeProperties().get(e);
            n.addLabel(()->label);
            nodeProperty.forEach(n::setProperty);
            n.setProperty("nodeId",e);
            int nodeId = (int) n.getId();
            nodeIds.put(e,nodeId);
            if(count >= 100000){
                tx.success();
                tx.close();
                tx = graphDb.beginTx();
                count = 0;
            }
        }

        tx.success();
        tx.close();

        tx = graphDb.beginTx();
        count = 0;
        for(Pair<Integer,Integer> pais: sampleGraph.getRels()) {
            Node n1 = graphDb.getNodeById(nodeIds.get(pais.getLeft()));
            Node n2 = graphDb.getNodeById(nodeIds.get(pais.getRight()));
            String type = sampleGraph.getRelationType(pais);
            Relationship r1 = n1.createRelationshipTo(n2, ()->type);
            //Relationship r2 = n2.createRelationshipTo(n1, ()->type);
            count = count + 1;
            if(count >= 100000){
                tx.success();
                tx.close();
                tx = graphDb.beginTx();
                count = 0;
            }
        }
        tx.success();
        tx.close();

        tx = graphDb.beginTx();

        long nSize = graphDb.getAllNodes().stream().count();
        long rSize = graphDb.getAllRelationships().stream().count();

        String str = String.format("version:%d  nodes:%d  relSize:%d",version,nSize,rSize);
        System.out.println(str);
        graphDb.shutdown();

    }

    @Override
    public void finish() {
        writeVersionPath(this.version_path,this.storePath);
    }
}
