package com.aswamedha.aswamedhaeducation.questions.exam;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aswamedha.aswamedhaeducation.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowResultFragment extends Fragment implements View.OnClickListener {
    public static final String CORRECT_ANSWER   =   "CORRECT_ANSWER";
    public static final String WRONG_ANSWER     =   "WRONG_ANSWER";
    public static final String TOTAL_QTN =   "TOTAL_QTN";
    public static final String QUESTION = "QUESTION";
    private PieChartView pieChartView;
    private RecyclerView recyclerShowResult;
    public ShowResultFragment() {
        // Required empty public constructor
    }

    public static ShowResultFragment getInstance( int correctAnswer, int wrongAnwer, int totalQuestion, ArrayList<Question>  questionsList ){
        Bundle bundle = new Bundle();
        bundle.putInt( CORRECT_ANSWER, correctAnswer );
        bundle.putInt( WRONG_ANSWER, wrongAnwer );
        bundle.putInt(TOTAL_QTN, totalQuestion );
        bundle.putParcelableArrayList(QUESTION, questionsList);
        ShowResultFragment showResultFragment = new ShowResultFragment();
        showResultFragment.setArguments( bundle );
        return showResultFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();



        View view = inflater.inflate( R.layout.fragment_show_result, container, false );
        pieChartView = view.findViewById( R.id.chart_view );
        recyclerShowResult = view.findViewById( R.id.recy_answer );
        /* https://www.codingdemos.com/android-pie-chart-tutorial/ */
        view.findViewById( R.id.parent ).setOnClickListener( this );
        if ( bundle != null ){
            int correctAnser = bundle.getInt( CORRECT_ANSWER );
            int wrongAnswer = bundle.getInt( WRONG_ANSWER );
            ArrayList<Question> questionList = bundle.getParcelableArrayList( QUESTION );
            int totalQtn = bundle.getInt( TOTAL_QTN );
            if (  correctAnser + wrongAnswer <= totalQtn ){
                int correctPercent = (int)( correctAnser*100/ totalQtn );
                int wrongPercent = (int)( wrongAnswer*100/ totalQtn ) ;
                List< SliceValue > pieData = new ArrayList<>();
                pieData.add( new SliceValue(  correctPercent, Color.GREEN));
                pieData.add( new SliceValue(   wrongPercent, Color.RED ));
                pieData.add( new SliceValue(    Math.abs( 100- (correctPercent + wrongPercent) ), Color.BLUE ));
                PieChartData pieChartData = new PieChartData( pieData );
                pieChartView.setPieChartData( pieChartData );
                recyclerShowResult.setHasFixedSize( true );
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getContext() );
                recyclerShowResult.setLayoutManager( layoutManager );
                if ( questionList != null ){
                    ResultReviewAdapter resultReviewAdapter = new ResultReviewAdapter( getContext(),questionList, new ResultReviewAdapter.AdapterClick() {
                        @Override
                        public void onExplanation(String explanation) {
                            showExplanation( explanation );
                        }
                    });
                    recyclerShowResult.setAdapter( resultReviewAdapter );
                }

            }else {
                pieChartView.setVisibility( View.GONE );
            }

        }

        return view;
    }
    private void showExplanation( String explanation ){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( getActivity() );
        alertDialogBuilder.setTitle("Explanation");
        alertDialogBuilder.setMessage( explanation );
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if ( id == R.id.parent ){
            //do nothing
        }
    }
}
