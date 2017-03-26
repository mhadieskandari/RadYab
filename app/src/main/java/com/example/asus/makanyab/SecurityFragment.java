package com.example.asus.makanyab;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.makanyab.db.MakanYabDataBase;
import com.example.asus.makanyab.models.Setting;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecurityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecurityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecurityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MakanYabDataBase dataBase;
    private OnFragmentInteractionListener mListener;


    private MakanYabDataBase getHelper() {
        if ( dataBase== null) {
            dataBase = OpenHelperManager.getHelper(getContext(), MakanYabDataBase.class);
        }
        return dataBase;
    }

    public SecurityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecurityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecurityFragment newInstance(String param1, String param2) {
        SecurityFragment fragment = new SecurityFragment();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_security, container, false);

        dataBase=getHelper();
        final EditText etOldPass=(EditText)view.findViewById(R.id.etOldPass);
        final EditText etNewPass=(EditText)view.findViewById(R.id.etNewPass);
        Button btnSave =(Button)view.findViewById(R.id.btnSave);
        final CheckBox chkHidden=(CheckBox)view.findViewById(R.id.chkHidden);


        chkHidden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chkHidden.isChecked()){
                    PackageManager p = getContext().getPackageManager();
                    ComponentName componentName = new ComponentName(getContext(), MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
                    p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    Toast.makeText(getContext(),getResources().getString(R.string.msg_luncher_hide),Toast.LENGTH_LONG).show();
                }
                else {
                    PackageManager p = getContext().getPackageManager();
                    ComponentName componentName = new ComponentName(getContext(), MainActivity.class);
                    p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                    Toast.makeText(getContext(),getResources().getString(R.string.msg_luncher_show),Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etOldPass.getText().equals("")){
                    Setting setting_pass=null;
                    try {
                        setting_pass=dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name,Setting.Field_Setting_PassWord).get(0);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    if(etOldPass.getText().toString().equals(setting_pass.getSetting_Val())){
                        if(etNewPass.getText().toString().equals("")){
                            Toast.makeText(getContext(),getResources().getString(R.string.msg_new_pass_required),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            setting_pass.setSetting_Val(etNewPass.getText().toString());
                            try {
                                dataBase.getSettingDao().update(setting_pass);
                                Toast.makeText(getContext(), getResources().getString(R.string.saved),Toast.LENGTH_SHORT).show();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else
                {
                    Toast.makeText(getContext(),getResources().getString(R.string.msg_old_pass_required),Toast.LENGTH_LONG).show();
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
