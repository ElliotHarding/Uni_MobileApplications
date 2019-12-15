package com.menu.menu;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.menu.menu.Classes.BaseCallback;
import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.Meal;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MealRegistration extends AppCompatActivity
{
    private Meal m_currentMeal = null;
    private DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    public static Bitmap FailedBitmap = null;

    ImageView m_img_image = null;
    EditText m_input_name = null;
    EditText m_input_maxNumberOfDishes = null;
    EditText m_input_price = null;
    EditText m_input_ingredients = null;
    EditText m_input_calories = null;
    Switch m_switch_takeaway = null;
    Switch m_switch_eatIn = null;
    Switch m_switch_isHalal = null;
    Switch m_switch_isVegan = null;
    Switch m_switch_isVegiterian = null;
    Switch m_switch_containsMilk = null;
    Switch m_switch_containsGluten = null;
    TextView m_txt_pickDateFrom = null;
    TextView m_txt_pickDateTo = null;
    TextView m_txt_hoursAvaliableFrom = null;
    TextView m_txt_hoursAvaliableTo = null;

    TimePickerDialog.OnTimeSetListener m_onHoursFromSetListener = null;
    TimePickerDialog.OnTimeSetListener m_onHoursToSetListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_registration);

        m_img_image = findViewById(R.id.img_image);
        m_input_name = findViewById(R.id.input_name);
        m_input_maxNumberOfDishes = findViewById(R.id.input_numerOfDishes);
        m_input_price = findViewById(R.id.input_price);
        m_input_ingredients = findViewById(R.id.input_ingredients);
        m_switch_takeaway = findViewById(R.id.switch_takeaway);
        m_switch_eatIn = findViewById(R.id.switch_eatIn);
        m_switch_isHalal = findViewById(R.id.switch_isHalal);
        m_switch_containsGluten = findViewById(R.id.switch_containsGluten);
        m_switch_containsMilk = findViewById(R.id.switch_containsMilk);
        m_switch_isVegan = findViewById(R.id.switch_isVegan);
        m_switch_isVegiterian = findViewById(R.id.switch_isVegetarian);
        m_txt_pickDateFrom = findViewById(R.id.txt_pickDateFrom);
        m_txt_pickDateTo = findViewById(R.id.txt_pickDateTo);
        m_txt_hoursAvaliableFrom = findViewById(R.id.txt_HoursAvaliableFrom);
        m_txt_hoursAvaliableTo = findViewById(R.id.txt_HoursAvaliableTo);
        m_input_calories = findViewById(R.id.input_calories);

        final Button btn_add = findViewById(R.id.btn_order);
        final Button btn_delete = findViewById(R.id.btn_delete);
        final Button btn_update = findViewById(R.id.btn_update);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("meal")) {
            m_currentMeal = (Meal) extras.getSerializable("meal");

            m_input_name.setText(m_currentMeal.getName());
            m_input_ingredients.setText(m_currentMeal.getIngredients());
            m_input_maxNumberOfDishes.setText(m_currentMeal.getMaxNoPortions());
            m_input_price.setText(m_currentMeal.getPrice());
            m_switch_takeaway.setChecked(m_currentMeal.IsTakeaway());
            m_switch_eatIn.setChecked(m_currentMeal.IsEatIn());
            m_switch_isHalal.setChecked(m_currentMeal.IsEatIn());
            m_switch_containsGluten.setChecked(m_currentMeal.getContainsGluten());
            m_switch_containsMilk.setChecked(m_currentMeal.getContainsMilk());
            m_switch_isVegan.setChecked(m_currentMeal.getVegan());
            m_switch_isVegiterian.setChecked(m_currentMeal.getVegiterian());
            m_txt_pickDateTo.setText(m_currentMeal.getHoursAvaliableTo());
            m_txt_pickDateFrom.setText(m_currentMeal.getHoursAvaliableFrom());
            m_input_calories.setText(m_currentMeal.getCalories());

            Bitmap bmp = m_currentMeal.getPicture();
            if (bmp != null)
                m_img_image.setImageBitmap(m_currentMeal.getPicture());
            else if (FailedBitmap != null)
            {
                m_img_image.setImageBitmap(FailedBitmap);
                FailedBitmap = null;
            }


            btn_add.setVisibility(View.INVISIBLE);
            btn_update.setVisibility(View.VISIBLE);
        }
        else
        {
            m_currentMeal = new Meal();
            m_currentMeal.setOwnerUsername(LocalSettings.GetLocalUser().getUsername());
            m_currentMeal.setOwnerId(LocalSettings.GetLocalUser().getId());

            //Since the meal is new, don't need to delete it...
            btn_delete.setVisibility(View.INVISIBLE);
            btn_update.setVisibility(View.INVISIBLE);
        }

        m_img_image.setOnClickListener(new View.OnClickListener()
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
                    amc.SetMessage("IF NOT EXISTS (SELECT * FROM " + m_dbComms.m_mealTable + " WHERE meal_name = '" + m_currentMeal.getName() + "')" + m_dbComms.m_mealInsert + "(" + m_currentMeal.GetInsertString() + ");");
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
                    umc.SetMessage("UPDATE " + m_dbComms.m_mealTable + " SET " + m_currentMeal.GetUpdateString() + "WHERE id = '" + m_currentMeal.getId() + "';");
                    m_dbComms.GenericUpload(umc);
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DeleteMealCallback dmc = new DeleteMealCallback();
                dmc.SetMessage("DELETE FROM " + m_dbComms.m_mealTable + " WHERE id='" + m_currentMeal.getId() + "';");
                m_dbComms.GenericUpload(dmc);
            }
        });

        m_onHoursFromSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1)
            {
                m_currentMeal.setHoursAvaliableFrom((i + ":" + i1));
                m_txt_pickDateFrom.setText(i + ":" + i1);
            }
        };

        m_onHoursToSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1)
            {
                m_currentMeal.setHoursAvaliableTo((i + ":" + i1));
                m_txt_pickDateTo.setText(i + ":" + i1);
            }
        };


        View.OnClickListener pickDateFromListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MealRegistration.this, m_onHoursFromSetListener, 0, 0, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        };
        m_txt_pickDateFrom.setOnClickListener(pickDateFromListener);
        m_txt_hoursAvaliableFrom.setOnClickListener(pickDateFromListener);

        View.OnClickListener pickDateToListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MealRegistration.this, m_onHoursToSetListener, 0, 0, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        };
        m_txt_pickDateTo.setOnClickListener(pickDateToListener);
        m_txt_hoursAvaliableTo.setOnClickListener(pickDateToListener);
    }

    private Boolean GetAndValidateMeal()
    {
        m_currentMeal.setMaxNoPortions(m_input_maxNumberOfDishes.getText().toString());
        m_currentMeal.setName(m_input_name.getText().toString());
        m_currentMeal.setPicture(((BitmapDrawable)m_img_image.getDrawable()).getBitmap());
        m_currentMeal.setIngredients(m_input_ingredients.getText().toString());
        m_currentMeal.setPrice(m_input_price.getText().toString());
        m_currentMeal.SetEatIn(m_switch_eatIn.isChecked(), m_switch_takeaway.isChecked());
        m_currentMeal.setContainsMilk(m_switch_containsMilk.isChecked());
        m_currentMeal.setHalal(m_switch_isHalal.isChecked());
        m_currentMeal.setContainsGluten(m_switch_containsGluten.isChecked());
        m_currentMeal.setContainsMilk(m_switch_containsMilk.isChecked());
        m_currentMeal.setVegan(m_switch_isVegan.isChecked());
        m_currentMeal.setVegiterian(m_switch_isVegiterian.isChecked());
        m_currentMeal.setCalories(m_input_calories.getText().toString());

        m_switch_containsMilk.isChecked();
        m_switch_isVegan.isChecked();
        m_switch_isVegiterian.isChecked();

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
        Toast t = Toast.makeText(MealRegistration.this, errorString,  Toast.LENGTH_LONG);
        t.show();
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
                m_currentMeal.setPicture(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage));
                m_img_image.setImageBitmap(m_currentMeal.getPicture());
            }
            catch (FileNotFoundException e)
            {
                SetError("Image file could not be found");
            }
            catch (IOException e)
            {
                SetError("An I/O error occured while acquiring the image");
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
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SetError("Update failed! Check internet connection.");
                    }
                });
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
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SetError("Adding meal failed! Check internet connection.");
                    }
                });
            }
            return null;
        }
    }

    private class DeleteMealCallback extends BaseCallback
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
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SetError("Delete failed! Check internet connection.");
                    }
                });
            }
            return null;
        }
    }
}
 