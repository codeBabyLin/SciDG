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
import java.util.Map;

public class VersionGraphOperationImplSP extends VersionGraphOperationDefaultImpl {

    String dataBasePath;
    private HashMap<Integer,GraphDatabaseService> graphdbs = new HashMap<>();
    public VersionGraphOperationImplSP(String dataPath){
        this.dataBasePath = dataPath;
        HashMap<Integer,String> version_path = new HashMap<>();
        readVersionPath(version_path,dataPath);
        for(Map.Entry map: version_path.entrySet()){
            int version = (int)map.getKey();
            String path = (String)map.getValue();

            GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(path));
            graphdbs.put(version,graphDb);
        }
    }

    void readVersionPath(HashMap<Integer,String> version_path, String dirPath){
        String fileNameDelta = new File(dirPath,"version_path.del").getAbsolutePath();
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

        }
    }

    @Override
    public SampleGraph querySingleVersion(SamepleGraphFilter samepleGraphFilter) {
        //String realPath = String.format("%s//server%d",dataBasePath,version);
        AtVersionFilter af = (AtVersionFilter) samepleGraphFilter.getOuterFilter();
        int version = af.getVersion();
        GraphDatabaseService graphDb = this.graphdbs.get(version);
        Transaction tx = graphDb.beginTx();
        SampleGraph sg = new SampleGraph();
        graphDb.getAllNodes().forEach(node -> {
            int rowId = (int) node.getProperty("nodeId");
            //int name = (int) node.getProperty("name");
            //nodes.add(name);
            sg.addNode(rowId,null,null);
        });
        graphDb.getAllRelationships().forEach(relationship -> {
            Node startNode =  relationship.getStartNode();
            Node endNode = relationship.getEndNode();
            int startName = (int) startNode.getProperty("nodeId");
            int endName = (int) endNode.getProperty("nodeId");
            //int id1 = (int) startNode.getId();
            //int id2 = (int) endNode.getId();
            //rels.add(Pair.of(startName,endName));
            sg.addRel(Pair.of(startName,endName),null,null);
        });
        tx.failure();
        tx.close();
        //graphDb.shutdown();
        return sg;
    }
}
