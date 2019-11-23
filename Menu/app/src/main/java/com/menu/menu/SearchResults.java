package com.menu.menu;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.MealsCallback;
import com.menu.menu.Classes.User;
import com.menu.menu.Classes.UsersCallback;
import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity
{
    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    List<Meal> m_mealInfoArray = new ArrayList<>();
    List<User> m_userInfoArray = new ArrayList<>();
    ListView m_displayList;
    EditText m_searchText;
    Boolean m_bShowingUsers = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        m_searchText = findViewById(R.id.input_search);
        m_displayList = findViewById(R.id.mealsList);

        Bundle extras = getIntent().getExtras();
        if(extras == null || !extras.containsKey("search"))
        {
            UpdateListWithUsers(null);
        }
        else
        {
            UpdateListWithUsers(extras.getString("search"));
        }

        Button btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(SearchResults.this, MainHub.class));
            }
        });

        m_searchText.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction()==KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    if(m_bShowingUsers)
                    {
                        UpdateListWithUsers(m_searchText.getText().toString());
                    }
                    else
                    {
                        UpdateListWithMeals(m_searchText.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });

        m_displayList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(m_bShowingUsers)
                {
                    ChefMeals.ChefId = m_userInfoArray.get(position).Id;
                    ChefMeals.ChefUsername = m_userInfoArray.get(position).Username;
                    startActivity(new Intent(SearchResults.this, ChefMeals.class));
                }
                else
                {
                    MealView.m_meal = m_mealInfoArray.get(position);
                    startActivity(new Intent(SearchResults.this, MealView.class));
                }
            }
        });
    }

    private void UpdateListWithUsers(String where)
    {
        SearchResults.GetChefsListCallback gclc = new SearchResults.GetChefsListCallback();
        String message = "SELECT * FROM " + m_dbComms.m_userTable + " WHERE is_chef = 'true' ";
        if(where != null)
        {
            message += "AND " + where;
        }
        gclc.SetMessage(message + ";");
        m_displayList.setAdapter(new SearchResults.UserListAdaptor());
        m_dbComms.RequestUserData(gclc);
    }

    private void UpdateListWithMeals(String where)
    {
        SearchResults.GetMealsListCallback gmlc = new SearchResults.GetMealsListCallback();
        String message = "SELECT * FROM " + m_dbComms.m_mealTable + " ";
        if(where != null)
        {
            message += where;
        }
        gmlc.SetMessage(message + ";");
        m_displayList.setAdapter(new SearchResults.MealListAdaptor());
        m_dbComms.RequestMealData(gmlc);
    }

    //Class used to create a corresponding UI element for each Meal in m_mealInfoArray
    //These UI elements are then added into m_displayList
    private class MealListAdaptor extends ArrayAdapter<Meal>
    {
        public MealListAdaptor()
        {
            super(SearchResults.this, R.layout.layout_meal_info, m_mealInfoArray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            //get view from activity_forms.xml
            View itemView = convertView;
            if (itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.layout_meal_info, parent, false);
            }

            Meal currentMeal = m_mealInfoArray.get(position);

            TextView subjectText = itemView.findViewById(R.id.listItem_Text);
            subjectText.setText(currentMeal.Name);

            TextView onSale = itemView.findViewById(R.id.listItem_OnSale);

            if (currentMeal.CurrentlyOnSale())
            {
                onSale.setText("On Sale");
                onSale.setTextColor(Color.GREEN);
            }
            else
            {
                onSale.setText("Marked Unavailable");
                onSale.setTextColor(Color.RED);
            }

            return itemView;
        }
    }

    //Class used to create a corresponding UI element for each Meal in m_mealInfoArray
    //These UI elements are then added into m_displayList
    private class UserListAdaptor extends ArrayAdapter<User>
    {
        public UserListAdaptor()
        {
            super(SearchResults.this, R.layout.layout_meal_info, m_userInfoArray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            //get view from activity_forms.xml
            View itemView = convertView;
            if (itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.layout_meal_info, parent, false);
            }

            User currentUser = m_userInfoArray.get(position);

            TextView subjectText = itemView.findViewById(R.id.listItem_Text);
            subjectText.setText(currentUser.Username);

            TextView onSale = itemView.findViewById(R.id.listItem_OnSale);
            onSale.setText(currentUser.FoodType);

            return itemView;
        }
    }

    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(SearchResults.this, errorString, Toast.LENGTH_LONG);
        t.show();
    }

    private class GetMealsListCallback extends MealsCallback
    {
        @Override
        public Void call() throws Exception
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(!m_meals.isEmpty())
                    {
                        m_mealInfoArray = m_meals;
                        m_displayList.setAdapter(new SearchResults.MealListAdaptor());
                    }
                    else
                    {
                        SetError(m_message);
                    }
                }
            });
            return null;
        }
    }

    private class GetChefsListCallback extends UsersCallback
    {
        @Override
        public Void call() throws Exception
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(!m_users.isEmpty())
                    {
                        m_userInfoArray = m_users;
                        m_displayList.setAdapter(new SearchResults.UserListAdaptor());
                    }
                    else
                    {
                        SetError(m_message);
                    }
                }
            });
            return null;
        }
    }
}
