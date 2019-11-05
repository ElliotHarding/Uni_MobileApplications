package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.ReturnPage;

import java.util.ArrayList;

public class Map extends AppCompatActivity
{
    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);

        Button btn_simMapClick = findViewById(R.id.btn_simMapClick);
        btn_simMapClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MealView.m_meal = m_dbComms.GetMeal("");
                MealView.m_previousPage = ReturnPage.PAGE_MAP;
                startActivity(new Intent(Map.this, MealView.class));
            }
        });

        Button btn_gotoSearchBar = findViewById(R.id.btn_gotoSearchBar);
        btn_gotoSearchBar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Map.this, SearchResults.class));
            }
        });

        Button btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavigateHome();
            }
        });
    }

    void RefreshMap()
    {
        ArrayList<Meal> meals = m_dbComms.GetNearbyMeals(LocalSettings.LocalUser);
        //todo...
    }

    @Override
    public void onBackPressed()
    {
        NavigateHome();
    }

    private void NavigateHome()
    {
        startActivity(new Intent(Map.this, MainHub.class));
    }
}
