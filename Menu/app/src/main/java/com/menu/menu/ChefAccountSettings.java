package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.User;

public class ChefAccountSettings extends AppCompatActivity
{
    User m_currentUser = LocalSettings.LocalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_account_settings);

        final Switch switch_chef = findViewById(R.id.switch_chef);
        final EditText input_foodType = findViewById(R.id.input_foodType);

        if(m_currentUser != null)
        {
            Boolean isChef = m_currentUser.IsChef.equals("true");
            switch_chef.setChecked(isChef);

            if(!isChef)
            {
                input_foodType.setVisibility(View.INVISIBLE);
                findViewById(R.id.txt_foodSpeciality).setVisibility(View.INVISIBLE);
            }
        }

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //todo validate food type
                m_currentUser.FoodType = input_foodType.getText().toString();
                m_currentUser.IsChef = switch_chef.isChecked() ? "true" : "false";
                LocalSettings.UpdateLocalUser(m_currentUser);

                startActivity(new Intent(ChefAccountSettings.this, Settings.class));
            }
        });

        switch_chef.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(switch_chef.isChecked())
                {
                    input_foodType.setVisibility(View.VISIBLE);
                    findViewById(R.id.txt_foodSpeciality).setVisibility(View.VISIBLE);
                }
                else
                {
                    input_foodType.setVisibility(View.VISIBLE);
                    findViewById(R.id.txt_foodSpeciality).setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
