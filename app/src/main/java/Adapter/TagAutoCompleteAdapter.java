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

import Model.DiscussionTags;

public class TagAutoCompleteAdapter extends ArrayAdapter<DiscussionTags> {

    private ArrayList<DiscussionTags> items;
    private ArrayList<DiscussionTags> itemsAll;
    private ArrayList<DiscussionTags> suggestions;
    private int viewResourceId;

    public TagAutoCompleteAdapter(Context context, int viewResourceId, ArrayList<DiscussionTags> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<DiscussionTags>) items.clone();
        this.suggestions = new ArrayList<DiscussionTags>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        DiscussionTags customer = items.get(position);
        if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.tv_tag);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView DiscussionTags Name:"+customer.getName());
                customerNameLabel.setText(customer.getTitle());
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
            String str = ((DiscussionTags) (resultValue)).getTitle();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DiscussionTags customer : itemsAll) {
                    if (customer.getTitle().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }
                Filter.FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<DiscussionTags> filteredList = (ArrayList<DiscussionTags>) results.values;
            if (results != null && results.count > 0) {
                clear();
                try {
                    for (DiscussionTags c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        }
    };

}