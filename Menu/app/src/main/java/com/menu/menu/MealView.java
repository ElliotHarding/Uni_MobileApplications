package com.menu.menu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.BasketItem;
import com.menu.menu.Classes.Order;
import com.menu.menu.Classes.UsersCallback;

public class MealView extends AppCompatActivity
{
    private Meal m_meal = null;
    public static Bitmap FailedBitmap = null;
    private TextView txt_address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);

        Bundle extras = getIntent().getExtras();
        if(extras == null || !extras.containsKey("meal"))
        {
            SetError("Error. Page loaded incorrectly.");
        }
        else
        {
            final EditText input_numDishesOrdered = findViewById(R.id.input_numerOfDishes);
            final Switch switch_takeaway = findViewById(R.id.switch_takeaway);
            final Switch switch_eatIn = findViewById(R.id.switch_eatIn);

            m_meal = (Meal)extras.getSerializable("meal");

            txt_address = findViewById(R.id.txt_addressFill);
            switch_takeaway.setActivated(m_meal.IsTakeaway());
            switch_eatIn.setActivated(m_meal.IsEatIn());
            ((TextView)findViewById(R.id.txt_name)).setText(m_meal.getName());
            ((TextView)findViewById(R.id.txt_pricePerDish)).setText(m_meal.getPrice() + "£");
            ((TextView)findViewById(R.id.txt_numberMeals)).setText("Number of dishes (Max : " + m_meal.getMaxNoPortions() + ")");
            ((TextView)findViewById(R.id.input_ingredients)).setText(m_meal.getIngredients());

            Bitmap bmp = m_meal.getPicture();
            if(bmp != null)
                ((ImageView)findViewById(R.id.img_image)).setImageBitmap(bmp);
            else if(FailedBitmap != null)
            {
                ((ImageView)findViewById(R.id.img_image)).setImageBitmap(FailedBitmap);
                FailedBitmap = null;
            }
            else
            {
                ((ImageView)findViewById(R.id.img_image)).setImageResource(R.drawable.ic_meal);
            }

            ((TextView)findViewById(R.id.txt_rating)).setText(m_meal.getRating() +"/5");

            DatabaseCommunicator dbComms = new DatabaseCommunicator();
            OtherUserInfoCallback OUIC = new OtherUserInfoCallback();
            OUIC.SetMessage("SELECT * FROM " + dbComms.m_userTable + " WHERE id='"+m_meal.getOwnerId()+"';");
            dbComms.RequestUserData(OUIC);


            final Dialog allergenDialog = new Dialog(this);
            allergenDialog.setContentView(R.layout.dialog_alergen_info);
            ((RadioButton)allergenDialog.findViewById(R.id.radio_containsGluten)).setChecked(m_meal.getContainsGluten());
            allergenDialog.findViewById(R.id.radio_containsGluten).setEnabled(false);
            ((RadioButton)allergenDialog.findViewById(R.id.radio_containsMilk)).setChecked(m_meal.getContainsMilk());
            allergenDialog.findViewById(R.id.radio_containsMilk).setEnabled(false);
            ((RadioButton)allergenDialog.findViewById(R.id.radio_isHalal)).setChecked(m_meal.getHalal());
            allergenDialog.findViewById(R.id.radio_isHalal).setEnabled(false);
            ((RadioButton)allergenDialog.findViewById(R.id.radio_isVegan)).setChecked(m_meal.getVegan());
            allergenDialog.findViewById(R.id.radio_isVegan).setEnabled(false);
            ((RadioButton)allergenDialog.findViewById(R.id.radio_isVegetarian)).setChecked(m_meal.getVegiterian());
            allergenDialog.findViewById(R.id.radio_isVegetarian).setEnabled(false);
            findViewById(R.id.btn_viewAllergenInfo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allergenDialog.show();
                }
            });

            findViewById(R.id.btn_order).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        int orderedDishes = Integer.parseInt(input_numDishesOrdered.getText().toString());
                        if(!(orderedDishes <= Integer.parseInt(m_meal.getMaxNoPortions()) && orderedDishes > 0))
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

                                        Intent intent = new Intent(MealView.this, MainHub.class);
                                        intent.putExtra("fragment", MainHub.BasketFragmentTag);
                                        finish();
                                        startActivity(intent);
                                        break;
                                }
                            }
                        };

                        //Add order
                        Basket.addOrder(new Order(m_meal, m_meal.getOwnerId(), LocalSettings.GetLocalUser().getId(), input_numDishesOrdered.getText().toString(), switch_takeaway.isChecked()));

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
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(MealView.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }

    private class OtherUserInfoCallback extends UsersCallback
    {
        @Override
        public Void call() throws Exception
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(m_users != null && !m_users.isEmpty())
                    {
                        txt_address.setText(m_users.get(0).getAddressLine1() + "\n" + m_users.get(0).getAddressLine2() + "\n" +m_users.get(0).getAddressLine3() + "\n" + m_users.get(0).getAddressPostCode());
                    }
                    else
                    {
                        SetError("Data not found! Check internet?");
                    }
                }
            });
            return null;
        }
    }
}
