package com.example.otp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.otp.R;

public class otp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        // Find the TextView by ID
        TextView textView = findViewById(R.id.otp_number);

        // Get the input text from the previous activity's extra
        Intent intent = getIntent();
        String inputText = intent.getStringExtra("input_text");

        // Set the input text in the TextView
        textView.setText(inputText);

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
