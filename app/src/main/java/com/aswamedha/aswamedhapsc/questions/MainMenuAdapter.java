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

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {
    private List<MainMenuModel> mMainMenulist;
    private ItemSelected itemSelected;
    public MainMenuAdapter(List<MainMenuModel> mainMenulist, ItemSelected itemSelected ){
        mMainMenulist = mainMenulist;
        this.itemSelected = itemSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.row_main_menu_hori , viewGroup, false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        int pos = viewHolder.getAdapterPosition();
        final MainMenuModel mainMenu = mMainMenulist.get( pos );
        String slNo = ( pos +1 )+ "";
//        viewHolder.txtSlNo.setText( slNo );
        viewHolder.txtMainMenu.setText( mainMenu.getMenuName() );
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelected.select( mainMenu );
            }
        });
    }



    @Override
    public int getItemCount() {
        return mMainMenulist.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSlNo, txtMainMenu;
        private CardView cardView;
        public ViewHolder(View view ){
            super(view);

            txtSlNo = view.findViewById( R.id.txt_sl_no );
            txtMainMenu = view.findViewById( R.id.txt_main_menu );
            cardView = view.findViewById( R.id.crd );
        }
    }
    public interface ItemSelected{
        void select( MainMenuModel mainMenuModel );
    }
}
