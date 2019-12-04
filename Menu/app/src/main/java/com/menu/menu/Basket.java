package com.menu.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.menu.menu.Classes.BasketListViewItem;
import com.menu.menu.Classes.DatabaseCommunicator;
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
        m_displayList.setAdapter(new BasketListViewItem(getContext(),orders));
        DatabaseCommunicator dbComms = new DatabaseCommunicator();

        for (Order order : orders)
        {
            UpdateMealOfOrder umoo = new UpdateMealOfOrder();
            umoo.SetMessage("SELECT * FROM " + dbComms.m_mealTable + " WHERE id='"+order.GetMealId()+"';");
            dbComms.RequestMealData(umoo);
        }
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
}
