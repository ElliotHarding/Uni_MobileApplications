package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.User;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final DatabaseCommunicator dbComms = new DatabaseCommunicator();
        final User currentUser = dbComms.GetUserViaUsername(LocalSettings.LocalUser.Username);

        final EditText input_email = findViewById(R.id.input_addressLine1);
        final EditText input_phone = findViewById(R.id.input_addressLine2);
        final EditText input_first = findViewById(R.id.input_addressLine3);
        final EditText input_last = findViewById(R.id.input_postCode);
        final EditText input_password = findViewById(R.id.input_password);

        findViewById(R.id.btn_signUp).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                currentUser.Email = input_email.getText().toString();
                currentUser.Phone = input_phone.getText().toString();
                currentUser.FirstName = input_first.getText().toString();
                currentUser.LastName = input_last.getText().toString();
                currentUser.Password = input_password.getText().toString();

                String errorString = ValidateSettings(currentUser);
                if (errorString == "NO-ERROR")
                {
                    if (dbComms.AddUser(currentUser))
                    {
                        LocalSettings.UpdateLocalUser(currentUser);
                        startActivity(new Intent(SignUp.this, Home.class));
                    }
                    else
                    {
                        //todo error
                    }
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
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }

    public static String ValidateSettings(User u)
    {
        //todo
        return "NO-ERROR";
    }
}
