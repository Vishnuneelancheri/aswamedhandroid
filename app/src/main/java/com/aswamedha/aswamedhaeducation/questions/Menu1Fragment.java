package com.aswamedha.aswamedhaeducation.questions;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Menu1Fragment extends Fragment {
    private static final String MAIN_MENU = "main_menu";
    private SwipeRefreshLayout swipeRefreshLayout;
    private String mainMenuId;
    private RecyclerView recyclerView;
    public Menu1Fragment() {
        // Required empty public constructor
    }

    public static Menu1Fragment getInstance( String mainMenuId ){
        Bundle bundle = new Bundle();
        bundle.putString( MAIN_MENU, mainMenuId );
        Menu1Fragment menu1Fragment = new Menu1Fragment();
        menu1Fragment.setArguments( bundle );
        return menu1Fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu1, container, false);
        view.findViewById( R.id.parent ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do nothing
            }
        });
        swipeRefreshLayout = view.findViewById( R.id.swipe_refresh );
        recyclerView = view.findViewById( R.id.recy_menu_1 );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        Bundle bundle = getArguments();
        mainMenuId = bundle.getString( MAIN_MENU );
        initView();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
            }
        });

        return view;
    }

    private void initView(){
        if ( mainMenuId == null )
            return;
        Activity activity = getActivity();
        if ( activity != null ){
            SharedPreferences sharedPref = activity.getSharedPreferences(AswamedhamApplication.SHARED_PREF, Context.MODE_PRIVATE);
            final int regId = sharedPref.getInt( AswamedhamApplication.REG_ID, -1);
            final String token = sharedPref.getString( AswamedhamApplication.TOKEN, "");
            final JSONObject params = new JSONObject();
            try{
                params.put("token", token );
                params.put("cust_id", regId );
                params.put("main_menu_id", mainMenuId );
            }catch (JSONException e ){
                //Do nothing
            }
            final MyProgressDialog myProgressDialog = new MyProgressDialog( activity );
            myProgressDialog.setCancelable( false );
            myProgressDialog.setCanceledOnTouchOutside( false );
            myProgressDialog.setColor( R.color.colorAccent );
            myProgressDialog.show();
            String url = AswamedhamApplication.IP_ADDRESS + "CustomerRegistration/get_all_menu_1";
            Networker.getInstance().posting(activity, url, params, new Networker.ResponseBridge() {
                @Override
                public void onSuccess(String response) {
                    analyzeResponse( response );
                    myProgressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing( false );
                }

                @Override
                public void onError(VolleyError error) {
                    myProgressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing( false );
                }
            });
        }else {

        }
    }
    private void analyzeResponse(String response){
        try{
            Menu1Response menu1Response = new Gson().fromJson( response, Menu1Response.class );
            if ( menu1Response.getStatus() > 0 ){
                Menu1Adapter menu1Adapter = new Menu1Adapter(menu1Response.getMenu1ModelList(), new Menu1Adapter.Menu1Selector() {
                    @Override
                    public void select(Menu1Model menu1Model) {
                        getFragmentManager().beginTransaction().add( R.id.frame_home,
                                MltipleChoiceHeaderFragment.getInstance( menu1Model ) )
                                .addToBackStack("e").commit();
                    }
                });
                recyclerView.setAdapter( menu1Adapter);
            }else {
                Toast.makeText( getContext(), menu1Response.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        }catch ( Exception e ){
            //Do nothing
        }
    }

}
