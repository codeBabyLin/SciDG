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
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

public class VersionGraphStoreImplDP implements VersionGraphStore {
    String dataStorePath;
    private SampleGraph tempGraph;
    private HashMap<Integer,String> realStorePath;
    private HashMap<Integer,String>  deltaStorePath;
    public VersionGraphStoreImplDP(String dataStorePath){
        this.dataStorePath = dataStorePath;
        this.realStorePath = new HashMap<>();
        this.deltaStorePath = new HashMap<>();
    }

    public <T> HashSet<T> setUnion(HashSet<T> A, HashSet<T> B){
        HashSet<T> C = new HashSet<>();
        C.addAll(A);
        C.addAll(B);
        return C;
    }
    public <T> HashSet<T> setDifference(HashSet<T> A,HashSet<T> B){
        HashSet<T> C = new HashSet<>();
        C.addAll(A);
        C.removeAll(B);
        return C;
    }

    public <T> HashSet<T> setIntersection(HashSet<T> A,HashSet<T> B){
        HashSet<T> C = new HashSet<>();
        C.addAll(A);
        C.retainAll(B);
        return C;
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
    @Override
    public void begin() {
        delfile(new File(this.dataStorePath));
    }

    @Override
    public void storeGraph(SampleGraph sampleGraph, int version) {




        HashSet<Integer> nodes  = sampleGraph.getNodes();
        HashSet<Pair<Integer,Integer>> rels = sampleGraph.getRels();
        String dataBasePath = String.format("%s//server%d//server",dataStorePath, version);
        new File(String.format("%s//server%d//",dataStorePath,version)).mkdir();
        String deltaFileName = String.format("%s//server%d//del.del",dataStorePath, version);
        //new File(deltaFileName)

        System.out.println(String.format("version: %d   dataPath:%s",version,dataBasePath));

        if(tempGraph == null){
            writeDataToDataBase(sampleGraph,dataBasePath);
            realStorePath.put(version,dataBasePath);
        }
        else {
            if(version % 3 == 0){
                writeDataToDataBase(sampleGraph,dataBasePath);
                realStorePath.put(version,dataBasePath);
            }
            HashSet<Integer> oldNodes = tempGraph.getNodes();
            HashSet<Pair<Integer, Integer>> oldRels = tempGraph.getRels();
            HashSet<Integer> nodesAdd = setDifference(nodes, oldNodes);
            HashSet<Integer> nodesDel = setDifference(oldNodes, nodes);
            HashSet<Pair<Integer, Integer>> relsAdd = setDifference(rels, oldRels);
            HashSet<Pair<Integer, Integer>> relsDel = setDifference(oldRels, rels);
            writeDeltaToFile(nodesAdd, nodesDel, relsAdd, relsDel, deltaFileName);
            deltaStorePath.put(version,deltaFileName);
        }
        tempGraph =sampleGraph;

    }

    @Override
    public void finish() {
        String realStoreFileName = new File(this.dataStorePath,"realStore.del").getAbsolutePath();
        String deltaStoreFileName = new File(this.dataStorePath,"deltaStore.del").getAbsolutePath();
        writeVersionPath(realStorePath,realStoreFileName);
        writeVersionPath(deltaStorePath,deltaStoreFileName);

    }

    void writeVersionPath(HashMap<Integer,String> version_path,String fileNameDelta){
        //String fileNameDelta = new File(dirPath,"version_path.del").getAbsolutePath();
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


    void writeDataToDataBase(SampleGraph sg, String path){
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(path));
        HashMap<Integer,Integer> nodeIds = new HashMap<>();
        HashSet<Integer> nodes = sg.getNodes();
        HashSet<Pair<Integer,Integer>> rels = sg.getRels();
        Transaction tx = graphDb.beginTx();
        for(Integer integer: nodes){
            Node n = graphDb.createNode();
            String label = sg.getNodeLabel(integer);
            n.addLabel(()->label);
            n.setProperty("nodeId",integer);
            int nodeId = (int) n.getId();
            nodeIds.put(integer,nodeId);
        }
        tx.success();
        tx.close();

        tx = graphDb.beginTx();

        int cnt = 0;
        for(Pair<Integer, Integer> integerIntegerPair:rels){
            cnt = cnt + 1;
            Node n1 = graphDb.getNodeById(nodeIds.get(integerIntegerPair.getLeft()));
            Node n2 = graphDb.getNodeById(nodeIds.get(integerIntegerPair.getRight()));
            String rtype = sg.getRelationType(integerIntegerPair);
            Relationship r1 = n1.createRelationshipTo(n2,()->rtype);
           // Relationship r2 = n2.createRelationshipTo(n1,rtype);
            if(cnt>=100000){
                 cnt = 0;
                 tx.success();
                 tx.close();
                 tx = graphDb.beginTx();
            }
        }
        tx.success();
        tx.close();
        graphDb.shutdown();
    }


    void writeDeltaToFile(HashSet<Integer> nodesAdd,HashSet<Integer> nodesDel,HashSet<Pair<Integer,Integer>> relsAdd,HashSet<Pair<Integer,Integer>> relsdel,String fileNameDelta){



        System.out.println(fileNameDelta);
        //String fileNameDel = new File(path,"del.del").getName();
        try {
            RandomAccessFile fp = new RandomAccessFile(fileNameDelta, "rw");
            fp.writeInt(nodesAdd.size());
            nodesAdd.forEach(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) {
                    try {
                        fp.writeInt(integer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            fp.writeInt(nodesDel.size());
            nodesDel.forEach(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) {
                    try {
                        fp.writeInt(integer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            fp.writeInt(relsAdd.size());
            relsAdd.forEach(new Consumer<Pair<Integer, Integer>>() {
                @Override
                public void accept(Pair<Integer, Integer> integerIntegerPair) {
                    int left = integerIntegerPair.getLeft();
                    int right = integerIntegerPair.getRight();
                    try {
                        fp.writeInt(left);
                        fp.writeInt(right);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            fp.writeInt(relsdel.size());
            relsdel.forEach(new Consumer<Pair<Integer, Integer>>() {
                @Override
                public void accept(Pair<Integer, Integer> integerIntegerPair) {
                    int left = integerIntegerPair.getLeft();
                    int right = integerIntegerPair.getRight();
                    try {
                        fp.writeInt(left);
                        fp.writeInt(right);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            fp.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



}
