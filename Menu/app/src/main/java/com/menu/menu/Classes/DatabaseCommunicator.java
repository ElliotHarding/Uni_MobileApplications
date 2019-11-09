package com.menu.menu.Classes;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseCommunicator
{
    private FirebaseDatabase m_db;
    private DatabaseReference m_mealImages;

    private final String m_userTable = "[menudatabase].[dbo].[User]";
    private final String m_orderTable = "[menudatabase].[dbo].[Order]";
    private final String m_mealTable = "[menudatabase].[dbo].[Meal]";
    private final String m_databaseUrl = "http://themenuapp.gearhostpreview.com/databaseAPI.php?request=";

    public enum LoginOption
    {
        Username,
        Email
    }

    public DatabaseCommunicator()
    {
        m_db = FirebaseDatabase.getInstance();
    }

    public void RequestUserData(UsersCallback usersCallback)
    {
        new ReqUserData().execute(usersCallback);
    }

    public void UploadUserData(ArrayList<User> users, UploadCallback uploadCallback)
    {
        uploadCallback.SetMessage(UserDataToString(users));
        new UploadData().execute(uploadCallback);
    }

    private String UserDataToString(ArrayList<User> users)
    {
        String string = "";
        return string;
    }

    public void RequestOrderData(OrdersCallback ordersCallback)
    {
        new ReqOrderData().execute(ordersCallback);
    }

    public void UploadOrderData(ArrayList<Order> orders, UploadCallback uploadCallback)
    {
        uploadCallback.SetMessage(OrderDataToString(orders));
        new UploadData().execute(uploadCallback);
    }

    private String OrderDataToString(ArrayList<Order> orders)
    {
        String string = "";
        return string;
    }

    public void RequestMealData(MealsCallback mealsCallback)
    {
        new ReqMealData().execute(mealsCallback);
    }

    public void UploadMealData(ArrayList<Meal> meals, UploadCallback uploadCallback)
    {
        uploadCallback.SetMessage(MealDataToString(meals));
        new UploadData().execute(uploadCallback);
    }

    private String MealDataToString(ArrayList<Meal> meals)
    {
        String string = "";
        return string;
    }

    private class ReqUserData extends AsyncTask<UsersCallback, String, String>
    {
        @Override
        protected String doInBackground(UsersCallback... params)
        {
            try
            {
                URLConnection connection = new URL(m_databaseUrl + params[0].GetMessage()).openConnection();
                InputStream res = connection.getInputStream();

                String result = IOUtils.toString(res, StandardCharsets.UTF_8);
                ArrayList<User> users = null;

                params[0].AddUsers(users);
                params[0].call();
            }
            catch (Exception e)
            {

            }
            return "Executed";
        }
    }

    private class ReqMealData extends AsyncTask<MealsCallback, String, String>
    {
        @Override
        protected String doInBackground(MealsCallback... params)
        {
            try
            {
                URLConnection connection = new URL(m_databaseUrl + params[0].GetMessage()).openConnection();
                InputStream res = connection.getInputStream();

                String result = IOUtils.toString(res, StandardCharsets.UTF_8);
                ArrayList<Meal> meals = null;

                params[0].AddMeals(meals);
                params[0].call();
            }
            catch (Exception e)
            {

            }
            return "Executed";
        }
    }

    private class ReqOrderData extends AsyncTask<OrdersCallback, String, String>
    {
        @Override
        protected String doInBackground(OrdersCallback... params)
        {
            try
            {
                URLConnection connection = new URL(m_databaseUrl + params[0].GetMessage()).openConnection();
                InputStream res = connection.getInputStream();

                String result = IOUtils.toString(res, StandardCharsets.UTF_8);
                ArrayList<Order> orders = null;

                params[0].AddOrders(orders);
                params[0].call();
            }
            catch (Exception e)
            {

            }
            return "Executed";
        }
    }

    private class UploadData extends AsyncTask<UploadCallback, String, String>
    {
        @Override
        protected String doInBackground(UploadCallback... params)
        {
            try
            {
                URLConnection connection = new URL(m_databaseUrl + params[0].GetMessage()).openConnection();
                InputStream res = connection.getInputStream();
                String result = IOUtils.toString(res, StandardCharsets.UTF_8);

                if (result.equals("Success"))
                {
                    params[0].SetPass(true);
                }
                else
                {
                    params[0].SetPass(false);
                }
                params[0].SetMessage(result);
            }
            catch (Exception e)
            {
                params[0].SetPass(false);
                params[0].SetMessage(e.getMessage());
            }

            return "Executed";
        }
    }

    public boolean TryLogin(String usernameOrEmail, String pass, LoginOption option)
    {
        return true;
    }

    public boolean TryLogout()
    {
        return true;
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
        return ReturnFakeUser();
    }


    private User ReturnFakeUser()
    {
        User elliot = new User();
        elliot.AddressLine1 = "aaaaaa";
        elliot.AddressLine2 = "bbbbbb";
        elliot.AddressLine3 = "cccccc";
        elliot.Email = "elliot.test@test.com";
        elliot.AddressPostCode = "666565";
        elliot.FirstName = "elliot";
        elliot.LastName = "harding";
        elliot.LoggedIn = true;
        elliot.Password = "password";
        elliot.Phone = "07450232555";
        elliot.Username = "elliot";
        return elliot;
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
