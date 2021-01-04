package com.aswamedha.aswamedhaeducation.empnewsnoticeboarddailynotes;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.aswamedha.aswamedhaeducation.adservice.AddServiceCustom;
import com.aswamedha.aswamedhaeducation.networking.Networker;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryTwoFragment extends Fragment {
    private static final String MAIN_MENU_ID = "main_menu_id";
    private RecyclerView mRecyclerViewCategoryTwo;
    public static CategoryTwoFragment getInstance( String mainMenuId ){
        Bundle bundle = new Bundle();
        bundle.putString( MAIN_MENU_ID, mainMenuId );
        CategoryTwoFragment categoryTwoFragment = new CategoryTwoFragment();
        categoryTwoFragment.setArguments( bundle );
        return categoryTwoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_two, container, false);
        mRecyclerViewCategoryTwo = view.findViewById( R.id.recy_cate_two );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getContext(),
                RecyclerView.VERTICAL, false );
        mRecyclerViewCategoryTwo.setLayoutManager( layoutManager );
        mRecyclerViewCategoryTwo.setHasFixedSize( true );
        Bundle bundle = getArguments();
        if ( bundle != null ){
            String mainMenuId = bundle.getString( MAIN_MENU_ID );
            if ( mainMenuId!= null ){
                loadData( mainMenuId );
            }
        }
        AdView adView = view.findViewById(R.id.adView);
        AddServiceCustom.getInstance().initAdd(getContext(), adView );

        return view;
    }
    private void loadData( String mainMenuId ){
        final Activity activity = getActivity();
        if ( activity!= null ){
            SharedPreferences sharedPref =
                    activity.getSharedPreferences(AswamedhamApplication.SHARED_PREF, Context.MODE_PRIVATE);
            final int regId = sharedPref.getInt( AswamedhamApplication.REG_ID, -1);
            final String token = sharedPref.getString( AswamedhamApplication.TOKEN, "");
            final JSONObject params = new JSONObject();
            try{
                params.put("token", token );
                params.put("cust_id", regId );
                params.put("main_mnu_id", mainMenuId);
            }catch ( JSONException e ){
                //Do nothing
            }
            final MyProgressDialog myProgressDialog = new MyProgressDialog( activity );
            myProgressDialog.setCancelable( false );
            myProgressDialog.setCanceledOnTouchOutside( false );
            myProgressDialog.setColor( R.color.colorAccent );
            myProgressDialog.show();
            String url =
                    AswamedhamApplication.IP_ADDRESS + "EmploymentNewsDailyNotes/getAllEmploymentDailySubMenuCustomer";
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
        if ( activity != null ){
            try{
                EmpDailyNotice empDailyNotice = new Gson().fromJson( response, EmpDailyNotice.class );
                List<CategoryTwo> categoryTwoList = empDailyNotice.getCategoryTwoList();
                CategoryTwoAdapter categoryTwoAdapter = new CategoryTwoAdapter(categoryTwoList,
                        new CategoryTwoAdapter.CategoryTwoSelector() {
                            @Override
                            public void select(CategoryTwo categoryTwo) {
                                changeFragment( categoryTwo.getId() );
                            }
                        });
                mRecyclerViewCategoryTwo.setAdapter( categoryTwoAdapter );
            }catch ( Exception e ){
                Toast.makeText( activity, "Something went wrong", Toast.LENGTH_LONG ).show();
            }
        }
    }
    private void changeFragment(String subMenuId ){
        FragmentManager fragmentManager = getFragmentManager();
        if ( fragmentManager!= null ){
            fragmentManager.beginTransaction().addToBackStack("sdf")
                    .add( R.id.frame_home, EmpDetailsFragment.getInstance( subMenuId ) ).commit();
        }
    }
}
