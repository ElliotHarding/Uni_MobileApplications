package com.menu.menu.Classes;

import java.util.ArrayList;

public class Order
{
    private String m_id = "NEWID()";
    private String m_mealId = null;
    private Meal m_meal = null;
    private String m_mealChefId = null;
    private String m_mealEaterId = null;
    private String m_numOfPortions = null;
    private String m_currentState = null;
    private String m_isTakeaway = null;
    private ArrayList<String> m_messages = new ArrayList<>();

    public Order()
    {
    }

    public Order(Meal meal, String mealChefId, String mealEaterId, String numberOfPortions, boolean isTakeaway)
    {
        m_mealId = meal.getId();
        m_meal = meal;
        m_mealChefId = mealChefId;
        m_mealEaterId = mealEaterId;
        m_numOfPortions = numberOfPortions;
        setIsTakeaway_b(isTakeaway);
    }

    private final static String m_startInsert = "INSERT INTO " + DatabaseCommunicator.m_orderTable + " (id, meal_id, meal_chef_id, meal_eater_id, num_portions_ordered, current_state, is_takeaway, messages) VALUES ";
    public String getInsertString()
    {
        final String d = "','";
        return m_startInsert + "(" + m_id + ",'" + m_mealId + d + m_mealChefId + d + m_mealEaterId + d + m_numOfPortions + d + m_currentState + d + m_isTakeaway + d + getMessages_sql() + "');";
    }

    public String getUpdateString()
    {
        final String d = "','";
        return "UPDATE" + DatabaseCommunicator.m_orderTable + " SET meal_id='"+m_mealId+"', meal_chef_id='"+m_mealChefId+"', meal_eater_id='"+m_mealEaterId+"', num_portions_ordered='"+m_numOfPortions+"', current_state='"+m_currentState+"', is_takeaway='"+m_isTakeaway+"', messages='" + getMessages_sql() + "' WHERE id='"+m_id+"'";
    }

    public String getId()
    {
        return m_id;
    }

    public void setId(String id)
    {
        this.m_id = id;
    }

    public void setNumberOfPortions(String numOfPortions)
    {
        m_numOfPortions = numOfPortions;
    }

    public void setNumberOfPortions(int numberOfPortions)
    {
        m_numOfPortions = String.valueOf(numberOfPortions);
    }

    public String getNumberOfPortions()
    {
        return m_numOfPortions;
    }

    public int getNumberOfPortions_n()
    {
        if(m_numOfPortions == null)
            return 0;

        return Integer.parseInt(m_numOfPortions);
    }

    public String getMealId()
    {
        return m_mealId;
    }

    public void setMealId(String mealId)
    {
        m_mealId = mealId;
    }

    public String getCurrentState()
    {
        return m_currentState;
    }

    public void setCurrentState(String currentState)
    {
        this.m_currentState = currentState;
    }

    public void setEaterId(String id)
    {
        m_mealEaterId = id;
    }

    public String getEaterId()
    {
        return m_mealEaterId;
    }

    public boolean getIsTakeaway()
    {
        return m_isTakeaway.equals("true");
    }

    public void setIsTakeaway_b(Boolean isTakeaway)
    {
        m_isTakeaway = isTakeaway ? "true" : "false";
    }

    public void setIsTakeaway(String isTakeaway)
    {
        m_isTakeaway = isTakeaway;
    }

    public void setChefId(String id)
    {
        m_mealChefId = id;
    }

    public String getChefId()
    {
        return m_mealChefId;
    }

    public enum State
    {
        AwatingResponse,
        InBasket,
        BeingMade,
        OnRoute,
        Delivered
    }

    public State getState_c()
    {
        switch (getCurrentState())
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

    public void setState_c(State cState)
    {
        switch (cState)
        {
            case AwatingResponse:
                SetState("Awaiting Response From Chef");
                break;
            case BeingMade:
                SetState("Being Made");
                break;
            case OnRoute:
                SetState("On Route");
                break;
            case Delivered:
                SetState("Delivered");
                break;
            case InBasket:
                SetState("In Basket");
                break;
        }
    }

    public String GetState()
    {
        return getCurrentState();
    }

    public void SetState(String currentState)
    {
        setCurrentState(currentState);
    }

    public void setMessages_sql(String stringList)
    {
        String[] messages = stringList.split(" --- ");
        for (String m : messages)
        {
            m_messages.add(m);
        }
    }

    public void setMessages(ArrayList<String> list)
    {
        m_messages = list;
    }

    public ArrayList<String> getMessages()
    {
        return m_messages;
    }

    public String getMessages_sql()
    {
        if(m_messages.size() == 0)
            return "null";

        String combinedMessages = "";
        for (String s : m_messages)
        {
            combinedMessages += s + " --- ";
        }

        return combinedMessages.substring(0, combinedMessages.length() - 5);
    }

    public void setMeal(Meal meal)
    {
        m_meal = meal;
    }

    public Meal getMeal()
    {
        return m_meal;
    }
}
