package com.aswamedha.aswamedhaeducation.empnewsnoticeboarddailynotes;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.MyProgressDialog;
import com.aswamedha.aswamedhaeducation.R;
import com.aswamedha.aswamedhaeducation.networking.Networker;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmpDetailsFragment extends Fragment {
    private static final String SUBMENUID = "submenuId";
    private RecyclerView mRecyclerView;
    public static EmpDetailsFragment getInstance(String subMenuId ){
        EmpDetailsFragment empDetailsFragment = new EmpDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString( SUBMENUID, subMenuId );
        empDetailsFragment.setArguments( bundle );
        return empDetailsFragment;
    }
    public EmpDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emp_details, container, false);
        mRecyclerView = view.findViewById( R.id.recy_details );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getContext(), RecyclerView.VERTICAL, false );
        mRecyclerView.setLayoutManager( layoutManager );
        mRecyclerView.setHasFixedSize( true );
        Bundle bundle = getArguments();
        String submenuId = bundle.getString( SUBMENUID );
        if ( submenuId!= null ){
            getDetailsList( submenuId );
        }
        view.findViewById( R.id.parent ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
    private void getDetailsList( String subMenuId ){
        final Activity activity = getActivity();
        if ( activity != null ){
            SharedPreferences sharedPref =
                    activity.getSharedPreferences(AswamedhamApplication.SHARED_PREF, Context.MODE_PRIVATE);
            final int regId = sharedPref.getInt( AswamedhamApplication.REG_ID, -1);
            final String token = sharedPref.getString( AswamedhamApplication.TOKEN, "");
            final JSONObject params = new JSONObject();

            try {
                params.put("token", token );
                params.put("cust_id", regId );
                params.put("sub_mnu_id", subMenuId);
            }catch ( Exception e ){
                //Do nothing
            }
            final MyProgressDialog myProgressDialog = new MyProgressDialog( activity );
            myProgressDialog.setCancelable( false );
            myProgressDialog.setCanceledOnTouchOutside( false );
            myProgressDialog.setColor( R.color.colorAccent );
            myProgressDialog.show();
            String url = AswamedhamApplication.IP_ADDRESS +
                        "EmploymentNewsDailyNotes/getAllEmploymentDailyDetailsCustomer";
            Networker.getInstance().posting(activity, url, params, new Networker.ResponseBridge() {
                @Override
                public void onSuccess(String response) {
                    analyzeResponse( response );
                    myProgressDialog.dismiss();
                }

                @Override
                public void onError(VolleyError error) {
                    myProgressDialog.dismiss();
                    Toast.makeText( activity,"Some error occurred", Toast.LENGTH_LONG ).show();
                }
            });
        }
    }
    private void analyzeResponse( String response ){
        Activity activity = getActivity();
        if ( activity!= null ){
            try{
                EmpDailyNotice empDailyNotice = new Gson().fromJson( response, EmpDailyNotice.class );
                if ( empDailyNotice.getStatus().getStatus() > 0 ){
                    List<Details> detailsList = empDailyNotice.getDetailsList();
                    DetailsViewAdapter detailsViewAdapter = new DetailsViewAdapter(detailsList, new DetailsViewAdapter.ItemSelect() {
                        @Override
                        public void select(Details details) {
                            Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( details.getWebUrl() ));
                            startActivity( browserIntent );
                        }
                    });
                    mRecyclerView.setAdapter( detailsViewAdapter );
                }else {
                    Toast.makeText( activity, empDailyNotice.getStatus().getMessage(), Toast.LENGTH_LONG ).show();
                }
            }catch ( Exception e ){
                //Do nothing
            }
        }
    }

}
