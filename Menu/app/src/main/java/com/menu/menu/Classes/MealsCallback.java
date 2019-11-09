package com.menu.menu.Classes;

import java.util.ArrayList;

public class MealsCallback extends BaseCallback
{
    private ArrayList<Meal> m_meals = null;
    public void AddMeals(ArrayList<Meal> m)
    {
        m_meals = m;
    }
}
