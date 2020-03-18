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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.MealsCallback;
import com.menu.menu.Classes.Order;
import com.menu.menu.Classes.OrdersCallback;

import java.util.ArrayList;

public class MyOrders extends Fragment
{
    private View.OnClickListener m_drawerListener;
    private ListView m_displayList;
    private ArrayList<Order> m_displayedOrders = new ArrayList<>();
    private ProgressBar m_progressBar;
    private DatabaseCommunicator m_dbComms = new DatabaseCommunicator();

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
        View root = inflater.inflate(R.layout.fragment_my_orders, container, false);
        root.findViewById(R.id.drawerToggle).setOnClickListener(m_drawerListener);
        m_displayList = root.findViewById(R.id.listView_orders);
        m_progressBar = root.findViewById(R.id.progressBar);
        m_displayList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getContext(), MeetupChat.class);
                intent.putExtra("orderId", m_displayedOrders.get(position).getId());
                startActivity(intent);
            }
        });

        update();

        return root;
    }

    @Override
    public void onResume()
    {
        update();
        super.onResume();
    }

    private void update()
    {
        m_progressBar.startNestedScroll(1);
        m_progressBar.setVisibility(View.VISIBLE);


        GetOrdersCallback goc = new GetOrdersCallback();
        goc.SetMessage("SELECT * FROM " + m_dbComms.m_orderTable + " WHERE meal_chef_id = '"+ LocalSettings.GetLocalUser().getId() +"' OR meal_eater_id='"+LocalSettings.GetLocalUser().getId()+"'");
        m_dbComms.RequestOrderData(goc);
    }

    private void SetError(String errorString)
    {
        Toast.makeText(getActivity(), errorString,  Toast.LENGTH_LONG).show();
    }

    class GetOrdersCallback extends OrdersCallback
    {
        @Override
        public Void call() throws Exception
        {
            (getActivity()).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (m_orders != null)
                    {
                        if(!m_orders.isEmpty())
                        {
                            for (Order order : m_orders)
                            {
                                GetMealOfOrderCallback gmooc = new GetMealOfOrderCallback();
                                gmooc.SetMessage("SELECT * FROM " + m_dbComms.m_mealTable + " WHERE id='"+order.getMealId()+"'");
                                gmooc.setOrder(order);
                                m_dbComms.RequestMealData(gmooc);
                            }
                        }
                        else
                        {
                            SetError("No orders to show!");
                            m_progressBar.setVisibility(View.INVISIBLE);
                            m_progressBar.stopNestedScroll();
                        }
                    }
                    else
                    {
                        SetError("Failed to retrieve orders!");
                        m_progressBar.setVisibility(View.INVISIBLE);
                        m_progressBar.stopNestedScroll();
                    }
                }
            });
            return null;
        }
    }

    class GetMealOfOrderCallback extends MealsCallback
    {
        Order m_order = null;
        @Override
        public Void call() throws Exception
        {
            (getActivity()).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (m_meals != null && m_meals.size() > 0)
                    {
                        m_order.setMeal(m_meals.get(0));
                        m_displayedOrders.add(m_order);
                        m_displayList.setAdapter(new OrderListViewItem(getContext(), m_displayedOrders));
                    }
                    else
                    {
                        SetError("Failed to retrieve an order! Check internet.");
                    }
                    m_progressBar.setVisibility(View.INVISIBLE);
                    m_progressBar.stopNestedScroll();
                }
            });
            return null;
        }

        public void setOrder(Order order)
        {
            m_order = order;
        }
    }

    //Class used to create a corresponding UI element for each ic_meal in an arraylist of meals
    //These UI elements are then added into displayList
    class OrderListViewItem extends ArrayAdapter<Order>
    {
        ArrayList<Order> m_orderItemListRef;

        public OrderListViewItem(Context c, ArrayList<Order> orderItemListRef)
        {
            super(c, R.layout.layout_order_info, orderItemListRef);
            m_orderItemListRef = orderItemListRef;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            //get view from activity_forms.xml
            View itemView = convertView;
            if (itemView == null)
            {
                itemView = (LayoutInflater.from(getContext())).inflate(R.layout.layout_order_info, parent, false);
            }

            final Order currentOrder = m_orderItemListRef.get(position);

            //Subject text
            final TextView txt_subject = itemView.findViewById(R.id.li_top);
            txt_subject.setText(currentOrder.getMeal().getPrice() + "£");

            //Ammount
            final TextView txt_orderAmount = itemView.findViewById(R.id.li_top2);
            txt_orderAmount.setText("Portions :" + currentOrder.getNumberOfPortions());

            //Price
            ((TextView)itemView.findViewById(R.id.li_text)).setText(currentOrder.getMeal().getName());

            //Img
            ImageView img = itemView.findViewById(R.id.img);
            Bitmap bmp = currentOrder.getMeal().getPicture();
            if(bmp!=null)
                img.setImageBitmap(bmp);
            else
                img.setImageResource(R.drawable.ic_meal);

            return itemView;
        }
    }
}
