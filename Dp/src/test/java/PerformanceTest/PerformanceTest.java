package PerformanceTest;


//import OperationImpl.VersionGraphOperationImplTP;
import OperationImpl.VersionGraphOperationImplDP;
import coautor.CoauthorDataStore;
import coautor.CoauthorDataTestConfig;
import dataConfig.QueryGenerator;
import dataConfig.QueryTest;
import dataConfig.QueryTestImpl;
import dppin.DPPINDataStore;
import dppin.PPINDataTestConfig;
import operation.VersionGraphOperation;
import operation.VersionGraphStore;
import university.UniversityDataStore;
import university.UniversityDataTestConfig;

public class PerformanceTest {

    //String coauthorPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Dp\\author";
    //String ppinPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Dp\\ppin";
    //String univerPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Dp\\univer";

    private CoauthorDataStore cds = new CoauthorDataStore();
    private DPPINDataStore dds = new DPPINDataStore();
    private UniversityDataStore uds = new UniversityDataStore(26);
    public PerformanceTest(){
        CoauthorDataStore cds = new CoauthorDataStore();
        DPPINDataStore dds = new DPPINDataStore();
        UniversityDataStore uds = new UniversityDataStore(26);
    }

    public void storeCoauthor(VersionGraphStore vgs){
        cds.storeVersionGraph(vgs);
    }

    public void storeDPPIN(VersionGraphStore vgs){
        dds.storeVersionGraph(vgs);
    }

    public void storeUniversity(VersionGraphStore vgs){
        uds.storeVersionGraph(vgs);
    }

    public void testAuthor(VersionGraphOperation vgo){
        CoauthorDataTestConfig qgc = new CoauthorDataTestConfig();
        QueryGenerator qg = new QueryGenerator(qgc);
        QueryTest qt = new QueryTestImpl(qg,vgo);
        qt.testAll();
    }
    public void testPPIN(VersionGraphOperation vgo){
        PPINDataTestConfig qgc = new PPINDataTestConfig();
        QueryGenerator qg = new QueryGenerator(qgc);
        QueryTest qt = new QueryTestImpl(qg,vgo);
        qt.testAll();
    }

    public void testUniversity(VersionGraphOperation vgo){
        UniversityDataTestConfig qgc = new UniversityDataTestConfig();
        QueryGenerator qg = new QueryGenerator(qgc);
        QueryTest qt = new QueryTestImpl(qg,vgo);
        qt.testAll();
    }



    public static void main(String[] args){

        PerformanceTest pft = new PerformanceTest();

        String coauthorPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Dp\\author";
        String ppinPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Dp\\ppin";
        String univerPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Dp\\univer";

        //String path = coauthorPath;
        String path = ppinPath;
        //String path = univerPath;

        //VersionGraphStore vgs = new VersionGraphStoreImplDP(path);
        //pft.storeCoauthor(vgs);
        //pft.storeDPPIN(vgs);
        //pft.storeUniversity(vgs);



        VersionGraphOperation vgo = new VersionGraphOperationImplDP(path);
        //pft.testAuthor(vgo);
        pft.testPPIN(vgo);
       // pft.testUniversity(vgo);


    }
}
