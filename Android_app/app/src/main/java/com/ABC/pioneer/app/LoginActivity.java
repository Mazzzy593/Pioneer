package com.ABC.pioneer.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


//登陆界面
public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private Button loginbtn;
    private Button regisbtn_jump;
    private EditText et_phone;
    // 保存所有用户的数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //在加载布局文件前判断是否登陆过
        sp = getSharedPreferences("PhoneNumber",MODE_PRIVATE);
        //.getBoolean("PhoneNumber",false)；当找不到"PhoneNumber"所对应的键值时默认返回false
        if(!sp.getString("PhoneNumber","").equals("")){
            Intent intent=new Intent(getApplication(),MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn = (Button)findViewById(R.id.btn_login);
        regisbtn_jump = (Button)findViewById(R.id.btn_register_jump);
        et_phone = (EditText)findViewById(R.id.et_phone);
        });

        //给regisbtn设置进入注册界面事件
        regisbtn_jump.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(),RegisterActivity.class);//启动RegisterActivity
                startActivity(it);
            }
        });
    }



}