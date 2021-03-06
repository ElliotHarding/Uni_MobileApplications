package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.UsersCallback;

public class Login extends AppCompatActivity
{
    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    private ProgressBar m_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button btn_login = findViewById(R.id.btn_login);
        final Button btn_signUp = findViewById(R.id.btn_signUp);
        final EditText input_usernameOrEmail = findViewById(R.id.input_usernameOrEmail);
        final EditText input_password = findViewById(R.id.input_password);

        m_progressBar = findViewById(R.id.progressBar);
        m_progressBar.setVisibility(View.INVISIBLE);
        m_progressBar.stopNestedScroll();

        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String usernameOrEmail = input_usernameOrEmail.getText().toString();
                String password = input_password.getText().toString();

                String select = "SELECT * FROM " + m_dbComms.m_userTable + " WHERE name = '" + usernameOrEmail + "' and password = '" + password + "' or contact_email = '"
                        + usernameOrEmail + "' and password = '" + password + "';";

                UserDataCallback ucb = new UserDataCallback();
                ucb.SetMessage(select);
                m_dbComms.RequestUserData(ucb);

                m_progressBar.startNestedScroll(1);
                m_progressBar.setVisibility(View.VISIBLE);
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });
    }

    @Override
    public void onBackPressed()
    {
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(Login.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }

    private class UserDataCallback extends UsersCallback
    {
        @Override
        public Void call() throws Exception
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (m_users != null && !m_users.isEmpty()) //Correct user found
                    {
                        LocalSettings.UpdateLocalUser(m_users.get(0));
                        LocalSettings.SaveLoginDetails(m_users.get(0).getUsername(), m_users.get(0).getPassword(), Login.this);
                        startActivity(new Intent(Login.this, MainHub.class));
                    }
                    else
                    {
                        if(m_bInternetIssue)
                            SetError("Failed to connect! Check internet?");
                        else
                            SetError("Incorrect Details");

                    }
                    m_progressBar.setVisibility(View.INVISIBLE);
                    m_progressBar.stopNestedScroll();
                }
            });
            return null;
        }
    }
}
