package of.modeselect.bq.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import of.modeselect.bq.R;

public class InternetFragment extends Fragment {
   // private WebView internetWebView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_internet,container,false);
//        internetWebView = view.findViewById(R.id.internetWebView);
//        internetWebView.getSettings().setJavaScriptEnabled(true);
//        internetWebView.setWebViewClient(new WebViewClient());
//        internetWebView.loadUrl("http://www.baidu.com");
        return view;
    }
}
