package com.menu.menu.Classes;

public class Meal extends ClassWithBitmap
{
    public String OwnerId = null;
    public String Name = null;
    public Boolean IsHalal = null;
    public Boolean IsVegan = null;
    public Boolean IsVegiterian = null;
    public Boolean ContainsMilk = null;
    public Boolean ContainsGluten = null;
    public String Ingredients = null;
    public String Calories = null;
    public String Price = null;
    public String MaxNoPortions = null;
    public String Id = null;
    public String OwnerUsername = null;
    public String EatIn = null;
    public String HoursAvaliableFrom = null;
    public String HoursAvaliableTo = null;

    public String GetInsertString()
    {
        final String d = "','";
        return "'" + OwnerId + d + Name + d + IsHalal + d + IsVegan + d + IsVegiterian + d + ContainsMilk + d + ContainsGluten + d +
                Ingredients + d + Calories + d + Price + d + MaxNoPortions + "', NEWID(),'"
                + OwnerUsername + d + EatIn + d + HoursAvaliableFrom + d + HoursAvaliableTo + "'";
    }

    public String GetUpdateString()
    {
        String d = "',";
        return "owner_user_id='" + OwnerId + d + "meal_name='" + Name + d + "is_halal='" + IsHalal + d + "is_vegan='" + IsVegan + d + "is_vegiterian='" + IsVegiterian + d + "contains_milk='" +
                ContainsMilk + d + "contains_gluten='" + ContainsGluten + d + "ingredients_list='" + Ingredients + d + "estimated_calories='" + Calories + d + "price='" +
                Price + d + "number_of_portions_avaliable='" + MaxNoPortions + d + "id='" + Id + d + "OwnerUsername='" + OwnerUsername + d + "eatIn='" + EatIn +
                d + "hoursAvaliableFrom='" + HoursAvaliableFrom + d + "hoursAvaliableTo='" + HoursAvaliableTo + d + "picture='" + PictureToSql() + "'";
    }

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

    public Boolean CurrentlyOnSale()
    {
        //todo
        return true;
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