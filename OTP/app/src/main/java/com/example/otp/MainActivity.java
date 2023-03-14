package com.example.otp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_phone);

        // Find the TextInputLayout by ID
        TextInputLayout textInputLayout = findViewById(R.id.filledTextField);

        // Find the TextInputEditText inside the TextInputLayout by ID
        TextInputEditText textInputEditText = textInputLayout.findViewById(R.id.edit_text);

        Button submitButton = findViewById(R.id.submit_button);

        // Set an OnClickListener on the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the phone number that the user entered
                String inputText = textInputEditText.getText().toString();

//                loadtext(inputText);
                submitPhone(inputText);
            }
        });

    }
    private <string> void loadtext(String phone) {

        String url = "https://develop.pregi.api.paylessgate.com/api/v1/sdk/vendor/XXXXXXXXXXXXXX/device/auth";
        String device_code = "748j8iz67ijglz556zl2bycc2sl8y2r9";
        String hash = "853bf04925053439c1710c760a140d2bdcd53073a65727647bb4de08fd98d870";

        Map<String, String> params = new HashMap<>();
        params.put("phone_number", phone);
        params.put("device_code", device_code);
        params.put("hash", hash);

        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseString = response.toString();
                        Log.d("API Response", responseString);
                        Intent intent = new Intent(MainActivity.this, otp.class);

                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(MainActivity.this, "Wrong number", Toast.LENGTH_LONG).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    private <string> void submitPhone(String phone) {

        String device_code = "748j8iz67ijglz556zl2bycc2sl8y2r9";
        String hash = "853bf04925053439c1710c760a140d2bdcd53073a65727647bb4de08fd98d870";
//        String[] authParam = {"phone_number=09099999999", "device_code=748j8iz67ijglz556zl2bycc2sl8y2r9",};
        Map<String, String> authParam = new LinkedHashMap<>();

        authParam.put("phone_number", phone);
        authParam.put("device_code", device_code);
        boolean result = sdkService.hashCheck(authParam, hash);

        if (result) {
            // Hash check succeeded
            Log.d("MainActivity", "Hash check succeeded");
//            Toast.makeText(MainActivity.this, "Hash check succeeded", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, otp.class);

            startActivity(intent);
        } else {
            // Hash check failed
            Log.d("MainActivity", "Hash check failed");
            Toast.makeText(MainActivity.this, "Wrong phone number", Toast.LENGTH_LONG).show();
        }

    }
}