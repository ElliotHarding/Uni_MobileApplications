package com.menu.menu.Classes;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseCommunicator
{
    FirebaseDatabase m_db;
    DatabaseReference m_mealImages;

    final String m_userTable = "[menudatabase].[dbo].[User]";
    final String m_orderTable = "[menudatabase].[dbo].[Order]";
    final String m_mealTable = "[menudatabase].[dbo].[Meal]";
    final String m_databaseUrl = "http://themenuapp.gearhostpreview.com/databaseAPI.php?request=";

    public enum LoginOption
    {
        Username,
        Email
    }

    public DatabaseCommunicator()
    {
        m_db = FirebaseDatabase.getInstance();
    }

    public boolean TryLogin(String usernameOrEmail, String pass, LoginOption option)
    {
        User u = null;

        switch (option)
        {
            case Username:
                u = GetUserViaUsername(usernameOrEmail);
                break;
            case Email:
                u = GetUserViaEmail(usernameOrEmail);
                break;
        }

        if (u == null)
            return false;

        if (u.Password.equals(pass))
        {
            u.LoggedIn = true;
            return UpdateUser(u);
        }

        return false;
    }

    public User GetUserViaUsername(String username)
    {
        return GetUserViaSelect("SELECT * FROM " + m_userTable + " WHERE username = '" + username + "';");
    }

    public User GetUserViaEmail(String email)
    {
        return GetUserViaSelect("SELECT * FROM " + m_userTable + " WHERE email = '" + email + "';");
    }

    private User GetUserViaSelect(String select)
    {
        new getData().execute(select);

    }

    private class getData extends AsyncTask<String, Void, Void>
    {
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                URLConnection connection = new URL(m_databaseUrl + params[0]).openConnection();
                InputStream res = connection.getInputStream();

                String result = IOUtils.toString(res, StandardCharsets.UTF_8);
                return result;
            }
            catch (Exception e)
            {
                return "failed";
            }
        }
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

    public ArrayList<Meal> GetNearbyMeals(User user)
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

    public List<Meal> GetFilteredMeals(String searchString)
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

}
