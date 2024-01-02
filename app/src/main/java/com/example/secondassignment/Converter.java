package com.example.secondassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Converter extends AppCompatActivity {

    private EditText edtAmount;
    private EditText edtResult;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        Intent intent = getIntent();
        String Email = intent.getStringExtra("User");
        Toast.makeText(this, "Welcome" + Email, Toast.LENGTH_SHORT).show();
        spinnerFrom = findViewById(R.id.spnFrom);
        spinnerTo = findViewById(R.id.spnTo);
        edtAmount = findViewById(R.id.edtAmount);
        edtResult = findViewById(R.id.edtResult);
        requestQueue = Volley.newRequestQueue(this);
    }
    public void btnConvertOnclick(View view) {

        try{
            double amount = Double.parseDouble(edtAmount.getText().toString());

            String from = spinnerFrom.getSelectedItem().toString();
            String to = spinnerTo.getSelectedItem().toString();

            String [] splitter = from.split(",");
            String fromSymbol = splitter[0];

            splitter = to.split(",");
            String toSymbol = splitter[0];
            String apiKey = "9478fbd87d099f380f59994978df91a3";
            String url = "https://api.exchangeratesapi.io/v1/convert?access_key="+apiKey+"&from="+fromSymbol+"&to="+toSymbol+"&amount="+ amount;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String resultString = response.getString("result");
                        edtResult.setText(resultString);
                    }
                    catch (Exception exception){
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            requestQueue.add(jsonObjectRequest);

        }
        catch (NumberFormatException numberFormatException){
            Toast.makeText(this, "Please enter the amount in the box", Toast.LENGTH_SHORT).show();
        }
    }
}