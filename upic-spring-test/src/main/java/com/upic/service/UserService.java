package com.upic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.upic.po.User;

public interface UserService {

	public User add(User user);
	
	public List<User> getAll();
	
	public User getUser(Long id);
}
