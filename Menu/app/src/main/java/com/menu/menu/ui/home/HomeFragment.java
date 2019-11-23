package com.menu.menu.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.menu.menu.R;

public class HomeFragment extends Fragment
{
    private MapView m_MapView;
    private GoogleMap m_googleMap;
    private View.OnClickListener m_drawerListener;

    public void SetDrawerButtonListner(View.OnClickListener listener)
    {
        m_drawerListener = listener;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

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

                // For showing a move to my location button
                //m_googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                m_googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                m_googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        root.findViewById(R.id.drawerToggle).setOnClickListener(m_drawerListener);

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
}