package com.menu.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.Order;

public class MeetupChat extends AppCompatActivity
{
    Meal m_meal = null;
    Order m_order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_chat);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("meal") && extras.containsKey("order"))
        {
            m_meal = (Meal)extras.getSerializable("meal");
            m_order = (Order)extras.getSerializable("order");
        }
        else
        {
            SetError("ic_meal not found! Check internet?");
        }
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(MeetupChat.this, errorString, Toast.LENGTH_LONG);
        t.show();
    }
}
