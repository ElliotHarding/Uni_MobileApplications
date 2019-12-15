package com.menu.menu.ui.home;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
    private DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    private Marker m_userMarker = null;

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
    private ArrayList<UsernameIdPair> m_markerInformaitonList = new ArrayList<>();

    public void SetDrawerButtonListner(View.OnClickListener listener)
    {
        m_drawerListener = listener;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //
        //Search view
        //
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


        //
        //Map view
        //
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

                m_googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
                {
                    @Override
                    public void onInfoWindowClick(Marker marker)
                    {
                        for(UsernameIdPair uip : m_markerInformaitonList)
                        {
                            if(uip.Username.equals(marker.getTitle()))
                            {
                                Intent intent = new Intent(getActivity(), ChefMeals.class);
                                intent.putExtra("chefId", uip.Id);
                                intent.putExtra("chefUsername", uip.Username);
                                startActivity(intent);
                            }
                        }
                    }
                });

            }
        });


        //
        //Buttons
        //
        root.findViewById(R.id.drawerToggle).setOnClickListener(m_drawerListener);

        root.findViewById(R.id.btn_locate).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UpdateLocation();
            }
        });

        root.findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateList();
            }
        });


        UpdateLocation();

        UpdateList();

        return root;
    }

    private void UpdateLocation()
    {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                if (locationResult != null)
                {
                    for (Location location : locationResult.getLocations())
                    {
                        if (location != null && m_googleMap != null)
                        {
                            LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                            if(m_userMarker != null)
                                m_userMarker.remove();

                            m_userMarker = m_googleMap.addMarker(new MarkerOptions().position(userLatLng).title("You"));

                            // For zooming automatically to the location of the marker
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(userLatLng).zoom(12).build();
                            m_googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }
                }

            }
        };
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
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

        if(m_googleMap != null)
            m_googleMap.clear();
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
            ((MainHub) getActivity()).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(m_users != null && !m_users.isEmpty())
                    {
                        if(m_googleMap != null)
                        {
                            for (User user : m_users)
                            {
                                try
                                {
                                    LatLng latLong = new LatLng(Integer.parseInt(user.getLatitude()), Integer.parseInt(user.getLongitude()));
                                    m_googleMap.addMarker(new MarkerOptions().position(latLong).title(user.getUsername()).snippet(user.getFoodType()));
                                    m_markerInformaitonList.add(new UsernameIdPair(user.getUsername(), user.getId()));
                                }
                                catch (Exception e)
                                {
                                    //SetError("At least one user failed to load!");
                                }
                            }
                        }
                        else
                        {
                            SetError("Map not loaded yet, can't fill.");
                        }
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