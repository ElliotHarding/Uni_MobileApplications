package com.menu.menu;

import android.content.Intent;
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

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.ui.home.HomeFragment;

public class MainHub extends AppCompatActivity
{
    //Deal with fragment management
    public static final String HomeFragmentTag = "HOME_FRAGMENT";
    public static final String BasketFragmentTag = "BASKET FRAGMENT";

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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                ((DrawerLayout)findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);

                switch (menuItem.getItemId())
                {
                    case R.id.nav_chefSettigns:
                        startActivity(new Intent(MainHub.this, ChefSettings.class));
                        break;
                    case R.id.nav_signOut:
                        if (new DatabaseCommunicator().TryLogout())
                        {
                            startActivity(new Intent(MainHub.this, Login.class));
                        }
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(MainHub.this, Settings.class));
                        break;
                    case R.id.nav_basket:

                        //Check not already on that fragment
                        Fragment hf = getSupportFragmentManager().findFragmentByTag(BasketFragmentTag);
                        if (hf == null || !hf.isVisible())
                        {
                            NavigateToFragment(BasketFragmentTag);
                        }
                        break;

                    case R.id.nav_home:

                        //Check not already on that fragment
                        hf = getSupportFragmentManager().findFragmentByTag(HomeFragmentTag);
                        if (hf == null || !hf.isVisible())
                        {
                            NavigateToFragment(HomeFragmentTag);
                        }
                        break;
                }
                return true;
            }
        });

        TextView title = navigationView.getHeaderView(0).findViewById(R.id.nav_bar_title);
        title.setText(LocalSettings.GetLocalUser().getUsername());

        TextView subTitle = navigationView.getHeaderView(0).findViewById(R.id.nav_bar_subTitle);
        subTitle.setText(LocalSettings.GetLocalUser().getEmail());

        ImageView imgTitle = navigationView.getHeaderView(0).findViewById(R.id.nav_bar_image);
        imgTitle.setImageBitmap(LocalSettings.GetLocalUser().getPicture());

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

    private void NavigateToFragment(String tag)
    {
        Fragment fragment = null;

        switch (tag)
        {
            case HomeFragmentTag:
                fragment = new HomeFragment();
                ((HomeFragment)fragment).SetDrawerButtonListner(m_drawerLisner);
                tag = HomeFragmentTag;
                break;

            case BasketFragmentTag:
                fragment = new Basket();
                ((Basket)fragment).SetDrawerButtonListner(m_drawerLisner);
                tag = BasketFragmentTag;
                break;
        }

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentHolder, fragment, tag);
        ft.commit();
    }

    @Override
    public void onBackPressed()
    {
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
