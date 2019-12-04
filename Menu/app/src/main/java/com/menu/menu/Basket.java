package com.menu.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.menu.menu.Classes.Order;

import java.util.ArrayList;

public class Basket extends Fragment
{
    private View.OnClickListener m_drawerListener;
    public static ArrayList<Order> orders = null;

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
        return root;
    }




}
