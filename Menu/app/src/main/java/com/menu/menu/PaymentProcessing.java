package com.menu.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.menu.menu.Classes.Meal;

public class PaymentProcessing extends AppCompatActivity
{
    private static Meal m_meal = null;
    private static Object m_previousPage = null;
    public static void Setup(Meal meal, Object previousPage)
    {
        m_meal = meal;
        m_previousPage = previousPage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processing);

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);
    }


}
