package kickstarter.payu.com.payukickstarter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import kickstarter.payu.com.constants.AppConstants;

public class ItemDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Bundle bundle = getIntent().getExtras();

        String URL = bundle.getString(AppConstants.URL_PARAM);

        WebView webView = (WebView) findViewById(R.id.itemDetail);
        webView.loadUrl(URL);
        webView.canGoBack();
    }
}