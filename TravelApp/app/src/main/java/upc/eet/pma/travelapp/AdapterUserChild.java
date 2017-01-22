package upc.eet.pma.travelapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import java.util.ArrayList;

public class AdapterUserChild extends BaseAdapter implements Filterable {
    protected Activity activity;
    //private Context mContext;
    protected ArrayList<UserChild> items;
    CustomFilter filter;
    ArrayList<UserChild> filterList;



    public AdapterUserChild(Activity activity, ArrayList<UserChild> items) {
        this.activity = activity;
        //this.mContext = mContext;
        this.items = items;
        this.filterList = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<UserChild> userChild) {
        for (int i = 0; i < userChild.size(); i++) {
            items.add(userChild.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_userchildclass, null);
        }

        UserChild dir = items.get(position);

        TextView email = (TextView) v.findViewById(R.id.email);
        email.setText(dir.getEmail());

        TextView Uid = (TextView) v.findViewById(R.id.Uid);
        Uid.setText(dir.getUid());



        return v;
    }

    /*@Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                myData = (List<MyDataType>) results.values;
                MyCustomAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d(Constants.TAG, "**** PERFORM FILTERING for: " + constraint);
                List<MyDataType> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    } */

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }
    //INNER CLASS
    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length()>0 ){
                //CONSTRAINT TO UPPER
                constraint = constraint.toString().toUpperCase();

                ArrayList<UserChild> filters = new ArrayList<UserChild>();

                //get specific items
                for(int i = 0; i < filterList.size();i++){
                    if (filterList.get(i).getEmail().toUpperCase().contains(constraint)){
                        UserChild p = new UserChild(filterList.get(i).getEmail(), filterList.get(i).getEmail());
                        filters.add(p);
                    }
                }

                results.count = filters.size();
                results.values = filters;

            }else {
                results.count = filterList.size();
                results.values = filterList;

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items = (ArrayList<UserChild>) results.values;
            notifyDataSetChanged();
        }
    }

}
