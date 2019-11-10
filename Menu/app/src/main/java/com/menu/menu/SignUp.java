package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.menu.menu.Classes.BaseCallback;
import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.User;

public class SignUp extends AppCompatActivity
{
    final DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    TextView m_txt_error = null;
    User m_currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        m_txt_error = findViewById(R.id.txt_error);
        m_txt_error.setVisibility(View.INVISIBLE);

        final EditText input_username = findViewById(R.id.input_name);
        final EditText input_email = findViewById(R.id.input_name);
        final EditText input_phone = findViewById(R.id.input_phone);
        final EditText input_fullName = findViewById(R.id.input_fullName);
        final EditText input_dob = findViewById(R.id.input_dob);
        final EditText input_password = findViewById(R.id.input_password);

        m_currentUser = LocalSettings.LocalUser;

        findViewById(R.id.btn_signUp).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_currentUser.Username = input_username.getText().toString();
                m_currentUser.Email = input_email.getText().toString();
                m_currentUser.Phone = input_phone.getText().toString();
                m_currentUser.FullName = input_fullName.getText().toString();
                m_currentUser.Password = input_password.getText().toString();
                m_currentUser.DOB = input_dob.getText().toString();
                m_currentUser.LoggedIn = "true";

                String errorString = ValidateSettings(m_currentUser);
                if (errorString.equals("NO-ERROR"))
                {
                    //Upload user
                    LocalSettings.UpdateLocalUser(m_currentUser);
                    RegisterCallback rcb = new RegisterCallback();
                    rcb.SetMessage("IF NOT EXISTS (SELECT * FROM " + m_dbComms.m_userTable + " WHERE name = '" + m_currentUser.Username + "')" + m_dbComms.m_userInsert + "(" + m_currentUser.GetInsertString() + ");");
                    m_dbComms.GenericUpload(rcb);
                }
                else
                {
                    SetError(errorString);
                }
            }
        });

        findViewById(R.id.btn_addressSettings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(SignUp.this, AddressEdit.class));
            }
        });
    }

    public static String ValidateSettings(User u)
    {
        //todo validation...
        return "NO-ERROR";
    }

    private void SetError(String errorString)
    {
        m_txt_error.setText(errorString);
        m_txt_error.setVisibility(View.VISIBLE);
    }

    private void NavigateLogin()
    {
        startActivity(new Intent(SignUp.this, Login.class));
    }

    @Override
    public void onBackPressed()
    {
        NavigateLogin();
    }

    class RegisterCallback extends BaseCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_message.equals("null"))
            {
                startActivity(new Intent(SignUp.this, MainHub.class));
            }
            else
            {
                SetError("Failed to add user! Check internet connection");
            }
            return null;
        }
    }
}
