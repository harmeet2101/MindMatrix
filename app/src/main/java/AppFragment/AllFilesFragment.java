package AppFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cli.knowledgebase.R;

import java.io.File;

import Adapter.AllFilesAdapter;
import Constants.Const;
import Interfaces.RecyclerViewItemClickListener;
import Utils.ViewUtils;

public class AllFilesFragment extends DialogFragment implements RecyclerViewItemClickListener {
    private RecyclerView mRecyclerView;
    private AllFilesAdapter adapter;
    ImageView iv_nodata;
    // this method create view for your Dialog


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_all_files, container, false);

        ImageView back = (ImageView) v.findViewById(R.id.iv_back);
        back.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.ic_home);

        iv_nodata = (ImageView) v.findViewById(R.id.iv_nodata);

        back.setPadding(5, 8, 5, 8);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadData(Environment.getExternalStorageDirectory().getPath());

            }
        });

        ((TextView) v.findViewById(R.id.tv_menu_name)).setText("Select Assignment");


        ImageView iv_close = (ImageView) v.findViewById(R.id.iv_close);
        iv_close.setVisibility(View.VISIBLE);

        iv_close.setPadding(5, 15, 5, 15);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData(Environment.getExternalStorageDirectory().getPath());

        return v;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;


    }

    private void loadData(String s) {

        File mfile = new File(s);
        File[] list = mfile.listFiles();

        if (list != null) {
            if (adapter == null) {
                adapter = new AllFilesAdapter(getActivity(), list, this);
                mRecyclerView.setAdapter(adapter);
            } else {
                adapter.setList(list);
                adapter.notifyDataSetChanged();

            }

            if (list.length > 0) {

                mRecyclerView.setVisibility(View.VISIBLE);
                iv_nodata.setVisibility(View.GONE);

            } else {
                mRecyclerView.setVisibility(View.GONE);
                iv_nodata.setVisibility(View.VISIBLE);
            }

        } else {
            mRecyclerView.setVisibility(View.GONE);
            iv_nodata.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void onItemClick(int position) {

        File file = adapter.getList()[position];

        if (file.isDirectory()) {

            loadData(file.getAbsolutePath());
        } else {

            // Toast.makeText(getActivity(), file.getPath(), Toast.LENGTH_LONG).show();

            Bundle bundle = new Bundle();
            bundle.putString(Const.IntentParams.PATH, file.getPath());

            ViewUtils.sendLocalBroadCast(getActivity(), Const.IntentFilter.SELECT_PATH, bundle);

            dismiss();


        }

    }


    

}