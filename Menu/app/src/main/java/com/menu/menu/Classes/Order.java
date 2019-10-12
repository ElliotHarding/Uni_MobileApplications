package com.menu.menu.Classes;

import java.util.Date;

public class Order
{
    public enum State
    {
        AwatingResponse,
        BeingMade,
        OnRoute,
        Delivered
    }

    public String GetStateAsString()
    {
        switch (CurrentState)
        {
            case AwatingResponse:
                return "Awaiting Response From Chef";
            case BeingMade:
                return "Being Made";
            case OnRoute:
                return "On Route";
            case Delivered:
                return "Delivered";
        }
        return "Unknown state";
    }

    public Date CurrentETA = null;
    public State CurrentState = State.AwatingResponse;
    public String Id = null;
    public Meal CorrespondingMeal = null;
    public int NumberOfMeals = 0;
}
