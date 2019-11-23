package com.menu.menu.Classes;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.menu.menu.R;
import java.util.ArrayList;

//Class used to create a corresponding UI element for each Meal in an arraylist of meals
//These UI elements are then added into displayList
class MealListViewItem extends ArrayAdapter<Meal>
{
    ArrayList<Meal> m_mealListRef;
    Activity m_activity;

    public MealListViewItem(Activity activity, ArrayList<Meal> mealListRef)
    {
        super(activity, R.layout.layout_meal_info, mealListRef);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //get view from activity_forms.xml
        View itemView = convertView;
        if (itemView == null)
        {
            itemView = m_activity.getLayoutInflater().inflate(R.layout.layout_meal_info, parent, false);
        }

        Meal currentMeal = m_mealListRef.get(position);

        //Subject text
        TextView subjectText = itemView.findViewById(R.id.li_text);
        subjectText.setText(currentMeal.Name);

        //On sale
        TextView onSale = itemView.findViewById(R.id.li_infoRight);
        if (currentMeal.CurrentlyOnSale())
        {
            onSale.setText("On Sale");
            onSale.setTextColor(Color.GREEN);
        }
        else
        {
            onSale.setText("Marked Unavailable");
            onSale.setTextColor(Color.RED);
        }

        //Price
        ((TextView)itemView.findViewById(R.id.li_infoLeft)).setText(currentMeal.Rating + "/5");

        //Img
        ImageView img = itemView.findViewById(R.id.img);
        img.setImageBitmap(currentMeal.Picture);

        return itemView;
    }
}
