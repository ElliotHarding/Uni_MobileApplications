package com.menu.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.MealListViewItem;
import com.menu.menu.Classes.MealsCallback;

import java.util.ArrayList;

public class MyMeals extends Fragment
{
    private DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    private ArrayList<Meal> m_mealInfoArray = new ArrayList<>();
    private ListView m_displayList;
    private ProgressBar m_progressBar;
    private View.OnClickListener m_drawerListener;

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
        View root = inflater.inflate(R.layout.fragment_my_meals, container, false);
        root.findViewById(R.id.drawerToggle).setOnClickListener(m_drawerListener);

        m_progressBar = root.findViewById(R.id.progressBar);

        m_displayList = root.findViewById(R.id.mealsList);
        UpdateList();

        Button btn_addMeal = root.findViewById(R.id.btn_addMeal);
        btn_addMeal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Not adding intent with meal key signifies new meal...
                startActivity(new Intent(getActivity(), MealRegistration.class));
            }
        });

        m_displayList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getActivity(), MealRegistration.class);
                Meal parcelMeal = m_mealInfoArray.get(position);
                MealRegistration.FailedBitmap = parcelMeal.getPicture();
                //parcelMeal.setPicture(null);
                intent.putExtra("meal", parcelMeal);
                startActivity(intent);
            }
        });

        return root;
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(getActivity(), errorString,  Toast.LENGTH_LONG);
        t.show();
    }

    private void UpdateList()
    {
        m_progressBar.startNestedScroll(1);
        m_progressBar.setVisibility(View.VISIBLE);

        GetMealsListCallback gmlc = new GetMealsListCallback();
        gmlc.SetMessage("SELECT * FROM " + m_dbComms.m_mealTable + " WHERE owner_user_id = '" + LocalSettings.GetLocalUser().getId() + "';");
        m_dbComms.RequestMealData(gmlc);
    }

    private class GetMealsListCallback extends MealsCallback
    {
        @Override
        public Void call() throws Exception
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(m_meals != null && !m_meals.isEmpty())
                    {
                        m_mealInfoArray = m_meals;
                        m_displayList.setAdapter(new MealListViewItem(getActivity(),m_mealInfoArray));
                    }
                    else
                    {
                        SetError(m_message);
                    }

                    m_progressBar.setVisibility(View.INVISIBLE);
                    m_progressBar.stopNestedScroll();
                }
            });
            return null;
        }
    }
}
