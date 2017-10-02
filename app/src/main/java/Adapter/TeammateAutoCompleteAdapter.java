package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.cli.knowledgebase.R;

import java.util.ArrayList;

import Model.TeammatesData;

public class TeammateAutoCompleteAdapter extends ArrayAdapter<TeammatesData> {

    private ArrayList<TeammatesData> items;
    private ArrayList<TeammatesData> itemsAll;
    private ArrayList<TeammatesData> suggestions;
    private int viewResourceId;

    public TeammateAutoCompleteAdapter(Context context, int viewResourceId, ArrayList<TeammatesData> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<TeammatesData>) items.clone();
        this.suggestions = new ArrayList<TeammatesData>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        TeammatesData customer = items.get(position);
        if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.tv_tag);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView TeammatesData Name:"+customer.getName());
                customerNameLabel.setText(customer.getFirstname() +" "+customer.getLastname());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((TeammatesData) (resultValue)).getFirstname()+" "+((TeammatesData) (resultValue)).getLastname();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (TeammatesData customer : itemsAll) {
                    if (customer.getFirstname().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<TeammatesData> filteredList = (ArrayList<TeammatesData>) results.values;
            if (results != null && results.count > 0) {
                clear();
                try {
                    for (TeammatesData c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        }
    };

}