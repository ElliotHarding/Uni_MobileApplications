package com.menu.menu.Classes;

public class Order
{
    public enum State
    {
        AwatingResponse,
        BeingMade,
        OnRoute,
        Delivered
    }

    public String Id = null;
    public String MealId = null;
    public String NumberOfMeals = null;
    public String OrdererId = null;
    public String CurrentState = null;
    public String ArrivalTime = null;

    public State GetState()
    {
        switch (CurrentState) {
            case "Awaiting Response From Chef":
                return State.AwatingResponse;
            case "Being Made":
                return State.BeingMade;
            case "On Route":
                return State.OnRoute;
            case "Delivered":
                return State.Delivered;
            default:
                return null;
        }
    }

    public void SetState(State cState)
    {
        switch (cState)
        {
            case AwatingResponse:
                CurrentState = "Awaiting Response From Chef";
            case BeingMade:
                CurrentState = "Being Made";
            case OnRoute:
                CurrentState = "On Route";
            case Delivered:
                CurrentState = "Delivered";
        }
    }
}
