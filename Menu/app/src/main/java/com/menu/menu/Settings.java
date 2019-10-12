package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.User;

public class Settings extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final DatabaseCommunicator dbComms = new DatabaseCommunicator();
        final User currentUser = dbComms.GetUserViaUsername(LocalSettings.LocalUser.Username);

        final EditText input_email = findViewById(R.id.input_addressLine1);
        final EditText input_phone = findViewById(R.id.input_addressLine2);
        final EditText input_first = findViewById(R.id.input_addressLine3);
        final EditText input_last = findViewById(R.id.input_postCode);
        final EditText input_password = findViewById(R.id.input_password);

        input_email.setText(currentUser.Email);
        input_phone.setText(currentUser.Phone);
        input_first.setText(currentUser.FirstName);
        input_last.setText(currentUser.LastName);
        input_password.setText(currentUser.Password);

        findViewById(R.id.btn_saveSettigns).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                currentUser.Email = input_email.getText().toString();
                currentUser.Phone = input_phone.getText().toString();
                currentUser.FirstName = input_first.getText().toString();
                currentUser.LastName = input_last.getText().toString();
                currentUser.Password = input_password.getText().toString();

                String errorString = SignUp.ValidateSettings(currentUser);
                if (errorString == "NO ERROR")
                {
                    dbComms.UpdateUser(currentUser);
                    LocalSettings.UpdateLocalUser(currentUser);
                    NavigateHome();
                }
                else
                {
                    //todo... errorString
                }
            }
        });

        findViewById(R.id.btn_quit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavigateHome();
            }
        });
    }

    private void NavigateHome()
    {
        startActivity(new Intent(Settings.this, Home.class));
    }
}
