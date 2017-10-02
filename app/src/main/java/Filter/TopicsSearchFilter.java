package Filter;

import android.widget.Filter;

import java.util.ArrayList;

import Adapter.TopicsListAdapter;
import Model.ScormData;

/**
 * Created by ganesha on 24/10/16.
 */


public class TopicsSearchFilter extends Filter {

    public ArrayList<ScormData> list;
    private ArrayList<ScormData> filteredlist;
    private TopicsListAdapter adapter;

    public TopicsSearchFilter(ArrayList<ScormData> list, TopicsListAdapter adapter) {
        this.adapter = adapter;
        this.list = list;
        this.filteredlist = new ArrayList();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredlist.clear();
        final FilterResults results = new FilterResults();

        //here you need to add proper items do filteredlist
        for (final ScormData item : list) {
            if (item.getChapterTitle().equals(constraint.toString())) {
                filteredlist.add(item);
            }
        }

        results.values = filteredlist;
        results.count = filteredlist.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setList(filteredlist);
        adapter.notifyDataSetChanged();
    }


    public ArrayList<ScormData> getlist() {

        return list;

    }
}
