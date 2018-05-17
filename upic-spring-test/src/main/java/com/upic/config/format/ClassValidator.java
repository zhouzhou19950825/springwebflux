package com.upic.config.format;

import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.upic.po.User;

public class ClassValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
			if((clazz.getSimpleName().equals("User"))) {
				return true;
			}
		
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(!(target instanceof User)) {
			return ;
		}
		User u=(User) target;
		if(u.getBirthday()==null) {
			u.setBirthday(new Date());
		}
	}

}
