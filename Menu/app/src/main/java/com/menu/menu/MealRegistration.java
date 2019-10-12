package com.menu.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.io.FileNotFoundException;
import java.io.IOException;

public class MealRegistration extends AppCompatActivity
{
    private static Meal m_currentMeal = null;
    public static void SetMeal(Meal meal)
    {
        m_currentMeal = meal;
    }

    TextView m_txt_error = null;
    ImageView m_img_image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_registration);

        final DatabaseCommunicator dbComms = new DatabaseCommunicator();

        m_txt_error = findViewById(R.id.txt_error);
        m_txt_error.setVisibility(View.INVISIBLE);

        m_img_image = findViewById(R.id.img_image);
        final ImageButton btn_uploadImage = findViewById(R.id.btn_upload);
        final EditText input_name = findViewById(R.id.input_name);
        final EditText input_maxNumberOfDishes = findViewById(R.id.input_numerOfDishes);
        final EditText input_price = findViewById(R.id.input_price);
        final EditText input_ingredients = findViewById(R.id.input_ingredients);
        final RadioButton toggle_onSale = findViewById(R.id.toggle_onSale);
        final Button btn_add = findViewById(R.id.btn_order);
        final Button btn_quit = findViewById(R.id.btn_back);
        final Button btn_delete = findViewById(R.id.btn_delete);
        final RadioButton radio_takeaway = findViewById(R.id.radio_takeaway);
        final RadioButton radio_eatIn = findViewById(R.id.radio_eatIn);

        if (m_currentMeal != null)
        {
            input_name.setText(m_currentMeal.Name);
            input_ingredients.setText(m_currentMeal.Ingredients);
            input_maxNumberOfDishes.setText(Integer.toString(m_currentMeal.MaxQuantity));
            input_price.setText(m_currentMeal.Price);
            toggle_onSale.setActivated(m_currentMeal.OnSale);
            //todo m_img_image.setImageBitmap(m_currentMeal.Image);
            radio_takeaway.setActivated(m_currentMeal.Takeaway);
            radio_eatIn.setActivated(m_currentMeal.EatIn);

            btn_add.setVisibility(View.INVISIBLE);
        }
        else
        {
            //Since the meal is new, don't need to delete it...
            btn_delete.setVisibility(View.INVISIBLE);

            m_currentMeal = new Meal();
            m_currentMeal.OwnerUsername = LocalSettings.LocalUser.Username;
        }

        btn_uploadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 3);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                try
                {
                    m_currentMeal.MaxQuantity = Integer.parseInt(input_maxNumberOfDishes.getText().toString());
                }
                catch (Exception e)
                {
                    m_txt_error.setText("Max quantity is either empty or invalid");
                    m_txt_error.setVisibility(View.VISIBLE);
                    return;
                }

                m_currentMeal.OnSale = toggle_onSale.isActivated();
                m_currentMeal.Name = input_name.getText().toString();
                //todo m_currentMeal.Image = ((BitmapDrawable)m_img_image.getDrawable()).getBitmap();
                m_currentMeal.Ingredients = input_ingredients.getText().toString();
                m_currentMeal.Price = input_price.getText().toString();
                m_currentMeal.EatIn = radio_eatIn.isActivated();
                m_currentMeal.Takeaway = radio_takeaway.isActivated();

                String errorString = ValidateMeal(m_currentMeal);
                if (errorString == "NO-ERROR")
                {
                    if (dbComms.AddMeal(m_currentMeal))
                    {
                        NavigateToPreviousPage();
                    }
                    else
                    {
                        m_txt_error.setText("Failed to add meal. Check internet connection.");
                        m_txt_error.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    m_txt_error.setText(errorString);
                    m_txt_error.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_quit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavigateToPreviousPage();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (dbComms.DeleteMeal(m_currentMeal))
                {
                    NavigateToPreviousPage();
                }
                else
                {
                    m_txt_error.setText("Failed to remove meal. Check internet connection.");
                    m_txt_error.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void NavigateToPreviousPage()
    {
        startActivity(new Intent(MealRegistration.this, ChefSettings.class));
    }

    private String ValidateMeal(Meal meal)
    {
        //todo validation...
        //name should be unique
        return "NO-ERROR";
    }

    //Overridden so we can get the uploaded image
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //GET_FROM_GALLERY = 3
        if(requestCode==3 && resultCode == Activity.RESULT_OK)
        {
            Uri selectedImage = data.getData();
            try
            {
                m_currentMeal.Image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                m_img_image.setImageBitmap(m_currentMeal.Image);
            }
            catch (FileNotFoundException e)
            {
                m_txt_error.setText("Image file could not be found");
                m_txt_error.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {
                m_txt_error.setText("An I/O error occured while acquiring the image");
                m_txt_error.setVisibility(View.VISIBLE);
            }
        }
    }
}
 