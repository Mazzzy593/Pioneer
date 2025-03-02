package com.ABC.pioneer.app;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ABC.pioneer.sensor.SensorArray;
import com.ABC.pioneer.sensor.payload.Crypto.GenerateKey;
import com.ABC.pioneer.sensor.payload.Crypto.SecretKey;
import com.ABC.pioneer.sensor.payload.Crypto.SpecificUsePayloadSupplier;
import com.ABC.pioneer.sensor.service.Connection;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private Button regisbtn;
    private EditText et_phone;
    private String result=new String();
    private final Context context = AppDelegate.getInstance();

    // register
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final SharedPreferences sp_PhoneNumber = getSharedPreferences("PhoneNumber",MODE_PRIVATE);
        regisbtn = (Button)findViewById(R.id.btn_register);
        et_phone = (EditText)findViewById(R.id.et_phone_1);
    }




}
