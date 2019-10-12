package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.Order;
import com.menu.menu.Classes.ReturnPage;

public class PaymentProcessing extends AppCompatActivity
{
    private static Meal m_meal = null;
    private static int m_numberOfMeals = 0;
    public static void Setup(Meal meal, final int numberOfMeals)
    {
        m_meal = meal;
        m_numberOfMeals = numberOfMeals;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processing);

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);

        final Button btn_pay = findViewById(R.id.btn_pay);
        final TextView txt_name = findViewById(R.id.txt_name);
        final TextView txt_price = findViewById(R.id.txt_price);

        if (m_meal != null)
        {
            txt_name.setText("Meal : " + m_meal.Name);
            txt_price.setText("Price : " + Double.toString(m_numberOfMeals * Double.parseDouble(m_meal.Price)));
        }
        else
        {
            txt_error.setText("Meal not found! Check internet?");
            txt_error.setVisibility(View.VISIBLE);
        }

        btn_pay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Order newOrder = new DatabaseCommunicator().AddOrder(m_meal, LocalSettings.LocalUser);
                if (newOrder != null)
                {
                    MealOrder.Setup(newOrder);
                    startActivity(new Intent(PaymentProcessing.this, MealOrder.class));
                }
                else
                {
                    txt_error.setText("Failed to place order! Check internet.");
                    txt_error.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
