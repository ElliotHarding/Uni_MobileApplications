package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.Meal;

public class MealRegistration extends AppCompatActivity
{
    private static Meal m_currentMeal = null;
    public static void SetMeal(Meal meal)
    {
        m_currentMeal = meal;
    }

    private static Object m_previousPage = null;
    private static void SetPreviousPage(Object previousPage)
    {
        m_previousPage = previousPage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_registration);

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);

        ImageButton btn_uploadImage = findViewById(R.id.btn_upload);
        ImageView img_image = findViewById(R.id.img_image);
        final EditText input_name = findViewById(R.id.input_name);
        EditText input_maxNumberOfDishes = findViewById(R.id.input_maxMeals);
        EditText input_price = findViewById(R.id.input_price);
        EditText input_ingredients = findViewById(R.id.input_ingredients);
        final RadioButton toggle_onSale = findViewById(R.id.toggle_onSale);
        Button btn_add = findViewById(R.id.btn_add);
        Button btn_quit = findViewById(R.id.btn_quit);

        if (m_currentMeal != null)
        {
            input_name.setText(m_currentMeal.Name);
            input_ingredients.setText(m_currentMeal.Ingredients);
            input_maxNumberOfDishes.setText(m_currentMeal.MaxQuantity);
            input_price.setText(m_currentMeal.Price);
            toggle_onSale.setActivated(m_currentMeal.OnSale);
            img_image.setImageBitmap(m_currentMeal.Image);
        }

        m_currentMeal.OwnerUsername = LocalSettings.LocalUser.Username;

        btn_uploadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_currentMeal.OnSale = toggle_onSale.isActivated();
                m_currentMeal.Name = input_name.getText().toString();

                String errorString = ValidateMeal(m_currentMeal);
                if (errorString == "NO-ERROR")
                {
                    if (new DatabaseCommunicator().AddMeal(m_currentMeal))
                    {
                        startActivity(new Intent(MealRegistration.this, m_previousPage.getClass()));
                    }
                    else
                    {
                        txt_error.setText("Failed to add meal. Check internet connection.");
                        txt_error.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    txt_error.setText(errorString);
                    txt_error.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_quit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MealRegistration.this, m_previousPage.getClass()));
            }
        });
    }

    private String ValidateMeal(Meal meal)
    {
        return "NO-ERROR";
    }


}
 