package university;

//import dppin.DPPINQueryGenerator;
import dataConfig.QueryGenerator;
import dataConfig.QueryGeneratorConfig;

public class UniversityQueryGenerator {

    public static void main(String[]args){
        QueryGeneratorConfig qgc = new UniversityDataTestConfig();
        QueryGenerator qg = new QueryGenerator(qgc);
        qg.generate();
    }
}
