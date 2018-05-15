package leapfrog_inc.summit.Fragment.Common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/09.
 */

public class WebViewFragment extends BaseFragment {

    private String mUrl;
    private String mTitle;

    public void set(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_webview, null);

        initContent(view);
        initAction(view);

        return view;
    }

    private void initContent(View view) {

        ((TextView)view.findViewById(R.id.titleTextView)).setText(mTitle);

        WebView webView = (WebView)view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        Client client = new Client();
        client.setCallback(new WebViewCallback() {
            @Override
            public void onPageFinished() {
                Loading.stop(getActivity());
            }
        });
        webView.setWebViewClient(client);
        webView.loadUrl(mUrl);

        Loading.start(getActivity());
    }

    private void initAction(View view) {

        ((ImageButton)view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFragment(AnimationType.horizontal);
            }
        });
    }

    static class Client extends android.webkit.WebViewClient {

        private WebViewCallback mCallback;

        void setCallback(WebViewCallback callback) {
            mCallback = callback;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mCallback.onPageFinished();
        }
    }

    interface WebViewCallback {
        void onPageFinished();
    }
}
