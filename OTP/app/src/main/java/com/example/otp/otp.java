package com.example.otp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.example.otp.R;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class otp extends AppCompatActivity implements
    SMSReceiver.OTPReceiveListener {

        public static final String TAG = MainActivity.class.getSimpleName();

        private SMSReceiver smsReceiver;

        PinView pinView;
        String code;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.otp_verification);
            AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);

            // This code requires one time to get Hash keys do comment and share key


            // Find the TextView by ID
            pinView = findViewById(R.id.pin_view);
            // Get the input text from the previous activity's extra
            Intent intent = getIntent();
            String phone_number = intent.getStringExtra("phone_number");

            Button submitButton = findViewById(R.id.confirm_button);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitCode(phone_number, code);
                }
            });
            Log.i(TAG, "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));
            startSMSListener();

        }


        /**
         * Starts SmsRetriever, which waits for ONE matching SMS message until timeout
         * (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
         * action SmsRetriever#SMS_RETRIEVED_ACTION.
         */
        private void startSMSListener() {
            try {
                smsReceiver = new SMSReceiver();
                smsReceiver.setOTPListener(this);

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
                this.registerReceiver(smsReceiver, intentFilter);

                SmsRetrieverClient client = SmsRetriever.getClient(this);

                Task<Void> task = client.startSmsRetriever();
                task.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // API successfully started
                    }
                });

                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Fail to start API
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            pinView.setText(matcher.group(0));
            code = matcher.group(0);
        }
    }


        @Override
        public void onOTPReceived(String otp) {
//            showToast(otp);
            getOtpFromMessage(otp);

            if (smsReceiver != null) {
                unregisterReceiver(smsReceiver);
                smsReceiver = null;
            }
        }

        @Override
        public void onOTPTimeOut() {
            showToast("OTP Time out");
        }

        @Override
        public void onOTPReceivedError(String error) {
            showToast(error);
        }


        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (smsReceiver != null) {
                unregisterReceiver(smsReceiver);
            }
        }


        private void showToast(String msg) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        private <string> void submitCode(String phone, String code) {

            String url = "https://b78f-119-13-56-108.ngrok.io/api/auth/verify-otp";

            Map<String, String> params = new HashMap<>();
            params.put("phone", phone);
            params.put("otpCode", code);

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
                        }
                    }
            );

            Volley.newRequestQueue(this).add(jsonObjectRequest);
        }
}
