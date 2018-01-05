package com.report.maker;

import java.util.List;

public class Student {
	private String name;
	private String email;
	private String major;
	private String advisor;
	private String grade_prob;
	
	private Semester recent_sem;
	private Semester recent_minus_one_sem;
	private Semester recent_minus_two_sem;


	private int critical_course_count;
	private int repeated_course_count;

	private List<String> critical_prog_courses;
	private List<String> repeated_courses;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getAdvisor() {
		return advisor;
	}

	public void setAdvisor(String advisor) {
		this.advisor = advisor;
	}

	public String getGrade_prob() {
		return grade_prob;
	}

	public void setGrade_prob(String grade_prob) {
		this.grade_prob = grade_prob;
	}

	public int getCritical_course_count() {
		return critical_course_count;
	}

	public void setCritical_course_count(int critical_course_count) {
		this.critical_course_count = critical_course_count;
	}

	public int getRepeated_course_count() {
		return repeated_course_count;
	}

	public void setRepeated_course_count(int repeated_course_count) {
		this.repeated_course_count = repeated_course_count;
	}

	public List<String> getCritical_prog_courses() {
		return critical_prog_courses;
	}

	public void setCritical_prog_courses(List<String> critical_prog_courses) {
		this.critical_prog_courses = critical_prog_courses;
	}

	public List<String> getRepeated_courses() {
		return repeated_courses;
	}

	public void setRepeated_courses(List<String> repeated_courses) {
		this.repeated_courses = repeated_courses;
	}

	public Semester getRecent_sem() {
		return recent_sem;
	}

	public void setRecent_sem(Semester recent_sem) {
		this.recent_sem = recent_sem;
	}

	public Semester getRecent_minus_one_sem() {
		return recent_minus_one_sem;
	}

	public void setRecent_minus_one_sem(Semester recent_minus_one_sem) {
		this.recent_minus_one_sem = recent_minus_one_sem;
	}

	public Semester getRecent_minus_two_sem() {
		return recent_minus_two_sem;
	}

	public void setRecent_minus_two_sem(Semester recent_minus_two_sem) {
		this.recent_minus_two_sem = recent_minus_two_sem;
	}
	

}
