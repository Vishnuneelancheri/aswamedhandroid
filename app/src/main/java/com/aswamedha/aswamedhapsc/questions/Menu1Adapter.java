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

public class Menu1Adapter extends RecyclerView.Adapter<Menu1Adapter.ViewHolder> {
    public interface Menu1Selector{
        void select( Menu1Model menu1Model );
    }
    private List<Menu1Model> menu1ModelList;
    private Menu1Selector menu1Selector;
    public Menu1Adapter(List<Menu1Model> menu1ModelList, Menu1Selector menu1Selector ){
        this.menu1ModelList = menu1ModelList;
        this.menu1Selector  = menu1Selector;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.row_menu1 , viewGroup, false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        int pos = viewHolder.getAdapterPosition();
        final Menu1Model menu1Model = this.menu1ModelList.get( pos );
        String slNo = ( pos + 1 )+"";
        viewHolder.txtSlNo.setText( slNo );
        String menuName = menu1Model.getMenu1Name();
        viewHolder.txtMainMenu.setText( menuName );
        /*try{
            String[] something = menuName.split("\\\\s+");
            if ( something.length > 0 )
                viewHolder.txtFirstLetter.setText( something[0] +" ");
            //viewHolder.txtMainMenu.setText( menuName.substring(1 ));
        }catch ( Exception e ){
            //Do nothing
        }*/
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu1Selector.select( menu1Model );
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.menu1ModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSlNo, txtMainMenu, txtFirstLetter;
        private CardView cardView;
        public ViewHolder(View view ){
            super(view);
            txtSlNo = view.findViewById( R.id.txt_sl_no );
            txtMainMenu = view.findViewById( R.id.txt_main_menu );
            cardView = view.findViewById( R.id.crd );
            txtFirstLetter = view.findViewById( R.id.txt_first_letter );
        }
    }
}
