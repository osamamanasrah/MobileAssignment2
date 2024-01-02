package com.example.secondassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import User.User;

public class log_in extends AppCompatActivity {

    private EditText edtEmail;
    private EditText editPassword;
    private CheckBox checkBox;
    private Button submit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setUpViews();
        sharedPreferences = getSharedPreferences("Users", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void setUpViews() {
        edtEmail = findViewById(R.id.edtEmail);
        editPassword = findViewById(R.id.edtPassword);
        checkBox = findViewById(R.id.checkbox);
        submit = findViewById(R.id.btnSubmit);
    }


    public void btnSubmitOnclick(View view) {

        if (edtEmail.getText().toString() != null && editPassword.getText().toString() != null) {

            boolean EmailExists = sharedPreferences.contains(edtEmail.getText().toString());
            if (!EmailExists) {
                Toast.makeText(this, "No such Email", Toast.LENGTH_SHORT).show();
            } else {
                String userJson = sharedPreferences.getString(edtEmail.getText().toString(), "");
                Gson gson = new Gson();
                User user = gson.fromJson(userJson, User.class);
                if(user.getPassword().equals(editPassword.getText().toString())) {
                    if (checkBox.isChecked()) {
                        user.setRemembered(true);
                        editor.remove(edtEmail.getText().toString());
                        editor.commit();
                        String editedUserJson =  gson.toJson(user, User.class);
                        editor.putString(edtEmail.getText().toString(), editedUserJson);
                        editor.commit();
                    }
                    Intent intent = new Intent(this, Converter.class);
                    intent.putExtra("User", user.getEmail());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(this, "Fill Email and password", Toast.LENGTH_SHORT).show();
        }
    }
}