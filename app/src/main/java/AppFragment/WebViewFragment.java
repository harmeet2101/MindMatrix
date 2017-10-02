package AppFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cli.knowledgebase.R;

import Adapter.AllFilesAdapter;
import Constants.Const;

public class WebViewFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
    private AllFilesAdapter adapter;
    ImageView iv_nodata;
    WebView webView;
    // this method create view for your Dialog


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_web_view, container, false);

        ImageView back = (ImageView) v.findViewById(R.id.iv_back);
        back.setVisibility(View.GONE);
        back.setImageResource(R.mipmap.ic_home);

       /* v.setLayoutParams(new LinearLayout.LayoutParams(
                AndyUtils.getScreenWidth(getActivity()), AndyUtils.getScreenHeight(getActivity())));
*/

        ((TextView) v.findViewById(R.id.tv_menu_name)).setText("WebView");


        ImageView iv_close = (ImageView) v.findViewById(R.id.iv_close);
        iv_close.setVisibility(View.VISIBLE);

        iv_close.setPadding(5, 15, 5, 15);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        webView = (WebView) v.findViewById(R.id.web_view);
        loadWebView(getArguments().getString(Const.IntentParams.URL));


        return v;
    }


    private void loadWebView(String url) {

        WebSettings webSettings = webView.getSettings();
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        webView.setScrollbarFadingEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                // callback.invoke(String origin, boolean allow, boolean remember);
                callback.invoke(origin, true, false);
            }
        });

        if (url.toLowerCase().endsWith("png") || url.toLowerCase().endsWith("jpeg") || url.toLowerCase().endsWith("jpg")) {
            webView.loadUrl(url);
        } else {

            webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;


    }


}