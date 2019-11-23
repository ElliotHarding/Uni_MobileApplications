package com.menu.menu.Classes;

import android.os.AsyncTask;
import com.google.firebase.database.FirebaseDatabase;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseCommunicator
{
    private FirebaseDatabase m_db;

    public final String m_userTable = "[menudatabase].[dbo].[User]";
    public final String m_orderTable = "[menudatabase].[dbo].[Order]";
    public final String m_mealTable = "[menudatabase].[dbo].[Meal]";
    public final String m_mealInsert = "INSERT INTO [menudatabase].[dbo].[Meal] (owner_user_id,meal_name,is_halal,is_vegan,is_vegiterian,contains_milk,contains_gluten,ingredients_list,estimated_calories,price,number_of_portions_avaliable,id,ownerUsername,eatIn,hoursAvaliableFrom,hoursAvaliableTo,picture_id) VALUES ";
    public final String m_userInsert = "INSERT INTO [menudatabase].[dbo].[User] (id,name,password,full_name,address_line_1,address_line_2,address_city,address_description,date_of_birth,logged_in,contact_email,contact_phone,rating,is_admin,picture_id,is_chef,latitude,longitude,food_type) VALUES ";
    private final String m_dbRequestUrl = "http://themenuapp.gearhostpreview.com/databaseAPI.php?request=";
    private final String m_dbPostUrl = "http://themenuapp.gearhostpreview.com/databaseAPIpost.php";

    public DatabaseCommunicator()
    {
        m_db = FirebaseDatabase.getInstance();
    }

    public void RequestUserData(UsersCallback usersCallback)
    {
        new ReqUserData().execute(usersCallback);
    }

    public void RequestOrderData(OrdersCallback ordersCallback)
    {
        new ReqOrderData().execute(ordersCallback);
    }

    public void RequestMealData(MealsCallback mealsCallback)
    {
        new ReqMealData().execute(mealsCallback);
    }

    public void GenericUpload(BaseCallback cb)
    {
        new UploadData().execute(cb);
    }

    private class ReqUserData extends AsyncTask<UsersCallback, String, String>
    {
        @Override
        protected String doInBackground(UsersCallback... params)
        {
            try
            {
                URLConnection connection = new URL(m_dbRequestUrl + params[0].GetMessage()).openConnection();
                InputStream res = connection.getInputStream();

                String webResponse = IOUtils.toString(res, StandardCharsets.UTF_8);
                if(webResponse!=null && new Character(webResponse.charAt(0)).equals('['))
                {
                    ArrayList<User> users = new ArrayList<>();

                    String[] results = ParseWebResponse(webResponse);

                    for (String user: results)
                    {
                        user = user.substring(0, user.length()-1);
                        String userElements[] = user.split(",");

                        User u = new User();
                        u.Id = userElements[0];
                        u.Username = userElements[1];
                        u.Password = userElements[2];
                        u.FullName = userElements[3];
                        u.AddressLine1 = userElements[4];
                        u.AddressLine2 = userElements[5];
                        u.AddressLine3 = userElements[6];
                        u.AddressPostCode = userElements[7];
                        u.AddressDescription = userElements[8];
                        u.DOB = userElements[9];
                        u.LoggedIn = userElements[10];
                        u.Email = userElements[11];
                        u.Phone = userElements[12];
                        u.Rating = userElements[13];
                        u.IsAdmin = userElements[14];
                        u.IsChef = userElements[15];
                        u.Latitude = userElements[16];
                        u.Longitude = userElements[17];
                        u.FoodType = userElements[18];
                        u.SetPicutreFromSql(userElements[19]);

                        users.add(u);
                    }
                    params[0].AddUsers(users);
                }
            }
            catch (Exception e)
            {
                params[0].SetMessage(e.getMessage());
            }

            //Call callback
            try
            {
                params[0].call();
            }
            catch (Exception e)
            {
                e.printStackTrace();
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
                URLConnection connection = new URL(m_dbRequestUrl + params[0].GetMessage()).openConnection();
                InputStream res = connection.getInputStream();

                String webResponse = IOUtils.toString(res, StandardCharsets.UTF_8);
                if(webResponse!=null && new Character(webResponse.charAt(0)).equals('['))
                {
                    ArrayList<Meal> meals = new ArrayList<>();

                    String[] results = ParseWebResponse(webResponse);

                    for (String meal: results)
                    {
                        meal = meal.substring(0, meal.length()-1);
                        String userElements[] = meal.split(",");

                        Meal m = new Meal();
                        m.OwnerId = userElements[0];
                        m.Name = userElements[1];
                        m.IsHalal = userElements[2].equals("true");
                        m.IsVegan = userElements[3].equals("true");
                        m.IsVegiterian = userElements[4].equals("true");
                        m.ContainsMilk = userElements[5].equals("true");
                        m.ContainsGluten = userElements[6].equals("true");
                        m.Ingredients = userElements[7];
                        m.Calories = userElements[8];
                        m.Price = userElements[9];
                        m.MaxNoPortions = userElements[10];
                        m.Id = userElements[11];
                        m.OwnerUsername = userElements[12];
                        m.EatIn = userElements[13];
                        m.HoursAvaliableFrom = userElements[14];
                        m.HoursAvaliableTo = userElements[15];
                        m.SetPicutreFromSql(userElements[16]);

                        meals.add(m);
                    }
                    params[0].AddMeals(meals);
                }
            }
            catch (Exception e)
            {
                params[0].SetMessage(e.getMessage());
            }

            //Call callback
            try
            {
                params[0].call();
            }
            catch (Exception e)
            {
                e.printStackTrace();
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
                URLConnection connection = new URL(m_dbRequestUrl + params[0].GetMessage()).openConnection();
                InputStream res = connection.getInputStream();

                String webResponse = IOUtils.toString(res, StandardCharsets.UTF_8);
                if(webResponse!=null && new Character(webResponse.charAt(0)).equals('['))
                {
                    ArrayList<Order> orders = new ArrayList<>();

                    String[] results = ParseWebResponse(webResponse);

                    for (String order: results)
                    {
                        order = order.substring(0, order.length()-1);
                        String userElements[] = order.split(",");

                        Order o = new Order();
                        o.Id = userElements[0];
                        o.MealId = userElements[1];
                        o.NumberOfMeals = userElements[2];
                        o.OrdererId = userElements[4];
                        o.CurrentState = userElements[5];

                        orders.add(o);
                    }
                    params[0].AddOrders(orders);
                }
            }
            catch (Exception e)
            {
                params[0].SetMessage(e.getMessage());
            }

            try
            {
                params[0].call();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return "Executed";
        }
    }

    private class UploadData extends AsyncTask<BaseCallback, String, String>
    {
        @Override
        protected String doInBackground(BaseCallback... params)
        {
            try
            {
                URLConnection connection = new URL(m_dbPostUrl).openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write("request="+params[0].GetMessage());
                writer.flush();
                InputStream res = connection.getInputStream();
                String result = IOUtils.toString(res, StandardCharsets.UTF_8);
                writer.close();

                params[0].SetMessage(result);
            }
            catch (Exception e)
            {
                params[0].SetMessage(e.getMessage());
            }

            try
            {
                params[0].call();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return "Executed";
        }
    }

    private String[] ParseWebResponse(String response)
    {
        //Get rid of first [
        response = response.substring(2);

        //Get rid of last ]
        response = response.substring(0, response.length()-1);


        //Put into array seperated by "["
        String results[];
        if(!response.contains("["))
        {
            results = new String[1];
            results[0] = response;
        }
        else
        {
            results = response.split("\\[");
        }
        return results;
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
        elliot.FullName = "elliot harding";
        elliot.LoggedIn = "true";
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
            m.EatIn = (i == 2) ? "no":"yes";
            m.Price = "6.10";
            m.MaxNoPortions = "10";
            m.Ingredients = "A \n B \n C";
            m.OwnerUsername = "Elliot";
            meals.add(m);
        }

        return meals;
    }

    public Meal GetMeal(String s)
    {
        Meal m = new Meal();
        m.Name = "Meal";
        m.EatIn = "no";
        m.Price = "6.10";
        m.MaxNoPortions = "10";
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
            m.EatIn = (i == 2) ? "no":"yes";
            m.Price = "6.10";
            m.MaxNoPortions = "10";
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
        o.ArrivalTime = (new Date(2019, 10, 3, 3, 3, 3)).toString();
        o.Id = "SKNEIOENFE";
        o.NumberOfMeals = "2";
        o.SetState(Order.State.AwatingResponse);

        return o;
    }

    public Order GetOrderUpdate(String id)
    {
        Order o = new Order();

        //Test Data:
        o.ArrivalTime = (new Date(2019, 10, 3, 3, 3, 3)).toString();
        o.Id = id;
        o.NumberOfMeals = "2";
        o.SetState(Order.State.AwatingResponse);

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
            m.EatIn = (i == 2) ? "no":"yes";
            m.Price = "6.10";
            m.MaxNoPortions = "10";
            m.Ingredients = "A \n B \n C";
            m.OwnerUsername = "Elliot";
            meals.add(m);
        }

        return meals;
    }

}
