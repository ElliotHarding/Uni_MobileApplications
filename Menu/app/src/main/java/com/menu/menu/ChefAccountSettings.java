package com.menu.menu;

/*
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TimePicker;
import com.menu.menu.Classes.BaseCallback;
import com.menu.menu.Classes.DatabaseCommunicator;
*/
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.User;

public class ChefAccountSettings extends AppCompatActivity
{
    User m_currentUser = LocalSettings.LocalUser;

    TextView m_txt_pickDateFrom = null;
    TextView m_txt_pickDateTo = null;
    TextView m_txt_hoursAvaliableFrom = null;
    TextView m_txt_hoursAvaliableTo = null;
    //TimePickerDialog.OnTimeSetListener m_onHoursFromSetListener = null;
    //TimePickerDialog.OnTimeSetListener m_onHoursToSetListener = null;
    String m_hoursAvaliableFrom = null;
    String m_hoursAvaliableTo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_account_settings);

        final Switch switch_chef = findViewById(R.id.switch_chef);
        final EditText input_foodType = findViewById(R.id.input_foodType);

        m_txt_pickDateFrom = findViewById(R.id.txt_chooseDateFrom);
        m_txt_pickDateTo = findViewById(R.id.txt_chooseDateTo);
        m_txt_hoursAvaliableFrom = findViewById(R.id.txt_hoursAvaliableFrom);
        m_txt_hoursAvaliableTo = findViewById(R.id.txt_hoursAvaliableTo);

        //May remove...
        m_txt_hoursAvaliableFrom.setVisibility(View.INVISIBLE);
        m_txt_hoursAvaliableTo.setVisibility(View.INVISIBLE);

        if(m_currentUser != null)
        {
            Boolean isChef = m_currentUser.IsChef.equals("true");
            switch_chef.setChecked(isChef);

            if(!isChef)
            {
                input_foodType.setVisibility(View.INVISIBLE);
                findViewById(R.id.txt_foodSpeciality).setVisibility(View.INVISIBLE);
                m_txt_pickDateFrom.setVisibility(View.INVISIBLE);
                m_txt_pickDateTo.setVisibility(View.INVISIBLE);
                //m_txt_hoursAvaliableFrom.setVisibility(View.INVISIBLE);
                //m_txt_hoursAvaliableTo.setVisibility(View.INVISIBLE);
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

                if(!switch_chef.isChecked() || (m_hoursAvaliableFrom == null && m_hoursAvaliableTo == null))
                {
                    onBackPressed();
                }
                /*else
                {
                    if(m_hoursAvaliableFrom != null && m_hoursAvaliableTo != null)
                    {

                        //to-do set loading bar
                        UpdateMealCallback umc = new UpdateMealCallback();
                        DatabaseCommunicator dbComms = new DatabaseCommunicator();
                        umc.SetMessage("UPDATE " + dbComms.m_mealTable + " SET hoursAvaliableFrom='" + m_hoursAvaliableFrom + "' WHERE owner_user_id='" + LocalSettings.LocalUser.Id + "';");
                        dbComms.GenericUpload(umc);

                    }
                    else
                    {
                        SetError("Make sure both time to & from are set for meal times.");
                    }
                }*/
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
                    //m_txt_pickDateFrom.setVisibility(View.VISIBLE);
                    //m_txt_pickDateTo.setVisibility(View.VISIBLE);
                    //m_txt_hoursAvaliableFrom.setVisibility(View.VISIBLE);
                    //m_txt_hoursAvaliableTo.setVisibility(View.VISIBLE);
                }
                else
                {
                    input_foodType.setVisibility(View.INVISIBLE);
                    findViewById(R.id.txt_foodSpeciality).setVisibility(View.INVISIBLE);
                    //m_txt_pickDateFrom.setVisibility(View.INVISIBLE);
                    //m_txt_pickDateTo.setVisibility(View.INVISIBLE);
                    //m_txt_hoursAvaliableFrom.setVisibility(View.INVISIBLE);
                    //m_txt_hoursAvaliableTo.setVisibility(View.INVISIBLE);
                }
            }
        });

        /*
        m_onHoursFromSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1)
            {
                m_hoursAvaliableFrom = (i + ":" + i1);
                m_txt_pickDateFrom.setText(i + ":" + i1);
            }
        };

        m_onHoursToSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1)
            {
                m_hoursAvaliableTo = (i + ":" + i1);
                m_txt_pickDateTo.setText(i + ":" + i1);
            }
        };

        View.OnClickListener pickDateFromListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ChefAccountSettings.this, m_onHoursFromSetListener, 0, 0, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        };
        m_txt_pickDateFrom.setOnClickListener(pickDateFromListener);
        m_txt_hoursAvaliableFrom.setOnClickListener(pickDateFromListener);

        View.OnClickListener pickDateToListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ChefAccountSettings.this, m_onHoursToSetListener, 0, 0, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        };
        m_txt_pickDateTo.setOnClickListener(pickDateToListener);
        m_txt_hoursAvaliableTo.setOnClickListener(pickDateToListener);
         */
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(ChefAccountSettings.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }

    /*private class UpdateMealCallback extends BaseCallback
    {
        @Override
        public Void call() throws Exception
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(m_message.equals("null"))
                    {
                        onBackPressed();
                    }
                    else
                    {
                        SetError("Update failed! Check internet connection.");
                    }
                }
            });

            return null;
        }
    }*/
}
