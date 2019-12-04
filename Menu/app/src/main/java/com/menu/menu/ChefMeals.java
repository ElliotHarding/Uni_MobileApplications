package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.MealListViewItem;
import com.menu.menu.Classes.MealsCallback;

import java.util.ArrayList;

public class ChefMeals extends AppCompatActivity
{
    public static String ChefId = null;
    public static String ChefUsername = null;

    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    ArrayList<Meal> m_mealInfoArray = new ArrayList<>();
    ListView m_displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_meals);

        ((TextView)findViewById(R.id.txt_username)).setText(ChefUsername);

        m_displayList = findViewById(R.id.mealsList);
        UpdateList();

        m_displayList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                MealView.m_meal = m_mealInfoArray.get(position);
                startActivity(new Intent(ChefMeals.this, MealView.class));
            }
        });
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(ChefMeals.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }

    private void UpdateList()
    {
        ChefMeals.GetMealsListCallback gmlc = new ChefMeals.GetMealsListCallback();
        gmlc.SetMessage("SELECT * FROM " + m_dbComms.m_mealTable + " WHERE owner_user_id = '" + ChefId + "';");
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
