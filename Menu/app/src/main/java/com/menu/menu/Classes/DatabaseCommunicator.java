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

public class DatabaseCommunicator
{
    private FirebaseDatabase m_db;

    public final String m_userTable = "[menudatabase].[dbo].[User]";
    public final String m_orderTable = "[menudatabase].[dbo].[Order]";
    public final String m_mealTable = "[menudatabase].[dbo].[Meal]";
    public final String m_mealInsert = "INSERT INTO [menudatabase].[dbo].[Meal] (owner_user_id,meal_name,is_halal,is_vegan,is_vegiterian,contains_milk,contains_gluten,ingredients_list,estimated_calories,price,number_of_portions_avaliable,id,ownerUsername,eatIn,hoursAvaliableFrom,hoursAvaliableTo,picture_id,rating) VALUES ";
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
                        u.setPicutreFromSql(userElements[19]);

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
                        m.setOwnerId(userElements[0]);
                        m.setName(userElements[1]);
                        m.setHalal(userElements[2].equals("true"));
                        m.setVegan(userElements[3].equals("true"));
                        m.setVegiterian(userElements[4].equals("true"));
                        m.setContainsMilk(userElements[5].equals("true"));
                        m.setContainsGluten(userElements[6].equals("true"));
                        m.setIngredients(userElements[7]);
                        m.setCalories(userElements[8]);
                        m.setPrice(userElements[9]);
                        m.setMaxNoPortions(userElements[10]);
                        m.setId(userElements[11]);
                        m.setOwnerUsername(userElements[12]);
                        m.setEatIn(userElements[13]);
                        m.setHoursAvaliableFrom(userElements[14]);
                        m.setHoursAvaliableTo(userElements[15]);
                        m.setPicutreFromSql(userElements[16]);
                        m.setRating(userElements[17]);

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
                        o.SetId(userElements[0]);
                        o.SetMealId(userElements[1]);
                        o.SetNumberOfMeals(userElements[2]);
                        o.SetOrdererId(userElements[4]);
                        o.SetState(userElements[5]);

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

}
