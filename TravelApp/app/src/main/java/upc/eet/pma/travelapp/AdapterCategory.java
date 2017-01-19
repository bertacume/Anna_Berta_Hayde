package upc.eet.pma.travelapp;

import android.app.Activity;
import android.content.Context;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.firebase.client.authentication.Constants;

import java.util.ArrayList;

public class AdapterCategory extends BaseAdapter  {
    protected Activity activity;
    //private Context mContext;
    protected ArrayList<Category> items;



    public AdapterCategory (Activity activity, ArrayList<Category> items) {
        this.activity = activity;
        //this.mContext = mContext;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Category> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
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
            v = inf.inflate(R.layout.item_category, null);
        }

        Category dir = items.get(position);

        TextView title = (TextView) v.findViewById(R.id.description);
        title.setText(dir.getName());

        TextView description = (TextView) v.findViewById(R.id.name);
        description.setText(dir.getDescription());



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


}
