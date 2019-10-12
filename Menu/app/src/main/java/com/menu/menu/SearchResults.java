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

public class SearchResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        final DatabaseCommunicator dbComms = new DatabaseCommunicator();

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);

        Button btn_simClickedResult = findViewById(R.id.btn_simClickedResult);
        btn_simClickedResult.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Meal clickedMeal = dbComms.GetMeal("");
                MealView.Setup(clickedMeal, ReturnPage.PAGE_SEARCHRESULTS);
                startActivity(new Intent(SearchResults.this, MealView.class));
            }
        });

        Button btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(SearchResults.this, Home.class));
            }
        });
    }
}
