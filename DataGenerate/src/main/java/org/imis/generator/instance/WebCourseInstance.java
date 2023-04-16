package org.imis.generator.instance;

public class WebCourseInstance {
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

    public String getWebCourseTopic() {
        return webCourseTopic;
    }

    public void setWebCourseTopic(String webCourseTopic) {
        this.webCourseTopic = webCourseTopic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(String courseHours) {
        this.courseHours = courseHours;
    }

    int id;
    String name;
    String webCourseTopic;
    String url;
    String courseHours;
}
