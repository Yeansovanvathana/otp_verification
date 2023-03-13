package com.example.otp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class otp extends AppCompatActivity {
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        // Get the input text from the previous activity's extra
        Intent intent = getIntent();
        Button submitButton = findViewById(R.id.confirm_button);

        // Find the TextView by ID
        EditText pinView = findViewById(R.id.pin_view);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pinView.getText().toString().length() == 6) {
                    code = pinView.getText().toString();
                    submitCode(code);
                } else {
                    Toast.makeText(otp.this, "Enter 6 digit code", Toast.LENGTH_SHORT).show();
                };

            }
        });
    }

    private <string> void submitCode(String code) {

        String url = "https://develop.pregi.api.paylessgate.com/api/v1/sdk/vendor/XXXXXXXXXXXXXX/device/android";
        String device_code = "748j8iz67ijglz556zl2bycc2sl8y2r9";
        String vendor_usre_code = "ZZZZZZZZZZZZZ";
        String pin_code =  "999999";
        String auth_token = "8vOgoSPUHw";
        String hash = "903c3bad3391cd526a0f7594d77feefa250e553277b05bb08ebe9a533cdde303";

        Map<String, String> params = new HashMap<>();
        params.put("device_code", device_code);
        params.put("vendor_usre_code", vendor_usre_code);
        params.put("pin_code", code);
        params.put("auth_token", auth_token);
        params.put("hash", hash);


        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            String responseString = response.toString();
                            Log.d("API Response", responseString);
                            Intent intent = new Intent(otp.this, finish.class);
                            startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(otp.this, "wrong pin code", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
