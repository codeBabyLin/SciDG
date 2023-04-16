package org.imis.generator;

import java.io.File;

public class EvoGenerator_new {
    public  void start(String[] args) {
        int univNum = 1, startIndex = 0, seed = 0;
        boolean daml = false;
        boolean evo = false;
        int evoParadigm;
        double evoChange = 0;
        int evoVersions = 0;
        double strict = 0, step;
        int evoOnlyChanges;
        String type = "r";
        String tempDir = "./temp";
        String userDir = "./test";
        String ontology = null;
        try {
            String arg;
            int i = 0;
            while (i < args.length) {
                arg = args[i++];
                if (arg.equals("-univ")) {
                    if (i < args.length) {
                        arg = args[i++];
                        univNum = Integer.parseInt(arg);
                        if (univNum < 1)
                            throw new NumberFormatException();
                    } else
                        throw new NumberFormatException();
                } else if (arg.equals("-index")) {
                    if (i < args.length) {
                        arg = args[i++];
                        startIndex = Integer.parseInt(arg);
                        if (startIndex < 0)
                            throw new NumberFormatException();
                    } else
                        throw new NumberFormatException();
                } else if (arg.equals("-seed")) {
                    if (i < args.length) {
                        arg = args[i++];
                        seed = Integer.parseInt(arg);
                        if (seed < 0)
                            throw new NumberFormatException();
                    } else
                        throw new NumberFormatException();
                } else if (arg.equals("-daml")) {
                    daml = true;
                } else if (arg.equals("-onto")) {
                    if (i < args.length) {
                        arg = args[i++];
                        ontology = arg;
                    } else
                        throw new Exception();
                } else if (arg.equals("-evo")) {
                    if (i < args.length) {
                        arg = args[i++];
                        evo = true;
                        evoParadigm = Integer.parseInt(arg);
                    } else
                        throw new Exception();
                } else if (arg.equals("-change")) {
                    if (i < args.length) {
                        arg = args[i++];
                        evoChange = Double.parseDouble(arg);
                    } else
                        throw new Exception();
                } else if (arg.equals("-versions")) {
                    if (i < args.length) {
                        arg = args[i++];
                        evoVersions = Integer.parseInt(arg);
                    } else
                        throw new Exception();
                } else if (arg.equals("-schemaEvo")) {
                    if (i < args.length) {
                        arg = args[i++];
                        strict = Double.parseDouble(arg);
                        if (strict < 0 || strict > 1)
                            throw new Exception();
                        step = (double) (1 - strict) / (evoVersions - 1);
             	/*classFilters.add(CS_C_FULLPROF);
             	classFilters.add(CS_C_ASSOPROF);
             	classFilters.add(CS_C_ASSTPROF);
             	classFilters.add(CS_C_LECTURER);
             	classFilters.add(CS_C_UNDERSTUD);
             	classFilters.add(CS_C_GRADSTUD);
             	//classFilters.add(CS_C_COURSE);
             	classFilters.add(CS_C_GRADCOURSE);
             	classFilters.add(CS_C_RESEARCHGROUP);*/
                        System.out.println("step: " + step);
                    } else
                        throw new Exception();
                } else if (arg.equals("-onlyChanges")) {
                    if (i < args.length) {
                        arg = args[i++];
                        evoOnlyChanges = Integer.parseInt(arg);
                        if (evoOnlyChanges == 0)
                            new File(tempDir).mkdirs();
                    } else
                        throw new Exception();
                } else if (arg.equals("-dir")) {
                    if (i < args.length) {
                        arg = args[i++];
                        userDir = arg;
                    } else
                        throw new NumberFormatException();
                }
                else if(arg.equals("-type")){
                    if(i<args.length){
                        arg = args[i++];
                        type = arg;
                    }
                    else
                        throw new NumberFormatException();
                }
                else
                    throw new Exception();
            }
            if (((long) startIndex + univNum - 1) > Integer.MAX_VALUE) {
                System.err.println("Index overflow!");
                throw new Exception();
            }
            if (null == ontology) {
                System.err.println("ontology url is requested!");
                throw new Exception();
            }
        } catch (Exception e) {
            System.err.println("Usage: Generator\n" +
                    "\t[-univ <num of universities(1~" + Integer.MAX_VALUE +
                    ")>]\n" +
                    "\t[-index <start index(0~" + Integer.MAX_VALUE +
                    ")>]\n" +
                    "\t[-seed <seed(0~" + Integer.MAX_VALUE + ")>]\n" +
                    "\t[-daml]\n" +
                    "\t-onto <univ-bench ontology url>");
            System.exit(0);
        }

        //new RdfGenerator().start(univNum,startIndex,seed,daml,ontology,evo,evoVersions,evoChange,strict);
        new CsvGenerator().start(univNum,startIndex,seed,daml,ontology,evo,evoVersions,evoChange,type);

        new QueryGenerator().start(evoVersions);


    }
}
