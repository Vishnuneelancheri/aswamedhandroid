package com.aswamedha.aswamedhapsc.empnewsnoticeboarddailynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aswamedha.aswamedhapsc.R;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailsViewAdapter extends RecyclerView.Adapter<DetailsViewAdapter.ViewHolder>{
    private List<Details> mDetailList;
    private ItemSelect mItemSelect;
    public DetailsViewAdapter(List<Details> detailsList, ItemSelect itemSelect ){
        mDetailList = detailsList;
        mItemSelect = itemSelect;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recy_emp_details , parent, false);
        return new DetailsViewAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        final Details details = mDetailList.get( pos );
        holder.txtHeader.setText( details.getHeader() );
        holder.txtDetails.setText( details.getDetails() );
        try{
            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
            Date date = simpleDateFormat.parse(details.getDateOfAdding() );

            holder.txtDate.setText( simpleDateFormat.format(date) );
        }catch ( ParseException e ){
            //Do nothing
        }


        try{
            URL url = new URL( details.getWebUrl() );
            url.toURI();
            holder.txtUrl.setText( details.getWebUrl() );
            holder.txtUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemSelect.select( details );
                }
            });
        }catch ( Exception e ){
            holder.txtUrl.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mDetailList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtHeader, txtDetails, txtUrl, txtDate;
        private CardView cardView;
        ViewHolder(View view ){
            super(view);
            txtHeader = view.findViewById( R.id.header );
            txtDetails = view.findViewById( R.id.details );
            txtUrl = view.findViewById( R.id.url );
            txtDate = view.findViewById( R.id.date );
            cardView = view.findViewById( R.id.parent );
        }
    }
    public interface ItemSelect{
        void select( Details details );
    }
}
