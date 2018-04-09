package com.upic.po;

public class User {

	private Long id;
	private String stuName;
	private String stuNum;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getStuNum() {
		return stuNum;
	}
	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", stuName=" + stuName + ", stuNum=" + stuNum + "]";
	}
}
