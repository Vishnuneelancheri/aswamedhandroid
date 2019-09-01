package com.aswamedha.aswamedhapsc.questions;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aswamedha.aswamedhapsc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonateMeFragment extends Fragment {


    public DonateMeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_donate_me, container, false );
    }

}
