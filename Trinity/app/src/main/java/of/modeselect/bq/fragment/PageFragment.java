package of.modeselect.bq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import of.modeselect.bq.R;
import of.modeselect.bq.activity.MainActivity;
import of.modeselect.bq.toast.OneToast;

/**
 * Created by MR.XIE on 2018/11/21.
 */
public class PageFragment extends Fragment implements View.OnClickListener{

    private ImageView ivPrevPage;
    private ImageView ivNextPage;
    private ImageView ivRefreshPage;
    private ImageView ivHomePage;
    private EditText etPageUrl;
    private WebView wvInternet;
    private UpdatePageTitle updatePageTitle;
    private int position=-1;//与标题栏的gridview一一对应的下标
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View view=inflater.inflate(R.layout.fragment_page,container,false);
     initView(view);
     initEvents();
     initDefaultBrower();
     return view;
}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(wvInternet!=null){
            //避免WebView内存泄露
            //销毁（ WebView ）的时候，先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空
            wvInternet.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            wvInternet.clearHistory();
            ((ViewGroup)wvInternet.getParent()).removeView(wvInternet);
            wvInternet.destroy();//释放资源
            wvInternet=null;
        }


    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setUpdatePageTitle(UpdatePageTitle updatePageTitle) {
        this.updatePageTitle = updatePageTitle;
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initDefaultBrower() {
        etPageUrl.setText(getResources().getString(R.string.DEFALUT_URL));
        wvInternet.loadUrl(getResources().getString(R.string.DEFALUT_URL));
        wvInternet.addJavascriptInterface(this,"android");
        wvInternet.setWebChromeClient(webChromeClient);
        wvInternet.setWebViewClient(webViewClient);
        WebSettings webSettings=wvInternet.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webSettings.setUseWideViewPort(true);//将图片调整到合适webview的大小
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕的大小
        //支持屏幕缩放
        webSettings.setSupportZoom(true);//支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false);//隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        setImageAlpha(0.5f,0.5f);
    }

    private  WebViewClient webViewClient=new WebViewClient(){

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { // 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
          synchronized (this){
              if(url==null)
                  return false;
              try {
                  if(!url.startsWith("http://")&&!url.startsWith("https://")){//如果网址不是以http://或https://开头的则调用系统的浏览器访问
                      Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                      startActivity(intent);
                      return true;
                  }
              }catch (Exception e){//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                  return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
              }
              //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器


              view.loadUrl(url);

              return true;
          }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面加载开始
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
           // Log.i("fksjdf", "onPageFinished: "+url+"//"+wvInternet.canGoBack()+"//"+wvInternet.canGoForward());

            synchronized (this){
                if(wvInternet.canGoForward()){
                    if(wvInternet.canGoBack()){
                        setImageAlpha(1.0f,1.0f);
                    }else{
                        setImageAlpha(0.5f,1.0f);
                    }
                }else if(wvInternet.canGoBack()){
                    setImageAlpha(1.0f,0.5f);
                }else{
                    setImageAlpha(0.5f,0.5f);
                }
                etPageUrl.setText(url);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                wvInternet.getSettings()
//                        .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            }
////            ---------------------
////                    作者：AndroidZhangG
////            来源：CSDN
////            原文：https://blog.csdn.net/zhangwenshuan/article/details/55513869
////            版权声明：本文为博主原创文章，转载请附上博文链接！
//            else{
//                handler.proceed();
//            }
            handler.proceed();
        }
    };

    private WebChromeClient webChromeClient=new WebChromeClient(){
       // 支持javascript的警告框
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            synchronized (this){
                new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle("JsAlert")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //注意:
                                //必须要这一句代码:result.confirm()表示:
                                //处理结果为确定状态同时唤醒WebCore线程
                                //否则不能继续点击按钮
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return  true;
            }

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {  //获取网页标题

            synchronized (this){
                super.onReceivedTitle(view, title);
                if(updatePageTitle!=null){
                    updatePageTitle.updatePageTitle(position,title);
                }
            }

        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {//加载进度回调
            super.onProgressChanged(view, newProgress);
        }
    };




    private void initEvents() {
        etPageUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){//得到焦点的处理

                }else{//失去焦点的处理
              etPageUrl.setSelection(0);//设置输入框光标定位到开头
                    //隐藏系统键盘
                    InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                      imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    }
                }
            }
        });
        etPageUrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //当i == XX_SEND 或者 XX_DONE时都触发
                //或者keyEvent.getKeyCode == ENTER 且 keyEvent.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (i == EditorInfo.IME_ACTION_SEND
                        || i == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
                    //处理事件
                    if(!etPageUrl.getText().toString().trim().contentEquals("")){//如果网址不为空则访问
                        wvInternet.loadUrl(String.valueOf(etPageUrl.getText()));
                    }

                }
                return false;
            }
        });
        ivPrevPage.setOnClickListener(this);
        ivNextPage.setOnClickListener(this);
        ivRefreshPage.setOnClickListener(this);
        ivHomePage.setOnClickListener(this);
    }





    private void setImageAlpha(float alphaPrevButton,float alphaNextButton){//设置回退和返回图片透明度
        Animation prevImageAlphaAnimation=new AlphaAnimation(alphaPrevButton,alphaPrevButton);
        prevImageAlphaAnimation.setFillAfter(true);
        prevImageAlphaAnimation.setDuration(0);
        ivPrevPage.startAnimation(prevImageAlphaAnimation);

        Animation nextImageAlphaAnimation=new AlphaAnimation(alphaNextButton,alphaNextButton);
        nextImageAlphaAnimation.setFillAfter(true);
        nextImageAlphaAnimation.setDuration(0);
        ivNextPage.startAnimation(nextImageAlphaAnimation);

    }

    private void initView(View view) {
        ivPrevPage = view.findViewById(R.id.iv_prev_page);
        ivNextPage = view.findViewById(R.id.iv_next_page);
        ivRefreshPage = view.findViewById(R.id.iv_refresh_page);
        ivHomePage = view.findViewById(R.id.iv_home_page);
        etPageUrl = view.findViewById(R.id.et_page_url);
        wvInternet = view.findViewById(R.id.wv_internet);
    }

    @Override
    public void onClick(View view) {

        synchronized (this){
            switch (view.getId()){
                case R.id.iv_prev_page:
                    if(wvInternet.canGoBack()){
                        wvInternet.goBack();
                    }
                    break;
                case R.id.iv_next_page:
                    if(wvInternet.canGoForward()){
                        wvInternet.goForward();
                    }
                    break;
                case R.id.iv_refresh_page:
                    wvInternet.reload();
                    break;
                case R.id.iv_home_page:
                    wvInternet.loadUrl(getResources().getString(R.string.DEFALUT_URL));
                    break;
                default:
                    break;
            }
        }

    }
    //用于更新网页的标题
    public interface UpdatePageTitle{
        void updatePageTitle(int position,String title);//position为与gridview一一对应的下标，title为网页的标题
    }
}
