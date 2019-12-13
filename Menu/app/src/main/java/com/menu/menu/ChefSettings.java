package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.MealListViewItem;
import com.menu.menu.Classes.MealsCallback;

import java.util.ArrayList;

public class ChefSettings extends AppCompatActivity
{
    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    ArrayList<Meal> m_mealInfoArray = new ArrayList<>();
    ListView m_displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_settings);

        m_displayList = findViewById(R.id.mealsList);
        UpdateList();

        Button btn_addMeal = findViewById(R.id.btn_addMeal);
        btn_addMeal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Not adding intent with meal key signifies new meal...
                startActivity(new Intent(ChefSettings.this, MealRegistration.class));
            }
        });

        m_displayList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ChefSettings.this, MealRegistration.class);
                intent.putExtra("meal", m_mealInfoArray.get(position));
                startActivity(intent);
            }
        });

        Button btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ChefSettings.this, MainHub.class));
            }
        });
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(ChefSettings.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }

    private void UpdateList()
    {
        GetMealsListCallback gmlc = new GetMealsListCallback();
        gmlc.SetMessage("SELECT * FROM " + m_dbComms.m_mealTable + " WHERE owner_user_id = '" + LocalSettings.GetLocalUser().getId() + "';");
        m_dbComms.RequestMealData(gmlc);
    }

    private class GetMealsListCallback extends MealsCallback
    {
        @Override
        public Void call() throws Exception
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(!m_meals.isEmpty())
                    {
                        m_mealInfoArray = m_meals;
                        m_displayList.setAdapter(new MealListViewItem(getApplicationContext(),m_mealInfoArray));
                    }
                    else
                    {
                        SetError(m_message);
                    }
                }
            });
            return null;
        }
    }
}
