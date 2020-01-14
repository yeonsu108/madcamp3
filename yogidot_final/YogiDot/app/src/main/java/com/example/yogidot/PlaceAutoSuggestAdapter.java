package com.example.yogidot;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;


import com.example.yogidot.models.PlaceApi;

import java.util.ArrayList;

public class PlaceAutoSuggestAdapter extends ArrayAdapter implements Filterable {
    static String p_id;
    static String ad_save;

    ArrayList<placearray> results;

    int resourse;
    Context context;

    PlaceApi placeApi=new PlaceApi();

    public PlaceAutoSuggestAdapter(Context context, int resID){
        super(context,resID);
        this.context=context;
        this.resourse=resID;
    }
    @Override
    public int getCount(){
        return results.size();
    }

    @Override
    public String getItem(int pos){
        Log.e("getItem>>>>>", results.toString());

        Log.e("getItem>>>>>", Integer.toString(pos) );

        Log.e("getItem>>>>>", results.get(pos).getPlaceid());
        p_id=results.get(pos).getPlaceid();
        ad_save=results.get(pos).getAddress();
        return results.get(pos).getAddress();
    }

    @Override
    public Filter getFilter(){
        Filter filter=new Filter(){
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                final FilterResults filterResults=new FilterResults();
                    if(constraint!=null){
                        Log.e("constraint", constraint.toString());
                        results=placeApi.autoComplete(constraint.toString());

                        filterResults.values=results;
                        filterResults.count=results.size();
                    }
                    return filterResults;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results!=null&&results.count>0){
                    notifyDataSetChanged();
                }
                else{
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
