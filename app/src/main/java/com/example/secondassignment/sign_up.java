package com.example.secondassignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import User.User;

public class sign_up extends AppCompatActivity {
    private EditText userName;
    private EditText Email;
    private EditText password;
    private EditText confirmPassword;
    private CheckBox checkBox;
    private RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SharedPreferences saftySharedPreference;
    SharedPreferences.Editor saftyeditor;
    private final String saftyUsername = "username";
    private final String saftyEmail = "Email";
    private final String saftyPassword = "pass";
    private final String saftyConfirmPassword = "confirm";
    private final String saftyRememberMe = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpViews();
        sharedPreferences = getSharedPreferences("Users", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        requestQueue = Volley.newRequestQueue(this);
        saftySharedPreference = getSharedPreferences("safty", Context.MODE_PRIVATE);
        saftyeditor = saftySharedPreference.edit();
        refill();
    }

    private void setUpViews() {
        userName = findViewById(R.id.edtSignUserName);
        Email = findViewById(R.id.edtSignEmail);
        password = findViewById(R.id.edtSignPass);
        confirmPassword = findViewById(R.id.edtSignConfirm);
        checkBox = findViewById(R.id.checkboxSign);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saftyeditor.putString(saftyUsername, userName.getText().toString());
        saftyeditor.putString(saftyEmail,Email.getText().toString());
        saftyeditor.putString(saftyPassword,password.getText().toString());
        saftyeditor.putString(saftyConfirmPassword, confirmPassword.getText().toString());
        saftyeditor.putBoolean(saftyRememberMe,checkBox.isChecked());
        saftyeditor.commit();
    }
    private void refill(){
        String username = saftySharedPreference.getString(saftyUsername, "");
        String pass = saftySharedPreference.getString(saftyPassword,"");
        String email = saftySharedPreference.getString(saftyEmail, "");
        String confirmation = saftySharedPreference.getString(saftyConfirmPassword, "");
        boolean rememberMe = saftySharedPreference.getBoolean(saftyRememberMe, false);
        userName.setText(username);
        password.setText(pass);
        Email.setText(email);
        confirmPassword.setText(confirmation);
        checkBox.setChecked(rememberMe);
    }

    public void btnSignSubmitOnclick(View view) {
        if(userName.getText().toString() != null){
            boolean validUserName = userName.getText().length() >= 4;
            if(validUserName){
                if(Email.getText().toString() != null){

                    String url = "https://validemail.io/v1/validate?api_key=N2y37bPQiwvuOWzWVJwlQXlmQjs1jV5e&email=" + Email.getText().toString();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getString("is_valid").equals("Invalid")){
                                    Toast.makeText(sign_up.this, "The email you entered doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    boolean validEmail = true;
                                    boolean sharedPreferenceNotEmpty = sharedPreferences.contains("hasElement");
                                    if (sharedPreferenceNotEmpty) {
                                        editor.remove("hasElement");
                                        editor.commit();
                                        Map<String, ?> map = sharedPreferences.getAll();
                                        for (Map.Entry<String, ?> entry : map.entrySet()) {
                                            if (entry.getKey().equals(Email.getText().toString())) {
                                                validEmail = false;
                                                break;
                                            }
                                        }
                                        editor.putBoolean("hasElement", true);
                                        editor.commit();
                                    }
                                    if(validEmail){
                                        if (password.getText().toString() != null) {
                                            if (password.getText().toString().length() >= 8) {
                                                if (confirmPassword.getText().toString() != null) {
                                                    if (confirmPassword.getText().toString().equals(password.getText().toString())) {
                                                        User newUser = new User(userName.getText().toString(), Email.getText().toString(), password.getText().toString(), checkBox.isChecked());
                                                        Gson gson = new Gson();
                                                        String jsonString = gson.toJson(newUser);
                                                        editor.putString(Email.getText().toString(), jsonString);
                                                        if (!sharedPreferences.contains("hasElement")) {
                                                            editor.putBoolean("hasElement", true);
                                                        }
                                                        editor.commit();
                                                        Intent intent = new Intent(sign_up.this, Converter.class);
                                                        intent.putExtra("User", newUser.getEmail());
                                                        startActivity(intent);
                                                    }
                                                    else {
                                                        Toast.makeText(sign_up.this, "The password confirmation doesn't match", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else {
                                                Toast.makeText(sign_up.this, "Please confirm the password", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                Toast.makeText(sign_up.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            Toast.makeText(sign_up.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        Toast.makeText(sign_up.this, "The email you entered is already used", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }

                else{
                    Toast.makeText(sign_up.this, "Please Enter your Email", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(sign_up.this, "This username must be At least 4 characters", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(sign_up.this, "Please enter your user name", Toast.LENGTH_SHORT).show();
        }
    }
}