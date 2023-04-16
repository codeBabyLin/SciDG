package DataGenerater;

//import org.imis.generator.EvoGenerator_new;

//import imis.generator.EvoGenerator_new;

import org.imis.generator.EvoGenerator_new;

import java.util.ArrayList;

public class BootStrap {

    //-univ 2 -index 0 -onto http://swat.cse.lehigh.edu/onto/univ-bench.owl -evo 0 -change 0.3 -versions 2 -schemaEvo 0.7
    public static void main(String[] args){
        ArrayList<String> params = new ArrayList<>();
        params.add("-univ");
        params.add("1");
        params.add("-index");
        params.add("0");
        params.add("-onto");
        params.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl");
        params.add("-evo");
        params.add("0");
        params.add("-change");
        params.add("0.3");
        params.add("-versions");
        params.add("26");

        params.add("-type");
        params.add("r");

        String[] ars = new String[params.size()];
        params.toArray(ars);
        //new EvoGenerator().start(ars);
        new EvoGenerator_new().start(ars);
    }


}
