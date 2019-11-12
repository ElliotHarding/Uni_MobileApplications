package com.menu.menu.Classes;

public class User
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
    public String PictureId = null;
    public String LatLong = null;

    public String GetInsertString()
    {
        final String d = "','";
        return Id + ",'" + Username + d + Password + d + FullName + d + AddressLine1 + d + AddressLine2 + d + AddressLine3 + d +
                AddressPostCode + d + AddressDescription + d + DOB + d + LoggedIn + d + Email + d + Phone + d + Rating + d + IsAdmin + d + PictureId + d + LatLong + "'";
    }
}
