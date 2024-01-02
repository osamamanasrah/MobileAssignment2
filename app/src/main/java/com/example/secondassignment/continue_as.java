package com.example.secondassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import User.User;

public class continue_as extends AppCompatActivity {
    private ListView listView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String [] strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_as);
        listView = findViewById(R.id.listView2);
        sharedPreferences = getSharedPreferences("Users", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    protected void onResume(){
        super.onResume();
        prepareListContent();
        populateList(strings);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                Intent intent = new Intent(continue_as.this, Converter.class);
                intent.putExtra("User", strings[position]);
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(itemClickListener);
    }

    private void populateList(String[] listContent) {
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContent);
        listView.setAdapter(adapter);
    }

    private void prepareListContent (){
        ArrayList <String> list = new ArrayList<>();

        if(sharedPreferences.contains("hasElement")){//we won't be here if there is no element in the shared pref
            editor.remove("hasElement");
            editor.commit();
        }
        Map<String, ?> map =  sharedPreferences.getAll();
        Gson gson = new Gson();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String jsonString = entry.getValue().toString();
            User user = gson.fromJson(jsonString, User.class);
            if(user.isRemembered()){
                list.add(user.getEmail());
            }
        }
        editor.putBoolean("hasElement", true);
        editor.commit();

        strings = new String[list.size()];
        for(int i = 0 ; i < strings.length ; i++){
            strings[i] = list.get(i);
        }
    }

    public void btnAnotherAccountOnClick(View view) {
        Intent intent = new Intent(this, log_in.class);
        startActivity(intent);
    }
}