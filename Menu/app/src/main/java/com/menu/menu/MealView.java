package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.ReturnPage;

import org.w3c.dom.Text;

//todo add address...
//add takeaway/eat-in option...

public class MealView extends AppCompatActivity
{
    private static Meal m_meal = null;
    private static ReturnPage m_previousPage = null;
    public static void Setup(Meal meal, ReturnPage returnPage)
    {
        m_meal = meal;
        m_previousPage = returnPage;
    }

    RadioButton m_radio_takeaway = null;
    RadioButton m_radio_eatIn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);

        final TextView txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);

        final TextView txt_name = findViewById(R.id.txt_name);
        final TextView txt_pricePerDish = findViewById(R.id.txt_pricePerDish);
        final TextView txt_ingredients = findViewById(R.id.txt_ingredients);
        final TextView txt_numberMeals = findViewById(R.id.txt_numberMeals);
        final ImageView img_image = findViewById(R.id.img_image);
        final EditText input_numDishesOrdered = findViewById(R.id.input_numerOfDishes);
        final Button btn_order = findViewById(R.id.btn_order);
        final Button btn_back = findViewById(R.id.btn_back);
        m_radio_takeaway = findViewById(R.id.radio_takeaway);
        m_radio_eatIn = findViewById(R.id.radio_EatIn);

        if (m_meal != null)
        {
            txt_name.setText(m_meal.Name);
            txt_pricePerDish.setText(m_meal.Price);
            txt_ingredients.setText(m_meal.Ingredients);
            txt_numberMeals.setText("Number of dishes (Max : " + Integer.toString(m_meal.MaxQuantity) + ")");
            //todo img_image.setImageBitmap(m_meal.Image);

            if (!m_meal.Takeaway)
            {
                m_radio_takeaway.setVisibility(View.INVISIBLE);
                m_radio_takeaway.setChecked(false);
                m_radio_eatIn.setChecked(true);
            }
            else if (!m_meal.EatIn)
            {
                m_radio_eatIn.setVisibility(View.INVISIBLE);
                m_radio_eatIn.setChecked(false);
                m_radio_takeaway.setChecked(true);
            }
            RadioChange(true);
        }
        else
        {
            txt_error.setText("Meal not found! Check internet?");
            txt_error.setVisibility(View.VISIBLE);
        }

        btn_order.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                int numDishesOrdered = 0;
                try
                {
                    numDishesOrdered = Integer.parseInt(input_numDishesOrdered.getText().toString());
                }
                catch (Exception e)
                {
                    txt_error.setText("Number of dishes to order is empty or invalid");
                    txt_error.setVisibility(View.VISIBLE);
                    return;
                }

                if (numDishesOrdered < m_meal.MaxQuantity && numDishesOrdered > 0 && m_meal != null)
                {
                    if (m_radio_takeaway.isChecked())
                    {
                        PaymentProcessing.Setup(m_meal, numDishesOrdered);
                        startActivity(new Intent(MealView.this, PaymentProcessing.class));
                    }
                    else
                    {
                        startActivity(new Intent(MealView.this, MeetupChat.class));
                    }
                }
                else
                {
                    txt_error.setText("Number of dishes to order surpasses the maximum " + Integer.toString(m_meal.MaxQuantity));
                    txt_error.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MealView.this, m_previousPage == ReturnPage.PAGE_MAP ? Map.class : SearchResults.class));
            }
        });

        m_radio_takeaway.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RadioChange(m_radio_takeaway.isChecked());
            }
        });

        m_radio_eatIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RadioChange(!m_radio_eatIn.isChecked());
            }
        });
    }

    void RadioChange(boolean takeaway)
    {
        if (m_meal.Takeaway && m_meal.EatIn)
        {
            if (takeaway)
            {
                m_radio_takeaway.setChecked(true);
                m_radio_eatIn.setChecked(false);
            }
            else
            {
                m_radio_takeaway.setChecked(false);
                m_radio_eatIn.setChecked(true);
            }
        }
    }
}
