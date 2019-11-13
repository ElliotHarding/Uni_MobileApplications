package com.menu.menu;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.Order;

public class MealOrder extends AppCompatActivity
{
    public static Order m_order = null;

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
        m_order = m_dbComms.GetOrderUpdate(m_order.Id);

        m_txt_eta.setText(m_order.ArrivalTime);
        m_txt_state.setText(m_order.CurrentState);
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(MealOrder.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }
}
