package com.menu.menu.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.menu.menu.R;
import java.util.ArrayList;

//Class used to create a corresponding UI element for each Meal in an arraylist of meals
//These UI elements are then added into displayList
public class MealListViewItem extends ArrayAdapter<Meal>
{
    ArrayList<Meal> m_mealListRef;

    public MealListViewItem(Context c, ArrayList<Meal> mealListRef)
        {
        super(c, R.layout.layout_meal_info, mealListRef);
        m_mealListRef = mealListRef;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //get view from activity_forms.xml
        View itemView = convertView;
        if (itemView == null)
        {
            itemView = (LayoutInflater.from(getContext())).inflate(R.layout.layout_meal_info, parent, false);
        }

        Meal currentMeal = m_mealListRef.get(position);

        //Subject text
        TextView subjectText = itemView.findViewById(R.id.li_text);
        subjectText.setText(currentMeal.getName());

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
        ((TextView)itemView.findViewById(R.id.li_infoLeft)).setText(currentMeal.getRating() + "/5");

        //Img
        ImageView img = itemView.findViewById(R.id.img);
        Bitmap bmp = currentMeal.getPicture();
        if(bmp!=null)
            img.setImageBitmap(bmp);

        return itemView;
    }
}
