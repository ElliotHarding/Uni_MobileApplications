package com.menu.menu.Classes;

import android.graphics.Bitmap;

public class Meal
{
    public Meal()
    {
    }

    public boolean OnSale = false;
    public String Name = null;
    public String Price = null;
    public String Ingredients = null;
    public String OwnerUsername = null;
    public int MaxQuantity = 0;
    public Bitmap Image;
    public boolean EatIn = false;
    public boolean Takeaway = !EatIn;
}
