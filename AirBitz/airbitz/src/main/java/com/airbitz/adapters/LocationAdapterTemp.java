package com.airbitz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbitz.R;
import com.airbitz.fragments.BusinessDirectoryFragment;

import java.util.List;

/**
 * Created on 2/11/14.
 */
public class LocationAdapterTemp extends ArrayAdapter<String> implements Filterable {

    private Context mContext;
    private List<String> mLocationValue;

    public LocationAdapterTemp(Context context, List<String> locationValue){
        super(context, R.layout.item_listview_location, locationValue);
        mContext = context;
        mLocationValue = locationValue;
    }

    @Override
    public int getCount() {
        return mLocationValue.size();
    }

    @Override
    public String getItem(int position) {
        return mLocationValue.get(position);
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_listview_location, parent, false);
        RelativeLayout layoutItem = (RelativeLayout) convertView.findViewById(R.id.layout_item);
        TextView textView = (TextView) convertView.findViewById(R.id.fragment_category_textview_title);
        textView.setTypeface(BusinessDirectoryFragment.montserratBoldTypeFace);
        textView.setText(mLocationValue.get(position));

//        if(position == 0 || position == 1){
//            layoutItem.setBackgroundColor(Color.parseColor("#006699"));
//        }

        return convertView;
    }
}


