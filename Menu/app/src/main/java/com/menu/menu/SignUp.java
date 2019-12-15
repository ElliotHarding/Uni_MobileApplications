package com.menu.menu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.BaseCallback;
import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class SignUp extends AppCompatActivity
{
    private final DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    private User m_currentUser = null;
    private DatePickerDialog.OnDateSetListener m_onDobSetListener = null;
    private ImageView m_img_image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText input_username = findViewById(R.id.input_name);
        final EditText input_email = findViewById(R.id.input_email);
        final EditText input_phone = findViewById(R.id.input_phone);
        final EditText input_fullName = findViewById(R.id.input_fullName);
        final TextView txt_edit_dob = findViewById(R.id.txt_edit_dob);
        final EditText input_password = findViewById(R.id.input_password);
        m_img_image = findViewById(R.id.img_image);

        m_currentUser = LocalSettings.GetLocalUser();

        findViewById(R.id.btn_signUp).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_currentUser.setUsername(input_username.getText().toString());
                m_currentUser.setEmail(input_email.getText().toString());
                m_currentUser.setPhone(input_phone.getText().toString());
                m_currentUser.setFullName(input_fullName.getText().toString());
                m_currentUser.setPassword(input_password.getText().toString());
                m_currentUser.setLoggedIn("true");

                String errorString = ValidateSettings(m_currentUser);
                if (errorString.equals("NO-ERROR"))
                {
                    //Upload user
                    LocalSettings.UpdateLocalUser(m_currentUser);
                    RegisterCallback rcb = new RegisterCallback();
                    rcb.SetMessage("IF NOT EXISTS (SELECT * FROM " + m_dbComms.m_userTable + " WHERE name = '" + m_currentUser.getUsername() + "')" + m_currentUser.GetInsertString() + ";");
                    m_dbComms.GenericUpload(rcb);
                }
                else
                {
                    SetError(errorString);
                }
            }
        });

        m_img_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 3);
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

        findViewById(R.id.btn_beAChef).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(SignUp.this, ChefAccountSettings.class));
            }
        });

        m_onDobSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year)
            {
                Date d = new Date(day, month, year);
                if (d.before(Date.from(Instant.now())))
                {
                    String dob = year + "-" + month + "-" + day;
                    m_currentUser.setDOB(dob);
                    txt_edit_dob.setText(dob);
                }
                else
                {
                    SetError("Invalid date of birth.");
                }
            }
        };

        txt_edit_dob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int day = 1;
                int month = 1;
                int year = 2000;

                String dob = m_currentUser.getDOB();
                if(dob != null)
                {
                    try
                    {
                        day = Integer.parseInt(dob.split("-")[0]);
                        month = Integer.parseInt(dob.split("-")[1]);
                        year = Integer.parseInt(dob.split("-")[2]);
                    }
                    catch (Exception e)
                    {
                    }
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this, m_onDobSetListener, day, month, year);
                datePickerDialog.setTitle("Date of Birth");
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });
    }

    //Overridden so we can get the uploaded image
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //GET_FROM_GALLERY = 3
        if(requestCode==3 && resultCode == Activity.RESULT_OK)
        {
            Uri selectedImage = data.getData();
            try
            {
                m_currentUser.setPicture(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage));
                m_img_image.setImageBitmap(m_currentUser.getPicture());
            }
            catch (FileNotFoundException e)
            {
                SetError("Image file could not be found");
            }
            catch (IOException e)
            {
                SetError("An I/O error occured while acquiring the image");
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        m_currentUser = LocalSettings.GetLocalUser();
    }

    public static String ValidateSettings(User u)
    {
        if(u.getLongitude() == null || u.getLatitude() == null)
        {
            return "Need to add address";
        }

        //todo validation...
        return "NO-ERROR";
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(SignUp.this, errorString, Toast.LENGTH_LONG);
        t.show();
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
