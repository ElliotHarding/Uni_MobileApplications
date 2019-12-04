package com.menu.menu.Classes;

import java.io.Serializable;

public class User extends ClassWithBitmap implements Serializable
{
    private String m_id = "NEWID()";
    private String m_username = "elliot"; //todo testing...
    private String m_password = "elliot";
    private String m_fullName = null;
    private String m_addressLine1 = null;
    private String m_addressLine2 = null;
    private String m_addressLine3 = null;
    private String m_addressPostCode = null;
    private String m_addressDescription = null;
    private String m_DOB = null;
    private String m_loggedIn = null;
    private String m_email = null;
    private String m_phone = null;
    private String m_rating = "5";
    private String m_isAdmin = null;
    private String m_latitude = null;
    private String m_longitude = null;
    private String m_isChef = null;
    private String m_foodType = null;

    public String GetInsertString()
    {
        final String d = "','";
        return getId() + ",'" + getUsername() + d + getPassword() + d + getFullName() + d + getAddressLine1() + d + getAddressLine2() + d + getAddressLine3() + d +
                getAddressPostCode() + d + getAddressDescription() + d + getDOB() + d + getLoggedIn() + d + getEmail() + d + getPhone() + d + getRating() + d + getIsAdmin() + d + pictureToSql() + d + getIsChef() + d + getLatitude() + d + getLongitude() + d + getFoodType() + d + pictureToSql() + "'";
    }

    public String GetUpdateString()
    {
        String d = "',";
        return "name='" + getUsername() + d + "password='" + getPassword() + d + "full_name='" + getFullName() + d + "address_line_1='" + getAddressLine1() + d + "address_line_2='" + getAddressLine2() + d + "address_city='" +
                getAddressLine3() + d + "address_post_code='" + getAddressPostCode() + d + "address_description='" + getAddressDescription() + d + "date_of_birth='" + getDOB() + d + "logged_in='" +
                getLoggedIn() + d + "contact_email='" + getEmail() + d + "contact_phone='" + getPhone() + d + "rating='" + getRating() + d + "is_admin='" + getIsAdmin() +
                d + "picture_id='" + pictureToSql() + d + "is_chef='" + getIsChef() + d + "latitude='" + getLatitude() + d + "longitude='" + getLongitude() + d + "food_type='" + getFoodType() + "'";
    }

    public String getId() {
        return m_id;
    }

    public void setId(String id) {
        m_id = id;
    }

    public String getUsername() {
        return m_username;
    }

    public void setUsername(String username) {
        m_username = username;
    }

    public String getPassword() {
        return m_password;
    }

    public void setPassword(String password) {
        m_password = password;
    }

    public String getFullName() {
        return m_fullName;
    }

    public void setFullName(String fullName) {
        m_fullName = fullName;
    }

    public String getAddressLine1() {
        return m_addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        m_addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return m_addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        m_addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return m_addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        m_addressLine3 = addressLine3;
    }

    public String getAddressPostCode() {
        return m_addressPostCode;
    }

    public void setAddressPostCode(String addressPostCode) {
        m_addressPostCode = addressPostCode;
    }

    public String getAddressDescription() {
        return m_addressDescription;
    }

    public void setAddressDescription(String addressDescription) {
        m_addressDescription = addressDescription;
    }

    public String getDOB() {
        return m_DOB;
    }

    public void setDOB(String DOB) {
        this.m_DOB = DOB;
    }

    public String getLoggedIn() {
        return m_loggedIn;
    }

    public void setLoggedIn(String loggedIn) {
        m_loggedIn = loggedIn;
    }

    public String getEmail() {
        return m_email;
    }

    public void setEmail(String email) {
        m_email = email;
    }

    public String getPhone() {
        return m_phone;
    }

    public void setPhone(String phone) {
        m_phone = phone;
    }

    public String getRating() {
        return m_rating;
    }

    public void setRating(String rating) {
        m_rating = rating;
    }

    public String getIsAdmin() {
        return m_isAdmin;
    }

    public Boolean getIsAdmin_b()
    {
        return m_isAdmin.equals("true");
    }

    public void setIsAdmin(String isAdmin)
    {
        m_isAdmin = isAdmin;
    }

    public void setIsAdmin_b(Boolean isAdmin)
    {
        m_isAdmin = isAdmin ? "true" : "false";
    }

    public String getLatitude() {
        return m_latitude;
    }

    public void setLatitude(String latitude) {
        m_latitude = latitude;
    }

    public String getLongitude() {
        return m_longitude;
    }

    public void setLongitude(String longitude) {
        m_longitude = longitude;
    }

    public String getIsChef() {
        return m_isChef;
    }

    public Boolean getIsChef_b()
    {
        return m_isChef.equals("true");
    }

    public void setIsChef(String isChef) {
        m_isChef = isChef;
    }

    public void setIsChef_b(Boolean isChef)
    {
        m_isChef = isChef ? "true" : "false";
    }

    public String getFoodType() {
        return m_foodType;
    }

    public void setFoodType(String foodType) {
        m_foodType = foodType;
    }
}
