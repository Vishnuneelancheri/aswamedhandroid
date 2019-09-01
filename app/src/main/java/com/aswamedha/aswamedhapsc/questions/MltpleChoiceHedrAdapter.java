package com.aswamedha.aswamedhapsc.questions;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aswamedha.aswamedhapsc.R;

import java.util.List;

public class MltpleChoiceHedrAdapter extends RecyclerView.Adapter< MltpleChoiceHedrAdapter.ViewHolder> {
    public interface HeaderSelect{
        void select( MltpleChoiceHeader mltpleChoiceHeader );
    }
    private List<MltpleChoiceHeader> mltpleChoiceHeaders;
    private HeaderSelect headerSelect;
    public MltpleChoiceHedrAdapter(List<MltpleChoiceHeader> mltpleChoiceHeaders, HeaderSelect headerSelect ){
        this.headerSelect = headerSelect;
        this.mltpleChoiceHeaders = mltpleChoiceHeaders;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.row_main_menu , viewGroup, false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        int pos = viewHolder.getAdapterPosition();
        String slNo = ( pos +1 )+ "";
        final MltpleChoiceHeader mltpleChoiceHeader = mltpleChoiceHeaders.get( pos );
        viewHolder.txtSlNo.setText( slNo );
        viewHolder.txtHdr.setText( mltpleChoiceHeader.getHdr() );
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerSelect.select( mltpleChoiceHeader );
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mltpleChoiceHeaders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSlNo, txtHdr;
        private CardView cardView;
        public ViewHolder(View view ){
            super(view);

            txtSlNo = view.findViewById( R.id.txt_sl_no );
            txtHdr = view.findViewById( R.id.txt_main_menu );
            cardView = view.findViewById( R.id.crd );
        }
    }
}
