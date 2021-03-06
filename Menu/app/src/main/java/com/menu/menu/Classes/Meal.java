package com.menu.menu.Classes;

import java.io.Serializable;

public class Meal extends SerializableBitmap implements Serializable
{
    private String m_ownerId = null;
    private String m_name = "No name";
    private Boolean m_bIsHalal = false;
    private Boolean m_bIsVegan = false;
    private Boolean m_bIsVegiterian = false;
    private Boolean m_bContainsMilk = false;
    private Boolean m_bContainsGluten = false;
    private String m_ingredients = "No ingredients";
    private String m_calories = "0";
    private String m_price = "0";
    private String m_maxNoPortions = "0";
    private String m_id = null;
    private String m_ownerUsername = null;
    private String m_eatIn = "false";
    private String m_hoursAvaliableFrom = "0";
    private String m_hoursAvaliableTo = "0";
    private String m_rating = "5";
    private final static String m_mealInsert = "INSERT INTO [menudatabase].[dbo].[meal] (owner_user_id,meal_name,is_halal,is_vegan,is_vegiterian,contains_milk,contains_gluten,ingredients_list,estimated_calories,price,number_of_portions_avaliable,id,ownerUsername,eatIn,hoursAvaliableFrom,hoursAvaliableTo,picture,rating) VALUES ";

    public String GetInsertString()
    {
        final String d = "','";
        return m_mealInsert + "('" + getOwnerId() + d + getName() + d + btos(getHalal()) + d + btos(getVegan()) + d + btos(getVegiterian()) + d + btos(getContainsMilk()) + d + btos(getContainsGluten()) + d +
                getIngredients() + d + getCalories() + d + getPrice() + d + getMaxNoPortions() + "', NEWID(),'"
                + getOwnerUsername() + d + getEatIn() + d + getHoursAvaliableFrom() + d + getHoursAvaliableTo() + d + pictureToSql() + d + getRating() + "')";
    }

    public String GetUpdateString()
    {
        String d = "',";
        return "owner_user_id='" + getOwnerId() + d + "meal_name='" + getName() + d + "is_halal='" + btos(getHalal()) + d + "is_vegan='" + btos(getVegan()) + d + "is_vegiterian='" + btos(getVegiterian()) + d + "contains_milk='" +
                btos(getContainsMilk()) + d + "contains_gluten='" + btos(getContainsGluten()) + d + "ingredients_list='" + getIngredients() + d + "estimated_calories='" + getCalories() + d + "price='" +
                getPrice() + d + "number_of_portions_avaliable='" + getMaxNoPortions() + d + "id='" + getId() + d + "OwnerUsername='" + getOwnerUsername() + d + "eatIn='" + getEatIn() +
                d + "hoursAvaliableFrom='" + getHoursAvaliableFrom() + d + "hoursAvaliableTo='" + getHoursAvaliableTo() + d + "picture='" + pictureToSql() + d + "rating='" + getRating() + "'";
    }

    private String btos(Boolean b)
    {
        return b ? "true" : "false";
    }

    public void SetEatIn(boolean eatIn, boolean takeaway)
    {
        if(eatIn && takeaway)
        {
            setEatIn("BOTH");
        }
        else if(eatIn)
        {
            setEatIn("EAT-IN");
        }
        else
        {
            setEatIn("TAKEAWAY");
        }
    }

    public Boolean CurrentlyOnSale()
    {
        //todo
        return true;
    }

    public Boolean IsEatIn()
    {
        return (getEatIn().equals("BOTH") || getEatIn().equals("EAT-IN"));
    }

    public Boolean IsTakeaway()
    {
        return (getEatIn().equals("BOTH") || getEatIn().equals("TAKEAWAY"));
    }

    public String getOwnerId()
    {
        return m_ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        m_ownerId = ownerId;
    }

    public String getName()
    {
        return m_name;
    }

    public void setName(String name)
    {
        m_name = name;
    }

    public Boolean getHalal()
    {
        return m_bIsHalal;
    }

    public void setHalal(Boolean halal)
    {
        m_bIsHalal = halal;
    }

    public Boolean getVegan()
    {
        return m_bIsVegan;
    }

    public void setVegan(Boolean vegan)
    {
        m_bIsVegan = vegan;
    }

    public Boolean getVegiterian()
    {
        return m_bIsVegiterian;
    }

    public void setVegiterian(Boolean vegiterian)
    {
        m_bIsVegiterian = vegiterian;
    }

    public Boolean getContainsMilk()
    {
        return m_bContainsMilk;
    }

    public void setContainsMilk(Boolean containsMilk)
    {
        m_bContainsMilk = containsMilk;
    }

    public Boolean getContainsGluten()
    {
        return m_bContainsGluten;
    }

    public void setContainsGluten(Boolean containsGluten)
    {
        m_bContainsGluten = containsGluten;
    }

    public String getIngredients()
    {
        String retval = "";
        for(int i = 0; i < m_ingredients.length(); i++)
        {
            if(m_ingredients.charAt(i) == 92)
            {
                retval += "\n";
                i++;
            }
            else
            {
                retval += m_ingredients.charAt(i);
            }
        }

        return retval;
    }

    public void setIngredients(String ingredients)
    {
        m_ingredients = ingredients;
    }

    public String getCalories()
    {
        return m_calories;
    }

    public void setCalories(String calories)
    {
        m_calories = calories;
    }

    public String getPrice()
    {
        return m_price;
    }

    public void setPrice(String price)
    {
        m_price = price;
    }

    public String getMaxNoPortions()
    {
        return m_maxNoPortions;
    }

    public int getMaxNoPortions_n()
    {
        try
        {
            return Integer.parseInt(m_maxNoPortions);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public void setMaxNoPortions(String maxNoPortions)
    {
        m_maxNoPortions = maxNoPortions;
    }

    public String getId()
    {
        return m_id;
    }

    public void setId(String id)
    {
        m_id = id;
    }

    public String getOwnerUsername()
    {
        return m_ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername)
    {
        m_ownerUsername = ownerUsername;
    }

    public String getEatIn()
    {
        return m_eatIn;
    }

    public void setEatIn(String eatIn)
    {
        m_eatIn = eatIn;
    }

    public String getHoursAvaliableFrom()
    {
        return m_hoursAvaliableFrom;
    }

    public void setHoursAvaliableFrom(String hoursAvaliableFrom)
    {
        m_hoursAvaliableFrom = hoursAvaliableFrom;
    }

    public String getHoursAvaliableTo()
    {
        return m_hoursAvaliableTo;
    }

    public void setHoursAvaliableTo(String hoursAvaliableTo)
    {
        m_hoursAvaliableTo = hoursAvaliableTo;
    }

    public String getRating()
    {
        if(m_rating == null || m_rating.equals("null"))
            return "0";
        return m_rating;
    }

    public void setRating(String rating)
    {
        m_rating = rating;
    }
}