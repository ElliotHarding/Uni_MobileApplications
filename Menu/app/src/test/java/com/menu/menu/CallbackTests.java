package com.menu.menu;

import com.menu.menu.Classes.BaseCallback;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.MealsCallback;
import com.menu.menu.Classes.Order;
import com.menu.menu.Classes.OrdersCallback;
import com.menu.menu.Classes.User;
import com.menu.menu.Classes.UsersCallback;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CallbackTests
{
    enum DatabaseCallbackState
    {
        Success,
        Failed,
        Waiting
    }

    DatabaseCallbackState testRequestUserDataState = DatabaseCallbackState.Waiting;
    @Test
    public void Test_RequestUserData()
    {
        Test_RequestUserData_CB test = new Test_RequestUserData_CB();
        ArrayList<User> users = new ArrayList<>();
        users.add(new User());
        test.AddUsers(users);
        try
        {
            test.call();
        }
        catch (Exception e)
        {
            fail();
        }

        while (testRequestUserDataState == DatabaseCallbackState.Waiting){}

        assertEquals(testRequestUserDataState, DatabaseCallbackState.Success);
    }

    class Test_RequestUserData_CB extends UsersCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_users != null && !m_users.isEmpty())
            {
                testRequestUserDataState = DatabaseCallbackState.Success;
            }
            else
            {
                testRequestUserDataState = DatabaseCallbackState.Failed;
            }
            return null;
        }
    }

    DatabaseCallbackState testRequestOrderDataState = DatabaseCallbackState.Waiting;
    @Test
    public void Test_RequestOrderData()
    {
        Test_RequestOrderData_CB test = new Test_RequestOrderData_CB();
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order());
        test.AddOrders(orders);

        try
        {
            test.call();
        }
        catch (Exception e)
        {
            fail();
        }

        while (testRequestOrderDataState == DatabaseCallbackState.Waiting){}

        assertEquals(testRequestOrderDataState, DatabaseCallbackState.Success);
    }

    class Test_RequestOrderData_CB extends OrdersCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_orders != null && !m_orders.isEmpty())
            {
                testRequestOrderDataState = DatabaseCallbackState.Success;
            }
            else
            {
                testRequestOrderDataState = DatabaseCallbackState.Failed;
            }
            return null;
        }
    }

    DatabaseCallbackState testRequestMealDataState = DatabaseCallbackState.Waiting;
    @Test
    public void Test_RequestMealData()
    {
        Test_RequestMealData_CB test = new Test_RequestMealData_CB();
        ArrayList<Meal> meals = new ArrayList<>();
        meals.add(new Meal());
        test.AddMeals(meals);

        try
        {
            test.call();
        }
        catch (Exception e)
        {
            fail();
        }

        while (testRequestMealDataState == DatabaseCallbackState.Waiting){}

        assertEquals(testRequestMealDataState, DatabaseCallbackState.Success);
    }

    class Test_RequestMealData_CB extends MealsCallback
    {
        @Override
        public Void call() throws Exception
        {
            if (m_meals != null && !m_meals.isEmpty())
            {
                testRequestMealDataState = DatabaseCallbackState.Success;
            }
            else
            {
                testRequestMealDataState = DatabaseCallbackState.Failed;
            }
            return null;
        }
    }

    DatabaseCallbackState testGenericUploadState = DatabaseCallbackState.Waiting;
    @Test
    public void Test_GenericUpload()
    {
        Test_GenericUpload_CB test = new Test_GenericUpload_CB();
        test.SetMessage("null");

        try
        {
            test.call();
        }
        catch (Exception e)
        {
            fail();
        }

        while (testGenericUploadState == DatabaseCallbackState.Waiting){}

        assertEquals(testGenericUploadState, DatabaseCallbackState.Success);
    }

    class Test_GenericUpload_CB extends BaseCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_message.equals("null"))
            {
                testGenericUploadState = DatabaseCallbackState.Success;
            }
            else
            {
                testGenericUploadState = DatabaseCallbackState.Failed;
            }
            return null;
        }
    }



}