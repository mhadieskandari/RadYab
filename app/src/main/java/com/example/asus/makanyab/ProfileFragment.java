package com.example.asus.makanyab;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.makanyab.db.GPSMonitorDataSource;
import com.example.asus.makanyab.db.MakanYabDataBase;
import com.example.asus.makanyab.models.Setting;

import java.sql.SQLException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GPSMonitorDataSource dataSource;

    MakanYabDataBase dataBase;

    EditText etMyName, etMyNumber;
    Button btnSave;

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_Setting_MyName = "MyName";
    public static final String ARG_Setting_MyPhone = "MyPhone";

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

//        getActivity().setTitle(getResources().getString(R.string.location_profile));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        dataBase =new MakanYabDataBase(getActivity());


//        dataSource = new GPSMonitorDataSource(getActivity());
//        dataSource.open();



        etMyName =(EditText)view.findViewById(R.id.etMyName);
        etMyNumber =(EditText)view.findViewById(R.id.etMyNumber);

        try {
//            etMyName.setText((CharSequence)dataSource.Setting_GetVal(ARG_Setting_MyName).getSetting_Val());
//            etMyNumber.setText((CharSequence)dataSource.Setting_GetVal(ARG_Setting_MyPhone).getSetting_Val());

            Setting myName= dataBase.getSettingDao().queryForEq("Setting_Name",ARG_Setting_MyName).get(0);
            Setting myNumber= dataBase.getSettingDao().queryForEq("Setting_Name",ARG_Setting_MyPhone).get(0);


            etMyName.setText(myName.getSetting_Val());
            etMyNumber.setText(myNumber.getSetting_Val());
        }
        catch (Exception e) {
            Log.d("MyLogTag",e.getMessage());
        }


        btnSave =(Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etMyNumber.getText().equals("") && !etMyName.getText().equals("")) {

                    Setting MyName = new Setting();
                    Setting MyPhone = new Setting();
                    MyName.setSetting_Name(ARG_Setting_MyName);
                    MyName.setSetting_Val(String.valueOf(etMyName.getText()));
                    //dataSource.Setting_Insert(MyName);

                    try {
                        dataBase.getSettingDao().create(MyName);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    MyPhone.setSetting_Name(ARG_Setting_MyPhone);
                    MyPhone.setSetting_Val(String.valueOf(etMyNumber.getText()));
//                    dataSource.Setting_Insert(MyPhone);
                    try {
                        dataBase.getSettingDao().create(MyPhone);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getActivity(),getString(R.string.Saved),Toast.LENGTH_LONG).show();
                }
                else {

                }
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
