package com.aswamedha.aswamedhapsc.questions.exam;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aswamedha.aswamedhapsc.AswamedhamApplication;
import com.aswamedha.aswamedhapsc.MyProgressDialog;
import com.aswamedha.aswamedhapsc.R;
import com.aswamedha.aswamedhapsc.networking.Networker;
import com.aswamedha.aswamedhapsc.questions.MltpleChoiceHeader;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowQuestionFragment extends Fragment implements View.OnClickListener {
    private static final String HDR = "hdr";
    private boolean isFinished = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList< Question > questionList = new ArrayList<>();
    private TextView txtQtn, txtOpta, txtOptb, txtOptc, txtOptd, txtCountDown, txtAnubandham, txtQtnNo;
    private Button  btnFinish;
    private ImageButton btnPrev, btnNxt;
    private SwipeRefreshLayout constraintLayout ;
    private int currentPos = 0;
    public ShowQuestionFragment() {
        // Required empty public constructor
    }

    public static ShowQuestionFragment getInstance(MltpleChoiceHeader mltpleChoiceHeader){
        Bundle bundle = new Bundle();
        bundle.putParcelable( HDR, mltpleChoiceHeader );
        ShowQuestionFragment showQuestionFragment = new ShowQuestionFragment();
        showQuestionFragment.setArguments( bundle );
        return showQuestionFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_question, container, false);
//        swipeRefreshLayout = view.findViewById( R.id.swipe_refresh );
        constraintLayout = view.findViewById( R.id.constraint );
        constraintLayout.setOnClickListener( this );
        txtCountDown = view.findViewById( R.id.txt_timer );
        Bundle bundle = getArguments();
        final MltpleChoiceHeader mltpleChoiceHeader = bundle.getParcelable( HDR );
        if ( mltpleChoiceHeader != null ){
            if ( mltpleChoiceHeader.getHdrId() > 0 ){
                init( mltpleChoiceHeader );
                /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        init( mltpleChoiceHeader );
                    }
                });*/
            }
        }
        txtQtn = view.findViewById( R.id.txt_qtn );
        txtQtnNo = view.findViewById( R.id.txt_qtn_number );
        txtOpta = view.findViewById( R.id.txt_opt_a );
        txtOptb = view.findViewById( R.id.txt_opt_b );
        txtOptc = view.findViewById( R.id.txt_opt_c );
        txtOptd = view.findViewById( R.id.txt_opt_d );
        btnNxt = view.findViewById( R.id.btn_next );
        btnPrev = view.findViewById( R.id.btn_prev );
        btnFinish = view.findViewById( R.id.btn_finish );

        txtOpta.setOnClickListener( this );
        txtOptb.setOnClickListener( this );
        txtOptc.setOnClickListener( this );
        txtOptd.setOnClickListener( this );
        btnNxt.setOnClickListener( this );
        btnPrev.setOnClickListener( this );
        btnFinish.setOnClickListener( this );

        return view;
    }
    private void init(MltpleChoiceHeader mltpleChoiceHeader ){
        Activity activity = getActivity();
        if ( activity!= null ){
            SharedPreferences sharedPref = activity.getSharedPreferences(AswamedhamApplication.SHARED_PREF, Context.MODE_PRIVATE);
            final int regId = sharedPref.getInt( AswamedhamApplication.REG_ID, -1);
            final String token = sharedPref.getString( AswamedhamApplication.TOKEN, "");
            final JSONObject params = new JSONObject();
            try{
                params.put("token", token );
                params.put("cust_id", regId );
                params.put("hdr_id", mltpleChoiceHeader.getHdrId() );
            }catch (JSONException e ){
                //Do nothing
            }
            final MyProgressDialog myProgressDialog = new MyProgressDialog( activity );
            myProgressDialog.setCancelable( false );
            myProgressDialog.setCanceledOnTouchOutside( false );
            myProgressDialog.setColor( R.color.colorAccent );
            myProgressDialog.show();
            String url = AswamedhamApplication.IP_ADDRESS + "CustomerRegistration/get_mlple_chce_qtn";
            Networker.getInstance().posting(activity, url, params, new Networker.ResponseBridge() {
                @Override
                public void onSuccess(String response) {
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
        QuestionResponse qustionResponse =  new Gson().fromJson( response, QuestionResponse.class );
        if ( qustionResponse.getQustionResponseSingleItems().size() > 0 ){
            List<String> idList = new ArrayList<>();
            for (QustionResponseSingleItem qustionResponseSingleItem : qustionResponse.getQustionResponseSingleItems()
                 ) {
                idList.add( qustionResponseSingleItem.getQtnid() );
            }
            Set<String> set = new HashSet<>(idList);
            idList.clear();
            idList.addAll( set );

            for (String id: idList ){
                String prevoisId = "";
                for ( QustionResponseSingleItem qustionResponseSingleItem : qustionResponse.getQustionResponseSingleItems()){
                    Question question = new Question();
                    if ( id.equals(qustionResponseSingleItem.getQtnid() )  ){
                        if ( !id.equals(prevoisId)){
                            question.setQuestId(id);
                            question.setQuestion( qustionResponseSingleItem.getQtnName() );
                            question.setAnswerOptionId( qustionResponseSingleItem.getAnswerOptionId() );
                            question.setAnubandham( qustionResponseSingleItem.getAnumbandham() );
                            List<Option> optionList = new ArrayList<>();
                            for ( QustionResponseSingleItem optionRes: qustionResponse.getQustionResponseSingleItems() ){
                                if ( id.equals( optionRes.getQtnid() ) ){
                                    Option option = new Option();
                                    option.setOptionId( optionRes.getOptionId() );
                                    option.setOptionTag( optionRes.getOptionTag() );
                                    option.setOptionValue( optionRes.getOptionValue() );
                                    optionList.add( option );
                                }
                            }
                            question.setOptionList( optionList );
                            questionList.add( question );
                        }
                        prevoisId = id;
                    }
                }
            }
            if (questionList.size()> 0 ){
                initView(currentPos);
                countDown( questionList.size()* 60 * 1000 );
            }else {
                Toast.makeText( getContext(), "No questions for you", Toast.LENGTH_SHORT).show();
            }
        }else Toast.makeText( getContext(), "No questions found ", Toast.LENGTH_SHORT ).show();
    }
    private void initView( int pos){
            try{

                btnNxt.setVisibility( View.VISIBLE );
                btnPrev.setVisibility( View.VISIBLE );
                if ( currentPos == 0 ){
                    btnPrev.setVisibility( View.INVISIBLE );
                }
                String questionNumber =  ( currentPos+1 ) + "/" + questionList.size();
                txtQtnNo.setText( questionNumber );
                Question question = questionList.get(pos);
                if ( question.getSelectedOption() == null ){
                    colorChange( null);
                }else {
                    Option selected = question.getSelectedOption();
                    if ( selected.getOptionTag().equals("A") ){
                        colorChange( txtOpta );
                    }else if ( selected.getOptionTag().equals("B") ){
                        colorChange( txtOptb );
                    }else if ( selected.getOptionTag().equals("C") ){
                        colorChange( txtOptc );
                    }else if ( selected.getOptionTag().equals("D") ){
                        colorChange( txtOptd );
                    }
                }
                String qtnName = ( currentPos + 1 )+") " + questionList.get( pos ).getQuestion();
                txtQtn.setText( qtnName );
                for ( Option option: question.getOptionList() ){
                    if ( option.getOptionTag().equals("A") ){
                        txtOpta.setText("a) "+option.getOptionValue());
                    }else if ( option.getOptionTag().equals("B")){
                        txtOptb.setText("b) "+ option.getOptionValue() );
                    }else if ( option.getOptionTag().equals("C")){
                        txtOptc.setText( "c) "+option.getOptionValue() );
                    }else if ( option.getOptionTag().equals("D")){
                        txtOptd.setText( "d) "+option.getOptionValue() );
                    }
                }
                if ( currentPos == questionList.size()-1 ){
                    btnNxt.setVisibility( View.INVISIBLE );
                }else if ( currentPos == 0 ){
                    btnPrev.setVisibility( View.INVISIBLE );
                }
            }catch ( Exception e){
                //Do nothing
            }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch ( id ){
            case R.id.btn_next:{
                if ( currentPos <= questionList.size()-2 ){
                    currentPos +=1;
                    initView( currentPos );
                }
            }
            break;
            case R.id.btn_prev:{
                if ( currentPos > 0 ){
                    currentPos -= 1;
                    initView( currentPos );
                }
            }
            break;
            case R.id.txt_opt_a:{
                if ( isFinished )return;
                questionList.get( currentPos ).setSelectedOption( questionList.get(currentPos).getOptionList().get(0));
                colorChange( txtOpta );
            }
            break;
            case R.id.txt_opt_b:{
                if ( isFinished )return;
                questionList.get( currentPos ).setSelectedOption( questionList.get(currentPos).getOptionList().get(1));
                colorChange( txtOptb );
            }
            break;
            case R.id.txt_opt_c:{
                if ( isFinished )return;
                questionList.get( currentPos ).setSelectedOption( questionList.get(currentPos).getOptionList().get(2));
                colorChange( txtOptc );
            }
            break;
            case R.id.txt_opt_d:{
                if ( isFinished )return;
                questionList.get( currentPos ).setSelectedOption( questionList.get(currentPos).getOptionList().get(3));
                colorChange( txtOptd );
            }
            break;
            case R.id.btn_finish:
                finishExam();
                break;


        }
    }

    private void colorChange( TextView textView ){
        if ( isFinished ){
            showAnswer();
            return;
        }
        txtOpta.setBackground( getResources().getDrawable( R.drawable.answer_ord ));
        txtOptb.setBackground( getResources().getDrawable( R.drawable.answer_ord ));
        txtOptc.setBackground( getResources().getDrawable( R.drawable.answer_ord ));
        txtOptd.setBackground( getResources().getDrawable( R.drawable.answer_ord ));

        txtOpta.setTextColor( getResources().getColor( R.color.black ));
        txtOptb.setTextColor( getResources().getColor( R.color.black ));
        txtOptc.setTextColor( getResources().getColor( R.color.black ));
        txtOptd.setTextColor( getResources().getColor( R.color.black ));
        if ( textView != null ){
            textView.setBackground( getResources().getDrawable( R.drawable.answer_select ));
            textView.setTextColor( getResources().getColor( R.color.white ));
        }

    }
    private void showAnswer(){
        txtOpta.setBackground( getResources().getDrawable( R.drawable.answer_ord ));
        txtOptb.setBackground( getResources().getDrawable( R.drawable.answer_ord ));
        txtOptc.setBackground( getResources().getDrawable( R.drawable.answer_ord ));
        txtOptd.setBackground( getResources().getDrawable( R.drawable.answer_ord ));

        Question question = questionList.get( currentPos );
        Option selectedOpt = question.getSelectedOption();
        if ( selectedOpt != null ){
            if ( selectedOpt.getOptionId().equals( question.getAnswerOptionId() ) ){
                switch ( selectedOpt.getOptionTag() ){
                    case "A":
                        txtOpta.setBackground( getResources().getDrawable( R.drawable.right_answer ));
                        break;
                    case "B":
                        txtOptb.setBackground( getResources().getDrawable( R.drawable.right_answer ));
                        break;
                    case "C":
                        txtOptc.setBackground( getResources().getDrawable( R.drawable.right_answer ));
                        break;
                    case "D":
                        txtOptd.setBackground( getResources().getDrawable( R.drawable.right_answer ));
                        break;
                }
            }else {
                switch ( selectedOpt.getOptionTag() ){
                    case "A":
                        txtOpta.setBackground( getResources().getDrawable( R.drawable.wrong_answer ));
                        break;
                    case "B":
                        txtOptb.setBackground( getResources().getDrawable( R.drawable.wrong_answer ));
                        break;
                    case "C":
                        txtOptc.setBackground( getResources().getDrawable( R.drawable.wrong_answer ));
                        break;
                    case "D":
                        txtOptd.setBackground( getResources().getDrawable( R.drawable.wrong_answer ));
                        break;
                }
                List<Option> optionList = question.getOptionList();
                for (Option option: optionList ) {
                    if ( option.getOptionId().equals( question.getAnswerOptionId() ) ){
                        switch ( option.getOptionTag() ){
                            case "A":
                                txtOpta.setBackground( getResources().getDrawable( R.drawable.right_answer ));
                                break;
                            case "B":
                                txtOptb.setBackground( getResources().getDrawable( R.drawable.right_answer ));
                                break;
                            case "C":
                                txtOptc.setBackground( getResources().getDrawable( R.drawable.right_answer ));
                                break;
                            case "D":
                                txtOptd.setBackground( getResources().getDrawable( R.drawable.right_answer ));
                                break;
                        }
                        break;
                    }
                }
            }
        }
    }
    private void countDown( long sec){
        new CountDownTimer(sec, 1000) {

            public void onTick(long millisUntilFinished) {
                long currentMilliSec = millisUntilFinished / 1000;
                long reminder = currentMilliSec % 60;
                long min =  currentMilliSec/ 60;
                String time = "Time remaining: "  + min + ":"+ reminder;

                txtCountDown.setText(Html.fromHtml( "<b>Time remaining: <font color='red'>"  + min + ":"+ reminder +"</font></b>" ));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                finishExam();
                //txtCountDown.setText("done!");
            }

        }.start();
    }
    private void finishExam(){
        isFinished = true;
        currentPos = 0;
        initView(currentPos);
        //txtAnubandham.setText("Anubandham");
        int totalCorrect = 0;
        int totalWrong = 0;
        int notAnswered = 0;
        for (Question qu : questionList ) {
            Option selctdOpt = qu.getSelectedOption();
            if ( selctdOpt != null ){
                String selectedOptionId = selctdOpt.getOptionId();
                if ( selectedOptionId.equals( qu.getAnswerOptionId() )){
                    totalCorrect += 1;
                }else {
                    totalWrong += 1;
                }
            }else {
                notAnswered += 0;
            }

        }
        String message = "Correct: "+ totalCorrect +"\nWrong:   " + totalWrong;
        final int correctAnswer = totalCorrect;
        final int wrongAnswer = totalWrong;
        Activity activity = getActivity();
        if ( activity == null ) return;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( activity );
        alertDialogBuilder.setTitle("Result");
        alertDialogBuilder.setMessage( message );
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        getFragmentManager().beginTransaction().add( R.id.frame_home,
                                 ShowResultFragment.getInstance( correctAnswer, wrongAnswer, questionList.size(), questionList ) )
                                .addToBackStack("e").commit();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
//199532122
