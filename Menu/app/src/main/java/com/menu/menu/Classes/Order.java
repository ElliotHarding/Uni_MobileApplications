package com.menu.menu.Classes;

import java.io.Serializable;

public class Order implements Serializable
{
    public enum State
    {
        AwatingResponse,
        InBasket,
        BeingMade,
        OnRoute,
        Delivered
    }

    private String m_id = null;
    private String m_mealId = null; //Even though we have m_meal, it may not be set at all times...
    private String m_numberOfMeals = null;
    private String m_ordererId = null;
    private String m_currentState = null;
    private String m_arrivalTime = null;
    private Meal m_meal = null;

    public Order()
    {

    }

    //Creating a basket order
    public Order(Meal meal, String numberOfMeals)
    {
        m_meal = meal;
        m_mealId = meal.getId();
        m_numberOfMeals = numberOfMeals;
        m_currentState = "In Basket";
    }

    public State cGetState()
    {
        switch (m_currentState)
        {
            case "Awaiting Response From Chef":
                return State.AwatingResponse;
            case "Being Made":
                return State.BeingMade;
            case "On Route":
                return State.OnRoute;
            case "Delivered":
                return State.Delivered;
            case "In Basket":
                return State.InBasket;
            default:
                return null;
        }
    }

    public void cSetState(State cState)
    {
        switch (cState)
        {
            case AwatingResponse:
                SetState("Awaiting Response From Chef");
            case BeingMade:
                SetState("Being Made");
            case OnRoute:
                SetState("On Route");
            case Delivered:
                SetState("Delivered");
            case InBasket:
                SetState("In Basket");
        }
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

    public String GetOrdererId()
    {
        return m_ordererId;
    }

    public void SetOrdererId(String ordererId)
    {
        m_ordererId = ordererId;
    }

    public String GetState()
    {
        return m_currentState;
    }

    public void SetState(String currentState)
    {
        m_currentState = currentState;
    }

    public String GetArrivalTime()
    {
        return m_arrivalTime;
    }

    public void SetArrivalTime(String arrivalTime)
    {
        m_arrivalTime = arrivalTime;
    }
}
