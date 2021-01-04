package com.aswamedha.aswamedhaeducation.questions;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.MyProgressDialog;
import com.aswamedha.aswamedhaeducation.R;
import com.aswamedha.aswamedhaeducation.adservice.AddServiceCustom;
import com.aswamedha.aswamedhaeducation.networking.Networker;
import com.aswamedha.aswamedhaeducation.questions.exam.ShowQuestionFragment;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MltipleChoiceHeaderFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView txtHeader;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String MENU_1MODEL = "menu1model";
    public static MltipleChoiceHeaderFragment getInstance( Menu1Model menu1Model ){
        Bundle bundle = new Bundle();
        bundle.putParcelable( MENU_1MODEL, menu1Model );
        MltipleChoiceHeaderFragment mltipleChoiceHeaderFragment = new MltipleChoiceHeaderFragment();
        mltipleChoiceHeaderFragment.setArguments( bundle );
        return mltipleChoiceHeaderFragment;
    }
    public MltipleChoiceHeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_mltiple_choice_header, container, false);
        recyclerView = view.findViewById( R.id.recy );
        txtHeader = view.findViewById( R.id.header );
        swipeRefreshLayout = view.findViewById( R.id.swipe_refresh );
        Bundle bundle = getArguments();
        final Menu1Model menu1Model = bundle.getParcelable( MENU_1MODEL );
        if ( menu1Model != null ){
            initView( menu1Model );
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    initView( menu1Model );
                }
            });
        }
        txtHeader.setText( menu1Model.getMenu1Name() );
        recyclerView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getContext() );
        recyclerView.setLayoutManager( layoutManager );

        AdView adView = view.findViewById(R.id.adView);
        AddServiceCustom.getInstance().initAdd(getContext(), adView );

        return view;
    }
    private void initView(Menu1Model menu1Model ){
        String menu1Id = menu1Model.getMenu1Id();
        if ( menu1Id != null ){
            Activity activity = getActivity();
            if ( activity != null ){
                SharedPreferences sharedPref = activity.getSharedPreferences(AswamedhamApplication.SHARED_PREF, Context.MODE_PRIVATE);
                final int regId = sharedPref.getInt( AswamedhamApplication.REG_ID, -1);
                final String token = sharedPref.getString( AswamedhamApplication.TOKEN, "");
                final JSONObject params = new JSONObject();
                try{
                    params.put("token", token );
                    params.put("cust_id", regId );
                    params.put("fk_menu_1", menu1Id );
                }catch (JSONException e ){
                    //Do nothing
                }
                final MyProgressDialog myProgressDialog = new MyProgressDialog( activity );
                myProgressDialog.setCancelable( false );
                myProgressDialog.setCanceledOnTouchOutside( false );
                myProgressDialog.setColor( R.color.colorAccent );
                myProgressDialog.show();
                String url = AswamedhamApplication.IP_ADDRESS + "CustomerRegistration/get_all_mtlple_chce_qtn_hdr";

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
            }
        }else {
            Toast.makeText( getContext(),"An error has occured", Toast.LENGTH_SHORT ).show();
        }
    }
    private void analyzeResponse( String response ){
        try{
            MltpleChoiceHdrResponse mltpleChoiceHdrResponse = new Gson().fromJson(
                    response, MltpleChoiceHdrResponse.class
            );
            if ( mltpleChoiceHdrResponse.getStatus() > 0 ){
                MltpleChoiceHedrAdapter mltpleChoiceHedrAdapter = new MltpleChoiceHedrAdapter( mltpleChoiceHdrResponse.getMltpleChoiceHeaderList(),
                        new MltpleChoiceHedrAdapter.HeaderSelect() {
                            @Override
                            public void select( MltpleChoiceHeader mltpleChoiceHeader ) {
                                getFragmentManager().beginTransaction().add( R.id.frame_home,
                                        ShowQuestionFragment.getInstance( mltpleChoiceHeader ))
                                        .addToBackStack("f").commit();
                            }
                        }, getContext());
                recyclerView.setAdapter( mltpleChoiceHedrAdapter );
            }
        }catch ( Exception e ){

        }
    }

}
