package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.UsersCallback;

public class Loader extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LocalSettings.LoadSettings();
        if (LocalSettings.IsLoginSaved())
        {
            UserDataCallback ucb = new UserDataCallback();
            DatabaseCommunicator dbComms = new DatabaseCommunicator();
            ucb.SetMessage("SELECT * FROM " + dbComms.m_userTable + " WHERE name = '" + LocalSettings.LocalUser.getUsername() + "' and password = '" + LocalSettings.LocalUser.getPassword() + "';");
            dbComms.RequestUserData(ucb);
        }
        else
        {
            startActivity(new Intent(Loader.this, Login.class));
        }

        setContentView(R.layout.activity_loader);
    }

    private class UserDataCallback extends UsersCallback
    {
        @Override
        public Void call() throws Exception
        {
            if (!m_users.isEmpty())
            {
                LocalSettings.UpdateLocalUser(m_users.get(0));
                startActivity(new Intent(Loader.this, MainHub.class));
            }
            else
            {
                startActivity(new Intent(Loader.this, Login.class));
            }
            return null;
        }
    }
}
