package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.BaseCallback;
import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.Order;

public class PaymentProcessing extends AppCompatActivity
{
    public static Meal m_meal = null;
    public static Order m_order = null;
    public static int m_numberOfMeals = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processing);

        final Button btn_pay = findViewById(R.id.btn_pay);
        final TextView txt_name = findViewById(R.id.txt_name);
        final TextView txt_price = findViewById(R.id.txt_price);

        if (m_meal != null)
        {
            txt_name.setText("Meal : " + m_meal.getName());
            txt_price.setText("Price : " + Double.toString(m_numberOfMeals * Double.parseDouble(m_meal.getPrice())));
        }
        else
        {
            SetError("Meal not found! Check internet?");
        }

        btn_pay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //todo set message
                UploadOrderCallBack uocb = new UploadOrderCallBack();
                uocb.SetMessage("");
                new DatabaseCommunicator().GenericUpload(uocb);
            }
        });
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(PaymentProcessing.this, errorString, Toast.LENGTH_LONG);
        t.show();
    }

    class UploadOrderCallBack extends BaseCallback
    {
        @Override
        public Void call() throws Exception
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(m_message.equals("null"))
                    {
                        MeetupChat.m_order = m_order;
                        startActivity(new Intent(PaymentProcessing.this, MeetupChat.class));
                    }
                    else
                    {
                        SetError("Failed to place order! Check internet.");
                    }
                }
            });
            return null;
        }
    }
}
