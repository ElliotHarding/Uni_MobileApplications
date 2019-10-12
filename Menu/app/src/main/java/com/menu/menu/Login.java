package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.User;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity
{
    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LocalSettings.LoadSettings();
        if (LocalSettings.IsLoginSaved())
        {
            if (m_dbComms.TryLogin(LocalSettings.LocalUser.Username, LocalSettings.LocalUser.Password, DatabaseCommunicator.LoginOption.Username))
            {
                NavigateToHome();
            }
        }

        final Button btn_login = findViewById(R.id.btn_login);
        final Button btn_signUp = findViewById(R.id.btn_signUp);
        final EditText input_usernameOrEmail = findViewById(R.id.input_usernameOrEmail);
        final EditText input_password = findViewById(R.id.input_password);

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                String usernameOrEmail = input_usernameOrEmail.getText().toString();
                String password = input_password.getText().toString();

                if (m_dbComms.TryLogin(usernameOrEmail, password, DatabaseCommunicator.LoginOption.Username))
                {
                    LocalSettings.UpdateLocalUser(m_dbComms.GetUserViaUsername(usernameOrEmail));
                    NavigateToHome();
                }
                else if (m_dbComms.TryLogin(usernameOrEmail, password, DatabaseCommunicator.LoginOption.Email))
                {
                    LocalSettings.UpdateLocalUser(m_dbComms.GetUserViaEmail(usernameOrEmail));
                    NavigateToHome();
                }
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

    private void NavigateToHome()
    {
        startActivity(new Intent(Login.this, Home.class));
    }

}
