package org.imis.generator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RdfGenerator extends BasicGenerator{
    void startSectionCB(int classType) {
        instances_[classType].logNum++;
        instances_[classType].logTotal++;
    }

    void startAboutSectionCB(int classType) {
        startSectionCB(classType);
    }

    /**
     * Callback by the writer when it adds a property statement.
     * @param property Type of the property.
     */
    void addPropertyCB(int property) {
        properties_[property].logNum++;
        properties_[property].logTotal++;
    }

    /**
     * Callback by the writer when it adds a property statement whose value is an individual.
     * @param classType Type of the individual.
     */
    void addValueClassCB(int classType) {
        instances_[classType].logNum++;
        instances_[classType].logTotal++;
    }

    public void start(int univNum, int startIndex, int seed, boolean daml,
                      String ontology,boolean evo,int evoVersions,double evoChange,double strict){
        this.evoChange = evoChange;
        this.evo = evo;
        this.evoVersions = evoVersions;
        this.strict = strict;

        this.ontology = ontology;

        this.isDaml_ = daml;
        if (daml)
            this.writer_ = new DamlWriter(this);
        else
            this.writer_ = new OwlWriter(this);

        this.writer_log = new OwlWriter(this);
        this.startIndex_ = startIndex;
        this.baseSeed_ = seed;
        this.instances_[CS_C_UNIV].num = univNum;
        this.instances_[CS_C_UNIV].count = startIndex;
        //this.evo = evo;

        evo = false;
        if(!evo){
            _generate();
            System.out.println("See log.txt for more details.");
            return;
        }

        _generate(evoVersions);

        System.out.println("See log.txt for more details.");

    }


    private void _generate() {
        System.out.println("Started...");
        try {
            log_ = new PrintStream(new FileOutputStream(System.getProperty("user.dir") +
                    "\\" + LOG_FILE));
            //writer_.start();
            for (int i = 0; i < instances_[CS_C_UNIV].num; i++) {
                _generateUniv(i + startIndex_);
            }

            //writer_.end();
            log_.close();
        }
        catch (IOException e) {
            System.out.println("Failed to create log file!");
        }
        System.out.println("Completed!");
    }

    private void _generate(int versions) {



        _generate(); //V0

        File[] files = new File(System.getProperty("user.dir")).listFiles();
        Model model = ModelFactory.createDefaultModel();
        //model.set
        for(File file : files){
            if(!file.getName().contains("owl")) continue;
            try{

                FileInputStream in = new FileInputStream(file);
                model.read(in, "http://example.com", "RDF/XML");
                in.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        long startingSize = model.size();
        model.close();
        double desSize = startingSize*(1+evoChange);
        int schemaEvol = (int)(strict*10);
        int schemaEvol2 = schemaEvol / (evoVersions-1);
        System.out.println("schema evol param: " + schemaEvol2);
        int howManyDepts = (int) Math.floor(totalDeptsV0*evoChange);
        HashMap<Integer, String> newClasses = new HashMap<Integer, String>();
        for(int i = 0; i < schemaEvol; i++)
            newClasses.put(i,"");

        double evoChangeOriginal = evoChange;
        boolean pub = false, conf = false, journ = false, tech = false,
                book = false, thes = false, proj = false, even = false;
        HashMap<String, HashMap<Integer, Double>> initialWeightsMap = new HashMap<String, HashMap<Integer,Double>>();
        for(String key : fileWeightsMap.keySet()){
            HashMap<Integer, Double> innerClone = new HashMap<Integer, Double>();
            for(Integer innerKey : fileWeightsMap.get(key).keySet()){
                innerClone.put(innerKey, fileWeightsMap.get(key).get(innerKey));
            }
            initialWeightsMap.put(key, innerClone);
        }
        for(int vi = 0 ; vi < evoVersions-1; vi++){

            globalVersionTrigger = true;
            File dir = new File(System.getProperty("user.dir")+"/v"+vi);
            dir.mkdirs();
            //assignFilters(classFilters);
            //System.out.println("current filters: " + currentFilters.toString());
            int classForChange ;

            //the number of depts (files) to evolve, based on the defined evoChange parameter

            List<String> asList = new ArrayList<String>(fileWeightsMap.keySet());

            writer_log = new OwlWriter(this);
            writer_log.startLogFile(dir.getAbsolutePath()+"/changes.rdf");
            writer_log.start();
            for(int d = 0 ; d < howManyDepts ; d++){

                //System.out.println("aslist: " + asList.size());
                if(asList.size() == 0) break;
                String randomFile = asList.get(random_.nextInt(asList.size()));
                instances_ = fileInstanceMap.get(randomFile);

                asList.remove(randomFile);

                //System.out.println("Selected file: " + randomFile);
                writer_ = new OwlWriter(this);

                writer_.startFile(dir.getAbsolutePath()+"/"+randomFile);
                writer_.start();
                for(Integer nextClass : fileWeightsMap.get(randomFile).keySet()){

                    classForChange = nextClass;

                    if(classForChange < 2) {
                        continue;
                    }

                    if(classForChange == 4) continue;
                    int totalIter = (int) Math.floor(2*initialWeightsMap.get(randomFile).get(classForChange))/howManyDepts;

                    for(int i = 0; i < totalIter ; i++){

                        _generateASection(classForChange,
                                instances_[classForChange].count );
                    }

                    if(nextClass == 21 ){
                        writer_log.addTypeClass(ontology+"WebCourse");
                        writer_log.addSuperClass(ontology+"WebCourse", ontology+"Course");
                    }
                    else if(nextClass == 20 ){
                        writer_log.addTypeClass(ontology+"VisitingStudent");
                        writer_log.addSuperClass(ontology+"VisitingStudent", ontology+"Student");
                    }
                    else if(nextClass == 19 ){
                        writer_log.addTypeClass(ontology+"VisitingProfessor");
                        writer_log.addSuperClass(ontology+"VisitingProfessor", ontology+"Professor");
                    }

                }

                for(int k = 0; k < schemaEvol2+1; k++){

                    if(newClasses.isEmpty()) break;
                    int newClass = _getRandomFromRange(0, newClasses.keySet().size()+1);
                    int index = 0;
                    for(Integer s : newClasses.keySet()){
                        if(index == newClass){
                            newClass = s;
                            break;
                        }
                        index++;
                    }
	              	/*if(newClass == 1){
	                  	_generatePublications();
	                  	newClasses.remove(newClass);
	                 }*/
                    if(newClass == 2){
                        _generateConferencePublications();
                        newClasses.remove(newClass);
                        writer_log.addTypeClass(ontology+"ConferencePublication");
                        writer_log.addSuperClass(ontology+"ConferencePublication", ontology+"Publication");
                    }
                    if(newClass == 3){
                        _generateJournalPublications();
                        newClasses.remove(newClass);
                        writer_log.addTypeClass(ontology+"JournalArticle");
                        writer_log.addSuperClass(ontology+"JournalArticle", ontology+"Publication");
                    }
                    if(newClass == 4 ){
                        _generateTechnicalReports();
                        newClasses.remove(newClass);
                        writer_log.addTypeClass(ontology+"TechnicalReport");
                        writer_log.addSuperClass(ontology+"TechnicalReport", ontology+"Publication");
                    }
                    if(newClass == 5 ){
                        _generateBooks();
                        newClasses.remove(newClass);
                        writer_log.addTypeClass(ontology+"Book");
                        writer_log.addSuperClass(ontology+"Book", ontology+"Publication");
                    }
                    if(newClass == 6 ){
                        _generateThesis();
                        newClasses.remove(newClass);
                        writer_log.addTypeClass(ontology+"Thesis");
                        writer_log.addSuperClass(ontology+"Thesis", ontology+"Publication");
                    }
                    if(newClass == 7 ){
                        _generateProjects();
                        newClasses.remove(newClass);
                        writer_log.addTypeClass(ontology+"Project");
                    }
                    if(newClass == 8){
                        _generateEvents();
                        newClasses.remove(newClass);
                        writer_log.addTypeClass(ontology+"Event");
                    }
                }

                writer_.end();
                writer_.endFile();
                //correction
                //remainingUnderCourses_.
                remainingUnderCourses_.clear();

                for (int i = 0; i < UNDER_COURSE_NUM + (int) (UNDER_COURSE_NUM*evoChange); i++) {
                    remainingUnderCourses_.add(new Integer(i));
                }
                remainingGradCourses_.clear();
                for (int i = 0; i < GRAD_COURSE_NUM + (int) (GRAD_COURSE_NUM*evoChange); i++) {
                    remainingGradCourses_.add(new Integer(i));
                }
                remainingWebCourses_.clear();
                for (int i = 0; i < WEB_COURSE_NUM + (int) (WEB_COURSE_NUM*evoChange); i++) {
                    remainingWebCourses_.add(new Integer(i));
                }

                assignWeights(randomFile);

            }
            evoChange = evoChange + evoVersions*evoChangeOriginal*evoChangeOriginal;

            writer_log.end();
            writer_log.endLogFile();


        }


    }



    private void _generateUniv(int index) {
        //this transformation guarantees no different pairs of (index, baseSeed) generate the same data
        seed_ = baseSeed_ * (Integer.MAX_VALUE + 1) + index;
        random_.setSeed(seed_);

        //determine department number
        instances_[CS_C_DEPT].num = _getRandomFromRange(DEPT_MIN, DEPT_MAX);
        instances_[CS_C_DEPT].count = 0;
        //generate departments
        //if(!evo){

        for (int i = 0; i < instances_[CS_C_DEPT].num; i++) {
            //System.out.println("index: " + i);
            _generateDept(index, i);
            String fileName = "University"+index+"_"+i+".owl";
            assignWeights(fileName);

        }

    }


}
