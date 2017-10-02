package Filter;

import android.widget.Filter;


import java.util.ArrayList;

import Adapter.QuizDataAdapter;
import Model.QuizData;

/**
 * Created by ganesha on 24/10/16.
 */


public class QuizSearchFilter extends Filter {

    public ArrayList<QuizData> list;
    private ArrayList<QuizData> filteredlist;
    private QuizDataAdapter adapter;

    public QuizSearchFilter(ArrayList<QuizData> list, QuizDataAdapter adapter) {
        this.adapter = adapter;
        this.list = list;
        this.filteredlist = new ArrayList();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredlist.clear();
        final FilterResults results = new FilterResults();

        //here you need to add proper items do filteredlist
        for (final QuizData item : list) {
            if (!item.getType().equals("practiceQuiz")) {
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


    public ArrayList<QuizData> getlist() {

        return list;

    }
}
