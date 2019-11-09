package com.menu.menu.Classes;

public class User
{
    public String Id = "NEWID()";
    public String Username = null;
    public String Password = null;
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
    public String PictureId = null;
    public String LatLong = null;

    public String GetInsertString()
    {
        final String d = ",";
        return Id + d + Username + Password + d + FullName + AddressLine1 + d + AddressLine2 + AddressLine3 + d +
                AddressPostCode + AddressDescription + d + DOB + LoggedIn + d + Email + Phone + d + Rating + IsAdmin + d + PictureId + d + LatLong;
    }
}
