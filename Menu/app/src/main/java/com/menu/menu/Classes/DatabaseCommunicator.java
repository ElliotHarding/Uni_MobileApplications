package com.menu.menu.Classes;

import com.menu.menu.Login;

import java.util.ArrayList;
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
            meals.add(m);
        }

        return meals;
    }

    public boolean AddMeal(Meal meal)
    {
        return true;
    }

    private User GetUser(String selectStatement)
    {
        return new User();
    }



}
