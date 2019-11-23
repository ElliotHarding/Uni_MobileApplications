package com.menu.menu;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.MealsCallback;

import java.util.ArrayList;
import java.util.List;

public class ChefSettings extends AppCompatActivity
{
    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    List<Meal> m_mealInfoArray = new ArrayList<>();
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
                MealRegistration.SetMeal(null); //null signifies new meal...
                startActivity(new Intent(ChefSettings.this, MealRegistration.class));
            }
        });

        m_displayList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                MealRegistration.SetMeal(m_mealInfoArray.get(position));
                startActivity(new Intent(ChefSettings.this, MealRegistration.class));
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
        gmlc.SetMessage("SELECT * FROM " + m_dbComms.m_mealTable + " WHERE owner_user_id = '" + LocalSettings.LocalUser.Id + "';");
        m_dbComms.RequestMealData(gmlc);
    }

    //Class used to create a corresponding UI element for each Meal in m_mealInfoArray
    //These UI elements are then added into m_displayList
    private class MealListAdaptor extends ArrayAdapter<Meal>
    {
        MealListAdaptor()
        {
            super(ChefSettings.this, R.layout.layout_meal_info, m_mealInfoArray);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            //get view from activity_forms.xml
            View itemView = convertView;
            if (itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.layout_meal_info, parent, false);
            }

            Meal currentMeal = m_mealInfoArray.get(position);


            //Name
            TextView subjectText = itemView.findViewById(R.id.listItem_Text);
            subjectText.setText(currentMeal.Name);


            //On sale
            TextView onSale = itemView.findViewById(R.id.listItem_OnSale);
            String whenAvaliable = "Avaliable; " + currentMeal.HoursAvaliableFrom + "-" + currentMeal.HoursAvaliableTo;
            onSale.setText(whenAvaliable);
            if (currentMeal.CurrentlyOnSale())
            {
                onSale.setTextColor(Color.GREEN);
            }
            else
            {
                onSale.setTextColor(Color.RED);
            }


            //Img
            ImageView img = itemView.findViewById(R.id.img);
            img.setImageBitmap(currentMeal.Picture);

            return itemView;
        }
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
                        m_displayList.setAdapter(new ChefSettings.MealListAdaptor());
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
