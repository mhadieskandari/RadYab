package com.example.asus.makanyab;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.makanyab.Adapter.AboutRecyclerAdapter;
import com.example.asus.makanyab.Adapter.ContactRecyclerAdapter;
import com.example.asus.makanyab.models.AboutItem;
import com.example.asus.makanyab.models.Contact;

import java.util.List;


public class AboutFragment extends Fragment {

    List<AboutItem> aboutItems = null;
    RecyclerView recyclerView= null;
    AboutRecyclerAdapter adapter=null;
    LinearLayoutManager mlinearLayoutManagerVertical =null;




    private OnFragmentInteractionListener mListener;

    public AboutFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.location_contact_us));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_about, container, false);

        aboutItems=new AboutItem(getContext()).setData();

        recyclerView= (RecyclerView) view.findViewById(R.id.AboutRecycler);
        adapter=new AboutRecyclerAdapter(getContext(), aboutItems);
        recyclerView.setAdapter(adapter);

        mlinearLayoutManagerVertical =new LinearLayoutManager(getContext());
        mlinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mlinearLayoutManagerVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());







//        TextView tvLinkedIn=(TextView)view.findViewById(R.id.tvLinkedIn);
//        TextView tvEmail= (TextView) view.findViewById(R.id.tvEmail);
//        TextView tvCall= (TextView) view.findViewById(R.id.tvPhoneNumber);
//        ImageView imgCall= (ImageView) view.findViewById(R.id.imgCall);
//        ImageView imgLinkedin= (ImageView) view.findViewById(R.id.imgLinkedIn);
//        ImageView imgEmail= (ImageView) view.findViewById(R.id.imgEmail);
//
//        tvCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "09353364377"));
//                startActivity(intent);
//            }
//        });
//
//        imgCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "09353364377"));
//                startActivity(intent);
//            }
//        });
//
//        imgEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//
//        tvEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        imgLinkedin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://hadi-eskandari-62291985"));
//                final PackageManager packageManager = getContext().getPackageManager();
//                final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                if (list.isEmpty()) {
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/hadi-eskandari-62291985/"));
//                }
//                startActivity(intent);
//            }
//        });
//
//
//        tvLinkedIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://hadi-eskandari-62291985"));
//                final PackageManager packageManager = getContext().getPackageManager();
//                final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                if (list.isEmpty()) {
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/hadi-eskandari-62291985/"));
//                }
//                startActivity(intent);
//            }
//        });



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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
