package com.example.asus.makanyab;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MyMapFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_Longitude = "Longitude";
    private static final String ARG_PARAM_Latitude = "Latitude";
    private static final String ARG_PARAM_Alititude = "Altitude";

    // TODO: Rename and change types of parameters
    private double mLongitude;
    private double mLatitude;
    private double mAltitude;

    MapView mMapView;
    private GoogleMap googleMap;


    private OnFragmentInteractionListener mListener;

    public MyMapFragment() {
        // Required empty public constructor
    }

    public static MyMapFragment newInstance(String Latitude, String Longitude, String Altitude) {
        MyMapFragment fragment = new MyMapFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM_Alititude,Double.parseDouble(Altitude));
        args.putDouble(ARG_PARAM_Latitude, Double.parseDouble(Latitude));
        args.putDouble(ARG_PARAM_Longitude, Double.parseDouble(Longitude));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAltitude = getArguments().getDouble(ARG_PARAM_Alititude);
            mLongitude = getArguments().getDouble(ARG_PARAM_Longitude);
            mLatitude = getArguments().getDouble(ARG_PARAM_Latitude);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mymap, container, false);


        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
       mMapView.getMapAsync(new OnMapReadyCallback() {
           @Override
           public void onMapReady(GoogleMap googleMap) {

//               GPSTracker gps = new GPSTracker(getContext());
//              // Toast.makeText(getApplication(),"longi :"+gps.getLongitude()+" lati:"+gps.getLatitude(),Toast.LENGTH_LONG).show();
//
               LatLng latLng=new LatLng(mLatitude,mLongitude);
               googleMap.addCircle(new CircleOptions().center(latLng));
           }
       });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
