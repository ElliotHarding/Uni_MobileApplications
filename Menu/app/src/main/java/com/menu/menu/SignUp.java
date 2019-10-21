package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.ReturnPage;
import com.menu.menu.Classes.User;

public class SignUp extends AppCompatActivity
{
    User m_currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final DatabaseCommunicator dbComms = new DatabaseCommunicator();

        if (AddressEdit.m_updatedAddress)
        {
            AddressEdit.m_updatedAddress = false;
        }
        else
        {
            m_currentUser = dbComms.GetUserViaUsername(LocalSettings.LocalUser.Username);
        }

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);

        final EditText input_email = findViewById(R.id.input_name);
        final EditText input_phone = findViewById(R.id.input_phone);
        final EditText input_first = findViewById(R.id.input_firstName);
        final EditText input_last = findViewById(R.id.input_lastName);
        final EditText input_password = findViewById(R.id.input_password);

        findViewById(R.id.btn_signUp).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_currentUser.Email = input_email.getText().toString();
                m_currentUser.Phone = input_phone.getText().toString();
                m_currentUser.FirstName = input_first.getText().toString();
                m_currentUser.LastName = input_last.getText().toString();
                m_currentUser.Password = input_password.getText().toString();

                String errorString = ValidateSettings(m_currentUser);
                if (errorString == "NO-ERROR")
                {
                    if (dbComms.AddUser(m_currentUser))
                    {
                        LocalSettings.UpdateLocalUser(m_currentUser);
                        startActivity(new Intent(SignUp.this, Home.class));
                    }
                    else
                    {
                        txt_error.setText("Failed to add user! Check network.");
                        txt_error.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    txt_error.setText(errorString);
                    txt_error.setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });

        findViewById(R.id.btn_addressSettings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                AddressEdit.m_returnPage = ReturnPage.PAGE_SIGNUP;
                AddressEdit.m_currentUser = m_currentUser;

                startActivity(new Intent(SignUp.this, AddressEdit.class));
            }
        });
    }

    public static String ValidateSettings(User u)
    {
        //todo validation...
        return "NO-ERROR";
    }
}
