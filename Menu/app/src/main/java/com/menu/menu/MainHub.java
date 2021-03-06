package com.menu.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.OrdersCallback;

public class MainHub extends AppCompatActivity
{
    //Deal with fragment management
    public static final String HomeFragmentTag = "HOME_FRAGMENT";
    public static final String BasketFragmentTag = "BASKET_FRAGMENT";
    public static final String SettingsFragmentTag = "SETTINGS_FRAGMENT";
    public static final String MyMealsFragmentTag = "MY_MEALS_FRAGMENT";
    public static final String MyOrdersFragmentTag = "MY_ORDERS_FRAGMENT";

    private NavigationView m_navigationView;

    final View.OnClickListener m_drawerLisner = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            ((DrawerLayout)findViewById(R.id.drawer_layout)).openDrawer(GravityCompat.START);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hub);

        m_navigationView = findViewById(R.id.nav_view);
        m_navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                ((DrawerLayout)findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);

                switch (menuItem.getItemId())
                {
                    case R.id.nav_chefSettigns:
                        m_navigationView.getMenu().getItem(1).setChecked(true);
                        NavigateToFragment(MyMealsFragmentTag);
                        break;

                    case R.id.nav_signOut:
                        LocalSettings.SignOutUser(getApplicationContext());
                        startActivity(new Intent(MainHub.this, Login.class));
                        break;

                    case R.id.nav_settings:
                        m_navigationView.getMenu().getItem(2).setChecked(true);
                        NavigateToFragment(SettingsFragmentTag);
                        break;

                    case R.id.nav_basket:
                        m_navigationView.getMenu().getItem(3).setChecked(true);
                        NavigateToFragment(BasketFragmentTag);
                        break;

                    case R.id.nav_currentOrders:
                        m_navigationView.getMenu().getItem(0).setChecked(true);
                        NavigateToFragment(MyOrdersFragmentTag);
                        break;

                    case R.id.nav_home:

                        NavigateToFragment(HomeFragmentTag);
                        break;

                    case R.id.nav_reportIssue:

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainHub.this);
                        builder.setTitle("Send Error Report");

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setData(Uri.parse("mailto:"));
                                intent.putExtra(Intent.EXTRA_EMAIL, "MeNU.contact.us@gmail.com");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Issue from " + LocalSettings.GetLocalUser().getId());
                                if (intent.resolveActivity(getPackageManager()) != null)
                                {
                                    startActivity(intent);
                                }
                                else
                                {
                                    SetError("Failed to send report. Check internet?");
                                }

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();

                        break;
                }
                return true;
            }
        });

        TextView title = m_navigationView.getHeaderView(0).findViewById(R.id.nav_bar_title);
        title.setText(LocalSettings.GetLocalUser().getUsername());

        TextView subTitle = m_navigationView.getHeaderView(0).findViewById(R.id.nav_bar_subTitle);
        subTitle.setText(LocalSettings.GetLocalUser().getEmail());

        ImageView imgTitle = m_navigationView.getHeaderView(0).findViewById(R.id.nav_bar_image);
        imgTitle.setImageBitmap(LocalSettings.GetLocalUser().getPicture());

        if(!LocalSettings.GetLocalUser().getIsChef_b())
            m_navigationView.findViewById(R.id.nav_chefSettigns).setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        if(extras == null || !extras.containsKey("fragment"))
        {
            NavigateToFragment(HomeFragmentTag);
        }
        else
        {
            NavigateToFragment(extras.getString("fragment"));
        }
    }

    public void NavigateToFragment(String tag)
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null || !fragment.isVisible())
        {
            switch (tag)
            {
                case HomeFragmentTag:
                    fragment = new Home();
                    ((Home)fragment).SetDrawerButtonListner(m_drawerLisner);
                    tag = HomeFragmentTag;
                    break;

                case BasketFragmentTag:
                    fragment = new Basket();
                    ((Basket)fragment).SetDrawerButtonListner(m_drawerLisner);
                    tag = BasketFragmentTag;
                    break;

                case SettingsFragmentTag:
                    fragment = new Settings();
                    ((Settings)fragment).SetDrawerButtonListner(m_drawerLisner);
                    tag = SettingsFragmentTag;
                    break;

                case MyMealsFragmentTag:
                    fragment = new MyMeals();
                    ((MyMeals)fragment).SetDrawerButtonListner(m_drawerLisner);
                    tag = MyMealsFragmentTag;
                    break;

                case MyOrdersFragmentTag:
                    fragment = new MyOrders();
                    ((MyOrders)fragment).SetDrawerButtonListner(m_drawerLisner);
                    tag = MyOrdersFragmentTag;
                    break;
            }

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentHolder, fragment, tag);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed()
    {
    }

    @Override
    protected void onResume()
    {
        //Check not already on that fragment
        Fragment hf = getSupportFragmentManager().findFragmentByTag(HomeFragmentTag);
        if (hf == null || !hf.isVisible())
        {
            m_navigationView.getMenu().getItem(0).setChecked(true);
        }
        super.onResume();
    }

    public void SetError(String errorString)
    {
        Toast t = Toast.makeText(MainHub.this, errorString,  Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    private AppBarConfiguration mAppBarConfiguration;
    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
