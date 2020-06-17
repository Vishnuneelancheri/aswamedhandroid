package com.aswamedha.aswamedhaeducation.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.aswamedha.aswamedhaeducation.R;

public class MyCustomPagerAdapter extends PagerAdapter {
    private Context mContext;
    private int[] mImages;
    private LayoutInflater mLayoutInflater;
    public MyCustomPagerAdapter(Context context, int images[] ){
        mContext = context;
        mImages = images;
        mLayoutInflater = ( LayoutInflater ) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }
    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.view_pager_item, container, false);

        ImageView imageView =   itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mImages[position]);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
