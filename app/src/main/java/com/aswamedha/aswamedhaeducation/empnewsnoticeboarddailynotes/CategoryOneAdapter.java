package com.aswamedha.aswamedhaeducation.empnewsnoticeboarddailynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aswamedha.aswamedhaeducation.R;

import java.util.List;

public class CategoryOneAdapter extends RecyclerView.Adapter<CategoryOneAdapter.ViewHolder> {
    private List<CategoryOne> mCategoryOneList;
    private CategoryOneSelector mCategoryOneSelector;
    public CategoryOneAdapter(List<CategoryOne> categoryOnes, CategoryOneSelector categoryOneSelector){
        mCategoryOneList = categoryOnes;
        mCategoryOneSelector = categoryOneSelector;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_main_menu , parent, false);
        return new CategoryOneAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        final CategoryOne categoryOne = mCategoryOneList.get( pos );
        holder.txtSlNo.setText( Integer.toString( ( pos + 1 ) ));
        holder.txtMainMenu.setText( categoryOne.getMenuName() );
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoryOneSelector.select( categoryOne );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryOneList.size();
    }

    public interface CategoryOneSelector{
        public void select( CategoryOne categoryOne );
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSlNo, txtMainMenu;
        private CardView cardView;
        ViewHolder(View view ){
            super(view);
            txtSlNo = view.findViewById( R.id.txt_sl_no );
            txtMainMenu = view.findViewById( R.id.txt_main_menu );
            cardView = view.findViewById( R.id.crd );
        }
    }
}
