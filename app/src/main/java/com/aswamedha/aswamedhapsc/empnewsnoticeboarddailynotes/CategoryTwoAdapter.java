package com.aswamedha.aswamedhapsc.empnewsnoticeboarddailynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aswamedha.aswamedhapsc.R;

import java.util.List;

public class CategoryTwoAdapter extends RecyclerView.Adapter<CategoryTwoAdapter.ViewHolder>{
    private List< CategoryTwo > mCategoryTwoList;
    private CategoryTwoSelector mCategoryTwoSelector;
    public interface CategoryTwoSelector{
        void select( CategoryTwo categoryTwo );
    }
    CategoryTwoAdapter( List<CategoryTwo> categoryTwoList, CategoryTwoSelector categoryTwoSelector ){
        mCategoryTwoList = categoryTwoList;
        mCategoryTwoSelector = categoryTwoSelector;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_main_menu , parent, false);
        return new CategoryTwoAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        final CategoryTwo categoryTwo = mCategoryTwoList.get( pos );
        holder.txtMainMenu.setText( categoryTwo.getSubMenuName() );
        holder.txtSlNo.setText( Integer.toString( pos + 1 ) );
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoryTwoSelector.select( categoryTwo );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryTwoList.size();
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
