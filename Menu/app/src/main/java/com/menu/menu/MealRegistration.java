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
import android.widget.Switch;
import android.widget.TextView;

import com.menu.menu.Classes.BaseCallback;
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

    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();

    TextView m_txt_error = null;
    ImageView m_img_image = null;

    ImageButton m_btn_uploadImage = null;
    EditText m_input_name = null;
    EditText m_input_maxNumberOfDishes = null;
    EditText m_input_price = null;
    EditText m_input_ingredients = null;
    Switch m_switch_takeaway = null;
    Switch m_switch_eatIn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_registration);

        m_txt_error = findViewById(R.id.txt_error);
        m_txt_error.setVisibility(View.INVISIBLE);

        m_img_image = findViewById(R.id.img_image);
        m_btn_uploadImage = findViewById(R.id.btn_upload);
        m_input_name = findViewById(R.id.input_name);
        m_input_maxNumberOfDishes = findViewById(R.id.input_numerOfDishes);
        m_input_price = findViewById(R.id.input_price);
        m_input_ingredients = findViewById(R.id.input_ingredients);
        m_switch_takeaway = findViewById(R.id.switch_takeaway);
        m_switch_eatIn = findViewById(R.id.switch_eatIn);

        final Button btn_add = findViewById(R.id.btn_order);
        final Button btn_delete = findViewById(R.id.btn_delete);
        final Button btn_update = findViewById(R.id.btn_update);

        if (m_currentMeal != null)
        {
            m_input_name.setText(m_currentMeal.Name);
            m_input_ingredients.setText(m_currentMeal.Ingredients);
            m_input_maxNumberOfDishes.setText(m_currentMeal.MaxNoPortions);
            m_input_price.setText(m_currentMeal.Price);
            //todo m_img_image.setImageBitmap(m_currentMeal.Image);
            m_switch_takeaway.setChecked(m_currentMeal.IsTakeaway());
            m_switch_eatIn.setChecked(m_currentMeal.IsEatIn());

            btn_add.setVisibility(View.INVISIBLE);
            btn_update.setVisibility(View.VISIBLE);
        }
        else
        {
            m_currentMeal = new Meal();
            m_currentMeal.OwnerUsername = LocalSettings.LocalUser.Username;
            m_currentMeal.OwnerId = LocalSettings.LocalUser.Id;

            //Since the meal is new, don't need to delete it...
            btn_delete.setVisibility(View.INVISIBLE);
            btn_update.setVisibility(View.INVISIBLE);
        }

        m_btn_uploadImage.setOnClickListener(new View.OnClickListener()
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
                if(GetAndValidateMeal())
                {
                    AddMealCallback amc = new AddMealCallback();
                    amc.SetMessage("IF NOT EXISTS (SELECT * FROM " + m_dbComms.m_mealTable + " WHERE meal_name = '" + m_currentMeal.Name + "')" + m_dbComms.m_mealInsert + "(" + m_currentMeal.GetInsertString() + ");");
                    m_dbComms.GenericUpload(amc);
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(GetAndValidateMeal())
                {
                    UpdateMealCallback umc = new UpdateMealCallback();
                    umc.SetMessage("UPDATE " + m_dbComms.m_mealTable + " SET " + m_currentMeal.GetUpdateString() + "WHERE id = '" + m_currentMeal.Id + "';");
                    m_dbComms.GenericUpload(umc);
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (m_dbComms.DeleteMeal(m_currentMeal))
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

    private Boolean GetAndValidateMeal()
    {
        m_currentMeal.MaxNoPortions = m_input_maxNumberOfDishes.getText().toString();
        m_currentMeal.Name = m_input_name.getText().toString();
        //todo m_currentMeal.Image = ((BitmapDrawable)m_img_image.getDrawable()).getBitmap();
        m_currentMeal.Ingredients = m_input_ingredients.getText().toString();
        m_currentMeal.Price = m_input_price.getText().toString();
        m_currentMeal.SetEatIn(m_switch_eatIn.isChecked(), m_switch_takeaway.isChecked());

        String errorString = ValidateMeal(m_currentMeal);
        if (errorString.equals("NO-ERROR"))
        {
            return true;
        }
        else
        {
            SetError(errorString);
            return false;
        }
    }

    private void SetError(String errorString)
    {
        m_txt_error.setText(errorString);
        m_txt_error.setVisibility(View.VISIBLE);
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
                m_img_image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage));
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

    private class UpdateMealCallback extends BaseCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_message.equals("null"))
            {
                NavigateToPreviousPage();
            }
            else
            {
                SetError(m_message);
            }

            return null;
        }
    }

    private class AddMealCallback extends BaseCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_message.equals("null"))
            {
                NavigateToPreviousPage();
            }
            else
            {
                SetError(m_message);
            }
            return null;
        }
    }
}
 