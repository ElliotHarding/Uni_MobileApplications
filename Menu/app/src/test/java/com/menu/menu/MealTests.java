package com.menu.menu;

import com.menu.menu.Classes.Meal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MealTests
{
    private Meal m_testMeal = new Meal();

    @Test
    public void Test_EatIn()
    {
        m_testMeal.SetEatIn(true, true);
        assertEquals(m_testMeal.IsEatIn(), true);
        assertEquals(m_testMeal.IsTakeaway(), true);
    }

    @Test
    public void Test_OwnerId()
    {
        m_testMeal.setOwnerId("a");
        assertEquals(m_testMeal.getOwnerId(), "a");
    }

    @Test
    public void Test_Name()
    {
        m_testMeal.setName("b");
        assertEquals(m_testMeal.getName(), "b");
    }

    @Test
    public void Test_Halal()
    {
        m_testMeal.setHalal(true);
        assertEquals(m_testMeal.getHalal(), true);
    }

    @Test
    public void Test_Vegan()
    {
        m_testMeal.setVegan(true);
        assertEquals(m_testMeal.getVegan(), true);
    }

    @Test
    public void Test_Vegiterian()
    {
        m_testMeal.setVegiterian(true);
        assertEquals(m_testMeal.getVegiterian(), true);
    }

    @Test
    public void Test_ContainsMilk()
    {
        m_testMeal.setContainsMilk(true);
        assertEquals(m_testMeal.getContainsMilk(), true);
    }

    @Test
    public void Test_ContainsGluten()
    {
        m_testMeal.setContainsGluten(true);
        assertEquals(m_testMeal.getContainsGluten(), true);
    }

    @Test
    public void Test_Ingredients()
    {
        m_testMeal.setIngredients("z");
        assertEquals(m_testMeal.getIngredients(), "z");
    }

    @Test
    public void Test_Calories()
    {
        m_testMeal.setCalories("55");
        assertEquals(m_testMeal.getCalories(), "55");
    }

    @Test
    public void Test_Price()
    {
        m_testMeal.setPrice("55");
        assertEquals(m_testMeal.getPrice(), "55");
    }

    @Test
    public void Test_MaxNoPortions()
    {
        m_testMeal.setMaxNoPortions("1");
        assertEquals(m_testMeal.getMaxNoPortions(), "1");
    }

    @Test
    public void Test_Id()
    {
        m_testMeal.setId("yyy");
        assertEquals(m_testMeal.getId(), "yyy");
    }

    @Test
    public void Test_OwnerUsername()
    {
        m_testMeal.setOwnerUsername("user");
        assertEquals(m_testMeal.getOwnerUsername(), "user");
    }

    @Test
    public void Test_HoursAvaliableFrom()
    {
        m_testMeal.setHoursAvaliableFrom("4");
        assertEquals(m_testMeal.getHoursAvaliableFrom(), "4");
    }

    @Test
    public void Test_HoursAvaliableTo()
    {
        m_testMeal.setHoursAvaliableTo("4");
        assertEquals(m_testMeal.getHoursAvaliableTo(), "4");
    }

    @Test
    public void Test_Rating()
    {
        m_testMeal.setRating("3");
        assertEquals(m_testMeal.getRating(), "3");
    }
}
