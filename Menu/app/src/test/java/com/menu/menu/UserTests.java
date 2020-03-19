package com.menu.menu;
import com.menu.menu.Classes.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTests
{
    /*
    TODO TALK ABOUT UNIT TESTS AND THEIR BENIFITS
    */
    //Test variable
    private User m_testUser = new User();

    @Test
    public void Test_InsertString()
    {
        assertEquals(m_testUser.GetInsertString(), "INSERT INTO [menudatabase].[dbo].[User] (id,name,password,full_name,address_line_1,address_line_2,address_city,address_post_code,address_description,date_of_birth,logged_in,contact_email,contact_phone,rating,is_admin,is_chef,latitude,longitude,food_type,picture_id) VALUES (NEWID(),'null','null','null','null','null','null','null','null','null','null','null','null','5','null','null','null','null','null','null')");
    }

    @Test
    public void Test_UpdateString()
    {
        assertEquals(m_testUser.GetUpdateString(), "name='null',password='null',full_name='null',address_line_1='null',address_line_2='null',address_city='null',address_post_code='null',address_description='null',date_of_birth='null',logged_in='null',contact_email='null',contact_phone='null',rating='5',is_admin='null',picture_id='null',is_chef='null',latitude='null',longitude='null',food_type='null'");
    }

    @Test
    public void Test_Id()
    {
        m_testUser.setId("a");
        assertEquals(m_testUser.getId(), "a");
    }

    @Test
    public void Test_Username()
    {
        m_testUser.setUsername("b");
        assertEquals(m_testUser.getUsername(), "b");
    }

    @Test
    public void Test_Password()
    {
        m_testUser.setPassword("c");
        assertEquals(m_testUser.getPassword(), "c");
    }

    @Test
    public void Test_FullName()
    {
        m_testUser.setFullName("d");
        assertEquals(m_testUser.getFullName(), "d");
    }

    @Test
    public void Test_AddressLine1()
    {
        m_testUser.setAddressLine1("e");
        assertEquals(m_testUser.getAddressLine1(), "e");
    }

    @Test
    public void Test_AddressLine2()
    {
        m_testUser.setAddressLine2("f");
        assertEquals(m_testUser.getAddressLine2(), "f");
    }

    @Test
    public void Test_AddressLine3()
    {
        m_testUser.setAddressLine3("g");
        assertEquals(m_testUser.getAddressLine3(), "g");
    }

    @Test
    public void Test_AddressPostCode()
    {
        m_testUser.setAddressPostCode("h");
        assertEquals(m_testUser.getAddressPostCode(), "h");
    }

    @Test
    public void Test_AddressDescription()
    {
        m_testUser.setAddressDescription("i");
        assertEquals(m_testUser.getAddressDescription(), "i");
    }

    @Test
    public void Test_DOB()
    {
        m_testUser.setDOB("j");
        assertEquals(m_testUser.getDOB(), "j");
    }

    @Test
    public void Test_LoggedIn()
    {
        m_testUser.setLoggedIn("true");
        assertEquals(m_testUser.getLoggedIn(), "true");
    }

    @Test
    public void Test_Email()
    {
        m_testUser.setEmail("k");
        assertEquals(m_testUser.getEmail(), "k");
    }

    @Test
    public void Test_Phone()
    {
        m_testUser.setPhone("l");
        assertEquals(m_testUser.getPhone(), "l");
    }

    @Test
    public void Test_Rating()
    {
        m_testUser.setRating("5");
        assertEquals(m_testUser.getRating(), "5");
    }

    @Test
    public void Test_IsAdmin()
    {
        m_testUser.setIsAdmin_b(true);
        assertEquals(m_testUser.getIsAdmin_b(), true);
        assertEquals(m_testUser.getIsAdmin(), "true");
    }

    @Test
    public void Test_Latitude()
    {
        m_testUser.setLatitude("y");
        assertEquals(m_testUser.getLatitude(), "y");
    }

    @Test
    public void Test_Longitude()
    {
        m_testUser.setLongitude("x");
        assertEquals(m_testUser.getLongitude(), "x");
    }

    @Test
    public void Test_IsChef()
    {
        m_testUser.setIsChef_b(true);
        assertEquals(m_testUser.getIsChef(), "true");
        assertEquals(m_testUser.getIsChef_b(), true);
    }

    @Test
    public void Test_FoodType()
    {
        m_testUser.setFoodType("u");
        assertEquals(m_testUser.getFoodType(), "u");
    }
}
