package com.menu.menu.Classes;

public class Meal
{
    public String OwnerId = null;
    public String Name = null;
    public String IsHalal = null;
    public String IsVegan = null;
    public String IsVegiterian = null;
    public String ContainsMilk = null;
    public String ContainsGluten = null;
    public String Ingredients = null;
    public String Calories = null;
    public String PictureId = null;
    public String Price = null;
    public String MaxNoPortions = null;
    public String Id = null;
    public String OnSale = null;
    public String OwnerUsername = null;
    public String EatIn = null;
    public String HoursAvaliableFrom = null;
    public String HoursAvaliableTo = null;

    public void SetEatIn(boolean eatIn, boolean takeaway)
    {
        if(eatIn && takeaway)
        {
            EatIn = "BOTH";
        }
        else if(eatIn)
        {
            EatIn = "EAT-IN";
        }
        else
        {
            EatIn = "TAKEAWAY";
        }
    }

    public Boolean IsEatIn()
    {
        return (EatIn.equals("BOTH") || EatIn.equals("EAT-IN"));
    }

    public Boolean IsTakeaway()
    {
        return (EatIn.equals("BOTH") || EatIn.equals("TAKEAWAY"));
    }
}