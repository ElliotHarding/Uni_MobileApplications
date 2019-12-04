package com.menu.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.MealsCallback;
import com.menu.menu.Classes.Order;

import java.util.ArrayList;

public class Basket extends Fragment
{
    private View.OnClickListener m_drawerListener;
    public static ArrayList<Order> orders = new ArrayList<>();
    ListView m_displayList;

    public Basket()
    {
    }

    public void SetDrawerButtonListner(View.OnClickListener listener)
    {
        m_drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_basket, container, false);
        root.findViewById(R.id.drawerToggle).setOnClickListener(m_drawerListener);
        m_displayList = root.findViewById(R.id.listView_basket);
        m_displayList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getContext(), MealView.class);
                intent.putExtra("meal", orders.get(position).GetMeal());
                startActivity(intent);
            }
        });

        Update();

        return root;
    }

    private void Update()
    {
        BasicUpdate();
        DatabaseCommunicator dbComms = new DatabaseCommunicator();

        for (Order order : orders)
        {
            UpdateMealOfOrder umoo = new UpdateMealOfOrder();
            umoo.SetMessage("SELECT * FROM " + dbComms.m_mealTable + " WHERE id='"+order.GetMealId()+"';");
            dbComms.RequestMealData(umoo);
        }
    }

    private void BasicUpdate()
    {
        m_displayList.setAdapter(new BasketListViewItem(getContext(),orders));
    }

    private class UpdateMealOfOrder extends MealsCallback
    {
        @Override
        public Void call() throws Exception
        {
            ((MainHub)getActivity()).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(!m_meals.isEmpty())
                    {
                        for (Order order : orders)
                        {
                            if (order.GetMeal().getId().equals(m_meals.get(0).getId()))
                            {
                                order.SetMeal(m_meals.get(0));
                            }
                        }
                        m_displayList.setAdapter(new BasketListViewItem(getContext(),orders));
                    }
                    else
                    {
                        SetError("Failed to update an order!");
                    }
                }
            });
            return null;
        }
    }

    private void SetError(String errorString)
    {
        Toast.makeText(getActivity(), errorString,  Toast.LENGTH_LONG).show();
    }

    //Class used to create a corresponding UI element for each Meal in an arraylist of meals
    //These UI elements are then added into displayList
    class BasketListViewItem extends ArrayAdapter<Order>
    {
        ArrayList<Order> m_orderListRef;

        public BasketListViewItem(Context c, ArrayList<Order> orderListRef)
        {
            super(c, R.layout.layout_meal_info, orderListRef);
            m_orderListRef = orderListRef;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
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
            Bitmap bmp = currentMeal.getPicture();
            if(bmp!=null)
                img.setImageBitmap(bmp);

            itemView.findViewById(R.id.btn_portionsAdd).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int mealNumber = currentOrder.GetNumberOfMeals_n();
                    if(mealNumber < currentOrder.GetMeal().getMaxNoPortions_n())
                    {
                        mealNumber+=1;
                        currentOrder.SetNumberOfMeals_n(mealNumber);
                        txt_orderAmount.setText(currentOrder.GetNumberOfMeals());
                    }
                    BasicUpdate();
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
                    BasicUpdate();
                }
            });

            itemView.findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    m_orderListRef.remove(position);
                    BasicUpdate();
                }
            });

            return itemView;
        }
    }
}
