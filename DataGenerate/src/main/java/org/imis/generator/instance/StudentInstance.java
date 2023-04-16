package org.imis.generator.instance;

import java.util.ArrayList;

public class StudentInstance {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(String memberOf) {
        this.memberOf = memberOf;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getAdvisor() {
        return advisor;
    }

    public void setAdvisor(int advisor) {
        this.advisor = advisor;
    }

    public String getUndergraduateDegreeFrom() {
        return undergraduateDegreeFrom;
    }

    public void setUndergraduateDegreeFrom(String undergraduateDegreeFrom) {
        this.undergraduateDegreeFrom = undergraduateDegreeFrom;
    }

    public String getDurationOfVisit() {
        return durationOfVisit;
    }

    public void setDurationOfVisit(String durationOfVisit) {
        this.durationOfVisit = durationOfVisit;
    }

    public boolean isResearchAssistant() {
        return isResearchAssistant;
    }

    public void setResearchAssistant(boolean researchAssistant) {
        isResearchAssistant = researchAssistant;
    }

    public int getTeachingAssistantOf() {
        return teachingAssistantOf;
    }

    public void setTeachingAssistantOf(int teachingAssistantOf) {
        this.teachingAssistantOf = teachingAssistantOf;
    }

    public void takeCourse(int id){
        this.takeCourses.add(id);
    }
    public void clearCourse(){
        this.takeCourses.clear();
    }

    int id;
    String name;
    String type;
    String memberOf;
    String emailAddress;

    public ArrayList<Integer> getTakeCourses() {
        ArrayList<Integer> copy = new ArrayList<>();
        for(int i = 0;i<takeCourses.size();i++){
            copy.add(takeCourses.get(i));
        }
        return copy;
    }

    ArrayList<Integer> takeCourses = new ArrayList<>();
    int advisor;
    String undergraduateDegreeFrom;
    String durationOfVisit;
    boolean isResearchAssistant;
    int teachingAssistantOf;
}
