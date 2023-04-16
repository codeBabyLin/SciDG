package org.imis.generator;

import com.hp.hpl.jena.vocabulary.RDF;
import org.apache.commons.lang3.StringUtils;
import org.imis.generator.Infor.*;
import org.imis.generator.instance.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.BiConsumer;


public class MiddleGenerator_new extends Constant {
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



    public ArrayList<ProfessorInstance> c_Professors =new ArrayList<>();

    public ArrayList<StudentInstance> c_Students =new ArrayList<>();


    public ArrayList<PublicationInstance> c_Publications =new ArrayList<>();

    public ArrayList<WebCourseInstance> c_WEbCourses =new ArrayList<>();
    public ArrayList<UnderCourseInstance> c_UnderCourses = new ArrayList<>();
    public ArrayList<GraduCourseInstance> c_GraduCourses = new ArrayList<>();


    public ArrayList<PublicationInstance> c_RemainPublications =new ArrayList<>();
    public ArrayList<WebCourseInstance> c_RemainWEbCourses =new ArrayList<>();
    public ArrayList<UnderCourseInstance> c_RemainUnderCourses = new ArrayList<>();
    public ArrayList<GraduCourseInstance> c_RemainGraduCourses = new ArrayList<>();

    public ArrayList<ProfessorInstance> c_Professors_add =new ArrayList<>();
    public ArrayList<ProfessorInstance> c_Professors_del =new ArrayList<>();

    public ArrayList<StudentInstance> c_Students_add =new ArrayList<>();
    public ArrayList<StudentInstance> c_Students_del =new ArrayList<>();

    public ArrayList<PublicationInstance> c_Publications_add =new ArrayList<>();
    public ArrayList<PublicationInstance> c_Publications_del =new ArrayList<>();

    public ArrayList<WebCourseInstance> c_WEbCourses_add =new ArrayList<>();
    public ArrayList<UnderCourseInstance> c_UnderCourses_add = new ArrayList<>();
    public ArrayList<GraduCourseInstance> c_GraduCourses_add = new ArrayList<>();
    public ArrayList<WebCourseInstance> c_WEbCourses_del =new ArrayList<>();
    public ArrayList<UnderCourseInstance> c_UnderCourses_del = new ArrayList<>();
    public ArrayList<GraduCourseInstance> c_GraduCourses_del = new ArrayList<>();

    public HashMap<Integer, ArrayList<Integer>> authors_add = new HashMap<Integer, ArrayList<Integer>>();
    public HashMap<Integer, ArrayList<Integer>> takeCourses_add = new HashMap<Integer, ArrayList<Integer>>();
    public HashMap<Integer, ArrayList<Integer>> teachs_add = new HashMap<Integer, ArrayList<Integer>>();
    public HashMap<Integer, ArrayList<Integer>> authors_del = new HashMap<Integer, ArrayList<Integer>>();
    public HashMap<Integer, ArrayList<Integer>> takeCourses_del = new HashMap<Integer, ArrayList<Integer>>();
    public HashMap<Integer, ArrayList<Integer>> teachs_del = new HashMap<Integer, ArrayList<Integer>>();

    IdGenerator idGen = new IdGenerator();

    public int getNextId(){
        return idGen.getNextId();
    }



    public MiddleGenerator_new(){
        instances_ = new InstanceCount[CLASS_NUM];
        for (int i = 0; i < CLASS_NUM; i++) {
            instances_[i] = new InstanceCount();
        }
        properties_ = new PropertyCount[PROP_NUM];
        for (int i = 0; i < PROP_NUM; i++) {
            properties_[i] = new PropertyCount();
        }

        random_ = new Random();
        //underCourses_ = new ArrayList();
        //        //gradCourses_ = new ArrayList();
        //        //webCourses_ = new ArrayList();
        //        //remainingUnderCourses_ = new ArrayList();
        //        //remainingGradCourses_ = new ArrayList();
        //        //remainingWebCourses_ = new ArrayList();
        //        //publications_ = new ArrayList();
        //        //projects_ = new ArrayList();
    }
//CS_C_FULLPROF,CS_C_VISITINGPROF,CS_C_ASSOPROF,CS_C_ASSTPROF,CS_C_LECTURER,CS_C_UNDERSTUD,CS_C_VISITSTUD,CS_C_GRADSTUD,CS_C_TA,CS_C_RA,CS_C_RESEARCHGROUP
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
    public void WriteCsvDelta(String dirPath){
        new File(dirPath).mkdirs();
        try {
            if(c_UnderCourses_add.size()>0 || c_GraduCourses_add.size()>0) {
                PrintStream courseaddPrint = new PrintStream(new FileOutputStream(new File(dirPath, "courseADD.csv")));
                courseaddPrint.println("id,name");
                for (UnderCourseInstance ui : c_UnderCourses_add) {
                    courseaddPrint.println(ui.getId() + "," + ui.getName());
                }
                for (GraduCourseInstance ui : c_GraduCourses_add) {
                    courseaddPrint.println(ui.getId() + "," + ui.getName());
                }
            }
            if(c_UnderCourses_del.size()>0 || c_GraduCourses_del.size() >0) {
                PrintStream courseaddPrint = new PrintStream(new FileOutputStream(new File(dirPath, "courseDel.csv")));
                courseaddPrint.println("id,name");
                for (UnderCourseInstance ui : c_UnderCourses_del) {
                    courseaddPrint.println(ui.getId() + "," + ui.getName());
                }
                for (GraduCourseInstance ui : c_GraduCourses_del) {
                    courseaddPrint.println(ui.getId() + "," + ui.getName());
                }
            }
            if(c_WEbCourses_add.size() >0) {
                PrintStream courseaddPrint = new PrintStream(new FileOutputStream(new File(dirPath, "webcourseADD.csv")));
                courseaddPrint.println("id,name,topic,url,hours");
                for (WebCourseInstance wi : c_WEbCourses_add) {
                    courseaddPrint.println(wi.getId() + "," + wi.getName() + "," + wi.getWebCourseTopic() + "," + wi.getUrl() + "," + wi.getCourseHours());
                }
            }
            if(c_WEbCourses_del.size() >0) {
                PrintStream courseaddPrint = new PrintStream(new FileOutputStream(new File(dirPath, "webcourseDel.csv")));
                courseaddPrint.println("id,name,topic,url,hours");
                for (WebCourseInstance wi : c_WEbCourses_del) {
                    courseaddPrint.println(wi.getId() + "," + wi.getName() + "," + wi.getWebCourseTopic() + "," + wi.getUrl() + "," + wi.getCourseHours());
                }
            }
            if(c_Professors_add.size()>0) {
                PrintStream professorPrint = new PrintStream(new FileOutputStream(new File(dirPath, "professorADD.csv")));
                professorPrint.println("id,name,type,emailAddress,researchInterest");
                for (ProfessorInstance pi : c_Professors_add) {
                    professorPrint.println(pi.getId() + "," + pi.getName() + "," + pi.getType() + "," + pi.getEmailAddress() + "," + pi.getResearchInterest());

                }
            }
            if(c_Professors_del.size()>0){
                PrintStream professorPrint = new PrintStream(new FileOutputStream(new File(dirPath, "professorDel.csv")));
                professorPrint.println("id,name,type,emailAddress,researchInterest");
                for (ProfessorInstance pi : c_Professors_del) {
                    professorPrint.println(pi.getId() + "," + pi.getName() + "," + pi.getType() + "," + pi.getEmailAddress() + "," + pi.getResearchInterest());

                }
            }


            if(c_Students_add.size()>0) {
                PrintStream stuPrint = new PrintStream(new FileOutputStream(new File(dirPath, "studentsADD.csv")));
                stuPrint.println("id,name,type,emailaddress");
                for (StudentInstance si : c_Students_add) {
                    stuPrint.println(si.getId() + "," + si.getName() + "," + si.getType() + "," + si.getEmailAddress());
                }
            }

            if(c_Students_del.size()>0) {
                PrintStream stuPrint = new PrintStream(new FileOutputStream(new File(dirPath, "studentsDel.csv")));
                stuPrint.println("id,name,type,emailaddress");
                for (StudentInstance si : c_Students_del) {
                    stuPrint.println(si.getId() + "," + si.getName() + "," + si.getType() + "," + si.getEmailAddress());
                }
            }

            if(c_Publications_add.size()>0) {
                PrintStream pubPrint = new PrintStream(new FileOutputStream(new File(dirPath, "publicationADD.csv")));
                pubPrint.println("id,name");
                for (PublicationInstance pi : c_Publications_add) {
                    pubPrint.println(pi.getId() + "," + pi.getName());
                }
            }
            if(c_Publications_del.size()>0) {
                PrintStream pubPrint = new PrintStream(new FileOutputStream(new File(dirPath, "publicationDel.csv")));
                pubPrint.println("id,name");
                for (PublicationInstance pi : c_Publications_del) {
                    pubPrint.println(pi.getId() + "," + pi.getName());
                }
            }
            if(takeCourses_add.size()>0) {
                PrintStream takeCoursePrint = new PrintStream(new FileOutputStream(new File(dirPath, "takeCourseAdd.csv")));
                takeCoursePrint.println("id,CoursesId");
                takeCourses_add.forEach(new BiConsumer<Integer, ArrayList<Integer>>() {
                    @Override
                    public void accept(Integer integer, ArrayList<Integer> integers) {
                        String str5 = StringUtils.join(integers, " ");
                        takeCoursePrint.println(integer + "," + str5);
                    }
                });
            }
            if(takeCourses_del.size()>0) {
                PrintStream takeCoursePrint = new PrintStream(new FileOutputStream(new File(dirPath, "takeCourseDel.csv")));
                takeCoursePrint.println("id,CoursesId");
                takeCourses_del.forEach(new BiConsumer<Integer, ArrayList<Integer>>() {
                    @Override
                    public void accept(Integer integer, ArrayList<Integer> integers) {
                        String str5 = StringUtils.join(integers, " ");
                        takeCoursePrint.println(integer + "," + str5);
                    }
                });
            }

            if(teachs_add.size()>0) {
                PrintStream teachPrint = new PrintStream(new FileOutputStream(new File(dirPath, "teachAdd.csv")));
                teachPrint.println("id,coursesId");
                teachs_add.forEach(new BiConsumer<Integer, ArrayList<Integer>>() {
                    @Override
                    public void accept(Integer integer, ArrayList<Integer> integers) {
                        String str5 = StringUtils.join(integers, " ");
                        teachPrint.println(integer + "," + str5);
                    }
                });
            }
            if(teachs_del.size()>0) {
                PrintStream teachPrint = new PrintStream(new FileOutputStream(new File(dirPath, "teachDel.csv")));
                teachPrint.println("id,coursesId");
                teachs_del.forEach(new BiConsumer<Integer, ArrayList<Integer>>() {
                    @Override
                    public void accept(Integer integer, ArrayList<Integer> integers) {
                        String str5 = StringUtils.join(integers, " ");
                        teachPrint.println(integer + "," + str5);
                    }
                });
            }

            if(authors_add.size()>0) {
                PrintStream authorsPrint = new PrintStream(new FileOutputStream(new File(dirPath, "authorsAdd.csv")));
                authorsPrint.println("pid,authorIds");
                authors_add.forEach(new BiConsumer<Integer, ArrayList<Integer>>() {
                    @Override
                    public void accept(Integer integer, ArrayList<Integer> integers) {
                        String str5 = StringUtils.join(integers, " ");
                        authorsPrint.println(integer + "," + str5);
                    }
                });

            }
            if(authors_del.size()>0) {
                PrintStream authorsPrint = new PrintStream(new FileOutputStream(new File(dirPath, "authorsDel.csv")));
                authorsPrint.println("pid,authorIds");
                authors_del.forEach(new BiConsumer<Integer, ArrayList<Integer>>() {
                    @Override
                    public void accept(Integer integer, ArrayList<Integer> integers) {
                        String str5 = StringUtils.join(integers, " ");
                        authorsPrint.println(integer + "," + str5);
                    }
                });

            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void WriteCsv(String dirPath)  {
        try {
            PrintStream coursePrint = new PrintStream(new FileOutputStream(new File(dirPath, "course.csv")));
            coursePrint.println("id,name");
            for (UnderCourseInstance ui : c_UnderCourses) {
                coursePrint.println(ui.getId() + "," + ui.getName());
            }
            for (GraduCourseInstance gi : c_GraduCourses) {
                coursePrint.println(gi.getId() + "," + gi.getName());
            }

            PrintStream webCoursePrint = new PrintStream(new FileOutputStream(new File(dirPath, "webCourse.csv")));
            webCoursePrint.println("id,name,topic,url,hours");
            for (WebCourseInstance wi : c_WEbCourses) {
                webCoursePrint.println(wi.getId() + "," + wi.getName()+","+wi.getWebCourseTopic()+","+wi.getUrl()+","+wi.getCourseHours());
            }

            PrintStream studentPrint = new PrintStream(new FileOutputStream(new File(dirPath, "student.csv")));
            studentPrint.println("id,name,type,emailaddress");
            PrintStream takeCoursetPrint = new PrintStream(new FileOutputStream(new File(dirPath, "takeCourse.csv")));
            takeCoursetPrint.println("id,CoursesId");

            for (StudentInstance si : c_Students) {
                studentPrint.println(si.getId() + "," + si.getName()+","+si.getType()+","+si.getEmailAddress());
                String str5 = StringUtils.join(si.getTakeCourses()," ");
                takeCoursetPrint.println(si.getId()+","+str5);
            }

            PrintStream publicationPrint = new PrintStream(new FileOutputStream(new File(dirPath, "publication.csv")));
            publicationPrint.println("id,name");

            PrintStream publicationAuthor = new PrintStream(new FileOutputStream(new File(dirPath, "authors.csv")));
            publicationAuthor.println("pid,authorIds");

            for (PublicationInstance pi : c_Publications) {
                if(pi.isLive()) {
                    publicationPrint.println(pi.getId() + "," + pi.getName());
                    String str4 = StringUtils.join(pi.getPublicationAuthor(), " ");
                    publicationAuthor.println(pi.getId() + "," + str4);
                }
            }

            PrintStream profPrint = new PrintStream(new FileOutputStream(new File(dirPath, "professor.csv")));
            profPrint.println("id,name,type,emailAddress,researchInterest");

            PrintStream teachCoursefPrint = new PrintStream(new FileOutputStream(new File(dirPath, "teach.csv")));
            teachCoursefPrint.println("id,coursesId");

            for (ProfessorInstance pi : c_Professors) {
                profPrint.println(pi.getId() + "," + pi.getName()+","+pi.getType()+","+pi.getEmailAddress()+","+pi.getResearchInterest());
                String str = StringUtils.join(pi.getCourses()," ");
                teachCoursefPrint.println(pi.getId()+","+str);
            }


        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    /*    for(UnderCourseInstance ui: c_UnderCourses){
            coursePrint.println(ui.getId()+","+ui.getName());
        }*/
    }

    //entity with relation $ relation
    //-  professor -> publication ,course
    //+

    //- student
    //+ student publication course ->>()

    //relation add


    private void delAuthors(double evoChange){
        int num = (int) (c_Publications.size() * evoChange);
        //ArrayList list = _getRandomList(num, 0, c_Publications.size()-1);
        for(int i = 0;i<num;i++){
            int pos = _getRandomFromRange(0,c_Publications.size()-1);
            PublicationInstance pi = c_Publications.get(pos);
            c_Publications_del.add(pi);
            c_Publications.remove(pos);
            //pi.setLive(false);
            authors_del.put(pi.getId(),pi.getPublicationAuthor());
        }

    }
    private void delTakeCourses(double evoChange){
        int num = (int) (c_Students.size() * evoChange);
        ArrayList list = _getRandomList(num, 0, c_Students.size()-1);
        for(int i = 0;i<list.size();i++){
            int pos = (int) list.get(i);
            StudentInstance si = c_Students.get(pos);
            takeCourses_del.put(si.getId(),si.getTakeCourses());
            si.clearCourse();
        }
    }


    private void addStudents(double evoChange){
        int sSize = c_Students.size();
        int num = (int) (sSize * evoChange);

        for (int i = sSize;i<sSize+num;i++){
            int type = _getRandomFromRange(1,3);
            switch(type){
                case 1:{
                    _updateCount(CS_C_GRADSTUD);
                   _generateAGradudateStudent(i);
                   break;
                }
                case 2:{
                    _updateCount(CS_C_VISITSTUD);
                   _generateAVisitingStudent(i);
                    break;
                }
                case 3:{
                    _updateCount(CS_C_UNDERSTUD);
                   _generateAnUndergraduateStudent(i);
                    break;
                }
            }
        }
    }
    private void addProfessors(double evoChange){ //course add pub add,  teach 和 author
        int pSize = c_Professors.size();
        int num = (int) (pSize * evoChange);
        for(int i = pSize;i< pSize + num;i++){
            ProfessorInstance pi = new ProfessorInstance();
            pi.setId(getNextId());
            int type = _getRandomFromRange(CS_C_FULLPROF,CS_C_LECTURER);
            _updateCount(type);
            _generateAProfessor(i,type,pi);
            c_Professors.add(pi);
            c_Professors_add.add(pi);
        }

    }

    private void addauthors(double evoChange){
        int size = c_Students.size();
        int num = (int) (size * evoChange);

        ArrayList list = _getRandomList(num, 0, c_Students.size()-1);
        for (int i = 0; i < list.size(); i++) {
            //si.takeCourse(c_GraduCourses.get((int)list.get(i)).getId());
            StudentInstance si = c_Students.get((int)list.get(i));
            int pNum = _getRandomFromRange(GRADSTUD_PUB_MIN,GRADSTUD_PUB_MAX);
            ArrayList list2 = _getRandomList(pNum, 0, c_Publications.size()-1);
            for (int j = 0;j<list2.size();j++){
                PublicationInstance pi = c_Publications.get((int)list2.get(j));
                pi.addPublicationAuthor(si.getId());
                authors_add.put(pi.getId(),pi.getPublicationAuthor());
            }
        }
    }
    private void addtakeCourses(double evoChange){

        int size = c_Students.size();
        int num = (int) (size * evoChange);

        ArrayList list = _getRandomList(num, 0, c_Students.size()-1);
        for (int i = 0; i < list.size(); i++) {
            StudentInstance si = c_Students.get((int)list.get(i));
            int n = _getRandomFromRange(GRADSTUD_COURSE_MIN, GRADSTUD_COURSE_MAX);
            ArrayList list2 = _getRandomList(n, 0, c_GraduCourses.size() - 1);
            for (int j = 0; j < list2.size(); j++) {
                si.takeCourse(c_GraduCourses.get((int)list2.get(j)).getId());
            }
            takeCourses_add.put(si.getId(),si.getTakeCourses());
        }


    }

    private void addBasicEntity(double evoChange){
        int courseSum = instances_[CS_C_COURSE].count + c_RemainUnderCourses.size();
        for (int i =courseSum ; i < courseSum*(1+evoChange); i++) {
            UnderCourseInstance ui = new UnderCourseInstance();
            ui.setId(getNextId());
            ui.setName(_getRelativeName(CS_C_COURSE,i));
            c_RemainUnderCourses.add(ui);
        }
        courseSum = instances_[CS_C_GRADCOURSE].count + c_RemainGraduCourses.size();
        for (int i = courseSum; i < courseSum*(1+evoChange); i++) {
            GraduCourseInstance  gi = new GraduCourseInstance();
            gi.setId(getNextId());
            gi.setName(_getRelativeName(CS_C_GRADCOURSE, i));
            c_RemainGraduCourses.add(gi);
        }
        courseSum = instances_[CS_C_WEBCOURSE].count + c_RemainWEbCourses.size();
        for (int i = courseSum; i < courseSum*(1+evoChange); i++) {
            WebCourseInstance wi = new WebCourseInstance();
            wi.setId(getNextId());
            wi.setName(_getRelativeName(CS_C_WEBCOURSE,i));
            c_RemainWEbCourses.add(wi);
        }
        int pubNum = instances_[CS_C_PUBLICATION].count + c_RemainPublications.size();
        for(int i = pubNum;i<pubNum*(1+evoChange);i++){
            PublicationInstance pi = new PublicationInstance();
            pi.setId(getNextId());
            pi.setName(_getRelativeName(CS_C_PUBLICATION,i));
            c_RemainPublications.add(pi);
        }
    }
    public void _generateDept(int univIndex, int index,int versions, double evoChange){
        _generateDept(univIndex,index,versions,evoChange,"r");
    }

    void printLog(int version){
        this.log_.println("----------------------");
        this.log_.println("----------v"+version+"----------");
        String str;
        if(version == 0) {
            str = String.format("CourseSize: %d",(c_UnderCourses.size()+c_GraduCourses.size()));
            this.log_.println(str);
            str = String.format("professorSize: %d",c_Professors.size());
            this.log_.println(str);
            str = String.format("studentsSize: %d",c_Students.size());
            this.log_.println(str);
            str = String.format("PubSize: %d",c_Publications.size());
            this.log_.println(str);
        }
        else{
            str = String.format("CourseSize: %d",(c_UnderCourses.size()+c_GraduCourses.size()));
            this.log_.println(str);
            str = String.format("CourseAdd: %d   CourseDel: %d",c_GraduCourses_add.size()+c_UnderCourses_add.size(),c_GraduCourses_del.size()+c_UnderCourses_del.size());
            this.log_.println(str);


            str = String.format("professorSize: %d",c_Professors.size());
            this.log_.println(str);
            str = String.format("professorAdd: %d  professorAdd: %d",c_Professors_add.size(),c_Professors_del.size());
            this.log_.println(str);


            str = String.format("studentsSize: %d",c_Students.size());
            this.log_.println(str);
            str = String.format("studentsAdd: %d   studentsDel: %d",c_Students_add.size(),c_Students_del.size());
            this.log_.println(str);


            str = String.format("PubSize: %d",c_Publications.size());
            this.log_.println(str);
            str = String.format("PubAdd: %d  PubDel: %d",c_Publications_add.size(),c_Publications_del.size());
            this.log_.println(str);
        }
    }
    public void _generateDept(int univIndex, int index,int versions, double evoChange,String changeType){
        String type = changeType;
        for(int i = 0;i<versions;i++){
            String dirPath = System.getProperty("user.dir") + "\\Csv\\V"+i+"\\"+ _getName(CS_C_UNIV, univIndex)+ INDEX_DELIMITER + index;
            new File(dirPath).mkdirs();
            if( i== 0){ //v0
                _generateDept(univIndex,index,dirPath);
                printLog(i);
            }
            else{
                c_Professors_add.clear();
                c_Professors_del.clear();
                c_Students_add.clear();
                c_Students_del.clear();
                c_GraduCourses_add.clear();
                c_GraduCourses_del.clear();
                c_UnderCourses_add.clear();
                c_UnderCourses_del.clear();
                c_WEbCourses_add.clear();
                c_WEbCourses_del.clear();
                c_Publications_add.clear();
                c_Publications_del.clear();
                authors_add.clear();
                authors_del.clear();
                takeCourses_add.clear();
                takeCourses_del.clear();
                teachs_add.clear();
                teachs_del.clear();
                if(changeType.equals("r")){
                    type = i %2 == 0?"r+":"r-";
                }
                switch (type) {
                    case "r+":{
                        addBasicEntity(evoChange);
                        addProfessors(evoChange);
                        addStudents(evoChange);
                        WriteCsv(dirPath);
                        String deltaPath = new File(dirPath,"change").getAbsolutePath();
                        WriteCsvDelta(deltaPath);
                        printLog(i);
                        break;
                        //
                    }
                    case "r-":{
                        delAuthors(evoChange);
                        delTakeCourses(evoChange);
                        WriteCsv(dirPath);
                        String deltaPath = new File(dirPath,"change").getAbsolutePath();
                        WriteCsvDelta(deltaPath);
                        printLog(i);
                        break;
                    }
                    default:
                        break;

                }
            }

        }
    }

    public void _generateDept(int univIndex, int index){
        String dirPath = System.getProperty("user.dir") + "\\Csv\\"+ _getName(CS_C_UNIV, univIndex)+ INDEX_DELIMITER + index;
        _generateDept(univIndex,index,dirPath);
    }

    public void _generateDept(int univIndex, int index,String dirPath)  {
        //-  professor -> publication ,course
        //+

        //- student
        //+ student publication course ->>()

        //totalDeptsV0++;
        //String fileName = System.getProperty("user.dir") + "\\" + _getName(CS_C_UNIV, univIndex) + INDEX_DELIMITER + index + _getFileSuffix();


        new File(dirPath).mkdirs();
        _setInstanceInfo();

        c_Professors.clear();
        c_Students.clear();
        c_WEbCourses.clear();
        c_GraduCourses.clear();
        c_UnderCourses.clear();
        c_RemainGraduCourses.clear();
        c_RemainUnderCourses.clear();
        c_RemainWEbCourses.clear();

        c_Publications.clear();
        c_RemainPublications.clear();

        for (int i = 0; i < UNDER_COURSE_NUM; i++) {
            UnderCourseInstance ui = new UnderCourseInstance();
            ui.setId(getNextId());
            ui.setName(_getRelativeName(CS_C_COURSE,i));
            c_RemainUnderCourses.add(ui);
        }
        for (int i = 0; i < GRAD_COURSE_NUM; i++) {
            GraduCourseInstance  gi = new GraduCourseInstance();
            gi.setId(getNextId());
            gi.setName(_getRelativeName(CS_C_GRADCOURSE, i));
            c_RemainGraduCourses.add(gi);
        }
        for (int i = 0; i < WEB_COURSE_NUM; i++) {
           WebCourseInstance wi = new WebCourseInstance();
           wi.setId(getNextId());
           wi.setName(_getRelativeName(CS_C_WEBCOURSE,i));
           c_RemainWEbCourses.add(wi);
        }
        for(int i = 0;i<PUBLICATION_NUM;i++){
            PublicationInstance pi = new PublicationInstance();
            pi.setId(getNextId());
            pi.setName(_getRelativeName(CS_C_PUBLICATION,i));
            c_RemainPublications.add(pi);
        }



        for (int i = 0; i < CLASS_NUM; i++) {
            instances_[i].logNum = 0;
        }
        for (int i = 0; i < PROP_NUM; i++) {
            properties_[i].logNum = 0;
        }

        chair_ = random_.nextInt(instances_[CS_C_FULLPROF].total);

        int [] classes = new int[]{CS_C_FULLPROF,CS_C_VISITINGPROF,CS_C_ASSOPROF,CS_C_ASSTPROF,CS_C_LECTURER,CS_C_UNDERSTUD,CS_C_VISITSTUD,CS_C_GRADSTUD,CS_C_TA,CS_C_RA,CS_C_RESEARCHGROUP};


         if (index == 0) {
             //_generateASection(CS_C_UNIV, univIndex);
             _updateCount(CS_C_UNIV);
         }
        //_generateASection(CS_C_DEPT, index);
        _updateCount(CS_C_DEPT);

        for(int j = 0;j< instances_[CS_C_FULLPROF].num;j++){
            _updateCount(CS_C_FULLPROF);
            _generateAFullProf(j);
        }
        for(int j = 0;j< instances_[CS_C_VISITINGPROF].num;j++){
            _updateCount(CS_C_VISITINGPROF);
            _generateAVisitingProf(j);
        }
        for(int j = 0;j< instances_[CS_C_ASSOPROF].num;j++){
            _updateCount(CS_C_ASSOPROF);
            _generateAnAssociateProfessor(j);
        }
        for(int j = 0;j< instances_[CS_C_ASSTPROF].num;j++){
            _updateCount(CS_C_ASSTPROF);
            _generateAnAssistantProfessor(j);
        }
        for(int j = 0;j< instances_[CS_C_LECTURER].num;j++){
            _updateCount(CS_C_LECTURER);
            _generateALecturer(j);
        }
        for(int j = 0;j< instances_[CS_C_UNDERSTUD].num;j++){
            _updateCount(CS_C_UNDERSTUD);
            _generateAnUndergraduateStudent(j);
        }
        for(int j = 0;j< instances_[CS_C_VISITSTUD].num;j++){
            _updateCount(CS_C_VISITSTUD);
            _generateAVisitingStudent(j);
        }
        for(int j = 0;j< instances_[CS_C_GRADSTUD].num;j++){
            _updateCount(CS_C_GRADSTUD);
            _generateAGradudateStudent(j);
        }

        WriteCsv(dirPath);
        System.out.println(dirPath + " generated");

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



    private void _generateAProfessor(int index,int type,ProfessorInstance p) {

        //ProfessorInstance p = new ProfessorInstance();
       // p.setId(getNextId());

        String name = _getRelativeName(type, index);
        p.setName(name);

        if(type != CS_C_LECTURER){
            String ri = _getRelativeName(CS_C_RESEARCH, random_.nextInt(RESEARCH_NUM));
            p.setResearchInterest(ri);
        }

        p.setType(CLASS_TOKEN[type]);

       if(type == CS_C_VISITINGPROF){
           String n = _getRandomFromRange(1, 10)+" month(s)";
           p.setVisitDuration(n);
       }
       //AssociateProfessor,AssistantProfessor,Lecturer

        int courseNum = _getRandomFromRange(FACULTY_COURSE_MIN, FACULTY_COURSE_MAX);
        for (int i = 0; i < courseNum; i++) {
            int pos = _getRandomFromRange(0,c_RemainUnderCourses.size()-1);
            UnderCourseInstance ui = c_RemainUnderCourses.get(pos);
            c_RemainUnderCourses.remove(pos);
            c_UnderCourses.add(ui);
            c_UnderCourses_add.add(ui);
            instances_[CS_C_COURSE].count++;
            p.addCourse(ui.getId());
        }
        courseNum = _getRandomFromRange(FACULTY_GRADCOURSE_MIN, FACULTY_GRADCOURSE_MAX);
        for (int i = 0; i < courseNum; i++) {
           int pos = _getRandomFromRange(0,c_RemainGraduCourses.size()-1);
           GraduCourseInstance gi = c_RemainGraduCourses.get(pos);
           c_RemainGraduCourses.remove(pos);
           c_GraduCourses.add(gi);
           c_GraduCourses_add.add(gi);
           instances_[CS_C_GRADCOURSE].count++;
           p.addCourse(gi.getId());
        }
        for (int i = 0; i < courseNum; i++) {
            int pos = _getRandomFromRange(0,c_RemainWEbCourses.size()-1);
            WebCourseInstance wi = c_RemainWEbCourses.get(pos);
            String topic = "topic"+_getRandomFromRange(1, 150);
            String url = "http://example.com/webcourse/"+_getRandomFromRange(1, 150);
            int hours = _getRandomFromRange(9, 17);
            wi.setWebCourseTopic(topic);
            wi.setUrl(url);
            wi.setCourseHours(String.valueOf(hours));
            c_RemainWEbCourses.remove(pos);
            c_WEbCourses.add(wi);
            c_WEbCourses_add.add(wi);
            instances_[CS_C_WEBCOURSE].count++;
            p.addCourse(wi.getId());
        }
        String n = _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM));
        p.setUndergraduateDegreeFrom(n);
        n = _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM));
        p.setMastersDegreeFrom(n);
        n = _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM));
        p.setDoctoralDegreeFrom(n);
        String email = _getEmail(type, index);
        p.setEmailAddress(email);
        if (index == chair_) {
           p.setChair(true);
        }

        int num = 0;
        if(type == CS_C_FULLPROF || type == CS_C_VISITINGPROF) num = _getRandomFromRange(FULLPROF_PUB_MIN, FULLPROF_PUB_MAX);
        if(type == CS_C_ASSOPROF) num = _getRandomFromRange(ASSOPROF_PUB_MIN, ASSOPROF_PUB_MAX);
        if(type == CS_C_ASSTPROF) num = _getRandomFromRange(ASSTPROF_PUB_MIN, ASSTPROF_PUB_MAX);
        if(type == CS_C_LECTURER) num = _getRandomFromRange(LEC_PUB_MIN, LEC_PUB_MAX);
        for (int i = 0; i < num; i++) {
            //PublicationInstance pi = new PublicationInstance();
            int pos = _getRandomFromRange(0,c_RemainPublications.size()-1);
            PublicationInstance pi = c_RemainPublications.get(pos);
            //pi.setId(getNextId());
            //pi.setName(_getRelativeName(CS_C_PUBLICATION, i));
            pi.addPublicationAuthor(p.getId());
            c_RemainPublications.remove(pos);
            c_Publications.add(pi);
            c_Publications_add.add(pi);
            instances_[CS_C_PUBLICATION].count++;
            authors_add.put(pi.getId(),pi.getPublicationAuthor());
        }
        teachs_add.put(p.getId(),p.getCourses());
        //c_Professors.add(p);
    }

    /**
     * Generates a full professor instances.
     * @param index Index of the full professor.
     */

    private void _generateAFullProf(int index) {
        ProfessorInstance p = new ProfessorInstance();
        p.setId(getNextId());
        _generateAProfessor(index,CS_C_FULLPROF,p);
        c_Professors.add(p);
    }



    private void _generateAVisitingProf(int index) {

        ProfessorInstance p = new ProfessorInstance();
        p.setId(getNextId());
        _generateAProfessor(index,CS_C_VISITINGPROF,p);
        c_Professors.add(p);

    }

    /**
     * Generates an associate professor instance.
     * @param index Index of the associate professor.
     */
    private void _generateAnAssociateProfessor(int index) {

        ProfessorInstance p = new ProfessorInstance();
        p.setId(getNextId());
        _generateAProfessor(index,CS_C_ASSOPROF,p);
        c_Professors.add(p);


    }

    /**
     * Generates an assistant professor instance.
     * @param index Index of the assistant professor.
     */
    private void _generateAnAssistantProfessor(int index) {

        ProfessorInstance p = new ProfessorInstance();
        p.setId(getNextId());
        _generateAProfessor(index,CS_C_ASSTPROF,p);
        c_Professors.add(p);

    }

    /**
     * Generates a lecturer instance.
     * @param index Index of the lecturer.
     */
    private void _generateALecturer(int index) {

        ProfessorInstance p = new ProfessorInstance();
        p.setId(getNextId());
        _generateAProfessor(index,CS_C_LECTURER,p);
        c_Professors.add(p);

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

        StudentInstance si = new StudentInstance();
        si.setId(getNextId());

        int type = CS_C_UNDERSTUD;
        String name = _getRelativeName(type, index);
        String email = _getEmail(type, index);
        si.setName(name);
        si.setEmailAddress(email);
        si.setType(CLASS_TOKEN[type]);
        int n = _getRandomFromRange(UNDERSTUD_COURSE_MIN, UNDERSTUD_COURSE_MAX);
        ArrayList list = _getRandomList(n, 0, c_UnderCourses.size() - 1);
        for (int i = 0; i < list.size(); i++) {
            si.takeCourse(c_UnderCourses.get((int)list.get(i)).getId());
        }
        if (0 == random_.nextInt(R_UNDERSTUD_ADVISOR)) {
            boolean isOk = false;
            while(!isOk){
                int p =  _getRandomFromRange(0,c_Professors.size()-1);
                if(!c_Professors.get(p).getType().equals(CLASS_TOKEN[CS_C_LECTURER])){
                    si.setAdvisor(c_Professors.get(p).getId());
                    isOk = true;
                }
            }

        }
        c_Students.add(si);
        c_Students_add.add(si);
        takeCourses_add.put(si.getId(),si.getTakeCourses());
    }

    private void _generateAVisitingStudent(int index) {

        StudentInstance si = new StudentInstance();
        si.setId(getNextId());


        int type = CS_C_VISITSTUD;
        String name = _getRelativeName(type, index);
        String email = _getEmail(type, index);
        si.setName(name);
        si.setEmailAddress(email);
        si.setType(CLASS_TOKEN[type]);
        //_generateAStudent_a(CS_C_VISITSTUD, index, id);
        int n = _getRandomFromRange(VISITSTUD_COURSE_MIN, VISITSTUD_COURSE_MAX);
        ArrayList list = _getRandomList(n, 0, c_UnderCourses.size() - 1);
        for (int i = 0; i < list.size(); i++) {
            si.takeCourse(c_UnderCourses.get((int)list.get(i)).getId());
        }

        String d = _getRandomFromRange(1, 10)+" month(s)";

        si.setDurationOfVisit(d);


        if (0 == random_.nextInt(R_VISITSTUD_ADVISOR)) {
            boolean isOk = false;
            while(!isOk){
                int p =  _getRandomFromRange(0,c_Professors.size()-1);
                if(!c_Professors.get(p).getType().equals(CLASS_TOKEN[CS_C_LECTURER])){
                    si.setAdvisor(c_Professors.get(p).getId());
                    isOk = true;
                }
            }
        }
        c_Students.add(si);
        c_Students_add.add(si);
        takeCourses_add.put(si.getId(),si.getTakeCourses());
    }

    /**
     * Generates a graduate student instance.
     * @param index Index of the graduate student.
     */
    private void _generateAGradudateStudent(int index) {


        StudentInstance si = new StudentInstance();
        si.setId(getNextId());


        int type = CS_C_GRADSTUD;
        String name = _getRelativeName(type, index);
        String email = _getEmail(type, index);
        si.setName(name);
        si.setEmailAddress(email);
        si.setType(CLASS_TOKEN[type]);
        //_generateAStudent_a(CS_C_GRADSTUD, index, id);
        int n = _getRandomFromRange(GRADSTUD_COURSE_MIN, GRADSTUD_COURSE_MAX);
        ArrayList list = _getRandomList(n, 0, c_GraduCourses.size() - 1);
        for (int i = 0; i < list.size(); i++) {
            si.takeCourse(c_GraduCourses.get((int)list.get(i)).getId());
        }

        if (0 == random_.nextInt(R_GRADSTUD_ADVISOR)) {
            boolean isOk = false;
            while(!isOk){
                int p =  _getRandomFromRange(0,c_Professors.size()-1);
                if(!c_Professors.get(p).getType().equals(CLASS_TOKEN[CS_C_LECTURER])){
                    si.setAdvisor(c_Professors.get(p).getId());
                    isOk = true;
                }
            }

        }

        //_assignGraduateStudentPublications(id, GRADSTUD_PUB_MIN, GRADSTUD_PUB_MAX);

        //PublicationInfo publication;

        int num  = _getRandomFromRange(GRADSTUD_PUB_MIN, GRADSTUD_PUB_MAX);
        ArrayList list1 = _getRandomList(num, 0, c_Publications.size() - 1);
        for (int i = 0; i < list1.size(); i++) {
            PublicationInstance pi = c_Publications.get((int)list1.get(i));
            pi.addPublicationAuthor(si.getId());
            authors_add.put(pi.getId(),pi.getPublicationAuthor());
        }
        c_Students.add(si);
        c_Students_add.add(si);
        takeCourses_add.put(si.getId(),si.getTakeCourses());
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
