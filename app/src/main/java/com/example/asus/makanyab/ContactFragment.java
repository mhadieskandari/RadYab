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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.makanyab.Adapter.ContactRecyclerAdapter;
import com.example.asus.makanyab.db.MakanYabDataBase;
import com.example.asus.makanyab.models.Contact;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    MakanYabDataBase dataBase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Contact> contacts = null;
    RecyclerView recyclerView= null;
    ContactRecyclerAdapter adapter=null;
    LinearLayoutManager mlinearLayoutManagerVertical =null;

    private OnFragmentInteractionListener mListener;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
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

        getActivity().setTitle(getResources().getString(R.string.location_contact_list));
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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact, container, false);


        dataBase=getHelper();

        try {
           contacts=dataBase.getContactDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        recyclerView= (RecyclerView) view.findViewById(R.id.contactRecyclerView);
        adapter=new ContactRecyclerAdapter(getContext(), contacts);
        recyclerView.setAdapter(adapter);

        mlinearLayoutManagerVertical =new LinearLayoutManager(getContext());
        mlinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mlinearLayoutManagerVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                Toast.makeText(getActivity(), "on Move", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//
//                Toast.makeText(getActivity(), "on Swiped ", Toast.LENGTH_SHORT).show();
//                //Remove swiped item from list and notify the RecyclerView
//            }
//        };
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);



        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                View alertView = getActivity().getLayoutInflater().inflate(R.layout.contact_create_alert_dialog_layout,null);
                final EditText etContactName = (EditText) alertView.findViewById(R.id.name);
                final EditText etContactPhone = (EditText) alertView.findViewById(R.id.phone);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(alertView)
                        // Add action buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    if(MainActivity.mIsPremium || dataBase.getContactDao().queryForAll().size()==0) {
                                        String name, phone;
                                        name = etContactName.getText().toString();
                                        phone = etContactPhone.getText().toString();

                                        if (name.equals("") || phone.equals("")) {
                                            Toast.makeText(getContext(), getResources().getString(R.string.msg_create_owner_surly), Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (dataBase.getContactDao().queryForEq(Contact.Field_PhoneNumber, phone).size() == 0) {
                                                Contact c = new Contact(name, phone, 0, true);
                                                dataBase.getContactDao().create(c);
                                                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_main, ContactFragment.newInstance("1", "2")).addToBackStack("Contact").commit();
                                            } else {
                                                Toast.makeText(getContext(),  getResources().getString(R.string.msg_is_exist_user), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    else {
                                        Toast.makeText(getContext(), getResources().getString(R.string.msg_more_than_one_user_upgrade_to_full_version), Toast.LENGTH_LONG).show();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel,null).show();
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
