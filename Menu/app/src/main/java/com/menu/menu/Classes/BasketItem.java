package com.menu.menu.Classes;

import java.io.Serializable;

public class BasketItem implements Serializable
{
    private String m_id = null;
    private String m_mealId = null; //Even though we have m_meal, it may not be set at all times...
    private String m_numberOfMeals = null;
    private Meal m_meal = null;
    private Boolean m_isTakeaway = null;

    public BasketItem()
    {
    }

    //Creating a basket order
    public BasketItem(Meal meal, String numberOfMeals, boolean isTakeaway)
    {
        m_meal = meal;
        m_mealId = meal.getId();
        m_numberOfMeals = numberOfMeals;
        m_isTakeaway = isTakeaway;
    }

    public String getId()
    {
        return m_id;
    }

    public void SetId(String id)
    {
        m_id = id;
    }

    public Meal GetMeal()
    {
        return m_meal;
    }

    public void SetMeal(Meal meal)
    {
        m_meal = meal;
    }

    public void SetMealId(String id)
    {
        m_mealId = id;
    }

    public String GetMealId()
    {
        return m_mealId;
    }

    public String GetNumberOfMeals()
    {
        return m_numberOfMeals;
    }

    public int GetNumberOfMeals_n()
    {
        try
        {
            return Integer.parseInt(m_numberOfMeals);
        }
        catch (Exception e)
        {
            return 0;
        }
    }


    public void SetNumberOfMeals(String numberOfMeals)
    {
        m_numberOfMeals = numberOfMeals;
    }

    public void SetNumberOfMeals_n(int num)
    {
        m_numberOfMeals = String.valueOf(num);
    }

    public Boolean getTakeaway()
    {
        return m_isTakeaway;
    }

    public void setTakeaway(Boolean takeaway)
    {
        m_isTakeaway = takeaway;
    }
}
