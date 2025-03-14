package com.bean;

import java.io.Serializable;
import java.util.Arrays;

//实例化用户信息类
public class UserMessage implements Serializable{
	private static final long serialVersionUID=-5059525171312166179L;
	private String type;	//类型
	private User user;		//用户对象
	private String str;     //返回值为字符串
	private int log;
	private String[] MatchingKeys=new String[14]; // 用于14天的matchingkeys
	private String token;

	//methods for user messages
	public UserMessage() {	}

	public UserMessage(int log) {
		this.log=log;
	}
	public UserMessage(String str) {
		this.str=str;
	}
	public UserMessage(String type,String str) {
		this.type=type;
		this.str=str;
	}
	public UserMessage(String type,User user) {
		this.type=type;
		this.user=user;
	}
	public UserMessage(String type,User user,String token) {
		this.type=type;
		this.user=user;
		this.token=token;
	}

	//getters and setters
	public String getType() {
		return type;
	}
	public User getUser() {
		return user;
	}
	public String getStr() {
		return str;
	}
	public int getLog() {
		return log;
	}
	public String getToken() {
		return token;
	}

	// set str, user, type, token
	public void setType(String type) {
		this.type = type;
	}
	public void setStr(String str) {
		this.str=str;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public void setToken(String token) {
		this.token = token;
	}
	public void setLog(int log) {
		this.log = log;
	}
	// Matchingkeys
	public void setMatchingKeys(int i,String MatchingKey) {
		MatchingKeys[i] = MatchingKey;
	}
	public String[] getMatchingKeys() {
		return MatchingKeys;
	}




	// Matchingkeys 用于匹配
	@Override
	public String toString() {
		return "UserMessage [type=" + type + ", user=" + user + ", str=" + str + ", log=" + log + ",
		 MatchingKeys=" + Arrays.toString(MatchingKeys) + ", token=" + token + "]";
	}
	
}
