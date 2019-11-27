package com.menu.menu.Classes;

public class Meal extends ClassWithBitmap
{
    private String m_ownerId = null;
    private String m_Name = null;
    private Boolean m_IsHalal = null;
    private Boolean m_IsVegan = null;
    private Boolean m_IsVegiterian = null;
    private Boolean m_ContainsMilk = null;
    private Boolean m_ContainsGluten = null;
    private String m_Ingredients = null;
    private String m_Calories = null;
    private String m_Price = null;
    private String m_MaxNoPortions = null;
    private String m_id = null;
    private String m_ownerUsername = null;
    private String m_eatIn = null;
    private String m_hoursAvaliableFrom = null;
    private String m_hoursAvaliableTo = null;
    private String m_rating = null;

    public String GetInsertString()
    {
        final String d = "','";
        return "'" + getOwnerId() + d + getName() + d + getHalal() + d + getVegan() + d + getVegiterian() + d + getContainsMilk() + d + getContainsGluten() + d +
                getIngredients() + d + getCalories() + d + getPrice() + d + getMaxNoPortions() + "', NEWID(),'"
                + getOwnerUsername() + d + getEatIn() + d + getHoursAvaliableFrom() + d + getHoursAvaliableTo() + d + getRating() + "'";
    }

    public String GetUpdateString()
    {
        String d = "',";
        return "owner_user_id='" + getOwnerId() + d + "meal_name='" + getName() + d + "is_halal='" + getHalal() + d + "is_vegan='" + getVegan() + d + "is_vegiterian='" + getVegiterian() + d + "contains_milk='" +
                getContainsMilk() + d + "contains_gluten='" + getContainsGluten() + d + "ingredients_list='" + getIngredients() + d + "estimated_calories='" + getCalories() + d + "price='" +
                getPrice() + d + "number_of_portions_avaliable='" + getMaxNoPortions() + d + "id='" + getId() + d + "OwnerUsername='" + getOwnerUsername() + d + "eatIn='" + getEatIn() +
                d + "hoursAvaliableFrom='" + getHoursAvaliableFrom() + d + "hoursAvaliableTo='" + getHoursAvaliableTo() + d + "picture='" + PictureToSql() + d + "rating='" + getRating() + "'";
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

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getHalal() {
        return IsHalal;
    }

    public void setHalal(Boolean halal) {
        IsHalal = halal;
    }

    public Boolean getVegan() {
        return IsVegan;
    }

    public void setVegan(Boolean vegan) {
        IsVegan = vegan;
    }

    public Boolean getVegiterian() {
        return IsVegiterian;
    }

    public void setVegiterian(Boolean vegiterian) {
        IsVegiterian = vegiterian;
    }

    public Boolean getContainsMilk() {
        return ContainsMilk;
    }

    public void setContainsMilk(Boolean containsMilk) {
        ContainsMilk = containsMilk;
    }

    public Boolean getContainsGluten() {
        return ContainsGluten;
    }

    public void setContainsGluten(Boolean containsGluten) {
        ContainsGluten = containsGluten;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public String getCalories() {
        return Calories;
    }

    public void setCalories(String calories) {
        Calories = calories;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMaxNoPortions() {
        return MaxNoPortions;
    }

    public void setMaxNoPortions(String maxNoPortions) {
        MaxNoPortions = maxNoPortions;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getOwnerUsername() {
        return OwnerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        OwnerUsername = ownerUsername;
    }

    public String getEatIn() {
        return EatIn;
    }

    public void setEatIn(String eatIn) {
        EatIn = eatIn;
    }

    public String getHoursAvaliableFrom() {
        return HoursAvaliableFrom;
    }

    public void setHoursAvaliableFrom(String hoursAvaliableFrom) {
        HoursAvaliableFrom = hoursAvaliableFrom;
    }

    public String getHoursAvaliableTo() {
        return HoursAvaliableTo;
    }

    public void setHoursAvaliableTo(String hoursAvaliableTo) {
        HoursAvaliableTo = hoursAvaliableTo;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }
}