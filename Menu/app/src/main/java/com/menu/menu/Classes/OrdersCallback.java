package com.menu.menu.Classes;

import java.util.ArrayList;

public class OrdersCallback extends BaseCallback
{
    protected ArrayList<Order> m_orders = null;
    public void AddOrders(ArrayList<Order> u)
    {
        m_orders = u;
    }
}