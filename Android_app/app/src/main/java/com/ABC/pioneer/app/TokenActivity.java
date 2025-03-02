package com.ABC.pioneer.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.ABC.pioneer.sensor.payload.Crypto.SpecificUsePayloadSupplier;
import com.ABC.pioneer.sensor.payload.Crypto.GenerateKey;
import com.ABC.pioneer.sensor.payload.Crypto.MatchingKey;
import com.ABC.pioneer.sensor.service.Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

