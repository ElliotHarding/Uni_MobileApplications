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
import com.menu.menu.Classes.ReturnPage;

import org.w3c.dom.Text;

public class MealView extends AppCompatActivity
{
    public static Meal m_meal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);

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
            txt_name.setText(m_meal.Name);
            txt_pricePerDish.setText(m_meal.Price + "£");
            txt_ingredients.setText(m_meal.Ingredients);
            txt_numberMeals.setText("Number of dishes (Max : " + m_meal.MaxNoPortions + ")");
            img_image.setImageBitmap(m_meal.Picture);
            ((RadioButton)findViewById(R.id.radio_containsGluten)).setChecked(m_meal.ContainsGluten);
            ((RadioButton)findViewById(R.id.radio_containsMilk)).setChecked(m_meal.ContainsMilk);
            ((RadioButton)findViewById(R.id.radio_isHalal)).setChecked(m_meal.IsHalal);
            ((RadioButton)findViewById(R.id.radio_isVegan)).setChecked(m_meal.IsVegan);
            ((RadioButton)findViewById(R.id.radio_isVegetarian)).setChecked(m_meal.IsVegiterian);
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
                int orderedDishes = 0;
                try
                {
                    orderedDishes = Integer.parseInt(input_numDishesOrdered.getText().toString());
                }
                catch (Exception e)
                {
                    SetError("Number of dishes to order is empty or invalid");
                    return;
                }
                //todo fix once serializable
                final int numDishesOrdered = orderedDishes;

                if (numDishesOrdered < Integer.parseInt(m_meal.MaxNoPortions) && numDishesOrdered > 0 && m_meal != null)
                {
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
                                        if (switch_takeaway.isChecked())
                                        {
                                            PaymentProcessing.m_meal = m_meal;
                                            PaymentProcessing.m_numberOfMeals = numDishesOrdered;
                                            startActivity(new Intent(MealView.this, PaymentProcessing.class));
                                        }
                                        else
                                        {
                                            MeetupChat.m_meal = m_meal;
                                            MeetupChat.m_numberOfMeals = numDishesOrdered;
                                            startActivity(new Intent(MealView.this, MeetupChat.class));
                                        }
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:

                                        Intent intent = new Intent(MealView.this, MainHub.class);
                                        intent.putExtra("fragment", MainHub.BasketFragmentTag);
                                        finish();
                                        startActivity(intent);
                                        break;
                                }
                            }
                        };

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
                else
                {
                    SetError("Number of dishes to order surpasses the maximum " + m_meal.MaxNoPortions);
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
