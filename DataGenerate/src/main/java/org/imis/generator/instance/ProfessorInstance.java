package org.imis.generator.instance;

import java.util.ArrayList;

public class ProfessorInstance {
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Integer> getCourses() {
        return Courses;
    }

    public boolean isChair() {
        return isChair;
    }

    public String getUndergraduateDegreeFrom() {
        return undergraduateDegreeFrom;
    }

    public String getMastersDegreeFrom() {
        return mastersDegreeFrom;
    }

    public String getDoctoralDegreeFrom() {
        return doctoralDegreeFrom;
    }

    public String getWorksFor() {
        return worksFor;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getResearchInterest() {
        return researchInterest;
    }

    int id;
    String name;
    String type;
    ArrayList<Integer> Courses = new ArrayList<>();
    boolean isChair = false;


    public void setChair(boolean isChair){
        this.isChair = isChair;
    }

    public void addCourse(int id){
        Courses.add(id);
    }

    public void setUndergraduateDegreeFrom(String undergraduateDegreeFrom) {
        this.undergraduateDegreeFrom = undergraduateDegreeFrom;
    }

    public void setMastersDegreeFrom(String mastersDegreeFrom) {
        this.mastersDegreeFrom = mastersDegreeFrom;
    }

    public void setDoctoralDegreeFrom(String doctoralDegreeFrom) {
        this.doctoralDegreeFrom = doctoralDegreeFrom;
    }

    public void setWorksFor(String worksFor) {
        this.worksFor = worksFor;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setResearchInterest(String researchInterest) {
        this.researchInterest = researchInterest;
    }

    String undergraduateDegreeFrom;
    String mastersDegreeFrom;
    String doctoralDegreeFrom;
    String worksFor;
    String emailAddress;
    String researchInterest;

    public String getVisitDuration() {
        return visitDuration;
    }

    public void setVisitDuration(String visitDuration) {
        this.visitDuration = visitDuration;
    }

    String visitDuration;
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setType(String type){
        this.type = type;
    }
}
