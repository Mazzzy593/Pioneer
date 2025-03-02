package com.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;


import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.bean.User;
import com.bean.UserMessage;
import com.dao.TaskThread;
import com.dao.UserDao_Imp;

//HTTPS��Serverʵ��
public class PioneerServer {
	private int port = 446;
	private boolean isServerDone = false;
	public static void main(String[] args) throws IOException {
		PioneerServer server = new PioneerServer();
		server.run();
	}

	public PioneerServer() {
	}

	PioneerServer(int port) {
		this.port = port;
	}

	// ��������ʼ��SSLContext
	public SSLContext createSSLContext() {
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(new FileInputStream("./server.p12"), "010320".toCharArray());
			// �Լ�ʵ�����ι������࣬������������ָ����֤��
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			keyManagerFactory.init(keyStore, "010320".toCharArray());
			KeyManager[] km = keyManagerFactory.getKeyManagers();
			
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
			trustManagerFactory.init(keyStore);
			TrustManager[] tm = trustManagerFactory.getTrustManagers();

			// Initialize SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
			sslContext.init(km, tm, null);

			return sslContext;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	// ��������ʼ����
	@SuppressWarnings({ "resource", "null" })
	public void run() throws IOException {
		try {
			SSLContext sslContext = this.createSSLContext();

			// ����������socket factory
			SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

			// ����ServerSocket���͵Ķ����ṩ�˿ں�
			SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.port,3);
			
			System.out.println("This is  Pioneer Server!");
			System.out.println("Waiting for the client to connect......");
			System.out.println("SSL server started");
		
			// ��û�ÿͻ�������ʱ���������������accept��������
			new TaskThread().start();
			ExecutorService fixedThreadPool=Executors.newFixedThreadPool(3);
			
			while(!isServerDone){
				SSLSocket socket = (SSLSocket) serverSocket.accept();
                // �̳߳ص�ʵ��
                fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						 System.out.println("�߳� " + Thread.currentThread().getName() + " ����");
						 try {
							 new ServerThread(socket).run();
						 }catch(Exception e) {
						 }finally {
							 if(Thread.currentThread().isInterrupted()) {
								 Thread.currentThread().stop();
							 }
							 System.out.println("finally:"+Thread.currentThread().getName() + "stop");
						 }
					}
            		
            	});
            }
		}catch (Exception ex){
            ex.printStackTrace();
        }
	}
	
	// �̴߳���
    static class ServerThread {
    	
    	private SSLSocket serverSocket = null;
    	final int readTimeout=60*1000;
    	final int connectTimeout=10*1000;
		UserMessage userMessage = null;

        ServerThread(SSLSocket sslSocket){
            this.serverSocket = sslSocket;
        }
		@SuppressWarnings("null")
		public void run()  {
			DataInputStream in=null;
			DataOutputStream out=null;
        	try {
        		serverSocket.setSoTimeout(connectTimeout);//������������ӳ�ʱ
            	serverSocket.setEnabledCipherSuites(serverSocket.getSupportedCipherSuites());
    			// ���ֹ���
            	serverSocket.startHandshake();
            	serverSocket.setSoTimeout(readTimeout);
    			// �Ự��ȡ
    			SSLSession sslSession = serverSocket.getSession();
    			System.out.println("SSLSession :");
    			System.out.println("\tProtocol : " + sslSession.getProtocol());
    			System.out.println("\tCipher suite : " + sslSession.getCipherSuite());
    			// ����ɹ����ӵĿͻ��˵�ַ
    			System.out.println("�ͻ���" + serverSocket.getInetAddress() + "���ӳɹ���");
    			in=new DataInputStream(serverSocket.getInputStream());
    			out = new DataOutputStream(serverSocket.getOutputStream()); 
    		    out.flush();
    		    String token=null;
    		    while (true) {
    		         //ִ�й���
    		    	 byte[] bytes=new byte[10000];
    		         User user = new User();
    	    		 String tag=null;
    	    		 String str="";
    		         UserDao_Imp userDao_Imp = new UserDao_Imp();
    		         int input=0;
    			     //��ȡ�ͻ��˷��͵�����
    		         do {
    		        	 input = in.read(bytes);
    		        	 str=new String(bytes);
    		        	 if(str.substring(0,1).equals("5")||str.substring(0,1).equals("6")) {break;}
    		         }while(input<=1);
    		         if(input!=0) {
    		        	String ini_str="";
    		        	str="";
    		        	ini_str=new String(bytes);
    		        	for(int i=0;i<ini_str.length();i++) {
    	    				char ch=ini_str.charAt(i);
    	    				if(ch!='\0'&&ch!=' ') {
    	    					str+=ch;
    	    				}
    	    			}
    		        	System.out.println("�ͻ��˷��ͳ��ȣ�"+str.length());
    		        	
    		          }else {break;}
    		          //����type���͵Ĳ�ͬ��ִ�в�ͬ�������ݿ⽻���Ĳ���
   				      //����tag�Ĳ�ͬ�����ж� 
   				      //tag=0����ʾע���û���0+11λ�ֻ���+2048λSecret_key
   				      //tag=1����ʾ�ϴ�ǰ��֤token
    		          //tag=2����ʾ����token
   		              //tag=3.��ʾ�Զ��ϴ�14��Matching_key
    		          //tag=4����ʾͨ��token��֤���ϴ�14��Matching_key
    		          //tag=5����ʾ�ͻ����������������������infected_users��������Ϣ
    		          String log=null;
    		          tag=str.substring(0,1);
    		          System.out.println("tag:"+tag);
    				  switch(tag) {  
    				  case("0")://"LoginUser"  
    					  user.setId(str.substring(1,12));//��ȡ�ֻ���
    				  	  user.setKey(str.substring(12)); //��ȡSecretKey
    				  	  System.out.println(user.getId());
    				  	  System.out.println("key:"+user.getKey().length());
    					  log=userDao_Imp.loginUser(user); 
    					  System.out.println("����ֵ��"+log);
    					  break;
    				  case("1")://"CheckToken"  token����Ϊ��6λ
    					  user.setToken(str.substring(1,7));
    				  	  token=user.getToken();
    				  	  System.out.println("token"+user.getToken());
    				  	  System.out.println("length"+user.getToken().length());
    					  log=userDao_Imp.CheckToken(user); 
    				  	  System.out.println("����ֵ"+log); 
    				  	  break;
    				  case("2")://"ApplyToken" 
    					  //�û��������붼����Ϊ��7λ
    					  user.setName(str.substring(1,8));
    				      user.setPassword(str.substring(8,15));
    				      System.out.println(user.getName());
    				      System.out.println(user.getPassword());
    	 				  token=userDao_Imp.ApplyToken(user); 
    				  	  break; 
    				  case("3")://һ���ֶε�upload14��Matchingkeys
    					  user.setId(str.substring(1,12));
    				  	  System.out.println("Upload id"+user.getId());
    				  	  user.setMatching_keys(str.substring(12));
    				  	  System.out.println("MatchingKeys"+user.getMatching_keys());
    				  	  log=userDao_Imp.Upload(user, token);
  				  	      System.out.println("log"+log); 
    				  	  break; 
    				  case("4"): //auto upload
    					  user.setId(str.substring(1,12));
    				  	  System.out.println("�Զ��ϴ��Ӵ�����Ϣ");
    				  	  System.out.println("id"+user.getId());
    				  	  user.setMatching_keys(str.substring(12));
    				  	  System.out.println(user.getMatching_keys());
    				  	  log=userDao_Imp.AutoUpload(user);
    					  System.out.println("log:"+log); 
    				  	  break; 
    				  case("5"):
    					  System.out.println("�ͻ��˴ӷ��������ظ�Ⱦ����Ϣ");
    					  log=userDao_Imp.Download();
    				  	  System.out.println("log:"+log); 
    				  	  System.out.println("����:"+log.length()); 
    				  	  break;
    				  case("6"):
					  	  System.out.println("���¸�Ⱦ����Ϣ");
    					  log=userDao_Imp.Update_Infectedusers();
    				  	  log+=userDao_Imp.Update_Contactedusers();
    				  	  break;
    				  default:
    					  break;
    				  } 	
    				  if(tag.equals("0")) { 
    					  if(log.equals("0")){out.writeUTF("0");System.out.println("����success��Ϣ���ͻ���~");break;} 
    					  if(log.equals("1")){out.writeUTF("1");break;} 
    					  if(log.equals("2")){out.writeUTF("2");break;}
    					  out.flush();
    				  } 
    				  if(tag.equals("1")) { 
    					  out.writeUTF(log);
    					  out.flush();
    					  if(!log.equals("0")) {
    						  break;
    					  }
    					  System.out.println("����success��Ϣ���ͻ���~"); 
    				  }
    				  if(tag.equals("2")) { 
    					  System.out.println("����ֵ��"+token);
						  out.writeUTF(token);
    					  out.flush();
    					  break;
    				  }
    				  if(tag.equals("3")) {    
    					  System.out.println("log:"+log);
    					  out.writeUTF(log);
						  out.flush();
						  if(log.equals("0")) {
	    					  System.out.println("����success��Ϣ���ͻ���~"); 
	    					  break;
	    				  }
    				  }
    				  if(tag.equals("4")) { 
    					  out.writeUTF(log);
						  out.flush();
						  if(log.equals("0")) {
	    					  System.out.println("�Զ��ϴ��Ӵ�����Ϣ�ɹ�~"); 
	    					  break;
	    				  }
    				  }
    				  if(tag.equals("5")) {
    					  if(log.equals(null)) {
    						  log="1";
    					  }
    					  out.writeUTF(log);
						  out.flush();
						  System.out.println("��Ⱦ����Ϣ���سɹ�");
						  break;
    				  }
    				  if(tag.equals("6")) {
    					  out.writeUTF(log);
						  out.flush();
						  break;
    				  }
    			} 
            }catch(IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("finally:"+Thread.currentThread().getName() + " interrupt");
			System.out.println("���̷߳���������");
			// �ͷ���Դ
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != serverSocket) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//�ж��߳�
			Thread.currentThread().interrupt();
		}
    }
  }
}