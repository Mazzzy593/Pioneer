package com.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.Scanner;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.view.View;

// Pioneer Client
public class PioneerClient {
	static int item;
	// host ip
    private String host = "192.168.43.19";
	// https connection
    private int port = 446;
    
    public static void main(String[] args) throws UnknownHostException, IOException{
        PioneerClient client = new PioneerClient();
        client.run();
    }
       
    PioneerClient(){      
    }
     
    PioneerClient(String host, int port){
        this.host = host;
        this.port = port;
    }
     
    // Create the and initialize the SSLContext
    private SSLContext createSSLContext(){
   	 try{
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
			// load local files for manage
            keyStore.load(new FileInputStream("D:\\Java\\cert\\Pioneer.keystore"),"sducst".toCharArray());
             
            // Create key manager
            // KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            // keyManagerFactory.init(keyStore, "sducst".toCharArray());
            // KeyManager[] km = keyManagerFactory.getKeyManagers();
            // keyStore.load(new FileInputStream("D:\\Java\\cert\\server.p12"), "010320".toCharArray());

            // keyStore.load(new FileInputStream("D:\\Java\\cert\\server.p12"), "010320".toCharArray());

			// Create key manager
			// KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			// keyManagerFactory.init(keyStore, "010320".toCharArray());
			// KeyManager[] km = keyManagerFactory.getKeyManagers();
			
            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            TrustManager[] tm = trustManagerFactory.getTrustManagers();
             
            // Initialize SSLContext 
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(km,  tm, null);
             
            return sslContext;
        } catch (Exception ex){
            ex.printStackTrace();
        }
         
        return null;
   }
     
    // Start to run the server

    
    public void run() throws IOException{
    	final int connectTimeout=5*1000;
		//SSLContext
		SSLContext sslContext = this.createSSLContext();
		 // Create socket factory
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        //����Socket���͵Ķ��󣬲��ṩ���������������Ͷ˿ں�
        SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(this.host, this.port);
        SSLSocket socket = (SSLSocket) sslSocketFactory.getDefault().createSocket();

        //SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket();
        socket.connect(new InetSocketAddress(host,port), connectTimeout);//�������ӳ�ʱ5s
        System.out.println("This is the Pioneer Client!");
		System.out.println("Successfully connected to the server"); 
        System.out.println("SSL client started");
     
        new ClientThread(socket).start();
    }
     
    static class ClientThread extends Thread{
    	private SSLSocket socket = null;
    	final int handshakeTimeout=3*1000;
    	final int sessionTimeout=7*1000;
     
        ClientThread(SSLSocket sslSocket){
            this.socket = sslSocket;
        }
    	public void run() {	
        	Scanner sc=null;
        	DataInputStream in=null;
			DataOutputStream out=null;
    		try {
    		
    			socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
    			socket.setSoTimeout(handshakeTimeout);
    			// Start handshake
    			socket.startHandshake();
    			socket.setSoTimeout(sessionTimeout);
    			 // Get session after the connection is established
                SSLSession sslSession = socket.getSession();
                System.out.println("SSLSession :");
                System.out.println("\tProtocol : "+sslSession.getProtocol());
                System.out.println("\tCipher suite : "+sslSession.getCipherSuite());
               
    			//��ʼ�����������
    			sc=new Scanner(System.in);
    			in=new DataInputStream(socket.getInputStream());
    			out = new DataOutputStream(socket.getOutputStream()); 

    			//ͨ���˵��������ӿ���̨����û��������Ϣ��������ֵ��Ϊ�½��Ķ���
    			String input=null;
    			String output="";
    			String str=null;
    			
    			String type=null;
				//tag=0����ʾע���û���0+11λ�ֻ���+2048λSecret_key
				//tag=1����ʾ�ϴ�ǰ��֤token
		        //tag=2����ʾ����token
		        //tag=3.��ʾ�Զ��ϴ�14��Matching_key
		        //tag=4����ʾͨ��token��֤���ϴ�14��Matching_key
    			//tag=5����ʾ���ط������洢��Infected_users��14���Matching_keys
    			//tag=6����ʾÿ�춨ʱ�ĸ��µĹ���
    			item = View.MenuView();
    			switch(item) {
    				//���ݲ˵�ѡ�񣬳�ʼ������
    				case 0://�˳�
    					type="Exit";
    					break;
    				case 1://�ֻ���ע��
    					type="LoginUser";
    					output="0";
    					break;
    				case 2://���������token�Ƿ���ȷ�������ϴ�
    					type="CheckToken";
    					output="1";
    					break;
    				case 3://ҽ����Ա����Ա����Token
    					type="ApplyToken";
    					output="2";
    					break;
    				case 4://�Զ��ϴ�
    					type="AutoUpload";
    					output="4";
    					break;
    				case 5://�ͻ������ظ�Ⱦ����Ϣ
    					type="Download";
    					break;
    				case 6://���¸�Ⱦ���Լ��Ӵ�����Ϣ
    					type="Update";
    					break;
    			 	}
    			//�������
    			output+=MenuView();//Ҫ������ַ���
    			System.out.println("���͵�����������Ϊ:"+output.length());
    			//��������ĳ��ȶ�̬�ķ���
    			byte[] out_bytes=new byte[output.length()];
    			out_bytes=output.getBytes();
    			
    			//bytes=input;
    			//�����û���Ϣ��������
    			out.write(out_bytes);
    			out.flush();
    			System.out.println("�ѷ�����Ϣ�������~:"+output);
    			
    			//�������, ��ȡ����˻ط�����֤���
    			//if(!type.equals("AutoUpload")) {
    				try { 
        				str=in.readUTF();
        				System.out.println("�ӷ������յ���֤�����"+str);
        				if(type.equals("Download")) {
        					int i =0;
        					int index1=0;
        					int index2=0;
        					String temp="";
        					while(true) {
        						index1=str.indexOf("SDU",i);
        						if(index1==-1) break;
        						index2=str.indexOf("SDU",index1+3);
        						if(index2==-1) break;
        						temp=str.substring(index1+3, index2);
        						System.out.println("temp:"+temp);
        						i=index2;
        					}
        				}
            		    if(str.equals("0")&&type.equals("CheckToken")) {
            		        output ="3";
            		        output += View.UploadMenuView();
            		        out_bytes=new byte[10000];
            		        out_bytes=output.getBytes();
            		        out.write(out_bytes);
            		    	out.flush();
            		    	System.out.println("�ѷ�����Ϣ�������~:"+output);
            		    	//System.out.println("�ѷ�����Ϣ�������~:"+out_bytes);
            		    	
            		    	str=in.readUTF();
            				System.out.println("�ӷ������յ���֤�����"+str);
            		    }
        		        //0��ʾע��ɹ�,1��ʾ�ֻ����ظ���2��ʾ��Կ�ظ�
        			}catch(Exception e) {
        				e.printStackTrace();
        			}
    			//}
    			}catch(IOException e) {
    				e.printStackTrace();
    		}finally {
    			System.out.println("�ͻ������ߣ��ͷ���Դ");
    			//�ͷ���Դ
    			if(null!=out) {
    				try {
    					out.close();
    				}catch(IOException e) {
    					e.printStackTrace();
    				}
    			}
    			if(null!=in) {
    				try {
    					in.close();
    				}catch(IOException e) {
    					e.printStackTrace();
    				}
    			}
    			if(null!=socket) {
    				try {
    					socket.close();
    				}catch(IOException e) {
    					e.printStackTrace();
    				}
    			}
    			if(null!=sc) {
    				sc.close();
    			}
    		}
    	}
    }
    
    
    private static String MenuView() {
		while(true) {
			String input=null;
			switch(item) {
			case 0://�˳�
				System.exit(-1);
				break;
			case 1://ע���û�
				input= View.loginUserMenuView();
				return input;
			case 2://�û��ϴ�token
				input=View.CheckTokenMenuView();
				return input;
			case 3://ҽ����Աtoken����
				input= View.ApplyTokenMenuView();
				return input;
			case 4://�Զ��ϴ�Matching_key
				input=View.AutoUploadView();
				return input;
			case 5:
				input="5";
				return input;
			case 6:
				input="6";
				return input;
			default:
				break;
			}
		}
    }
    
}