package com.aswamedha.aswamedhapsc.questions.exam;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aswamedha.aswamedhapsc.R;

import java.util.List;

public class ResultReviewAdapter extends RecyclerView.Adapter< ResultReviewAdapter.ViewHolder> {
    private List< Question > mQuestionList;
    private AdapterClick mAdapterClick;
    private Context mContext;
    public ResultReviewAdapter( Context context, List< Question > questionList, AdapterClick adapterClick ){
        mQuestionList = questionList;
        mAdapterClick = adapterClick;
        mContext      = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        View view = layoutInflater.inflate(R.layout.answer_with_details , viewGroup, false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        int pos = viewHolder.getAdapterPosition();
        final Question question = mQuestionList.get( pos );
        viewHolder.txtQuestion.setText( question.getQuestion() );
        List<Option> optionList = question.getOptionList();
        for ( Option option: optionList ){
            if ( option.getOptionTag().equals("A") ){
                viewHolder.txtAnswA.setText( "a) "+ option.getOptionValue() );
            }else if ( option.getOptionTag().equals("B") ){
                viewHolder.txtAnswB.setText( "b) "+ option.getOptionValue() );
            }else if ( option.getOptionTag().equals("C") ){
                viewHolder.txtAnswC.setText( "c) "+ option.getOptionValue() );
            }else if ( option.getOptionTag().equals("D") ){
                viewHolder.txtAnswD.setText( "d) "+ option.getOptionValue() );
            }
            if ( question.getAnswerOptionId().equals(option.getOptionId() ) ){
                Drawable drwbleCorrctAns = mContext.getResources().getDrawable( R.drawable.right_answer );
                switch ( option.getOptionTag() ){
                    case "A":
                        viewHolder.txtAnswA.setBackground( drwbleCorrctAns );
                        break;
                    case "B":
                        viewHolder.txtAnswB.setBackground( drwbleCorrctAns );
                        break;
                    case "C":
                        viewHolder.txtAnswC.setBackground( drwbleCorrctAns );
                        break;
                    case "D":
                        viewHolder.txtAnswD.setBackground( drwbleCorrctAns );
                        break;
                    default:break;
                }
            }
        }
        Option selectedOpt = question.getSelectedOption();
        if ( selectedOpt != null ){
            if ( !selectedOpt.getOptionId().equals( question.getAnswerOptionId() ) ){
                Drawable drwbleWrongAns = mContext.getResources().getDrawable( R.drawable.wrong_answer );
                switch ( selectedOpt.getOptionTag() ){
                    case "A":
                        viewHolder.txtAnswA.setBackground( drwbleWrongAns );
                        break;
                    case "B":
                        viewHolder.txtAnswB.setBackground( drwbleWrongAns);
                        break;
                    case "C":
                        viewHolder.txtAnswC.setBackground( drwbleWrongAns);
                        break;
                    case "D":
                        viewHolder.txtAnswD.setBackground( drwbleWrongAns );
                        break;
                    default:break;
                }
            }
        }
        viewHolder.btnExplanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterClick.onExplanation( question.getAnubandham() );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtQuestion, txtAnswA, txtAnswB, txtAnswC, txtAnswD;
        private Button btnExplanation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestion = itemView.findViewById( R.id.txt_question );
            txtAnswA = itemView.findViewById( R.id.txt_opt_a );
            txtAnswB = itemView.findViewById( R.id.txt_opt_b );
            txtAnswC = itemView.findViewById( R.id.txt_opt_c );
            txtAnswD = itemView.findViewById( R.id.txt_opt_d );
            btnExplanation = itemView.findViewById( R.id.btn_explanation );

        }
    }
    public interface AdapterClick{
        void onExplanation( String explanation );
    }

}
