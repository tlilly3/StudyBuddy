package com.thomas.studybuddy;

import java.util.List;

/**
 * Created by thomas on 4/10/17.
 */

public class ClassSession {
    private String section;
    private String professor;
    private String school;
    private String classNumber;
    private List<String> participants;
    private String host;

    public ClassSession(String section, String professor, String school, String classNumber, List<String> participants, String host) {
        this.section = section;
        this.professor = professor;
        this.school = school;
        this.classNumber = classNumber;
        this.participants = participants;
        this.host = host;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
