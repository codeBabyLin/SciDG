package org.imis.generator;

import com.hp.hpl.jena.vocabulary.RDF;
import org.imis.generator.Infor.*;

import java.io.PrintStream;
import java.util.*;

public class BasicGenerator extends Constant {
    String ontology;
    /** (class) instance information */
    public InstanceCount[] instances_;
    /** property instance information */
    public PropertyCount[] properties_;
    /** data file writer */
    public Writer writer_;
    public Writer writer_log;
    /** generate DAML+OIL data (instead of OWL) */
    public boolean isDaml_;
    /** random number generator */
    public Random random_;
    /** seed of the random number genertor for the current university */
    public long seed_ = 0l;
    /** user specified seed for the data generation */
    public long baseSeed_ = 0l;
    /** list of undergraduate courses generated so far (in the current department) */
    public ArrayList underCourses_;
    /** list of graduate courses generated so far (in the current department) */
    public ArrayList gradCourses_;
    public ArrayList webCourses_;
    /** list of remaining available undergraduate courses (in the current department) */
    public ArrayList remainingUnderCourses_;
    /** list of remaining available graduate courses (in the current department) */
    public ArrayList remainingGradCourses_;
    public ArrayList remainingWebCourses_;
    /** list of publication instances generated so far (in the current department) */
    public ArrayList publications_;
    public ArrayList projects_;
    /** index of the full professor who has been chosen as the department chair */
    public int chair_;
    /** starting index of the universities */
    public int startIndex_;
    /** log writer */
    public PrintStream log_ = null;
    public boolean evo = false;
    public int evoParadigm = -1;
    public double evoChange = 0d;
    public int evoVersions = 0;
    public double strict = 0;
    public double step = 0;
    public int evoOnlyChanges = -1;
    public ArrayList<Integer> classFilters = new ArrayList<Integer>();
    public HashSet<Integer> currentFilters = new HashSet<Integer>();
    public String tempDir = System.getProperty("user.dir") + "/temp";
    public String userDir = System.getProperty("user.dir");
    public HashMap<Integer, Double> changeWeights = new HashMap<Integer, Double>();
    public HashMap<String, InstanceCount[]> fileInstanceMap = new HashMap<String, InstanceCount[]>();
    public HashMap<String, HashMap<Integer, Double>> fileWeightsMap = new HashMap<String, HashMap<Integer,Double>>();
    public int totalDeptsV0 = 0;

    public boolean globalVersionTrigger = false;

    public BasicGenerator(){
        instances_ = new InstanceCount[CLASS_NUM];
        for (int i = 0; i < CLASS_NUM; i++) {
            instances_[i] = new InstanceCount();
        }
        properties_ = new PropertyCount[PROP_NUM];
        for (int i = 0; i < PROP_NUM; i++) {
            properties_[i] = new PropertyCount();
        }

        random_ = new Random();
        underCourses_ = new ArrayList();
        gradCourses_ = new ArrayList();
        webCourses_ = new ArrayList();
        remainingUnderCourses_ = new ArrayList();
        remainingGradCourses_ = new ArrayList();
        remainingWebCourses_ = new ArrayList();
        publications_ = new ArrayList();
        projects_ = new ArrayList();
    }

    private void _setInstanceInfo() {
        int subClass, superClass;

        for (int i = 0; i < CLASS_NUM; i++) {
            switch (i) {
                case CS_C_UNIV:
                    break;
                case CS_C_DEPT:
                    break;
                case CS_C_FULLPROF:
                    instances_[i].num = _getRandomFromRange(FULLPROF_MIN, FULLPROF_MAX);
                    break;
                case CS_C_VISITINGPROF:
                    instances_[i].num = _getRandomFromRange(VISITINGPROF_MIN, VISITINGPROF_MAX);
                    break;
                case CS_C_ASSOPROF:
                    instances_[i].num = _getRandomFromRange(ASSOPROF_MIN, ASSOPROF_MAX);
                    break;
                case CS_C_ASSTPROF:
                    instances_[i].num = _getRandomFromRange(ASSTPROF_MIN, ASSTPROF_MAX);
                    break;
                case CS_C_LECTURER:
                    instances_[i].num = _getRandomFromRange(LEC_MIN, LEC_MAX);
                    break;
                case CS_C_UNDERSTUD:
                    instances_[i].num = _getRandomFromRange(R_UNDERSTUD_FACULTY_MIN *
                                    instances_[CS_C_FACULTY].total,
                            R_UNDERSTUD_FACULTY_MAX *
                                    instances_[CS_C_FACULTY].total);
                    break;
                case CS_C_VISITSTUD:
                    instances_[i].num = _getRandomFromRange(R_VISITSTUD_FACULTY_MIN *
                                    instances_[CS_C_FACULTY].total,
                            R_VISITSTUD_FACULTY_MAX *
                                    instances_[CS_C_FACULTY].total);
                    break;
                case CS_C_GRADSTUD:
                    instances_[i].num = _getRandomFromRange(R_GRADSTUD_FACULTY_MIN *
                                    instances_[CS_C_FACULTY].total,
                            R_GRADSTUD_FACULTY_MAX *
                                    instances_[CS_C_FACULTY].total);
                    break;
                case CS_C_TA:
                    instances_[i].num = _getRandomFromRange(instances_[CS_C_GRADSTUD].total /
                                    R_GRADSTUD_TA_MAX,
                            instances_[CS_C_GRADSTUD].total /
                                    R_GRADSTUD_TA_MIN);
                    break;
                case CS_C_RA:
                    instances_[i].num = _getRandomFromRange(instances_[CS_C_GRADSTUD].total /
                                    R_GRADSTUD_RA_MAX,
                            instances_[CS_C_GRADSTUD].total /
                                    R_GRADSTUD_RA_MIN);
                    break;
                case CS_C_RESEARCHGROUP:
                    instances_[i].num = _getRandomFromRange(RESEARCHGROUP_MIN, RESEARCHGROUP_MAX);
                    break;
                default:
                    instances_[i].num = CLASS_INFO[i][INDEX_NUM];
                    break;
            }
            instances_[i].total = instances_[i].num;
            subClass = i;
            while ( (superClass = CLASS_INFO[subClass][INDEX_SUPER]) != CS_C_NULL) {
                instances_[superClass].total += instances_[i].num;
                subClass = superClass;
            }
        }
    }

    public void _generateDept(int univIndex, int index) {
        totalDeptsV0++;
        String fileName = System.getProperty("user.dir") + "\\" +
                _getName(CS_C_UNIV, univIndex) + INDEX_DELIMITER + index + _getFileSuffix();
        writer_.startFile(fileName);

        //reset
        _setInstanceInfo();
        underCourses_.clear();
        gradCourses_.clear();
        remainingUnderCourses_.clear();
        remainingGradCourses_.clear();
        remainingWebCourses_.clear();
        for (int i = 0; i < UNDER_COURSE_NUM; i++) {
            remainingUnderCourses_.add(new Integer(i));
        }
        for (int i = 0; i < GRAD_COURSE_NUM; i++) {
            remainingGradCourses_.add(new Integer(i));
        }
        for (int i = 0; i < WEB_COURSE_NUM; i++) {
            remainingWebCourses_.add(new Integer(i));
        }
        publications_.clear();
        projects_.clear();
        for (int i = 0; i < CLASS_NUM; i++) {
            instances_[i].logNum = 0;
        }
        for (int i = 0; i < PROP_NUM; i++) {
            properties_[i].logNum = 0;
        }

        //decide the chair
        chair_ = random_.nextInt(instances_[CS_C_FULLPROF].total);

        if (index == 0) {
            _generateASection(CS_C_UNIV, univIndex);
        }
        _generateASection(CS_C_DEPT, index);
        for (int i = CS_C_DEPT + 1; i < CLASS_NUM; i++) {
            instances_[i].count = 0;
            for (int j = 0; j < instances_[i].num; j++) {
                _generateASection(i, j);
            }
        }

        _generatePublications();
   /* _generateConferencePublications();
    _generateJournalPublications();
    _generateTechnicalReports();
    _generateBooks();
    _generateThesis();
    _generateProjects();
    _generateEvents();*/
        _generateCourses();
        _generateRaTa();

        System.out.println(fileName + " generated");
        String bar = "";
        for (int i = 0; i < fileName.length(); i++)
            bar += '-';
        log_.println(bar);
        log_.println(fileName);
        log_.println(bar);
        _generateComments();
        writer_.endFile();
    }



    public void _generateASection(int classType, int index) {
        _updateCount(classType);

    /*if(classType != CS_C_UNIV && classType != CS_C_DEPT && classType != CS_C_FACULTY && classType != CS_C_PROF &&
    		 classType != CS_C_COURSE && !currentFilters.contains(classType))
    	return;*/

        switch (classType) {
            case CS_C_UNIV:
                _generateAUniv(index);
                break;
            case CS_C_DEPT:
                _generateADept(index);
                break;
            case CS_C_FACULTY:
                _generateAFaculty(index);
                break;
            case CS_C_PROF:
                _generateAProf(index);
                break;
            case CS_C_FULLPROF:
                _generateAFullProf(index);
                break;
            case CS_C_ASSOPROF:
                _generateAnAssociateProfessor(index);
                break;
            case CS_C_ASSTPROF:
                _generateAnAssistantProfessor(index);
                break;
            case CS_C_LECTURER:
                _generateALecturer(index);
                break;
            case CS_C_UNDERSTUD:
                _generateAnUndergraduateStudent(index);
                break;
            case CS_C_GRADSTUD:
                _generateAGradudateStudent(index);
                break;
            case CS_C_COURSE:
                _generateACourse(index);
                break;
            case CS_C_GRADCOURSE:
                _generateAGraduateCourse(index);
                break;
            case CS_C_WEBCOURSE:
                _generateAWebCourse(index);
                break;
            case CS_C_RESEARCHGROUP:
                _generateAResearchGroup(index);
                break;
            case CS_C_VISITINGPROF:
                _generateAVisitingProf(index);
                break;
            case CS_C_VISITSTUD:
                _generateAVisitingStudent(index);
                break;
            default:
                break;
        }
    }



    public void assignWeights(String fileName){



        InstanceCount[] thisInstCount = new InstanceCount[instances_.length];
        List<Integer> hello = new ArrayList<Integer>(changeWeights.keySet());
        Collections.sort(hello);
        for(int i = 0; i < instances_.length; i++){
            thisInstCount[i] = new InstanceCount();
            thisInstCount[i].count = instances_[i].count;
            thisInstCount[i].logNum = instances_[i].logNum;
            thisInstCount[i].logTotal = instances_[i].logTotal;
            thisInstCount[i].num = instances_[i].num;
            thisInstCount[i].total = instances_[i].total;
        }
      /*for(Integer nextClass : hello){
    	  thisInstCount[nextClass] = new InstanceCount();
    	  thisInstCount[nextClass].count = instances_[nextClass].count;
    	  thisInstCount[nextClass].logNum = instances_[nextClass].logNum;
    	  thisInstCount[nextClass].logTotal = instances_[nextClass].logTotal;
    	  thisInstCount[nextClass].num = instances_[nextClass].num;
    	  thisInstCount[nextClass].total = instances_[nextClass].total;

      	//System.out.println("int: "+ nextClass + ", weight: " + changeWeights.get(nextClass) + ", "
      		//	+ "count: " + instances_[nextClass].total);
      }*/

        fileInstanceMap.put(fileName, thisInstCount);
        changeWeights = new HashMap<Integer, Double>();
        changeWeights.put(CS_C_UNIV, Math.floor(instances_[CS_C_UNIV].num*evoChange));
        changeWeights.put(CS_C_DEPT, Math.floor(instances_[CS_C_DEPT].num*evoChange)*0.2);
        changeWeights.put(CS_C_FACULTY, Math.floor(instances_[CS_C_FACULTY].num*evoChange));
        changeWeights.put(CS_C_PROF, Math.floor(instances_[CS_C_PROF].num*evoChange));
        changeWeights.put(CS_C_FULLPROF, Math.floor(instances_[CS_C_FULLPROF].num*evoChange)*16);
        changeWeights.put(CS_C_ASSOPROF, Math.floor(instances_[CS_C_ASSOPROF].num*evoChange)*22);
        changeWeights.put(CS_C_ASSTPROF, Math.floor(instances_[CS_C_ASSTPROF].num*evoChange)*18);
        changeWeights.put(CS_C_LECTURER, Math.floor(instances_[CS_C_LECTURER].num*evoChange)*11);
        changeWeights.put(CS_C_UNDERSTUD, Math.floor(instances_[CS_C_UNDERSTUD].num*evoChange)*35);
        changeWeights.put(CS_C_GRADSTUD, Math.floor(instances_[CS_C_GRADSTUD].num*evoChange)*24);
        changeWeights.put(CS_C_COURSE, Math.floor(UNDER_COURSE_NUM*evoChange));//*20);
        changeWeights.put(CS_C_GRADCOURSE, Math.floor(GRAD_COURSE_NUM*evoChange));//*10.1);
        changeWeights.put(CS_C_RESEARCHGROUP, Math.floor(instances_[CS_C_RESEARCHGROUP].num*evoChange)*28);
        changeWeights.put(CS_C_VISITINGPROF, Math.floor(instances_[CS_C_VISITINGPROF].num*evoChange)*18);
        changeWeights.put(CS_C_VISITSTUD, Math.floor(instances_[CS_C_VISITSTUD].num*evoChange)*14);
        changeWeights.put(CS_C_PROJECT, Math.floor(instances_[CS_C_PROJECT].num*evoChange)*14);
        changeWeights.put(CS_C_WEBCOURSE, Math.floor(WEB_COURSE_NUM*evoChange));//*61);
        changeWeights.put(CS_C_PUBLICATION, Math.floor(instances_[CS_C_PUBLICATION].num*evoChange)*61);
        fileWeightsMap.put(fileName, changeWeights);
    }
    /**
     * Generates a university instance.
     * @param index Index of the instance.
     */
    private void _generateAUniv(int index) {
        writer_.startSection(CS_C_UNIV, _getId(CS_C_UNIV, index));
        writer_.addProperty(CS_P_NAME, _getRelativeName(CS_C_UNIV, index), false);
        writer_.endSection(CS_C_UNIV);
    }

    /**
     * Generates a department instance.
     * @param index Index of the department.
     */
    public void _generateADept(int index) {
        writer_.startSection(CS_C_DEPT, _getId(CS_C_DEPT, index));
        writer_.addProperty(CS_P_NAME, _getRelativeName(CS_C_DEPT, index), false);
        writer_.addProperty(CS_P_SUBORGANIZATIONOF, CS_C_UNIV,
                _getId(CS_C_UNIV, instances_[CS_C_UNIV].count - 1));
        writer_.endSection(CS_C_DEPT);
    }

    /**
     * Generates a faculty instance.
     * @param index Index of the faculty.
     */
    private void _generateAFaculty(int index) {
        writer_.startSection(CS_C_FACULTY, _getId(CS_C_FACULTY, index));
        _generateAFaculty_a(CS_C_FACULTY, index, _getId(CS_C_FACULTY, index));
        writer_.endSection(CS_C_FACULTY);
    }

    /**
     * Generates properties for the specified faculty instance.
     * @param type Type of the faculty.
     * @param index Index of the instance within its type.
     */
    private void _generateAFaculty_a(int type, int index, String id) {
        int indexInFaculty;
        int courseNum;
        int courseIndex;
        boolean dup;
        CourseInfo course;

        indexInFaculty = instances_[CS_C_FACULTY].count - 1;

        writer_.addProperty(CS_P_NAME, _getRelativeName(type, index), false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#name", _getRelativeName(type, index), false );
        }
        //undergradutate courses
        courseNum = _getRandomFromRange(FACULTY_COURSE_MIN, FACULTY_COURSE_MAX);
        for (int i = 0; i < courseNum; i++) {
            courseIndex = _AssignCourse(indexInFaculty);
            writer_.addProperty(CS_P_TEACHEROF, _getId(CS_C_COURSE, courseIndex), true);
            if(globalVersionTrigger){
                writer_log.addPropertyInstance(id, ontology+"#teacherOf", _getId(CS_C_COURSE, courseIndex), true );
            }
        }
        //gradutate courses
        courseNum = _getRandomFromRange(FACULTY_GRADCOURSE_MIN, FACULTY_GRADCOURSE_MAX);
        for (int i = 0; i < courseNum; i++) {
            courseIndex = _AssignGraduateCourse(indexInFaculty);
            writer_.addProperty(CS_P_TEACHEROF, _getId(CS_C_GRADCOURSE, courseIndex), true);
            if(globalVersionTrigger){
                writer_log.addPropertyInstance(id, ontology+"#teacherOf", _getId(CS_C_GRADCOURSE, courseIndex), true );
            }
        }
        for (int i = 0; i < courseNum; i++) {
            courseIndex = _AssignWebCourse(indexInFaculty);
            writer_.addProperty(CS_P_TEACHEROF, _getId(CS_C_WEBCOURSE, courseIndex), true);
            if(globalVersionTrigger){
                writer_log.addPropertyInstance(id, ontology+"#teacherOf", _getId(CS_C_WEBCOURSE, courseIndex), true );
            }
        }
        //person properties
        String n = _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM));
        writer_.addProperty(CS_P_UNDERGRADFROM, CS_C_UNIV, n);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#undergraduateDegreeFrom", n, true );
        }
        n = _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM));
        writer_.addProperty(CS_P_GRADFROM, CS_C_UNIV,
                n);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#mastersDegreeFrom", n, true );
        }
        n = _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM));
        writer_.addProperty(CS_P_DOCFROM, CS_C_UNIV,
                n);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#doctoralDegreeFrom", n, true );
        }
        writer_.addProperty(CS_P_WORKSFOR,
                _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#worksFor",  _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true );
        }
        writer_.addProperty(CS_P_EMAIL, _getEmail(type, index), false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#email",  _getEmail(type, index), false );
        }
        writer_.addProperty(CS_P_TELEPHONE, "xxx-xxx-xxxx", false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#telephone",  "xxx-xxx-xxxx", false );
        }
    }

    /**
     * Assigns an undergraduate course to the specified faculty.
     * @param indexInFaculty Index of the faculty.
     * @return Index of the selected course in the pool.
     */
    private int _AssignCourse(int indexInFaculty) {
        //NOTE: this line, although overriden by the next one, is deliberately kept
        // to guarantee identical random number generation to the previous version.
        int pos = _getRandomFromRange(0, remainingUnderCourses_.size() - 1);
        pos = 0; //fetch courses in sequence

        CourseInfo course = new CourseInfo();
        course.indexInFaculty = indexInFaculty;
        course.globalIndex = ( (Integer) remainingUnderCourses_.get(pos)).intValue();
        underCourses_.add(course);

        remainingUnderCourses_.remove(pos);

        return course.globalIndex;
    }

    /**
     * Assigns a graduate course to the specified faculty.
     * @param indexInFaculty Index of the faculty.
     * @return Index of the selected course in the pool.
     */
    private int _AssignGraduateCourse(int indexInFaculty) {
        //NOTE: this line, although overriden by the next one, is deliberately kept
        // to guarantee identical random number generation to the previous version.
        int pos = _getRandomFromRange(0, remainingGradCourses_.size() - 1);
        //int pos = _getRandomFromRange(0, remainingGradCourses_.size() );
        pos = 0; //fetch courses in sequence

        CourseInfo course = new CourseInfo();
        course.indexInFaculty = indexInFaculty;
        course.globalIndex = ( (Integer) remainingGradCourses_.get(pos)).intValue();
        gradCourses_.add(course);

        remainingGradCourses_.remove(pos);

        return course.globalIndex;
    }

    private int _AssignWebCourse(int indexInFaculty) {
        //NOTE: this line, although overriden by the next one, is deliberately kept
        // to guarantee identical random number generation to the previous version.
        int pos = _getRandomFromRange(0, remainingWebCourses_.size() - 1);
        pos = 0; //fetch courses in sequence

        CourseInfo course = new CourseInfo();
        course.indexInFaculty = indexInFaculty;
        course.globalIndex = ( (Integer) remainingWebCourses_.get(pos)).intValue();
        webCourses_.add(course);

        remainingWebCourses_.remove(pos);

        return course.globalIndex;
    }

    /**
     * Generates a professor instance.
     * @param index Index of the professor.
     */
    private void _generateAProf(int index) {
        writer_.startSection(CS_C_PROF, _getId(CS_C_PROF, index));
        _generateAProf_a(CS_C_PROF, index, _getId(CS_C_PROF, index));
        writer_.endSection(CS_C_PROF);
    }

    /**
     * Generates properties for a professor instance.
     * @param type Type of the professor.
     * @param index Index of the intance within its type.
     */
    private void _generateAProf_a(int type, int index, String id) {
        _generateAFaculty_a(type, index, id);
        String ri = _getRelativeName(CS_C_RESEARCH,
                random_.nextInt(RESEARCH_NUM));
        writer_.addProperty(CS_P_RESEARCHINTEREST,
                ri , false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#researchInterest", ri, false );
        }
    }

    /**
     * Generates a full professor instances.
     * @param index Index of the full professor.
     */
    private void _generateAFullProf(int index) {
        String id;

        id = _getId(CS_C_FULLPROF, index);
        writer_.startSection(CS_C_FULLPROF, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#FullProfessor", true);
        }
        _generateAProf_a(CS_C_FULLPROF, index, id);
        if (index == chair_) {
            writer_.addProperty(CS_P_HEADOF,
                    _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
            if(globalVersionTrigger){
                writer_log.addPropertyInstance(id, ontology+"#headOf", _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
            }
        }
        writer_.endSection(CS_C_FULLPROF);
        _assignFacultyPublications(id, FULLPROF_PUB_MIN, FULLPROF_PUB_MAX);
    }

    /**
     * Generates a full professor instances.
     * @param index Index of the full professor.
     */
    private void _generateAVisitingProf(int index) {
        String id;

        id = _getId(CS_C_VISITINGPROF, index);
        writer_.startSection(CS_C_VISITINGPROF, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#VisitingProfessor", true);
        }
        _generateAProf_a(CS_C_VISITINGPROF, index, id);
        writer_.addProperty(CS_P_VISITSASPROF, CS_C_UNIV,
                _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)));
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#visitsAsProfessor", _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)), true);
        }
        String n = _getRandomFromRange(1, 10)+" month(s)";
        writer_.addProperty(CS_P_VISITDURATION, n ,false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#visitDuration", n, false);
        }
        writer_.endSection(CS_C_VISITINGPROF);

    }

    /**
     * Generates an associate professor instance.
     * @param index Index of the associate professor.
     */
    private void _generateAnAssociateProfessor(int index) {
        String id = _getId(CS_C_ASSOPROF, index);
        writer_.startSection(CS_C_ASSOPROF, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#AssociateProfessor", true);
        }
        _generateAProf_a(CS_C_ASSOPROF, index, id);
        writer_.endSection(CS_C_ASSOPROF);
        _assignFacultyPublications(id, ASSOPROF_PUB_MIN, ASSOPROF_PUB_MAX);

    }

    /**
     * Generates an assistant professor instance.
     * @param index Index of the assistant professor.
     */
    private void _generateAnAssistantProfessor(int index) {
        String id = _getId(CS_C_ASSTPROF, index);
        writer_.startSection(CS_C_ASSTPROF, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#AssistantProfessor", true);
        }
        _generateAProf_a(CS_C_ASSTPROF, index, id);
        writer_.endSection(CS_C_ASSTPROF);
        _assignFacultyPublications(id, ASSTPROF_PUB_MIN, ASSTPROF_PUB_MAX);
    }

    /**
     * Generates a lecturer instance.
     * @param index Index of the lecturer.
     */
    private void _generateALecturer(int index) {
        String id = _getId(CS_C_LECTURER, index);
        writer_.startSection(CS_C_LECTURER, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#Lecturer", true);
        }
        _generateAFaculty_a(CS_C_LECTURER, index, id);
        writer_.endSection(CS_C_LECTURER);
        _assignFacultyPublications(id, LEC_PUB_MIN, LEC_PUB_MAX);
    }

    /**
     * Assigns publications to the specified faculty.
     * @param author Id of the faculty
     * @param min Minimum number of publications
     * @param max Maximum number of publications
     */
    private void _assignFacultyPublications(String author, int min, int max) {
        int num;
        PublicationInfo publication;

        num = _getRandomFromRange(min, max);
        for (int i = 0; i < num; i++) {
            publication = new PublicationInfo();
            publication.id = _getId(CS_C_PUBLICATION, i, author);
            publication.name = _getRelativeName(CS_C_PUBLICATION, i);
            publication.authors = new ArrayList();
            publication.authors.add(author);
            publications_.add(publication);
        }
    }

    /**
     * Assigns publications to the specified graduate student. The publications are
     * chosen from some faculties'.
     * @param author Id of the graduate student.
     * @param min Minimum number of publications.
     * @param max Maximum number of publications.
     */
    private void _assignGraduateStudentPublications(String author, int min, int max) {
        int num;
        PublicationInfo publication;

        num = _getRandomFromRange(min, max);
        ArrayList list = _getRandomList(num, 0, publications_.size() - 1);
        for (int i = 0; i < list.size(); i++) {
            publication = (PublicationInfo) publications_.get( ( (Integer) list.get(i)).
                    intValue());
            publication.authors.add(author);
        }
    }

    /**
     * Generates publication instances. These publications are assigned to some faculties
     * and graduate students before.
     */
    public void _generatePublications() {
        for (int i = 0; i < publications_.size(); i++) {
            _generateAPublication( (PublicationInfo) publications_.get(i));
            if(globalVersionTrigger){
                PublicationInfo pub = (PublicationInfo) publications_.get(i);
                writer_log.addPropertyInstance(pub.id, RDF.type.getURI(), ontology+"#Publication", true);
                writer_log.addPropertyInstance(pub.id, ontology+"#name", pub.name, false);
                for(String author : (ArrayList<String>) pub.authors){
                    writer_log.addPropertyInstance(pub.id, ontology+"#author", author, true);
                }
            }
        }
    }

    public void _generateConferencePublications() {
        for (int i = 0; i < publications_.size()/25; i++) {
            _generateAConferencePublication( (PublicationInfo) publications_.get(i));
        }
    }

    public void _generateJournalPublications() {
        for (int i = 0; i < publications_.size()/20; i++) {
            _generateAJournalPublication( (PublicationInfo) publications_.get(i));
        }
    }

    public void _generateTechnicalReports() {
        for (int i = 0; i < publications_.size()/10; i++) {
            _generateATechnicalReport( (PublicationInfo) publications_.get(i));
        }
    }

    public void _generateBooks() {
        for (int i = 0; i < publications_.size()/15; i++) {
            _generateABook( (PublicationInfo) publications_.get(i));
        }
    }

    public void _generateThesis() {
        for (int i = 0; i < publications_.size()/20; i++) {
            _generateAThesis( (PublicationInfo) publications_.get(i));
        }
    }

    public void _generateProjects() {
        int num = _getRandomFromRange(PROJECT_NUM_MIN, PROJECT_NUM_MAX);
        for (int i = 0; i < num; i++) {
            _generateAProject( _getRandomFromRange(150, 43958) );
        }
    }

    public void _generateEvents() {
        int num = _getRandomFromRange(EVENT_NUM_MIN, EVENT_NUM_MAX);
        for (int i = 0; i < num; i++) {
            _generateAnEvent(_getRandomFromRange(150, 43958) );
        }
    }

    /**
     * Generates a publication instance.
     * @param publication Information of the publication.
     */
    private void _generateAPublication(PublicationInfo publication) {
        writer_.startSection(CS_C_PUBLICATION, publication.id);
        writer_.addProperty(CS_P_NAME, publication.name, false);
        for (int i = 0; i < publication.authors.size(); i++) {
            writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
                    (String) publication.authors.get(i), true);
        }
        writer_.endSection(CS_C_PUBLICATION);
    }

    private void _generateAConferencePublication(PublicationInfo publication) {
        instances_[CS_C_CONFPUBLICATION].count++;
        instances_[CS_C_CONFPUBLICATION].num++;
        writer_.startSection(CS_C_CONFPUBLICATION, publication.id);
        writer_.addProperty(CS_P_NAME, "Conference " + publication.name, false);
        for (int i = 0; i < publication.authors.size(); i++) {
            writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
                    (String) publication.authors.get(i), true);
        }
        String n = "Venue"+_getRandomFromRange(0, 1500);
        writer_.addProperty(CS_P_VENUE, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#venue", n, false);
        }
        n = ""+_getRandomFromRange(1, 28)+"-"+_getRandomFromRange(1, 12)+"-"+_getRandomFromRange(2000, 2016);
        writer_.addProperty(CS_P_DATE, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#date", n, false);
        }
        n = "ISBN"+System.nanoTime();
        writer_.addProperty(CS_P_ISBN, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#isbn", n, false);
        }
        writer_.endSection(CS_C_CONFPUBLICATION);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, RDF.type.getURI(), ontology+"#ConferencePublication", true);
            writer_log.addPropertyInstance(publication.id, ontology+"#name", "Conference " + publication.name, false);
            for(String author : (ArrayList<String>) publication.authors){
                writer_log.addPropertyInstance(publication.id, ontology+"#author", author, true);
            }
        }

    }

    private void _generateAJournalPublication(PublicationInfo publication) {
        instances_[CS_C_JOURNALPUBLICATION].count++;
        instances_[CS_C_JOURNALPUBLICATION].num++;
        writer_.startSection(CS_C_JOURNALPUBLICATION, publication.id);
        String n = "Journal " + _getRandomFromRange(1, 2500);
        publication.name = n;
        writer_.addProperty(CS_P_NAME, publication.name, false);
        for (int i = 0; i < publication.authors.size(); i++) {
            writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
                    (String) publication.authors.get(i), true);
        }
        int advisor = random_.nextInt(instances_[CS_C_FULLPROF].total);
        writer_.addProperty(CS_P_EDITORINCHIEF,
                _getId(CS_C_FULLPROF, advisor), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#editorInChief",_getId(CS_C_FULLPROF, advisor) , true);
        }
        n = ""+_getRandomFromRange(1, 28)+"-"+_getRandomFromRange(1, 12)+"-"+_getRandomFromRange(2000, 2016);
        writer_.addProperty(CS_P_DATE,n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#date",n , false);
        }
        n = "ISBN"+System.nanoTime();
        writer_.addProperty(CS_P_ISBN, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#isbn",n , false);
        }
        writer_.endSection(CS_C_JOURNALPUBLICATION);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, RDF.type.getURI(), ontology+"#JournalPublication", true);
            writer_log.addPropertyInstance(publication.id, ontology+"#name", "Conference " + publication.name, false);
            for(String author : (ArrayList<String>) publication.authors){
                writer_log.addPropertyInstance(publication.id, ontology+"#author", author, true);
            }
        }
    }

    private void _generateATechnicalReport(PublicationInfo publication) {
        instances_[CS_C_TECHNICALREPORT].count++;
        instances_[CS_C_TECHNICALREPORT].num++;
        writer_.startSection(CS_C_TECHNICALREPORT, publication.id);

        publication.name = "TR " + _getRandomFromRange(1, 2500);
        writer_.addProperty(CS_P_NAME, publication.name, false);

        for (int i = 0; i < publication.authors.size(); i++) {
            writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
                    (String) publication.authors.get(i), true);
        }
        String n = ""+_getRandomFromRange(1, 28)+"-"+_getRandomFromRange(1, 12)+"-"+_getRandomFromRange(2000, 2016);
        writer_.addProperty(CS_P_DATE, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#date",n , false);
        }
        n = "TR-ID-"+System.nanoTime();
        writer_.addProperty(CS_P_REPORTID, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#technicalReportID",n , false);
        }
        writer_.endSection(CS_C_TECHNICALREPORT);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, RDF.type.getURI(), ontology+"#TechnicalReport", true);
            writer_log.addPropertyInstance(publication.id, ontology+"#name", publication.name, false);
            for(String author : (ArrayList<String>) publication.authors){
                writer_log.addPropertyInstance(publication.id, ontology+"#author", author, true);
            }
        }
    }

    private void _generateABook(PublicationInfo publication) {
        instances_[CS_C_BOOK].count++;
        instances_[CS_C_BOOK].num++;
        writer_.startSection(CS_C_BOOK, publication.id);
        publication.name = "Book " + _getRandomFromRange(1, 2500);
        writer_.addProperty(CS_P_NAME, publication.name, false);
        for (int i = 0; i < publication.authors.size(); i++) {
            writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
                    (String) publication.authors.get(i), true);
        }
        String n = ""+_getRandomFromRange(1, 28)+"-"+_getRandomFromRange(1, 12)+"-"+_getRandomFromRange(2000, 2016);
        writer_.addProperty(CS_P_DATE, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#date",n , false);
        }
        n = "ISBN"+System.nanoTime();
        writer_.addProperty(CS_P_ISBN, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#isbn",n , false);
        }
        writer_.endSection(CS_C_BOOK);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, RDF.type.getURI(), ontology+"#Book", true);
            writer_log.addPropertyInstance(publication.id, ontology+"#name", "Conference " + publication.name, false);
            for(String author : (ArrayList<String>) publication.authors){
                writer_log.addPropertyInstance(publication.id, ontology+"#author", author, true);
            }
        }
    }

    private void _generateAThesis(PublicationInfo publication) {
        instances_[CS_C_THESIS].count++;
        instances_[CS_C_THESIS].num++;
        writer_.startSection(CS_C_THESIS, publication.id);
        publication.name = "Thesis " + _getRandomFromRange(1, 2500);
        writer_.addProperty(CS_P_NAME, publication.name, false);
        for (int i = 0; i < publication.authors.size(); i++) {
            writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
                    (String) publication.authors.get(i), true);
        }
        String n = ""+_getRandomFromRange(1, 28)+"-"+_getRandomFromRange(1, 12)+"-"+_getRandomFromRange(2000, 2016);
        writer_.addProperty(CS_P_DATE, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#date",n , false);
        }
        int advisor = random_.nextInt(instances_[CS_C_FULLPROF].total);
        writer_.addProperty(CS_P_SUPERVISOR,
                _getId(CS_C_FULLPROF, advisor), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, ontology+"#supervisor",_getId(CS_C_FULLPROF, advisor) , true);
        }
        writer_.endSection(CS_C_THESIS);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(publication.id, RDF.type.getURI(), ontology+"#Thesis", true);
            writer_log.addPropertyInstance(publication.id, ontology+"#name", publication.name, false);
            for(String author : (ArrayList<String>) publication.authors){
                writer_log.addPropertyInstance(publication.id, ontology+"#author", author, true);
            }
        }
    }

    private void _generateAProject(int index) {
        String id = _getId(CS_C_PROJECT, index);
        writer_.startSection(CS_C_PROJECT, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#Project", true);
        }
        writer_.addProperty(CS_P_NAME, "Project", false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#name", "Project", false);
        }
        String n = _getRandomFromRange(1,50)+" month(s)";
        writer_.addProperty(CS_P_DURATION, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#duration", n, false);
        }
        n = _getRandomFromRange(100000,5000000)+" euro(s)";
        writer_.addProperty(CS_P_BUDGET,  n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#budget", n, false);
        }
        int advisor = random_.nextInt(instances_[CS_C_FULLPROF].total);
        writer_.addProperty(CS_P_SCIENTIFICADVISOR,
                _getId(CS_C_FULLPROF, advisor), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#scientificAdvisor", _getId(CS_C_FULLPROF, advisor), true);
        }
        int group = random_.nextInt(instances_[CS_C_RESEARCHGROUP].total);
        writer_.addProperty(CS_P_RESEARCHGROUP,
                _getId(CS_C_RESEARCHGROUP, group), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#researchGroup", _getId(CS_C_RESEARCHGROUP, group), true);
        }
        int uni = random_.nextInt(instances_[CS_C_UNIV].total);
        writer_.addProperty(CS_P_FUNDEDBY,
                _getId(CS_C_UNIV, uni), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#fundedBy", _getId(CS_C_UNIV, uni), true);
        }

        writer_.endSection(CS_C_PROJECT);

    }

    private void _generateAnEvent(int index) {
        String id = _getId(CS_C_EVENT, index);
        writer_.startSection(CS_C_EVENT, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#Event", true);
        }
        writer_.addProperty(CS_P_NAME, "Event", false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#name", "Event", false);
        }
        String n = CS_EVENT_TYPES[_getRandomFromRange(0,2)];
        writer_.addProperty(CS_P_EVENTTYPE, n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#eventType", n, false);
        }
        int organizer = random_.nextInt(instances_[CS_C_FULLPROF].total);
        writer_.addProperty(CS_P_EVENTORGANIZER,
                _getId(CS_C_FULLPROF, organizer), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#eventOrganizer", _getId(CS_C_FULLPROF, organizer), true);
        }
        writer_.endSection(CS_C_EVENT);
    }

    /**
     * Generates properties for the specified student instance.
     * @param type Type of the student.
     * @param index Index of the instance within its type.
     */
    private void _generateAStudent_a(int type, int index, String id) {
        writer_.addProperty(CS_P_NAME, _getRelativeName(type, index), false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#name", _getRelativeName(type, index), false);
        }
        writer_.addProperty(CS_P_MEMBEROF,
                _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#memberOf", _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
        }
        writer_.addProperty(CS_P_EMAIL, _getEmail(type, index), false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#email", _getEmail(type, index), false);
        }
        writer_.addProperty(CS_P_TELEPHONE, "xxx-xxx-xxxx", false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#telephone", "xxx-xxx-xxxx", false);
        }
    }

    /**
     * Generates an undergraduate student instance.
     * @param index Index of the undergraduate student.
     */
    private void _generateAnUndergraduateStudent(int index) {
        int n;
        ArrayList list;
        String id = _getId(CS_C_UNDERSTUD, index);
        writer_.startSection(CS_C_UNDERSTUD, id);
        _generateAStudent_a(CS_C_UNDERSTUD, index, id);
        n = _getRandomFromRange(UNDERSTUD_COURSE_MIN, UNDERSTUD_COURSE_MAX);
        list = _getRandomList(n, 0, underCourses_.size() - 1);
        for (int i = 0; i < list.size(); i++) {
            CourseInfo info = (CourseInfo) underCourses_.get( ( (Integer) list.get(i)).
                    intValue());
            writer_.addProperty(CS_P_TAKECOURSE, _getId(CS_C_COURSE, info.globalIndex), true);
            if(globalVersionTrigger){
                writer_log.addPropertyInstance(id, ontology+"#takesCourse", _getId(CS_C_COURSE, info.globalIndex), true);
            }
        }
        if (0 == random_.nextInt(R_UNDERSTUD_ADVISOR)) {
            String ad = _selectAdvisor();
            writer_.addProperty(CS_P_ADVISOR, ad, true);
            if(globalVersionTrigger){
                writer_log.addPropertyInstance(id, ontology+"#advisor", ad, true);
            }
        }
        writer_.endSection(CS_C_UNDERSTUD);
    }

    private void _generateAVisitingStudent(int index) {
        int n;
        ArrayList list;
        String id = _getId(CS_C_VISITSTUD, index);
        writer_.startSection(CS_C_VISITSTUD, id);
        _generateAStudent_a(CS_C_VISITSTUD, index, id);
        n = _getRandomFromRange(VISITSTUD_COURSE_MIN, VISITSTUD_COURSE_MAX);
        list = _getRandomList(n, 0, underCourses_.size() - 1);
        for (int i = 0; i < list.size(); i++) {
            CourseInfo info = (CourseInfo) underCourses_.get( ( (Integer) list.get(i)).
                    intValue());
            writer_.addProperty(CS_P_TAKECOURSE, _getId(CS_C_COURSE, info.globalIndex), true);
            if(globalVersionTrigger){
                writer_log.addPropertyInstance(id, ontology+"#takesCourse", _getId(CS_C_COURSE, info.globalIndex), true);
            }
        }
        writer_.addProperty(CS_P_VISITSASSTUD,
                _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#visitsAsStudent", _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
        }
        String d = _getRandomFromRange(1, 10)+" month(s)";
        writer_.addProperty(CS_P_VISITDURATION, d,false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#visitDuration", d, false);
        }
        if (0 == random_.nextInt(R_VISITSTUD_ADVISOR)) {
            String ad = _selectAdvisor();
            writer_.addProperty(CS_P_ADVISOR, ad, true);
            if(globalVersionTrigger){
                writer_log.addPropertyInstance(id, ontology+"#advisor", ad, true);
            }
        }
        writer_.endSection(CS_C_VISITSTUD);
    }

    /**
     * Generates a graduate student instance.
     * @param index Index of the graduate student.
     */
    private void _generateAGradudateStudent(int index) {
        int n;
        ArrayList list;
        String id;

        id = _getId(CS_C_GRADSTUD, index);
        writer_.startSection(CS_C_GRADSTUD, id);
        _generateAStudent_a(CS_C_GRADSTUD, index, id);
        n = _getRandomFromRange(GRADSTUD_COURSE_MIN, GRADSTUD_COURSE_MAX);
        list = _getRandomList(n, 0, gradCourses_.size() - 1);
        for (int i = 0; i < list.size(); i++) {
            CourseInfo info = (CourseInfo) gradCourses_.get( ( (Integer) list.get(i)).
                    intValue());
            writer_.addProperty(CS_P_TAKECOURSE,
                    _getId(CS_C_GRADCOURSE, info.globalIndex), true);
            if(globalVersionTrigger){
                writer_log.addPropertyInstance(id, ontology+"#takesCourse", _getId(CS_C_GRADCOURSE, info.globalIndex), true);
            }
        }
        writer_.addProperty(CS_P_UNDERGRADFROM, CS_C_UNIV,
                _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)));
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#undergraduateDegreeFrom", _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)), true);
        }
        if (0 == random_.nextInt(R_GRADSTUD_ADVISOR)) {
            String ad = _selectAdvisor();
            writer_.addProperty(CS_P_ADVISOR, _selectAdvisor(), true);
            if(globalVersionTrigger){
                writer_log.addPropertyInstance(id, ontology+"#advisor", ad, true);
            }

        }

        _assignGraduateStudentPublications(id, GRADSTUD_PUB_MIN, GRADSTUD_PUB_MAX);
        writer_.endSection(CS_C_GRADSTUD);
    }

    /**
     * Select an advisor from the professors.
     * @return Id of the selected professor.
     */
    private String _selectAdvisor() {
        int profType;
        int index;

        profType = _getRandomFromRange(CS_C_FULLPROF, CS_C_ASSTPROF);
        index = random_.nextInt(instances_[profType].total);
        return _getId(profType, index);
    }

    /**
     * Generates a TA instance according to the specified information.
     * @param ta Information of the TA.
     */
    private void _generateATa(TaInfo ta) {
        String id = _getId(CS_C_GRADSTUD, ta.indexInGradStud);
        writer_.startAboutSection(CS_C_TA, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#TeachingAssistant", true);
        }
        writer_.addProperty(CS_P_TAOF, _getId(CS_C_COURSE, ta.indexInCourse), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#teachingAssistantOf", _getId(CS_C_COURSE, ta.indexInCourse), true);
        }
        writer_.endSection(CS_C_TA);
    }

    /**
     * Generates an RA instance according to the specified information.
     * @param ra Information of the RA.
     */
    private void _generateAnRa(RaInfo ra) {
        writer_.startAboutSection(CS_C_RA, _getId(CS_C_GRADSTUD, ra.indexInGradStud));
        writer_.endSection(CS_C_RA);
    }

    /**
     * Generates a course instance.
     * @param index Index of the course.
     */
    private void _generateACourse(int index) {
        String id = _getId(CS_C_COURSE, index);
        writer_.startSection(CS_C_COURSE, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#Course", true);
        }
        writer_.addProperty(CS_P_NAME,
                _getRelativeName(CS_C_COURSE, index), false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#name", _getRelativeName(CS_C_COURSE, index), false);
        }
        writer_.endSection(CS_C_COURSE);
    }

    /**
     * Generates a graduate course instance.
     * @param index Index of the graduate course.
     */
    private void _generateAGraduateCourse(int index) {
        String id = _getId(CS_C_GRADCOURSE, index);
        writer_.startSection(CS_C_GRADCOURSE, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#GraduateCourse", true);
        }
        writer_.addProperty(CS_P_NAME,
                _getRelativeName(CS_C_GRADCOURSE, index), false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#name", _getRelativeName(CS_C_GRADCOURSE, index), false);
        }
        writer_.endSection(CS_C_GRADCOURSE);
    }

    private void _generateAWebCourse(int index) {
        String id = _getId(CS_C_WEBCOURSE, index);
        writer_.startSection(CS_C_WEBCOURSE, id);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#WebCourse", true);
        }
        writer_.addProperty(CS_P_NAME,
                _getRelativeName(CS_C_WEBCOURSE, index), false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#name", _getRelativeName(CS_C_WEBCOURSE, index), false);
        }
        String n = "topic"+_getRandomFromRange(1, 150);
        writer_.addProperty(CS_P_TOPIC,
                n, false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#topic", n, false);
        }
        n = "http://example.com/webcourse/"+_getRandomFromRange(1, 150);
        writer_.addProperty(CS_P_URL,
                n , false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#url", n, false);
        }
        int hours = _getRandomFromRange(9, 17);
        writer_.addProperty(CS_P_HOURS,
                hours+"-"+(int)(hours+2), false);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#hours", hours+"-"+(int)(hours+2), false);
        }
        writer_.endSection(CS_C_WEBCOURSE);
    }

    /**
     * Generates course/graduate course instances. These course are assigned to some
     * faculties before.
     */
    private void _generateCourses() {
        for (int i = 0; i < underCourses_.size(); i++) {
            _generateACourse( ( (CourseInfo) underCourses_.get(i)).globalIndex);
        }
        for (int i = 0; i < gradCourses_.size(); i++) {
            _generateAGraduateCourse( ( (CourseInfo) gradCourses_.get(i)).globalIndex);
        }
        for (int i = 0; i < webCourses_.size(); i++) {
            _generateAWebCourse( ( (CourseInfo) webCourses_.get(i)).globalIndex);
        }
    }

    /**
     * Chooses RAs and TAs from graduate student and generates their instances accordingly.
     */
    private void _generateRaTa() {
        if(instances_[CS_C_TA].total == 0) return;
        ArrayList list, courseList;
        TaInfo ta;
        RaInfo ra;
        ArrayList tas, ras;
        int i;

        tas = new ArrayList();
        ras = new ArrayList();
        list = _getRandomList(instances_[CS_C_TA].total + instances_[CS_C_RA].total,
                0, instances_[CS_C_GRADSTUD].total - 1);
        System.out.println("underCourses " + (underCourses_.size() - 1));
        System.out.println("instances ta " + instances_[CS_C_TA].total);
        courseList = _getRandomList(instances_[CS_C_TA].total, 0,
                underCourses_.size() - 1);

        for (i = 0; i < courseList.size(); i++) {
            ta = new TaInfo();
            ta.indexInGradStud = ( (Integer) list.get(i)).intValue();
            ta.indexInCourse = ( (CourseInfo) underCourses_.get( ( (Integer)
                    courseList.get(i)).intValue())).globalIndex;
            _generateATa(ta);
        }
        while (i < list.size()) {
            ra = new RaInfo();
            ra.indexInGradStud = ( (Integer) list.get(i)).intValue();
            _generateAnRa(ra);
            i++;
        }
    }

    /**
     * Generates a research group instance.
     * @param index Index of the research group.
     */
    private void _generateAResearchGroup(int index) {
        String id;
        id = _getId(CS_C_RESEARCHGROUP, index);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, RDF.type.getURI(), ontology+"#ResearchGroup", true);
        }
        writer_.startSection(CS_C_RESEARCHGROUP, id);
        writer_.addProperty(CS_P_SUBORGANIZATIONOF,
                _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
        if(globalVersionTrigger){
            writer_log.addPropertyInstance(id, ontology+"#subOrganizationOf", _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
        }
        writer_.endSection(CS_C_RESEARCHGROUP);
    }


    private String _getFileSuffix() {
        return isDaml_ ? ".daml" : ".owl";
    }

    private String _getId(int classType, int index) {
        String id;

        switch (classType) {
            case CS_C_UNIV:
                id = "http://www." + _getRelativeName(classType, index) + ".edu";
                break;
            case CS_C_DEPT:
                id = "http://www." + _getRelativeName(classType, index) + "." +
                        _getRelativeName(CS_C_UNIV, instances_[CS_C_UNIV].count - 1) +
                        ".edu";
                break;
            default:
                id = _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1) + ID_DELIMITER +
                        _getRelativeName(classType, index);
                break;
        }

        return id;
    }

    private String _getId(int classType, int index, String param) {
        String id;

        switch (classType) {
            case CS_C_PUBLICATION:
                //NOTE: param is author id
                id = param + ID_DELIMITER + CLASS_TOKEN[classType] + index;
                break;
            default:
                id = _getId(classType, index);
                break;
        }

        return id;
    }

    private String _getName(int classType, int index) {
        String name;

        switch (classType) {
            case CS_C_UNIV:
                name = _getRelativeName(classType, index);
                break;
            case CS_C_DEPT:
                name = _getRelativeName(classType, index) + INDEX_DELIMITER +
                        (instances_[CS_C_UNIV].count - 1);
                break;
            //NOTE: Assume departments with the same index share the same pool of courses and researches
            case CS_C_COURSE:
            case CS_C_GRADCOURSE:
            case CS_C_RESEARCH:
                name = _getRelativeName(classType, index) + INDEX_DELIMITER +
                        (instances_[CS_C_DEPT].count - 1);
                break;
            default:
                name = _getRelativeName(classType, index) + INDEX_DELIMITER +
                        (instances_[CS_C_DEPT].count - 1) + INDEX_DELIMITER +
                        (instances_[CS_C_UNIV].count - 1);
                break;
        }

        return name;
    }

    private String _getRelativeName(int classType, int index) {
        String name;

        switch (classType) {
            case CS_C_UNIV:
                //should be unique too!
                name = CLASS_TOKEN[classType] + index;
                break;
            case CS_C_DEPT:
                name = CLASS_TOKEN[classType] + index;
                break;
            default:
                name = CLASS_TOKEN[classType] + index;
                break;
        }

        return name;
    }

    private String _getEmail(int classType, int index) {
        String email = "";

        switch (classType) {
            case CS_C_UNIV:
                email += _getRelativeName(classType, index) + "@" +
                        _getRelativeName(classType, index) + ".edu";
                break;
            case CS_C_DEPT:
                email += _getRelativeName(classType, index) + "@" +
                        _getRelativeName(classType, index) + "." +
                        _getRelativeName(CS_C_UNIV, instances_[CS_C_UNIV].count - 1) + ".edu";
                break;
            default:
                email += _getRelativeName(classType, index) + "@" +
                        _getRelativeName(CS_C_DEPT, instances_[CS_C_DEPT].count - 1) +
                        "." + _getRelativeName(CS_C_UNIV, instances_[CS_C_UNIV].count - 1) +
                        ".edu";
                break;
        }

        return email;
    }


    private void _updateCount(int classType) {
        int subClass, superClass;

        instances_[classType].count++;
        subClass = classType;
        while ( (superClass = CLASS_INFO[subClass][INDEX_SUPER]) != CS_C_NULL) {
            instances_[superClass].count++;
            subClass = superClass;
        }
    }

    private void _updateCount(int classType, InstanceCount inst) {
        int subClass, superClass;

        inst.count++;
        subClass = classType;
        while ( (superClass = CLASS_INFO[subClass][INDEX_SUPER]) != CS_C_NULL) {
            inst.count++;
            subClass = superClass;
        }
    }

    public ArrayList _getRandomList(int num, int min, int max) {
        ArrayList list = new ArrayList();
        ArrayList tmp = new ArrayList();
        for (int i = min; i <= max; i++) {
            tmp.add(new Integer(i));
        }

        for (int i = 0; i < num; i++) {
            if(tmp.size() > 1){
                int pos = _getRandomFromRange(0, tmp.size() - 1);
                list.add( (Integer) tmp.get(pos));
                tmp.remove(pos);
            }
        }

        return list;
    }

    public  int _getRandomFromRange(int min, int max) {

        return min + random_.nextInt(max - min + 1);
    }

    public void _generateComments() {
        int classInstNum = 0; //total class instance num in this department
        long totalClassInstNum = 0l; //total class instance num so far
        int propInstNum = 0; //total property instance num in this department
        long totalPropInstNum = 0l; //total property instance num so far
        String comment;

        comment = "External Seed=" + baseSeed_ + " Interal Seed=" + seed_;
        log_.println(comment);
        log_.println();

        comment = "CLASS INSTANCE# TOTAL-SO-FAR";
        log_.println(comment);
        comment = "----------------------------";
        log_.println(comment);
        for (int i = 0; i < CLASS_NUM; i++) {
            comment = CLASS_TOKEN[i] + " " + instances_[i].logNum + " " +
                    instances_[i].logTotal;
            log_.println(comment);
            classInstNum += instances_[i].logNum;
            totalClassInstNum += instances_[i].logTotal;
        }
        log_.println();
        comment = "TOTAL: " + classInstNum;
        log_.println(comment);
        comment = "TOTAL SO FAR: " + totalClassInstNum;
        log_.println(comment);

        comment = "PROPERTY---INSTANCE NUM";
        log_.println();
        comment = "PROPERTY INSTANCE# TOTAL-SO-FAR";
        log_.println(comment);
        comment = "-------------------------------";
        log_.println(comment);
        for (int i = 0; i < PROP_NUM; i++) {
            comment = PROP_TOKEN[i] + " " + properties_[i].logNum;
            comment = comment + " " + properties_[i].logTotal;
            log_.println(comment);
            propInstNum += properties_[i].logNum;
            totalPropInstNum += properties_[i].logTotal;
        }
        log_.println();
        comment = "TOTAL: " + propInstNum;
        log_.println(comment);
        comment = "TOTAL SO FAR: " + totalPropInstNum;
        log_.println(comment);

        System.out.println("CLASS INSTANCE #: " + classInstNum + ", TOTAL SO FAR: " +
                totalClassInstNum);
        System.out.println("PROPERTY INSTANCE #: " + propInstNum +
                ", TOTAL SO FAR: " + totalPropInstNum);
        System.out.println();

        log_.println();
    }
}
