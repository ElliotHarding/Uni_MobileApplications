package com.menu.menu.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.menu.menu.ChefMeals;
import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.User;
import com.menu.menu.Classes.UsersCallback;
import com.menu.menu.MainHub;
import com.menu.menu.R;
import com.menu.menu.SearchResults;

import java.util.ArrayList;

public class HomeFragment extends Fragment
{
    private MapView m_MapView;
    private GoogleMap m_googleMap;
    private View.OnClickListener m_drawerListener;
    DatabaseCommunicator m_dbComms = new DatabaseCommunicator();

    class UsernameIdPair
    {
        UsernameIdPair(String username, String id)
        {
            Username = username;
            Id = id;
        }
        String Username;
        String Id;
    }
    ArrayList<UsernameIdPair> m_markerInformaitonList = new ArrayList<>();

    public void SetDrawerButtonListner(View.OnClickListener listener)
    {
        m_drawerListener = listener;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final SearchView searchView = root.findViewById(R.id.searchView);
        searchView.setOnSearchClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), SearchResults.class);
                intent.putExtra("search", searchView.getQuery());
                startActivity(intent);
            }
        });

        m_MapView = root.findViewById(R.id.mapView);
        m_MapView.onCreate(savedInstanceState);
        m_MapView.onResume();

        try
        {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        m_MapView.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap mMap)
            {
                m_googleMap = mMap;

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                m_googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                m_googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                m_googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
                {
                    @Override
                    public void onInfoWindowClick(Marker marker)
                    {
                        for(UsernameIdPair uip : m_markerInformaitonList)
                        {
                            if(uip.Username.equals(marker.getTitle()))
                            {
                                ChefMeals.ChefId = uip.Id;
                                ChefMeals.ChefUsername = uip.Username;
                                startActivity(new Intent(getActivity(), ChefMeals.class));
                            }
                        }
                    }
                });
            }
        });

        root.findViewById(R.id.drawerToggle).setOnClickListener(m_drawerListener);

        UpdateList();

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        m_MapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        m_MapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        m_MapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        m_MapView.onLowMemory();
    }

    private void UpdateList()
    {
        HomeFragment.GetChefsListCallback gmlc = new HomeFragment.GetChefsListCallback();
        gmlc.SetMessage("SELECT * FROM " + m_dbComms.m_userTable + " WHERE is_chef = 'true';");
        m_dbComms.RequestUserData(gmlc);
    }

    private void SetError(String errorString)
    {
        Toast.makeText(getActivity(), errorString,  Toast.LENGTH_LONG).show();
    }

    private class GetChefsListCallback extends UsersCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(!m_users.isEmpty())
            {
                ((MainHub)getActivity()).runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for(User user : m_users)
                        {
                            try
                            {
                                LatLng latLong = new LatLng(Integer.parseInt(user.getLatitude()), Integer.parseInt(user.getLongitude()));
                                m_googleMap.addMarker(new MarkerOptions().position(latLong).title(user.getUsername()).snippet(user.getFoodType()));
                                m_markerInformaitonList.add(new UsernameIdPair(user.getUsername(), user.getId()));
                            }
                            catch (Exception e)
                            {
                                SetError("At least one user failed to load!");
                            }
                        }
                    }
                });
            }
            else
            {
                SetError(m_message);
            }
            return null;
        }
    }
}