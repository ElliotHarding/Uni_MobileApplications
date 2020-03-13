package com.menu.menu;

import com.menu.menu.Classes.Order;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class OrderTests
{
    Order m_testOrder = new Order();

    @Test
    public void Test_InsertString()
    {
        assertEquals(m_testOrder.GetInsertString(), "INSERT INTO [menudatabase].[dbo].[Order] (id,meal_id,num_portions_ordered,meal_orderer_id,currentState,isTakeaway,messages) VALUES ('null','','','null','null','null','null');");
    }

    @Test
    public void Test_Id()
    {
        m_testOrder.setId("id");
        assertEquals(m_testOrder.getId(), "id");
    }

    @Test
    public void Test_MealIds()
    {
        ArrayList<String> mealIds = new ArrayList<>();
        mealIds.add("s");
        m_testOrder.setMealIds_sql("s");
        assertEquals(m_testOrder.getMealIds(), mealIds);
        m_testOrder.setMealIds(mealIds);
        assertEquals(m_testOrder.getMealIds(), mealIds);
    }

    @Test
    public void Test_NumOfPortionsList()
    {
        ArrayList<String> numOfPortions = new ArrayList<>();
        numOfPortions.add("s");
        m_testOrder.setNumOfPortionsList(numOfPortions);
        assertEquals(m_testOrder.getNumOfPortionsList(), numOfPortions);
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
    public void Test_MealOrdererId()
    {
        m_testOrder.setMealOrdererId("yyy");
        assertEquals(m_testOrder.getMealOrdererId(), "yyy");
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
