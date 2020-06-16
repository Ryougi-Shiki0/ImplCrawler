package com.myproject.multifunctioncrawler.access;

import com.myproject.multifunctioncrawler.pojo.User;

/**
 * @author Arthas
 */
public class UserContext {
	
	private static ThreadLocal<User> userHolder = new ThreadLocal<>();
	
	public static void setUser(User user) {
		userHolder.set(user);
	}
	
	public static User getUser() {
		return userHolder.get();
	}

}
