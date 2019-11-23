package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.ReturnPage;
import com.menu.menu.Classes.User;
import com.menu.menu.Classes.UsersCallback;

public class Settings extends AppCompatActivity
{
    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    User m_currentUser = LocalSettings.LocalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText input_email = findViewById(R.id.input_name);
        final EditText input_phone = findViewById(R.id.input_phone);
        final EditText input_first = findViewById(R.id.input_firstName);
        final EditText input_last = findViewById(R.id.input_lastName);
        final EditText input_password = findViewById(R.id.input_password);

        input_email.setText(m_currentUser.Email);
        input_phone.setText(m_currentUser.Phone);
        input_first.setText(m_currentUser.FullName.split("|$|")[0]);
        input_last.setText(m_currentUser.FullName.split("|$|")[1]);
        input_password.setText(m_currentUser.Password);

        findViewById(R.id.btn_saveSettigns).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_currentUser.Email = input_email.getText().toString();
                m_currentUser.Phone = input_phone.getText().toString();
                m_currentUser.FullName = input_first.getText().toString() + "|$|" + input_last.getText().toString();
                m_currentUser.Password = input_password.getText().toString();

                String errorString = SignUp.ValidateSettings(m_currentUser);
                if (errorString.equals("NO-ERROR"))
                {
                    //todo...
                    if (m_dbComms.UpdateUser(m_currentUser))
                    {
                        LocalSettings.UpdateLocalUser(m_currentUser);
                        NavigateHome();
                    }
                    else
                    {
                        SetError("Failed to add user! Check network.");
                    }
                }
                else
                {
                    SetError(errorString);
                }
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavigateHome();
            }
        });

        findViewById(R.id.btn_addressSettings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Settings.this, AddressEdit.class));
            }
        });
    }

    private void NavigateHome()
    {
        startActivity(new Intent(Settings.this, MainHub.class));
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(Settings.this, errorString, Toast.LENGTH_LONG);
        t.show();
    }
}
