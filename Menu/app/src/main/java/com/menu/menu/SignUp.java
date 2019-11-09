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
    final DatabaseCommunicator m_dbComms = new DatabaseCommunicator();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
                User currentUser = LocalSettings.LocalUser;
                currentUser.Email = input_email.getText().toString();
                currentUser.Phone = input_phone.getText().toString();
                currentUser.FirstName = input_first.getText().toString();
                currentUser.LastName = input_last.getText().toString();
                currentUser.Password = input_password.getText().toString();

                String errorString = ValidateSettings(currentUser);
                if (errorString.equals("NO-ERROR"))
                {
                    if (m_dbComms.AddUser(currentUser))
                    {
                        LocalSettings.UpdateLocalUser(currentUser);
                        startActivity(new Intent(SignUp.this, MainHub.class));
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
                NavigateLogin();
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

    private void NavigateLogin()
    {
        startActivity(new Intent(SignUp.this, Login.class));
    }

    @Override
    public void onBackPressed()
    {
        NavigateLogin();
    }
}
