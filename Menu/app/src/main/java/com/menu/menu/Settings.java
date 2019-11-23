package com.menu.menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.BaseCallback;
import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.ReturnPage;
import com.menu.menu.Classes.User;
import com.menu.menu.Classes.UsersCallback;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Settings extends AppCompatActivity
{
    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    User m_currentUser = LocalSettings.LocalUser;
    ImageView m_img_image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText input_email = findViewById(R.id.input_name);
        final EditText input_phone = findViewById(R.id.input_phone);
        final EditText input_first = findViewById(R.id.input_firstName);
        final EditText input_last = findViewById(R.id.input_lastName);
        final EditText input_password = findViewById(R.id.input_password);
        m_img_image = findViewById(R.id.img_image);

        input_email.setText(m_currentUser.Email);
        input_phone.setText(m_currentUser.Phone);
        input_first.setText(m_currentUser.FullName.split("|$|")[0]);
        input_last.setText(m_currentUser.FullName.split("|$|")[1]);
        input_password.setText(m_currentUser.Password);
        if(m_currentUser.Picture != null)
        {
            m_img_image.setImageBitmap(m_currentUser.Picture);
        }


        findViewById(R.id.btn_saveSettigns).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_currentUser.Email = input_email.getText().toString();
                m_currentUser.Phone = input_phone.getText().toString();
                m_currentUser.FullName = input_first.getText().toString() + "|$|" + input_last.getText().toString();
                m_currentUser.Password = input_password.getText().toString();

                String errorString = SignUp.ValidateSettings(m_currentUser);
                if (errorString.equals("NO-ERROR"))
                {
                    UpdateUserCallback umc = new UpdateUserCallback();
                    umc.SetMessage("UPDATE " + m_dbComms.m_userTable + " SET " + m_currentUser.GetUpdateString() + " WHERE id = '" + m_currentUser.Id + "';");
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
                startActivity(new Intent(Settings.this, AddressEdit.class));
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
                m_currentUser.Picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                m_img_image.setImageBitmap(m_currentUser.Picture);
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
