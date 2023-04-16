package OperationImpl;

import filters.AtVersionFilter;
import operation.SamepleGraphFilter;
import operation.VersionGraphOperationDefaultImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import sampleGraph.SampleGraph;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class VersionGraphOperationImplDP extends VersionGraphOperationDefaultImpl {


    private String dataStorePath;
    private HashMap<Integer,String> realStore_path;
    private HashMap<Integer,String> deltaStore_path;
    private HashMap<Integer, GraphDatabaseService> graphdbs = new HashMap<>();
    public VersionGraphOperationImplDP(String dataStorePath){
        this.dataStorePath = dataStorePath;
        String realStoreFileName = new File(this.dataStorePath,"realStore.del").getAbsolutePath();
        String deltaStoreFileName = new File(this.dataStorePath,"deltaStore.del").getAbsolutePath();
        this.realStore_path = new HashMap<>();
        this.deltaStore_path = new HashMap<>();
        readVersionPath(realStore_path,realStoreFileName);
        readVersionPath(deltaStore_path,deltaStoreFileName);
        for(Map.Entry map: realStore_path.entrySet()){
            int version = (int)map.getKey();
            String path = (String)map.getValue();
            GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(path));
            graphdbs.put(version,graphDb);
        }

    }

    void readVersionPath(HashMap<Integer,String> version_path, String fileNameDelta){
        //String fileNameDelta = new File(dirPath,"version_path.del").getAbsolutePath();
        System.out.println(fileNameDelta);
        try {
            RandomAccessFile fp = new RandomAccessFile(fileNameDelta, "r");
            int size = fp.readInt();
            for(int i = 0;i<size;i++){
                int key = fp.readInt();
                String value = fp.readUTF();
                version_path.put(key,value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public SampleGraph querySingleVersion(SamepleGraphFilter samepleGraphFilter) {
        AtVersionFilter af = (AtVersionFilter) samepleGraphFilter.getOuterFilter();
        int version = af.getVersion();
        SampleGraph sg = new SampleGraph();
        if(realStore_path.get(version)!=null){
            GraphDatabaseService graphDb = graphdbs.get(version);
            readDataToGraph(sg,graphDb);
        }
        else{
            if(realStore_path.get(version+1)!=null){
                GraphDatabaseService graphDb = graphdbs.get(version+1);
                readDataToGraph(sg,graphDb);
                String deltaFileName = deltaStore_path.get(version+1);
                undoDelta(sg,deltaFileName);
            }
            else{
                if(realStore_path.get(version-1)!=null){
                    GraphDatabaseService graphDb = graphdbs.get(version-1);
                    readDataToGraph(sg,graphDb);
                    String deltaFileName = deltaStore_path.get(version);
                    redoDelta(sg,deltaFileName);
                }
                else{
                    GraphDatabaseService graphDb = graphdbs.get(version-2);
                    readDataToGraph(sg,graphDb);
                    String deltaFileName = deltaStore_path.get(version-1);
                    redoDelta(sg,deltaFileName);
                    deltaFileName = deltaStore_path.get(version);
                    redoDelta(sg,deltaFileName);
                }
            }
        }
        return sg;
    }


    private void readDataToGraph(SampleGraph sg, GraphDatabaseService graphDb){
        Transaction tx = graphDb.beginTx();
        graphDb.getAllNodes().forEach(node -> {
            int rowId = (int) node.getProperty("nodeId");
            sg.addNode(rowId,null,null);
        });
        graphDb.getAllRelationships().forEach(relationship -> {
            Node startNode =  relationship.getStartNode();
            Node endNode = relationship.getEndNode();
            int startName = (int) startNode.getProperty("nodeId");
            int endName = (int) endNode.getProperty("nodeId");
            sg.addRel(Pair.of(startName,endName),null,null);
        });
        tx.failure();
        tx.close();
    }

    private void readDeltaFromFile(HashSet<Integer> nodesAdd, HashSet<Integer> nodesDel, HashSet<Pair<Integer,Integer>> relsAdd, HashSet<Pair<Integer,Integer>> relsdel, String fileNameDelta) {
        //String fileNameDelta = new File(path,"delta.del").getAbsolutePath();
        int size;
        try {
            RandomAccessFile fp = new RandomAccessFile(fileNameDelta, "r");
            size = fp.readInt();
            for (int i = 0; i < size; i++) {
                nodesAdd.add(fp.readInt());
            }
            size = fp.readInt();
            for (int i = 0; i < size; i++) {
                nodesDel.add(fp.readInt());
            }
            size = fp.readInt();
            for (int i = 0; i < size; i++) {
                int n1 = fp.readInt();
                int n2 = fp.readInt();
                relsAdd.add(Pair.of(n1, n2));
                relsAdd.add(Pair.of(n2, n1));
            }
            size = fp.readInt();
            for (int i = 0; i < size; i++) {
                int n1 = fp.readInt();
                int n2 = fp.readInt();
                relsdel.add(Pair.of(n1, n2));
                relsdel.add(Pair.of(n2, n1));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void redoDelta(SampleGraph sg, String deltaFileName){
        HashSet<Integer> nodesAdd = new HashSet<>();
        HashSet<Integer> nodesDel = new HashSet<>();
        HashSet<Pair<Integer,Integer>> relsAdd = new HashSet<>();
        HashSet<Pair<Integer,Integer>> relsdel = new HashSet<>();
        readDeltaFromFile(nodesAdd,nodesDel,relsAdd,relsdel,deltaFileName);
        sg.addNodes(nodesAdd,new HashMap<>(),new HashMap<>());
        sg.delNodes(nodesDel);
        sg.addRels(relsAdd,new HashMap<>(),new HashMap<>());
        sg.delRels(relsdel);
    }
    private void undoDelta(SampleGraph sg, String deltaFileName){
        HashSet<Integer> nodesAdd = new HashSet<>();
        HashSet<Integer> nodesDel = new HashSet<>();
        HashSet<Pair<Integer,Integer>> relsAdd = new HashSet<>();
        HashSet<Pair<Integer,Integer>> relsdel = new HashSet<>();
        readDeltaFromFile(nodesAdd,nodesDel,relsAdd,relsdel,deltaFileName);
        sg.addNodes(nodesDel,new HashMap<>(),new HashMap<>());
        sg.delNodes(nodesAdd);
        sg.addRels(relsdel,new HashMap<>(),new HashMap<>());
        sg.delRels(relsAdd);
    }

}
