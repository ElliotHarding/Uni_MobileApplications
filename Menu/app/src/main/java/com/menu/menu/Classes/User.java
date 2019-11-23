package com.menu.menu.Classes;

import java.io.Serializable;

public class User extends ClassWithBitmap implements Serializable
{
    public String Id = "NEWID()";
    public String Username = "elliot"; //todo testing...
    public String Password = "elliot"; //todo testing...
    public String FullName = null;
    public String AddressLine1 = null;
    public String AddressLine2 = null;
    public String AddressLine3 = null;
    public String AddressPostCode = null;
    public String AddressDescription = null;
    public String DOB = null;
    public String LoggedIn = null;
    public String Email = null;
    public String Phone = null;
    public String Rating = null;
    public String IsAdmin = null;
    public String Latitude = null;
    public String Longitude = null;
    public String IsChef = null;
    public String FoodType = null;

    public String GetInsertString()
    {
        final String d = "','";
        return Id + ",'" + Username + d + Password + d + FullName + d + AddressLine1 + d + AddressLine2 + d + AddressLine3 + d +
                AddressPostCode + d + AddressDescription + d + DOB + d + LoggedIn + d + Email + d + Phone + d + Rating + d + IsAdmin + d + Picture + d + IsChef + d + Latitude + d + Longitude + d + FoodType + d + PictureToSql() + "'";
    }

    public String GetUpdateString()
    {
        String d = "',";
        return "name='" + Username + d + "password='" + Password + d + "full_name='" + FullName + d + "address_line_1='" + AddressLine1 + d + "address_line_2='" + AddressLine2 + d + "address_city='" +
                AddressLine3 + d + "address_post_code='" + AddressPostCode + d + "address_description='" + AddressDescription + d + "date_of_birth='" + DOB + d + "logged_in='" +
                LoggedIn + d + "contact_email='" + Email + d + "contact_phone='" + Phone + d + "rating='" + Rating + d + "is_admin='" + IsAdmin +
                d + "picture_id='" + PictureToSql() + d + "is_chef='" + IsChef + d + "latitude='" + Latitude + d + "longitude='" + Longitude + d + "food_type='" + FoodType + "'";
    }
}
