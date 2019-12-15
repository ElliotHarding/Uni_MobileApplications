package com.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.MealListViewItem;
import com.menu.menu.Classes.MealsCallback;
import com.menu.menu.Classes.User;
import com.menu.menu.Classes.UsersCallback;
import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity
{
    private DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    private ArrayList<Meal> m_mealInfoArray = new ArrayList<>();
    private List<User> m_userInfoArray = new ArrayList<>();
    private ListView m_displayList;
    private EditText m_searchText;
    private Boolean m_bShowingUsers = true;
    private ProgressBar m_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        m_searchText = findViewById(R.id.input_search);
        m_displayList = findViewById(R.id.mealsList);
        m_progressBar = findViewById(R.id.progressBar);

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
                    Intent intent = new Intent(SearchResults.this, ChefMeals.class);
                    intent.putExtra("chefId", m_userInfoArray.get(position).getId());
                    intent.putExtra("chefUsername", m_userInfoArray.get(position).getUsername());
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(SearchResults.this, MealView.class);
                    intent.putExtra("meal", m_mealInfoArray.get(position));
                    startActivity(intent);
                }
            }
        });
    }

    private void UpdateListWithUsers(String where)
    {
        m_progressBar.startNestedScroll(1);
        m_progressBar.setVisibility(View.VISIBLE);

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
        m_progressBar.startNestedScroll(1);
        m_progressBar.setVisibility(View.VISIBLE);

        SearchResults.GetMealsListCallback gmlc = new SearchResults.GetMealsListCallback();
        String message = "SELECT * FROM " + m_dbComms.m_mealTable + " ";
        if(where != null)
        {
            message += where;
        }
        gmlc.SetMessage(message + ";");
        m_displayList.setAdapter(new MealListViewItem(getApplicationContext(),m_mealInfoArray));
        m_dbComms.RequestMealData(gmlc);
    }

    //Class used to create a corresponding UI element for each ic_meal in m_mealInfoArray
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

            TextView subjectText = itemView.findViewById(R.id.li_text);
            subjectText.setText(currentUser.getUsername());

            TextView onSale = itemView.findViewById(R.id.li_infoRight);
            onSale.setText(currentUser.getFoodType());

            //Img
            ImageView img = itemView.findViewById(R.id.img);
            img.setImageBitmap(currentUser.getPicture());

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
                        m_displayList.setAdapter(new MealListViewItem(getApplicationContext(), m_mealInfoArray));
                    }
                    else
                    {
                        SetError(m_message);
                    }
                    m_progressBar.setVisibility(View.INVISIBLE);
                    m_progressBar.stopNestedScroll();
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
                    m_progressBar.setVisibility(View.INVISIBLE);
                    m_progressBar.stopNestedScroll();
                }
            });
            return null;
        }
    }
}
