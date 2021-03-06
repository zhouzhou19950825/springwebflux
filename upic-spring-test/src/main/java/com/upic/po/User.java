package com.upic.po;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonView;

public class User implements Serializable{

	public interface father{}
	public interface son extends father{}
	@JsonView(son.class)
	private Long id;
	@JsonView(father.class)
	private String stuName;
	@JsonView(father.class)
	private String stuNum;
	@JsonView(father.class)
	private String sex;
	private Date birthday;
	
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
	
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", stuName=" + stuName + ", stuNum=" + stuNum + "]";
	}
	public User(Long id, String stuName, String stuNum, String sex) {
		super();
		this.id = id;
		this.stuName = stuName;
		this.stuNum = stuNum;
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
}
