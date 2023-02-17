package com.example.otp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.otp.R;

public class otp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        // Find the TextView by ID
        TextView textView = findViewById(R.id.otp_code);
        TextView textView_Number = findViewById(R.id.otp_number);
        PinView pinView = findViewById(R.id.pin_view);
        // Get the input text from the previous activity's extra
        Intent intent = getIntent();

        String PinCode = intent.getStringExtra("code");
        String phone_number = intent.getStringExtra("phone_number");
        // Set the input text in the TextView
        textView.setText(PinCode);
        textView_Number.setText(phone_number);
//        pinView.setText(PinCode);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < PinCode.length(); i++) {
                    char c = PinCode.charAt(i);
                    pinView.setText(pinView.getText().append(Character.toString(c)));
                }
            }
        }, 2000);

        Button submitButton = findViewById(R.id.confirm_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(otp.this, finish.class);

                // Start the next activity
                startActivity(intent);
            }
        });
    }
}
