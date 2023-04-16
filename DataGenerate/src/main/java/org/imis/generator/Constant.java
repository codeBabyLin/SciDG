package org.imis.generator;

public class Constant {
    public final int CS_C_NULL = -1;
    /** University */
    public final int CS_C_UNIV = 0;
    /** Department */
    public final int CS_C_DEPT = CS_C_UNIV + 1;
    /** Faculty */
    public final int CS_C_FACULTY = CS_C_DEPT + 1;
    /** Professor */
    public final int CS_C_PROF = CS_C_FACULTY + 1;
    /** FullProfessor */
    public final int CS_C_FULLPROF = CS_C_PROF + 1;
    /** AssociateProfessor */
    public final int CS_C_ASSOPROF = CS_C_FULLPROF + 1;
    /** AssistantProfessor */
    public final int CS_C_ASSTPROF = CS_C_ASSOPROF + 1;
    /** Lecturer */
    public final int CS_C_LECTURER = CS_C_ASSTPROF + 1;
    /** Student */
    public final int CS_C_STUDENT = CS_C_LECTURER + 1;
    /** UndergraduateStudent */
    public final int CS_C_UNDERSTUD = CS_C_STUDENT + 1;
    /** GraduateStudent */
    public final int CS_C_GRADSTUD = CS_C_UNDERSTUD + 1;
    /** TeachingAssistant */
    public final int CS_C_TA = CS_C_GRADSTUD + 1;
    /** ResearchAssistant */
    public final int CS_C_RA = CS_C_TA + 1;
    /** Course */
    public final int CS_C_COURSE = CS_C_RA + 1;
    /** GraduateCourse */
    public final int CS_C_GRADCOURSE = CS_C_COURSE + 1;
    /** Publication */
    public final int CS_C_PUBLICATION = CS_C_GRADCOURSE + 1;
    /** Chair */
    public final int CS_C_CHAIR = CS_C_PUBLICATION + 1;
    /** Research */
    public final int CS_C_RESEARCH = CS_C_CHAIR + 1;
    /** ResearchGroup */
    public final int CS_C_RESEARCHGROUP = CS_C_RESEARCH + 1;

    public final int CS_C_VISITINGPROF = CS_C_RESEARCHGROUP + 1;

    public final int CS_C_VISITSTUD = CS_C_VISITINGPROF + 1;

    public final int CS_C_WEBCOURSE = CS_C_VISITSTUD + 1;  //21

    public final int CS_C_PROJECT = CS_C_WEBCOURSE + 1;

    public final int CS_C_EVENT = CS_C_PROJECT + 1;

    public final int CS_C_CONFPUBLICATION = CS_C_EVENT + 1;

    public final int CS_C_JOURNALPUBLICATION = CS_C_CONFPUBLICATION + 1;

    public final int CS_C_TECHNICALREPORT = CS_C_JOURNALPUBLICATION + 1;

    public final int CS_C_BOOK = CS_C_TECHNICALREPORT + 1;

    public final int CS_C_THESIS = CS_C_BOOK + 1;
    /** class information */
    public final int[][] CLASS_INFO = {
            /*{instance number if not specified, direct super class}*/
            //NOTE: the super classes specifed here do not necessarily reflect the entailment of the ontology
            {2, CS_C_NULL}, //CS_C_UNIV
            {1, CS_C_NULL}, //CS_C_DEPT
            {0, CS_C_NULL}, //CS_C_FACULTY
            {0, CS_C_FACULTY}, //CS_C_PROF
            {0, CS_C_PROF}, //CS_C_FULLPROF
            {0, CS_C_PROF}, //CS_C_ASSOPROF
            {0, CS_C_PROF}, //CS_C_ASSTPROF
            {0, CS_C_FACULTY}, //CS_C_LECTURER
            {0, CS_C_NULL}, //CS_C_STUDENT
            {0, CS_C_STUDENT}, //CS_C_UNDERSTUD
            {0, CS_C_STUDENT}, //CS_C_GRADSTUD
            {0, CS_C_NULL}, //CS_C_TA
            {0, CS_C_NULL}, //CS_C_RA
            {0, CS_C_NULL}, //CS_C_COURSE, treated as undergrad course here
            {0, CS_C_NULL}, //CS_C_GRADCOURSE
            {0, CS_C_NULL}, //CS_C_PUBLICATION
            {0, CS_C_NULL}, //CS_C_CHAIR
            {0, CS_C_NULL}, //CS_C_RESEARCH
            {0, CS_C_NULL}, //CS_C_RESEARCHGROUP
            {0, CS_C_PROF}, //CS_C_VISITPROF
            {0, CS_C_STUDENT}, //CS_C_VISITSTUD
            {0, CS_C_NULL}, //CS_C_RESEARCH
            {0, CS_C_NULL}, //CS_C_PROJECT
            {0, CS_C_NULL}, //CS_C_EVENT
            {0, CS_C_PUBLICATION}, //CS_C_CONFPUB
            {0, CS_C_PUBLICATION}, //CS_C_JOURNALPUB
            {0, CS_C_PUBLICATION}, //CS_C_TR
            {0, CS_C_PUBLICATION}, //CS_C_BOOK
            {0, CS_C_PUBLICATION}, //CS_C_THESIS
    };
    /** class name strings */
    public final String[] CLASS_TOKEN = {
            "University", //CS_C_UNIV
            "Department", //CS_C_DEPT
            "Faculty", //CS_C_FACULTY
            "Professor", //CS_C_PROF
            "FullProfessor", //CS_C_FULLPROF
            "AssociateProfessor", //CS_C_ASSOPROF
            "AssistantProfessor", //CS_C_ASSTPROF
            "Lecturer", //CS_C_LECTURER
            "Student", //CS_C_STUDENT
            "UndergraduateStudent", //CS_C_UNDERSTUD
            "GraduateStudent", //CS_C_GRADSTUD
            "TeachingAssistant", //CS_C_TA
            "ResearchAssistant", //CS_C_RA
            "Course", //CS_C_COURSE
            "GraduateCourse", //CS_C_GRADCOURSE
            "Publication", //CS_C_PUBLICATION
            "Chair", //CS_C_CHAIR
            "Research", //CS_C_RESEARCH
            "ResearchGroup", //CS_C_RESEARCHGROUP
            "VisitingProfessor",
            "VisitingStudent",
            "WebCourse",
            "ResearchProject",
            "Event",
            "ConferencePublication",
            "JournalArticle",
            "TechnicalReport",
            "Book",
            "Thesis"
    };
    /** number of classes */
    public final int CLASS_NUM = CLASS_INFO.length;
    /** index of instance-number in the elements of array CLASS_INFO */
    public final int INDEX_NUM = 0;
    /** index of super-class in the elements of array CLASS_INFO */
    public final int INDEX_SUPER = 1;
    public String[] CS_EVENT_TYPES = new String[] {"Conferene", "Workshop", "Summer School"};
    ///////////////////////////////////////////////////////////////////////////
    //ontology property information
    ///////////////////////////////////////////////////////////////////////////
    /** name */
    public final int CS_P_NAME = 0;
    /** takesCourse */
    public final int CS_P_TAKECOURSE = CS_P_NAME + 1;
    /** teacherOf */
    public final int CS_P_TEACHEROF = CS_P_TAKECOURSE + 1;
    /** undergraduateDegreeFrom */
    public final int CS_P_UNDERGRADFROM = CS_P_TEACHEROF + 1;
    /** mastersDegreeFrom */
    public final int CS_P_GRADFROM = CS_P_UNDERGRADFROM + 1;
    /** doctoralDegreeFrom */
    public final int CS_P_DOCFROM = CS_P_GRADFROM + 1;
    /** advisor */
    public final int CS_P_ADVISOR = CS_P_DOCFROM + 1;
    /** memberOf */
    public final int CS_P_MEMBEROF = CS_P_ADVISOR + 1;
    /** publicationAuthor */
    public final int CS_P_PUBLICATIONAUTHOR = CS_P_MEMBEROF + 1;
    /** headOf */
    public final int CS_P_HEADOF = CS_P_PUBLICATIONAUTHOR + 1;
    /** teachingAssistantOf */
    public final int CS_P_TAOF = CS_P_HEADOF + 1;
    /** reseachAssistantOf */
    public final int CS_P_RESEARCHINTEREST = CS_P_TAOF + 1;
    /** emailAddress */
    public final int CS_P_EMAIL = CS_P_RESEARCHINTEREST + 1;
    /** telephone */
    public final int CS_P_TELEPHONE = CS_P_EMAIL + 1;
    /** subOrganizationOf */
    public final int CS_P_SUBORGANIZATIONOF = CS_P_TELEPHONE + 1;
    /** worksFor */
    public final int CS_P_WORKSFOR = CS_P_SUBORGANIZATIONOF + 1;

    public final int CS_P_VISITSASPROF = CS_P_WORKSFOR + 1;

    public final int CS_P_VISITDURATION = CS_P_VISITSASPROF + 1;

    public final int CS_P_VISITSASSTUD = CS_P_VISITDURATION + 1;

    public final int CS_P_TOPIC = CS_P_VISITSASSTUD + 1;

    public final int CS_P_URL = CS_P_TOPIC + 1;

    public final int CS_P_HOURS = CS_P_URL + 1;

    public final int CS_P_DURATION = CS_P_HOURS + 1;

    public final int CS_P_FUNDEDBY = CS_P_DURATION + 1;

    public final int CS_P_SCIENTIFICADVISOR = CS_P_FUNDEDBY + 1;

    public final int CS_P_BUDGET = CS_P_SCIENTIFICADVISOR + 1;

    public final int CS_P_RESEARCHGROUP = CS_P_BUDGET + 1;

    public final int CS_P_EVENTTYPE = CS_P_RESEARCHGROUP + 1;

    public final int CS_P_EVENTORGANIZER = CS_P_EVENTTYPE + 1;

    public final int CS_P_DATE = CS_P_EVENTORGANIZER + 1;

    public final int CS_P_ISBN = CS_P_DATE + 1;

    public final int CS_P_VENUE = CS_P_ISBN + 1;

    public final int CS_P_EDITORINCHIEF = CS_P_VENUE + 1;

    public final int CS_P_REPORTID = CS_P_EDITORINCHIEF + 1;

    public final int CS_P_SUPERVISOR = CS_P_REPORTID + 1;
    /** property name strings */
    public final String[] PROP_TOKEN = {
            "name",
            "takesCourse",
            "teacherOf",
            "undergraduateDegreeFrom",
            "mastersDegreeFrom",
            "doctoralDegreeFrom",
            "advisor",
            "memberOf",
            "publicationAuthor",
            "headOf",
            "teachingAssistantOf",
            "researchInterest",
            "emailAddress",
            "telephone",
            "subOrganizationOf",
            "worksFor",
            "visitsAsProfessor",
            "durationOfVisit",
            "visitsAsStudent",
            "webCourseTopic",
            "url",
            "courseHours",
            "projectDuration",
            "fundedBy",
            "scientificAdvisor",
            "budget",
            "researchGroup",
            "eventType",
            "eventOrganizer",
            "date",
            "isbn",
            "venue",
            "editorInChief",
            "technicalReportID",
            "supervisor"

    };
    /** number of properties */
    public final int PROP_NUM = PROP_TOKEN.length;

    ///////////////////////////////////////////////////////////////////////////
    //restrictions for data generation
    ///////////////////////////////////////////////////////////////////////////
    /** size of the pool of the undergraduate courses for one department */
    public static final int UNDER_COURSE_NUM = 100; //must >= max faculty # * FACULTY_COURSE_MAX
    /** size of the pool of the graduate courses for one department */
    public static final int GRAD_COURSE_NUM = 100; //must >= max faculty # * FACULTY_GRADCOURSE_MAX

    public static final int WEB_COURSE_NUM = 100; //must >= max faculty # * FACULTY_GRADCOURSE_MAX

    public static final int PUBLICATION_NUM = 1000;
    /** size of the pool of universities */
    public static final int UNIV_NUM = 1000;
    /** size of the pool of reasearch areas */
    public static final int RESEARCH_NUM = 30;
    public static final int PROJECT_NUM_MIN = 10;
    public static final int PROJECT_NUM_MAX = 30;
    public static final int EVENT_NUM_MIN = 15;
    public static final int EVENT_NUM_MAX = 45;
    /** minimum number of departments in a university */
    public static final int DEPT_MIN = 15;
    /** maximum number of departments in a university */
    public static final int DEPT_MAX = 25;
    //must: DEPT_MAX - DEPT_MIN + 1 <> 2 ^ n
    /** minimum number of publications of a full professor */
    public static final int FULLPROF_PUB_MIN = 15;
    /** maximum number of publications of a full professor */
    public static final int FULLPROF_PUB_MAX = 20;
    /** minimum number of publications of an associate professor */
    public static final int ASSOPROF_PUB_MIN = 10;
    /** maximum number of publications of an associate professor */
    public static final int ASSOPROF_PUB_MAX = 18;
    /** minimum number of publications of an assistant professor */
    public static final int ASSTPROF_PUB_MIN = 5;
    /** maximum number of publications of an assistant professor */
    public static final int ASSTPROF_PUB_MAX = 10;
    /** minimum number of publications of a graduate student */
    public static final int GRADSTUD_PUB_MIN = 0;
    /** maximum number of publications of a graduate student */
    public static final int GRADSTUD_PUB_MAX = 5;
    /** minimum number of publications of a lecturer */
    public static final int LEC_PUB_MIN = 0;
    /** maximum number of publications of a lecturer */
    public static final int LEC_PUB_MAX = 5;
    /** minimum number of courses taught by a faculty */
    public static final int FACULTY_COURSE_MIN = 1;
    /** maximum number of courses taught by a faculty */
    public static final int FACULTY_COURSE_MAX = 2;
    /** minimum number of graduate courses taught by a faculty */
    public static final int FACULTY_GRADCOURSE_MIN = 1;
    /** maximum number of graduate courses taught by a faculty */
    public static final int FACULTY_GRADCOURSE_MAX = 2;
    /** minimum number of courses taken by a undergraduate student */
    public static final int UNDERSTUD_COURSE_MIN = 2;
    /** maximum number of courses taken by a undergraduate student */
    public static final int UNDERSTUD_COURSE_MAX = 4;
    public static final int VISITSTUD_COURSE_MIN = 2;
    /** maximum number of courses taken by a undergraduate student */
    public static final int VISITSTUD_COURSE_MAX = 4;
    /** minimum number of courses taken by a graduate student */
    public static final int GRADSTUD_COURSE_MIN = 1;
    /** maximum number of courses taken by a graduate student */
    public static final int GRADSTUD_COURSE_MAX = 3;
    /** minimum number of research groups in a department */
    public static final int RESEARCHGROUP_MIN = 10;
    /** maximum number of research groups in a department */
    public static final int RESEARCHGROUP_MAX = 20;
    //faculty number: 30-42
    /** minimum number of full professors in a department*/
    public static final int FULLPROF_MIN = 7;
    /** maximum number of full professors in a department*/
    public static final int FULLPROF_MAX = 10;
    public static final int VISITINGPROF_MIN = 7;
    /** maximum number of full professors in a department*/
    public static final int VISITINGPROF_MAX = 10;
    /** minimum number of associate professors in a department*/
    public static final int ASSOPROF_MIN = 10;
    /** maximum number of associate professors in a department*/
    public static final int ASSOPROF_MAX = 14;
    /** minimum number of assistant professors in a department*/
    public static final int ASSTPROF_MIN = 8;
    /** maximum number of assistant professors in a department*/
    public static final int ASSTPROF_MAX = 11;
    /** minimum number of lecturers in a department*/
    public static final int LEC_MIN = 5;
    /** maximum number of lecturers in a department*/
    public static final int LEC_MAX = 7;
    /** minimum ratio of undergraduate students to faculties in a department*/
    public static final int R_UNDERSTUD_FACULTY_MIN = 8;
    /** maximum ratio of undergraduate students to faculties in a department*/
    public static final int R_UNDERSTUD_FACULTY_MAX = 14;
    /** minimum ratio of undergraduate students to faculties in a department*/
    public static final int R_VISITSTUD_FACULTY_MIN = 2;
    /** maximum ratio of undergraduate students to faculties in a department*/
    public static final int R_VISITSTUD_FACULTY_MAX = 3;
    /** minimum ratio of graduate students to faculties in a department*/
    public static final int R_GRADSTUD_FACULTY_MIN = 3;
    /** maximum ratio of graduate students to faculties in a department*/
    public static final int R_GRADSTUD_FACULTY_MAX = 4;
    //MUST: FACULTY_COURSE_MIN >= R_GRADSTUD_FACULTY_MAX / R_GRADSTUD_TA_MIN;
    /** minimum ratio of graduate students to TA in a department */
    public static final int R_GRADSTUD_TA_MIN = 4;
    /** maximum ratio of graduate students to TA in a department */
    public static final int R_GRADSTUD_TA_MAX = 5;
    /** minimum ratio of graduate students to RA in a department */
    public static final int R_GRADSTUD_RA_MIN = 3;
    /** maximum ratio of graduate students to RA in a department */
    public static final int R_GRADSTUD_RA_MAX = 4;
    /** average ratio of undergraduate students to undergraduate student advising professors */
    public static final int R_UNDERSTUD_ADVISOR = 5;
    /** average ratio of undergraduate students to undergraduate student advising professors */
    public static final int R_VISITSTUD_ADVISOR = 2;
    /** average ratio of graduate students to graduate student advising professors */
    public static final int R_GRADSTUD_ADVISOR = 1;

    /** delimiter between different parts in an id string*/
    public static final char ID_DELIMITER = '/';
    /** delimiter between name and index in a name string of an instance */
    public static final char INDEX_DELIMITER = '_';
    /** name of the log file */
    public static final String LOG_FILE = "log.txt";

}
