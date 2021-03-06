package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.MealListViewItem;
import com.menu.menu.Classes.MealsCallback;

import java.util.ArrayList;

public class ChefMeals extends AppCompatActivity
{
    private String m_chefId = null;
    private DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    private ArrayList<Meal> m_mealInfoArray = new ArrayList<>();
    private ListView m_displayList;
    private ProgressBar m_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_meals);

        m_progressBar = findViewById(R.id.progressBar);

        Bundle extras = getIntent().getExtras();
        if(extras == null || !extras.containsKey("chefId") || !extras.containsKey("chefUsername"))
        {
            SetError("Error. Page loaded incorrectly.");
        }
        else
        {
            m_chefId = extras.getString("chefId");
            ((TextView)findViewById(R.id.txt_username)).setText(extras.getString("chefUsername"));

            m_displayList = findViewById(R.id.mealsList);
            UpdateList();

            m_displayList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(ChefMeals.this, MealView.class);
                    Meal parcelMeal = m_mealInfoArray.get(position);
                    MealView.FailedBitmap = parcelMeal.getPicture();
                    parcelMeal.setPicture(null);
                    intent.putExtra("meal", parcelMeal);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(ChefMeals.this, MainHub.class));
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(ChefMeals.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }

    private void UpdateList()
    {
        m_progressBar.startNestedScroll(1);
        m_progressBar.setVisibility(View.VISIBLE);

        ChefMeals.GetMealsListCallback gmlc = new ChefMeals.GetMealsListCallback();
        gmlc.SetMessage("SELECT * FROM " + m_dbComms.m_mealTable + " WHERE owner_user_id = '" + m_chefId + "';");
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
                    if(m_meals != null && !m_meals.isEmpty())
                    {
                        m_mealInfoArray = m_meals;
                        m_displayList.setAdapter(new MealListViewItem(getApplicationContext(),m_mealInfoArray));
                    }
                    else
                    {
                        SetError(m_message);
                    }

                    m_progressBar.setVisibility(View.INVISIBLE);
                    m_progressBar.stopNestedScroll();
                }
            });
            return null;
        }
    }
}
