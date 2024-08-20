package com.dao;

import com.bean.User;	

//���ݿ���ʽӿ�
public interface UserDao {
//����û���Ϣ
boolean insert(User user);
//ɾ���û���Ϣ
boolean delete(User user);

//��ѯ�û���Ϣ
boolean select_users_ID(User user);
boolean select_users_key(User user);

//ע���û���Ϣ
String loginUser(User user);

//ҽ����Ա����token
String ApplyToken(User user);

//���token�Ƿ���ȷ
String CheckToken(User user);

//token��֤
String Upload(User user,String token);

//�ϴ���Ⱦ����Ϣ
String Update_Infectedusers() ;

//�ϴ����нӴ�����Ϣ
String Update_Contactedusers() ;

//���ظ�Ⱦ����Ϣ
String Download();
}
