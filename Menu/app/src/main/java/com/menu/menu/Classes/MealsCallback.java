package com.menu.menu.Classes;

import java.util.ArrayList;

public class MealsCallback extends BaseCallback
{
    protected ArrayList<Meal> m_meals = null;
    void AddMeals(ArrayList<Meal> m)
    {
        m_meals = m;
    }
}
