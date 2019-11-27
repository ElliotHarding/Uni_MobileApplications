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
    public static Meal m_meal = null;
    public static Order m_order = null;
    public static int m_numberOfMeals = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_chat);
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(MeetupChat.this, errorString, Toast.LENGTH_LONG);
        t.show();
    }
}
