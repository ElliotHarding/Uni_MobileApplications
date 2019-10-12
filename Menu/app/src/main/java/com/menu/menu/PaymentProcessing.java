package com.menu.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PaymentProcessing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processing);

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);
    }
}
