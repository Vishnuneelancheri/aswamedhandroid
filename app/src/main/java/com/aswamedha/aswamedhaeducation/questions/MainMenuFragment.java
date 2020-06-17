package com.aswamedha.aswamedhaeducation.questions;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.MyProgressDialog;
import com.aswamedha.aswamedhaeducation.R;
import com.aswamedha.aswamedhaeducation.empnewsnoticeboarddailynotes.CategoryOneFragment;
import com.aswamedha.aswamedhaeducation.networking.Networker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**card_job_search
 * A simple {@link Fragment} subclass.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyMainMenu;

    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        view.findViewById( R.id.parent ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do nothing
            }
        });
        recyMainMenu = view.findViewById( R.id.recy_main_menu );
        /*RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getContext(),
                RecyclerView.HORIZONTAL, false);*/
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager( getContext(), 3);
        recyMainMenu.setLayoutManager( layoutManager );
        recyMainMenu.setHasFixedSize( true );
        loadData();
        view.findViewById( R.id.card_job_search ).setOnClickListener( this  );
        view.findViewById( R.id.card_daily_notes ).setOnClickListener( this );
        view.findViewById( R.id.current_affairs ).setOnClickListener( this );
        return view;
    }

    private void loadData(){
        Activity activity = getActivity();
        if ( activity!= null ){

            SharedPreferences sharedPref = activity.getSharedPreferences(AswamedhamApplication.SHARED_PREF, Context.MODE_PRIVATE);
            final int regId = sharedPref.getInt( AswamedhamApplication.REG_ID, -1);
            final String token = sharedPref.getString( AswamedhamApplication.TOKEN, "");

            final JSONObject params = new JSONObject();
            try{
                params.put("token", token );
                params.put("cust_id", regId );
            }catch (JSONException e ){
                //Do nothing
            }
            Log.d("tokencustid", params.toString() );
            final MyProgressDialog myProgressDialog = new MyProgressDialog( activity );
            myProgressDialog.setCancelable( false );
            myProgressDialog.setCanceledOnTouchOutside( false );
            myProgressDialog.setColor( R.color.colorAccent );
            myProgressDialog.show();
            String url = AswamedhamApplication.IP_ADDRESS + "CustomerRegistration/get_all_main_menu";
            Networker.getInstance().posting(activity, url, params, new Networker.ResponseBridge() {
                @Override
                public void onSuccess(String response) {
                    Log.d("mainmenuresp", response );
                    analyzeResponse( response );
                    myProgressDialog.dismiss();
                }

                @Override
                public void onError(VolleyError error) {
                    myProgressDialog.dismiss();
                }
            });

        }
    }
    private void analyzeResponse( String response ){
        Context context = getContext();
        if ( context!= null ){
            try {
                MainMenuResponse mainMenuResponse = new Gson().fromJson( response, MainMenuResponse.class );
                if ( mainMenuResponse.getStatus() > 0 ){
                    MainMenuAdapter mainMenuAdapter = new MainMenuAdapter(mainMenuResponse.getMainMenuModelList(), new MainMenuAdapter.ItemSelected() {
                        @Override
                        public void select(MainMenuModel mainMenuModel) {
                            getFragmentManager().beginTransaction().add( R.id.frame_home,
                                    Menu1Fragment.getInstance( Integer.toString( mainMenuModel.getMainMenuId() ) ) ).addToBackStack("d").commit();
                        }
                    });
                    recyMainMenu.setAdapter( mainMenuAdapter );
                }else {
                    Toast.makeText( context, mainMenuResponse.getMessage(), Toast.LENGTH_SHORT ).show();
                }
            }catch ( Exception e){
                Toast.makeText( getContext(), "Unexpected error occurred", Toast.LENGTH_LONG ).show();
            }
        }

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if ( id == R.id.card_job_search ){
            goToCategoryOneFragment("1");
        }else if ( id == R.id.card_daily_notes ){
            goToCategoryOneFragment("2");
        }else if ( id == R.id.current_affairs ){
            goToCategoryOneFragment("3");
        }
    }
    private void goToCategoryOneFragment(String category){
        FragmentManager fragmentManager = getFragmentManager();
        if ( fragmentManager != null ){
            fragmentManager.
                    beginTransaction().add( R.id.frame_home, CategoryOneFragment.getInstance(category))
                    .addToBackStack("dl").commit();
        }
    }
}
