package com.menu.menu.Classes;

import java.io.Serializable;

public class Meal extends ClassWithBitmap implements Serializable
{
    private String m_ownerId = null;
    private String m_name = null;
    private Boolean m_bIsHalal = null;
    private Boolean m_bIsVegan = null;
    private Boolean m_bIsVegiterian = null;
    private Boolean m_bContainsMilk = null;
    private Boolean m_bContainsGluten = null;
    private String m_ingredients = null;
    private String m_calories = null;
    private String m_price = null;
    private String m_maxNoPortions = null;
    private String m_id = null;
    private String m_ownerUsername = null;
    private String m_eatIn = null;
    private String m_hoursAvaliableFrom = null;
    private String m_hoursAvaliableTo = null;
    private String m_rating = null;

    public String GetInsertString()
    {
        final String d = "','";
        return "'" + getOwnerId() + d + getName() + d + btos(getHalal()) + d + btos(getVegan()) + d + btos(getVegiterian()) + d + btos(getContainsMilk()) + d + btos(getContainsGluten()) + d +
                getIngredients() + d + getCalories() + d + getPrice() + d + getMaxNoPortions() + "', NEWID(),'"
                + getOwnerUsername() + d + getEatIn() + d + getHoursAvaliableFrom() + d + getHoursAvaliableTo() + d + pictureToSql() + d + getRating() + "'";
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
        return m_ingredients;
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
            return "-";
        return m_rating;
    }

    public void setRating(String rating)
    {
        m_rating = rating;
    }
}