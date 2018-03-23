package com.upic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.upic.po.User;
import com.upic.service.UserService;
@Service
public class UserServiceImpl implements UserService {

	private static Map<Long, User> userMap = new HashMap<Long, User>();

	@Override
	public User add(User user) {
		userMap.put(user.getId(), user);
		return user;
	}

	@Override
	public List<User> getAll() {
		List<User> u = new ArrayList<User>();
		userMap.forEach((x, y) -> {
			u.add(y);
		});
		return u;
	}

	@Override
	public User getUser(Long id) {
		return userMap.get(id);
	}

}
