package com.menu.menu;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.BasketItem;
import com.menu.menu.Classes.DatabaseCommunicator;

public class MealOrder extends AppCompatActivity
{
    public static BasketItem m_basketItem = null;

    TextView m_txt_state;
    TextView m_txt_eta;
    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();

    //automatic refresh
    Handler m_handler = new Handler();
    final int m_handlerDelay = 1500; //5s
    int m_connectionErrorCount = 0; //number of times connection has failed

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_order);

        m_txt_state = findViewById(R.id.txt_orderState);
        m_txt_eta = findViewById(R.id.txt_eta);

        m_handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Update();
                if(m_connectionErrorCount < 3)
                {
                    m_handler.postDelayed(this, m_handlerDelay);
                }
            }
        }, m_handlerDelay);
    }

    private void Update()
    {
        //todo

        m_txt_eta.setText(m_basketItem.GetArrivalTime());
        m_txt_state.setText(m_basketItem.GetState());
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(MealOrder.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }
}
