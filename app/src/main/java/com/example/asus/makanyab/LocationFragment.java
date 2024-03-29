package com.example.asus.makanyab;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asus.makanyab.Adapter.LocationRecyclerAdapter;
import com.example.asus.makanyab.db.MakanYabDataBase;
import com.example.asus.makanyab.models.Contact;
import com.example.asus.makanyab.models.Location;
import com.example.asus.makanyab.models.Setting;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    MakanYabDataBase dataBase;

    List<Location> locations = null;
    RecyclerView recyclerView= null;
    LocationRecyclerAdapter adapter=null;
    LinearLayoutManager mlinearLayoutManagerVertical =null;




    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle(getResources().getString(R.string.location_list));
    }


    private MakanYabDataBase getHelper() {
        if ( dataBase== null) {
            dataBase = OpenHelperManager.getHelper(getContext(), MakanYabDataBase.class);
        }
        return dataBase;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_location, container, false);


        dataBase=getHelper();



        try {

            locations=dataBase.getLocationDao().queryForAll();
//            dataBase.getLocationDao().delete(locations);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        recyclerView= (RecyclerView) view.findViewById(R.id.locationRecyclerView);
        adapter=new LocationRecyclerAdapter(getContext(), locations);
        recyclerView.setAdapter(adapter);

        mlinearLayoutManagerVertical =new LinearLayoutManager(getContext());
        mlinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mlinearLayoutManagerVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                View alertView = getActivity().getLayoutInflater().inflate(R.layout.location_create_alert_dialog_layout,null);

                final Spinner spinnerContacts = (Spinner) alertView.findViewById(R.id.spinnerContact);
                final List<Contact> contactlist;
                ArrayAdapter<Contact> adapter;
                try {
                    if(MainActivity.mIsPremium || dataBase.getLocationDao().queryForAll().size()<20) {
                        contactlist = dataBase.getContactDao().queryForEq(Contact.Field_State, 2);
                        adapter = new ArrayAdapter<Contact>(getContext(), android.R.layout.simple_spinner_dropdown_item, contactlist);
                        spinnerContacts.setAdapter(adapter);

                        builder.setView(alertView)
                                // Add action buttons
                                .setPositiveButton(R.string.request, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            Contact contact = contactlist.get(spinnerContacts.getSelectedItemPosition());
                                            String msg = "GL:" + dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name, ProfileFragment.ARG_Setting_MyName).get(0).getSetting_Val() + "," + dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name, ProfileFragment.ARG_Setting_MyPhone).get(0).getSetting_Val() + ";";

                                            sendSMSMessage(contact, msg);

                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton(R.string.cancel, null).show();
                    }
                    else {
                        Toast.makeText(getContext(), getResources().getString(R.string.msg_more_than_twoenty_location_upgrade_to_full_version), Toast.LENGTH_LONG).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout

            }
        });

        return view;
    }

    protected void sendSMSMessage(Contact contact,String message) {
//        Log.i("Send SMS", "");
//        String phoneNo = "";
//        String message = "";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contact.getPhoneNumber(), null, message, null, null);
            Toast.makeText(getActivity(), "Registration Request sent.", Toast.LENGTH_LONG).show();
            dataBase.getLocationDao().create(new Location(contact));
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_main, LocationFragment.newInstance("1", "2")).addToBackStack("Location").commit();
        }

        catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
