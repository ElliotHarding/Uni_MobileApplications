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
import android.widget.TextView;

import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.ui.home.HomeFragment;

public class MainHub extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hub);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
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
                    case R.id.nav_home:
                        startActivity(new Intent(MainHub.this, MainHub.class));
                        break;
                }
                return true;
            }
        });



        TextView title = navigationView.getHeaderView(0).findViewById(R.id.nav_bar_title);
        title.setText(LocalSettings.LocalUser.Username);

        TextView subTitle = navigationView.getHeaderView(0).findViewById(R.id.nav_bar_subTitle);
        subTitle.setText(LocalSettings.LocalUser.Email);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment home = new HomeFragment();
        ((HomeFragment)home).SetDrawerButtonListner(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ((DrawerLayout)findViewById(R.id.drawer_layout)).openDrawer(GravityCompat.START);
            }
        });
        ft.replace(R.id.fragmentHolder, home);
        ft.commit();
    }

    @Override
    public void onBackPressed()
    {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_hub, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
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
            case R.id.nav_home:
                startActivity(new Intent(MainHub.this, MainHub.class));
                break;
        }
        return true;
    }
}
