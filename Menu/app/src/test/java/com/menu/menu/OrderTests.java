package com.menu.menu;

import com.menu.menu.Classes.Order;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class OrderTests
{
    Order m_testOrder = new Order();

    @Test
    public void Test_Id()
    {
        m_testOrder.setId("id");
        assertEquals(m_testOrder.getId(), "id");
    }

    @Test
    public void Test_MealId()
    {
        m_testOrder.setMealId("S");
        assertEquals(m_testOrder.getMealId(), "S");
    }

    @Test
    public void Test_NumOfPortions()
    {
        String numOfPortions = "22";
        m_testOrder.setNumberOfPortions(22);
        assertEquals(m_testOrder.getNumberOfPortions(), numOfPortions);
        assertEquals(m_testOrder.getNumberOfPortions_n(), 22);
    }

    @Test
    public void Test_CurrentState()
    {
        m_testOrder.setCurrentState("state");
        assertEquals(m_testOrder.getCurrentState(), "state");

        m_testOrder.setState_c(Order.State.AwatingResponse);
        assertEquals(m_testOrder.getState_c(), Order.State.AwatingResponse);
        assertEquals(m_testOrder.getCurrentState(), "Awaiting Response From Chef");

        m_testOrder.setState_c(Order.State.BeingMade);
        assertEquals(m_testOrder.getState_c(), Order.State.BeingMade);
        assertEquals(m_testOrder.getCurrentState(), "Being Made");
    }

    @Test
    public void Test_MealEaterId()
    {
        m_testOrder.setEaterId("eater");
        assertEquals(m_testOrder.getEaterId(), "eater");
    }

    @Test
    public void Test_MealChefId()
    {
        m_testOrder.setEaterId("chef");
        assertEquals(m_testOrder.getEaterId(), "chef");
    }


    @Test
    public void Test_Takeaway()
    {
        m_testOrder.setIsTakeaway_b(true);
        assertEquals(m_testOrder.getIsTakeaway(), true);
    }

    @Test
    public void Test_Messages()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("s");
        m_testOrder.setMessages(list);

        assertEquals(m_testOrder.getMessages(), list);
        assertEquals(m_testOrder.getMessages_sql(), "s");
    }
}
