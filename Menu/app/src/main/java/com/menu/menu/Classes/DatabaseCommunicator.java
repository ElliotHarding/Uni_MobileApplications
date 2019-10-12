package com.menu.menu.Classes;

import com.menu.menu.Login;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseCommunicator
{
    public enum LoginOption
    {
        Username,
        Email
    }

    public boolean TryLogin(String usernameOrEmail, String pass, LoginOption option)
    {
        String statement = "SELECT * FROM USERS WHERE Password = " + pass + " AND ";

        switch (option)
        {
            case Username:
                statement += "Username = ";
                break;
            case Email:
                statement += "Email = ";
                break;
        }

        statement += usernameOrEmail;

        User u = GetUser(statement);
        if (u == null)
            return false;

        u.LoggedIn = true;

        return UpdateUser(u);
    }

    public User GetUserViaUsername(String username)
    {
        return GetUser("Select * FROM USERS WHERE id = " + username);
    }

    public User GetUserViaEmail(String email)
    {
        return GetUser("Select * FROM USERS WHERE id = " + email);
    }

    public boolean UpdateUser(User u)
    {
        return true;
    }

    public boolean AddUser(User user)
    {
        return true;
    }

    public ArrayList<Meal> GetChefsMeals(String username)
    {
        ArrayList<Meal> meals = new ArrayList<Meal>();

        //Test data:
        for (int i = 0; i < 4; i++)
        {
            Meal m = new Meal();
            m.Name = "Meal " + Integer.toString(i);
            m.OnSale = true;
            m.Takeaway = true;
            m.EatIn = i == 2;
            m.Price = "6.10";
            m.MaxQuantity = 10;
            m.Ingredients = "A \n B \n C";
            m.OwnerUsername = "Elliot";
            meals.add(m);
        }

        return meals;
    }

    public Meal GetMeal(String s)
    {
        Meal m = new Meal();
        m.Name = "Meal " + Integer.toString(1);
        m.OnSale = true;
        m.Takeaway = true;
        m.EatIn = true;
        m.Price = "6.10";
        m.MaxQuantity = 10;
        m.Ingredients = "A \n B \n C";
        m.OwnerUsername = "Elliot";
        return  m;
    }

    public boolean AddMeal(Meal meal)
    {
        return true;
    }

    public boolean DeleteMeal(Meal meal)
    {
        return true;
    }

    //Returns null if failed
    public Order AddOrder(Meal meal, User localUser)
    {
        Order o = new Order();

        //Test Data:
        o.CurrentETA = new Date(2019, 10, 3, 3, 3, 3);
        o.Id = "SKNEIOENFE";
        o.NumberOfMeals = 2;
        o.CurrentState = Order.State.OnRoute;

        return o;
    }

    public Order GetOrderUpdate(String id)
    {
        Order o = new Order();

        //Test Data:
        o.CurrentETA = new Date(2019, 10, 3, 3, 3, 3);
        o.Id = id;
        o.NumberOfMeals = 2;
        o.CurrentState = Order.State.OnRoute;

        return o;
    }





    private User GetUser(String selectStatement)
    {
        return new User();
    }

}
