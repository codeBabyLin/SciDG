package dppin;

import dataConfig.QueryGenerator;
import dataConfig.QueryGeneratorConfig;

public class QueryGeneratorRun {
    public static void main(String[]args){
        QueryGeneratorConfig qgc = new PPINDataTestConfig();
        QueryGenerator qg = new QueryGenerator(qgc);
        qg.generate();
    }
}
