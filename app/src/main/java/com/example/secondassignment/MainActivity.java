package com.example.secondassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;
import User.User;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("Users", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void btnLogINOnclick(View view) {
        Intent intent;
        if(sharedPreferences.contains("hasElement")){
            editor.remove("hasElement");
            editor.commit();
            boolean doesRememberedUsersExist = false;

            Map<String, ?> map = sharedPreferences.getAll();
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                String userJson = entry.getValue().toString();
                Gson gson = new Gson();
                User user = gson.fromJson(userJson, User.class);
                if(user.isRemembered()){
                    doesRememberedUsersExist = true;
                    break;
                }
            }
            editor.putBoolean("hasElement", true);
            editor.commit();
            if(doesRememberedUsersExist) {
                intent = new Intent(this, continue_as.class);
            }
            else{
                intent = new Intent(this, log_in.class);
            }
        }
        else {
            intent = new Intent(this, log_in.class);
        }
        startActivity(intent);
    }

    public void btnSignUpOnclick(View view) {
        Intent intent = new Intent(this, sign_up.class);
        startActivity(intent);
    }
}