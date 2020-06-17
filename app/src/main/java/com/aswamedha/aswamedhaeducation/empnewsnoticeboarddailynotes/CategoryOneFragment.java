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
import com.aswamedha.aswamedhaeducation.networking.Networker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryOneFragment extends Fragment {
    private RecyclerView mRecyclerViewCategoryOne;
    private static final String CAT  = "Category";
    public CategoryOneFragment() {
        // Required empty public constructor
    }
    public static CategoryOneFragment getInstance( String category ){
        Bundle bundle = new Bundle();
        bundle.putString( CAT, category );
        CategoryOneFragment categoryOneFragment =  new CategoryOneFragment();
        categoryOneFragment.setArguments( bundle );
        return categoryOneFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category_one, container, false);

        mRecyclerViewCategoryOne = view.findViewById( R.id.recy_cate_one );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getContext(),
                RecyclerView.VERTICAL, false );
        mRecyclerViewCategoryOne.setLayoutManager( layoutManager );
        mRecyclerViewCategoryOne.setHasFixedSize( true );
        Bundle bundle = getArguments();
        if ( bundle!= null ){
            String category = getArguments().getString( CAT );
            if ( category != null && !category.isEmpty() )
                loadCategory( category );
        }
        view.findViewById( R.id.parent ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do nothing
            }
        });
        return view;
    }
    private void loadCategory( String category ){
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
                params.put("type", category);
            }catch ( JSONException e ){
                //Do nothing
            }
            final MyProgressDialog myProgressDialog = new MyProgressDialog( activity );
            myProgressDialog.setCancelable( false );
            myProgressDialog.setCanceledOnTouchOutside( false );
            myProgressDialog.setColor( R.color.colorAccent );
            myProgressDialog.show();
            String url = AswamedhamApplication.IP_ADDRESS + "EmploymentNewsDailyNotes/getAllEmploymentDailyMainMenuCustomer";
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
                if ( empDailyNotice.getStatus().getStatus() > 0 ){
                    List< CategoryOne > categoryOneList = empDailyNotice.getCategoryOneList();
                    CategoryOneAdapter categoryOneAdapter = new CategoryOneAdapter(categoryOneList, new CategoryOneAdapter.CategoryOneSelector() {
                        @Override
                        public void select(CategoryOne categoryOne) {
                            FragmentManager fragmentManager = getFragmentManager();
                            if ( fragmentManager!= null ){
                                fragmentManager.beginTransaction().addToBackStack("sofa")
                                        .add(  R.id.frame_home,CategoryTwoFragment.getInstance( categoryOne.getId())).commit();
                            }
                        }
                    });
                    mRecyclerViewCategoryOne.setAdapter( categoryOneAdapter );
                }else
                    Toast.makeText( activity, empDailyNotice.getStatus().getMessage(), Toast.LENGTH_SHORT ).show();
            }catch ( Exception e ){
                Toast.makeText( activity, "Something went wrong", Toast.LENGTH_LONG ).show();
            }
        }

    }

}
