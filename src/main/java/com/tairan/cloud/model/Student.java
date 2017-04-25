package com.tairan.cloud.model;

public class Student{
	private int num;
	private String name;
	private int x;
	private int y;
	private String profession;
	private String college;

	public Student(int num, String name, int x, int y, String profession, String college) {
		super();
		this.num = num;
		this.name = name;
		this.x = x;
		this.y = y;
		this.profession = profession;
		this.college = college;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	@Override
	public String toString() {
		return "Student [num=" + num + ", name=" + name + ", x=" + x + ", y=" + y + ", profession=" + profession
				+ ", college=" + college + "]";
	}
	
}
