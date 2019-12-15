package com.menu.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.menu.menu.Classes.BasketItem;
import com.menu.menu.Classes.Meal;

public class MeetupChat extends AppCompatActivity
{
    Meal m_meal = null;
    BasketItem m_basketItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_chat);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("meal") && extras.containsKey("order"))
        {
            m_meal = (Meal)extras.getSerializable("meal");
            m_basketItem = (BasketItem)extras.getSerializable("order");
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
