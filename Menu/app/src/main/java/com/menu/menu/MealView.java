package com.menu.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.Order;

public class MealView extends AppCompatActivity
{
    public static Meal m_meal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);

        final TextView txt_rating = findViewById(R.id.txt_rating);
        final TextView txt_name = findViewById(R.id.txt_name);
        final TextView txt_pricePerDish = findViewById(R.id.txt_pricePerDish);
        final TextView txt_ingredients = findViewById(R.id.txt_ingredients);
        final TextView txt_numberMeals = findViewById(R.id.txt_numberMeals);
        final ImageView img_image = findViewById(R.id.img_image);
        final EditText input_numDishesOrdered = findViewById(R.id.input_numerOfDishes);
        final Button btn_order = findViewById(R.id.btn_order);
        final Switch switch_takeaway = findViewById(R.id.switch_takeaway);
        final Switch switch_eatIn = findViewById(R.id.switch_eatIn);

        switch_takeaway.setActivated(m_meal.IsTakeaway());
        switch_eatIn.setActivated(m_meal.IsEatIn());

        if (m_meal != null)
        {
            txt_name.setText(m_meal.getName());
            txt_pricePerDish.setText(m_meal.getPrice() + "£");
            txt_ingredients.setText(m_meal.getIngredients());
            txt_numberMeals.setText("Number of dishes (Max : " + m_meal.getMaxNoPortions() + ")");
            img_image.setImageBitmap(m_meal.Picture);
            txt_rating.setText(m_meal.getRating() +"/5");
            ((RadioButton)findViewById(R.id.radio_containsGluten)).setChecked(m_meal.getContainsGluten());
            ((RadioButton)findViewById(R.id.radio_containsMilk)).setChecked(m_meal.getContainsMilk());
            ((RadioButton)findViewById(R.id.radio_isHalal)).setChecked(m_meal.getHalal());
            ((RadioButton)findViewById(R.id.radio_isVegan)).setChecked(m_meal.getVegan());
            ((RadioButton)findViewById(R.id.radio_isVegetarian)).setChecked(m_meal.getVegiterian());
        }
        else
        {
            SetError("Meal not found! Check internet?");
        }

        btn_order.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    int orderedDishes = Integer.parseInt(input_numDishesOrdered.getText().toString());
                    if(!(orderedDishes < Integer.parseInt(m_meal.getMaxNoPortions()) && orderedDishes > 0))
                    {
                        SetError("Number of dishes to order surpasses the maximum " + m_meal.getMaxNoPortions());
                        return;
                    }
                }
                catch (Exception e)
                {
                    SetError("Number of dishes to order is empty or invalid");
                    return;
                }

                if(switch_takeaway.isChecked() || switch_eatIn.isChecked())
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case DialogInterface.BUTTON_POSITIVE:
                                    onBackPressed();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    if (switch_takeaway.isChecked())
                                    {
                                        Intent intent = new Intent(MealView.this, MainHub.class);
                                        intent.putExtra("fragment", MainHub.BasketFragmentTag);
                                        finish();
                                        startActivity(intent);
                                    }

                                    break;
                            }
                        }
                    };

                    //Add meal to basket
                    Basket.orders.add(new Order(m_meal.getId(), input_numDishesOrdered.getText().toString()));

                    AlertDialog.Builder builder = new AlertDialog.Builder(MealView.this);
                    builder.setMessage("Continue shopping?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("Check Out", dialogClickListener).show();
                }
                else
                {
                    SetError("Please select a meal order option.");
                }
            }
        });

        switch_takeaway.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch_eatIn.setChecked(false);
            }
        });

        switch_eatIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch_takeaway.setChecked(false);
            }
        });
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(MealView.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }
}
