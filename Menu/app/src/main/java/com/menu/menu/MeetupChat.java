package com.menu.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MeetupChat extends AppCompatActivity
{

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
