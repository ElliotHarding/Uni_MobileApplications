package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.User;
import com.menu.menu.Classes.UsersCallback;

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
            UserDataCallback ucb = new UserDataCallback();
            ucb.SetMessage("SELECT * FROM " + m_dbComms.m_userTable + " WHERE name = '" + LocalSettings.LocalUser.Username + "' and password = '" + LocalSettings.LocalUser.Password + "';");
            m_dbComms.RequestUserData(ucb);
        }

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);

        final Button btn_login = findViewById(R.id.btn_login);
        final Button btn_signUp = findViewById(R.id.btn_signUp);
        final EditText input_usernameOrEmail = findViewById(R.id.input_usernameOrEmail);
        final EditText input_password = findViewById(R.id.input_password);

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

    void OnFailedLogin()
    {

    }

    private class UserDataCallback extends UsersCallback
    {
        @Override
        public Void call() throws Exception
        {
            if (!m_users.isEmpty()) //Correct user found
            {
                LocalSettings.UpdateLocalUser(m_users.get(0));
                startActivity(new Intent(Login.this, MainHub.class));
            }
            else
            {
                OnFailedLogin();
            }
            return null;
        }
    }
}
