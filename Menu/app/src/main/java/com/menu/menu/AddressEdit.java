package com.menu.menu;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.User;

public class AddressEdit extends AppCompatActivity
{
    User m_currentUser = LocalSettings.LocalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);

        final EditText input_addressLine1 = findViewById(R.id.input_name);
        final EditText input_addressLine2 = findViewById(R.id.input_phone);
        final EditText input_addressLine3 = findViewById(R.id.input_firstName);
        final EditText input_postCode = findViewById(R.id.input_lastName);

        input_addressLine1.setText((m_currentUser.getAddressLine1() != null) ? m_currentUser.getAddressLine1() : "");
        input_addressLine2 .setText((m_currentUser.getAddressLine2() != null) ? m_currentUser.getAddressLine2() : "");
        input_addressLine3.setText((m_currentUser.getAddressLine3() != null) ? m_currentUser.getAddressLine3() : "");
        input_postCode.setText((m_currentUser.getAddressPostCode() != null) ? m_currentUser.getAddressPostCode() : "");

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final String addLn1 = input_addressLine1.getText().toString();
                final String addLn2 = input_addressLine2.getText().toString();
                final String addLn3 = input_addressLine3.getText().toString();
                final String addPostCode = input_postCode.getText().toString();

                try
                {
                    Geocoder coder = new Geocoder(getParent());
                    Address address = coder.getFromLocationName(addLn1 + " " + addLn2 + " " + addLn3 + " " + addPostCode,1).get(0);

                    m_currentUser.setLatitude(String.valueOf(address.getLatitude()) );
                    m_currentUser.setLongitude(String.valueOf(address.getLongitude()));
                    m_currentUser.setAddressLine1(addLn1);
                    m_currentUser.setAddressLine2(addLn2);
                    m_currentUser.setAddressLine3(addLn3);
                    m_currentUser.setAddressPostCode(addPostCode);
                    LocalSettings.UpdateLocalUser(m_currentUser);
                    onBackPressed();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    SetError("Incorrect address");
                }
            }
        });
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(AddressEdit.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }
}
