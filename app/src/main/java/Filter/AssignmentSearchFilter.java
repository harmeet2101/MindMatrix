package Filter;

import android.widget.Filter;

import java.util.ArrayList;

import Adapter.AssignmentDataAdapter;
import Model.AssignmentData;

/**
 * Created by ganesha on 24/10/16.
 */


public class AssignmentSearchFilter extends Filter {

    public ArrayList<AssignmentData> list;
    private ArrayList<AssignmentData> filteredlist;
    private AssignmentDataAdapter adapter;

    public AssignmentSearchFilter(ArrayList<AssignmentData> list, AssignmentDataAdapter adapter) {
        this.adapter = adapter;
        this.list = list;
        this.filteredlist = new ArrayList();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredlist.clear();
        final FilterResults results = new FilterResults();

        //here you need to add proper items do filteredlist
        if (!constraint.equals(""))
            for (final AssignmentData item : list) {
                if (item.getType().equals(constraint)) {
                    filteredlist.add(item);
                }
            }
        else
            filteredlist.addAll(list);

        results.values = filteredlist;
        results.count = filteredlist.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setList(filteredlist);
        adapter.notifyDataSetChanged();
    }


    public ArrayList<AssignmentData> getlist() {
        return filteredlist;

    }
}
