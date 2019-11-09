package com.menu.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.menu.menu.Classes.User;

public class AddressEdit extends AppCompatActivity
{
    public static User m_currentUser = null;
    public static boolean m_updatedAddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);

        m_updatedAddress = false;

        final EditText input_addressLine1 = findViewById(R.id.input_name);
        final EditText input_addressLine2 = findViewById(R.id.input_phone);
        final EditText input_addressLine3 = findViewById(R.id.input_firstName);
        final EditText input_postCode = findViewById(R.id.input_lastName);

        input_addressLine1.setText(m_currentUser.AddressLine1);
        input_addressLine2 .setText(m_currentUser.AddressLine2);
        input_addressLine3.setText(m_currentUser.AddressLine3);
        input_postCode.setText(m_currentUser.AddressPostCode);

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
                if (errorString.equals("NO-ERROR"))
                {
                    m_updatedAddress = true;
                    onBackPressed();
                }
                else
                {
                    txt_error.setText(errorString);
                    txt_error.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private String ValidateUserAddress(User user)
    {
        //todo validation...
        return "NO-ERROR";
    }

}
