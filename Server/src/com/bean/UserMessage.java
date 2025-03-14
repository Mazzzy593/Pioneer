package com.bean;

import java.io.Serializable;
import java.util.Arrays;

//ʵ�����û���Ϣ��
public class UserMessage implements Serializable{
	private static final long serialVersionUID=-5059525171312166179L;
	private String type;	//����
	private User user;		//�û�����
	private String str;     //����ֵΪ�ַ���
	private int log;
	private String[] MatchingKeys=new String[14]; // ����14���matchingkeys
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




	// Matchingkeys ����ƥ��
	@Override
	public String toString() {
		return "UserMessage [type=" + type + ", user=" + user + ", str=" + str + ", log=" + log + ",
		 MatchingKeys=" + Arrays.toString(MatchingKeys) + ", token=" + token + "]";
	}
	
}
