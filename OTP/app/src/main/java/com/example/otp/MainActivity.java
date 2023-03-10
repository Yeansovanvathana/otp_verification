package com.example.otp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_phone);
        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);
        // Find the TextInputLayout by ID
        TextInputLayout textInputLayout = findViewById(R.id.filledTextField);

        // Find the TextInputEditText inside the TextInputLayout by ID
        TextInputEditText textInputEditText = textInputLayout.findViewById(R.id.edit_text);

        Button submitButton = findViewById(R.id.submit_button);

        // Set an OnClickListener on the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the text that the user entered
                String inputText = textInputEditText.getText().toString();

//                // Show a Toast message with the input text
//                Intent intent = new Intent(MainActivity.this, otp.class);
//
//                // Pass the input text as an extra to the next activity
//                intent.putExtra("input_text", inputText);
//
//                // Start the next activity
//                startActivity(intent);
                Intent intent = new Intent(MainActivity.this, otp.class);

                intent.putExtra("phone_number", inputText);
                startActivity(intent);
                loadtext(inputText, appSignatureHashHelper.getAppSignatures().get(0));
            }
        });

    }

    private <string> void loadtext(String phone, String hashkey) {
//        Intent intent = new Intent(MainActivity.this, otp.class);
//        startActivity(intent);

        String url = "https://b78f-119-13-56-108.ngrok.io/api/auth/send-otp";

        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("hashkey", hashkey);

        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseString = response.toString();
                        Log.d("API Response", responseString);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}