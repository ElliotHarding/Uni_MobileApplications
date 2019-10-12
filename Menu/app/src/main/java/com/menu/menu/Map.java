package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.ReturnPage;

public class Map extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);

        final DatabaseCommunicator dbComms = new DatabaseCommunicator();

        Button btn_simMapClick = findViewById(R.id.btn_simMapClick);
        btn_simMapClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Meal clickedMeal = dbComms.GetMeal("");
                MealView.Setup(clickedMeal, ReturnPage.PAGE_MAP);
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
    }
}
