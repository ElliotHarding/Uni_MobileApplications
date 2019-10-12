package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.menu.menu.Classes.User;

public class AddressEdit extends AppCompatActivity
{
    private static Object m_returnPage;
    private static User m_currentUser;

    public static void Setup(Object returnPage, User currentUser)
    {
        m_currentUser = currentUser;
        m_returnPage = returnPage;
    }

    public static User GetUpdatedUser()
    {
        return m_currentUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);

        final EditText input_addressLine1 = findViewById(R.id.input_name);
        final EditText input_addressLine2 = findViewById(R.id.input_phone);
        final EditText input_addressLine3 = findViewById(R.id.input_firstName);
        final EditText input_postCode = findViewById(R.id.input_lastName);

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_currentUser.AddressLine1 = input_addressLine1.getText().toString();
                m_currentUser.AddressLine2 = input_addressLine2.getText().toString();
                m_currentUser.AddressLine3 = input_addressLine3.getText().toString();
                m_currentUser.AddressPostCode = input_postCode.getText().toString();

                String errorString = ValidateUserAddress(m_currentUser);
                if (errorString == "NO ERROR")
                {
                    NavigateOut();
                }
                else
                {
                    txt_error.setText(errorString);
                    txt_error.setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.btn_quit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavigateOut();
            }
        });
    }

    private String ValidateUserAddress(User user)
    {
        //todo

        return "NO ERROR";
    }

    private void NavigateOut()
    {
        startActivity(new Intent(AddressEdit.this, m_returnPage.getClass()));
    }
}
