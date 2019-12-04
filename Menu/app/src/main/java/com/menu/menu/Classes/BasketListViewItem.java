package com.menu.menu.Classes;

import android.content.Context;
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
public class BasketListViewItem extends ArrayAdapter<Order>
{
    ArrayList<Order> m_orderListRef;

    public BasketListViewItem(Context c, ArrayList<Order> orderListRef)
    {
        super(c, R.layout.layout_meal_info, orderListRef);
        m_orderListRef = orderListRef;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //get view from activity_forms.xml
        View itemView = convertView;
        if (itemView == null)
        {
            itemView = (LayoutInflater.from(getContext())).inflate(R.layout.layout_basket_item, parent, false);
        }

        final Order currentOrder = m_orderListRef.get(position);
        final Meal currentMeal = currentOrder.GetMeal();

        //Subject text
        final TextView txt_subject = itemView.findViewById(R.id.li_top);
        txt_subject.setText(currentMeal.getName());

        //Ammount
        final TextView txt_orderAmount = itemView.findViewById(R.id.li_amount);
        txt_orderAmount.setText(currentOrder.GetNumberOfMeals());

        //Price
        ((TextView)itemView.findViewById(R.id.li_text)).setText(currentMeal.getPrice() + "£");

        //Img
        ImageView img = itemView.findViewById(R.id.img);
        img.setImageBitmap(currentMeal.getPicture());

        itemView.findViewById(R.id.btn_portionsAdd).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int mealNumber = currentOrder.GetNumberOfMeals_n();
                if(mealNumber < currentOrder.GetNumberOfMeals_n())
                {
                    mealNumber+=1;
                    currentOrder.SetNumberOfMeals_n(mealNumber);
                    txt_orderAmount.setText(currentOrder.GetNumberOfMeals());
                }
            }
        });

        itemView.findViewById(R.id.btn_portionsSub).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int mealNumber = currentOrder.GetNumberOfMeals_n();
                if(mealNumber > 0)
                {
                    mealNumber-=1;
                    currentOrder.SetNumberOfMeals_n(mealNumber);
                }
            }
        });

        itemView.findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_orderListRef.remove(this);
            }
        });

        return itemView;
    }
}

