package com.menu.menu;

import com.menu.menu.Classes.Order;

public class OrderTests
{
    Order m_testOrder = new Order();

    public String GetInsertString()
    {
        final String d = "','";
        return m_startInsert + "('" + m_id + d + getNumOfPortionsList_sql() + d + getMealIds_sql() + d + getMealOrdererId() + d + getCurrentState() + d + m_isTakeaway + d + getMessages_sql() + "');";
    }

    public String getId()
    {
        return m_id;
    }

    public void setId(String id)
    {
        this.m_id = id;
    }

    public ArrayList<String> getMealIds()
    {
        return m_mealIds;
    }

    private String getMealIds_sql()
    {
        if(m_mealIds.size() == 0)
            return "";

        String retString = "";
        for (String s : m_mealIds)
        {
            retString += s + "$";
        }

        return retString.substring(0, retString.length()-1);
    }

    public void setMealIds(ArrayList<String> mealIds)
    {
        this.m_mealIds = mealIds;
    }

    public void setMealIds_sql(String string)
    {
        String[] list = string.split("$");
        for (String s : list)
        {
            m_mealIds.add(s);
        }
    }

    private String getNumOfPortionsList_sql()
    {
        if(m_numOfPortionsList.size() == 0)
            return "";

        String retString = "";
        for (String s : m_numOfPortionsList)
        {
            retString += s + "$";
        }

        return retString.substring(0, retString.length()-1);
    }

    public ArrayList<String> getNumOfPortionsList()
    {
        return m_numOfPortionsList;
    }

    public void setNumOfPortionsList_sql(String string)
    {
        String[] list = string.split("$");
        for (String s : list)
        {
            m_numOfPortionsList.add(s);
        }
    }

    public void setNumOfPortionsList(ArrayList<String> numOfPortionsList)
    {
        this.m_numOfPortionsList = numOfPortionsList;
    }

    public String getCurrentState()
    {
        return m_currentState;
    }

    public void setCurrentState(String currentState)
    {
        this.m_currentState = currentState;
    }

    public void setMealOrdererId(String userElement)
    {
        m_mealOrdererId = userElement;
    }

    public String getMealOrdererId()
    {
        return m_mealOrdererId;
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
}
