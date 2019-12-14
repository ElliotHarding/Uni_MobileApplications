package com.menu.menu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

public class Settings extends AppCompatActivity
{
    private DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    private User m_currentUser = LocalSettings.GetLocalUser();
    private ImageView m_img_image = null;
    private DatePickerDialog.OnDateSetListener m_onDobSetListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText input_email = findViewById(R.id.input_name);
        final EditText input_phone = findViewById(R.id.input_phone);
        final EditText input_fullName = findViewById(R.id.input_fullName);
        final EditText input_password = findViewById(R.id.input_password);
        final TextView txt_edit_dob = findViewById(R.id.txt_edit_dob);
        m_img_image = findViewById(R.id.img_image);

        input_email.setText(m_currentUser.getEmail());
        input_phone.setText(m_currentUser.getPhone());
        input_fullName.setText(m_currentUser.getFullName());
        input_password.setText(m_currentUser.getPassword());
        txt_edit_dob.setText(m_currentUser.getDOB());
        if(m_currentUser.getPicture() != null)
        {
            m_img_image.setImageBitmap(m_currentUser.getPicture());
        }


        findViewById(R.id.btn_saveSettigns).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_currentUser.setEmail(input_email.getText().toString());
                m_currentUser.setPhone(input_phone.getText().toString());
                m_currentUser.setFullName(input_fullName.getText().toString());
                m_currentUser.setPassword(input_password.getText().toString());

                String errorString = SignUp.ValidateSettings(m_currentUser);
                if (errorString.equals("NO-ERROR"))
                {
                    UpdateUserCallback umc = new UpdateUserCallback();
                    umc.SetMessage("UPDATE " + m_dbComms.m_userTable + " SET " + m_currentUser.GetUpdateString() + " WHERE id = '" + m_currentUser.getId() + "';");
                    m_dbComms.GenericUpload(umc);
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
                startActivity(new Intent(Settings.this, AddressEdit.class));
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

        findViewById(R.id.btn_chef).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Settings.this, ChefAccountSettings.class));
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
                    m_currentUser.setDOB(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
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
                        day = Integer.parseInt(dob.split("/")[0]);
                        month = Integer.parseInt(dob.split("/")[1]);
                        year = Integer.parseInt(dob.split("/")[2]);
                    }
                    catch (Exception e)
                    {
                    }
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(Settings.this, m_onDobSetListener, day, month, year);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        m_currentUser = LocalSettings.GetLocalUser();
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

    private void NavigateHome()
    {
        startActivity(new Intent(Settings.this, MainHub.class));
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(Settings.this, errorString, Toast.LENGTH_LONG);
        t.show();
    }

    private class UpdateUserCallback extends BaseCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_message.equals("null"))
            {
                LocalSettings.UpdateLocalUser(m_currentUser);
                NavigateHome();
            }
            else
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SetError("Update failed! Check internet connection.");
                    }
                });
            }

            return null;
        }
    }
}
