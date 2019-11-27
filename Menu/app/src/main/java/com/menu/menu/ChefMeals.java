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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.MealsCallback;

import java.util.ArrayList;
import java.util.List;

public class ChefMeals extends AppCompatActivity
{
    public static String ChefId = null;
    public static String ChefUsername = null;

    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    List<Meal> m_mealInfoArray = new ArrayList<>();
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

    //Class used to create a corresponding UI element for each Meal in m_mealInfoArray
    //These UI elements are then added into m_displayList
    private class MealListAdaptor extends ArrayAdapter<Meal>
    {
        MealListAdaptor()
        {
            super(ChefMeals.this, R.layout.layout_meal_info, m_mealInfoArray);
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
            TextView subjectText = itemView.findViewById(R.id.li_text);
            subjectText.setText(currentMeal.getName());


            //On sale
            TextView onSale = itemView.findViewById(R.id.li_infoRight);
            String whenAvaliable = "Avaliable; " + currentMeal.getHoursAvaliableFrom() + "-" + currentMeal.getHoursAvaliableTo();
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
                        m_displayList.setAdapter(new ChefMeals.MealListAdaptor());
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
