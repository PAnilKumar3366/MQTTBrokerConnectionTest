package care.ai.apptest.UI

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import care.ai.apptest.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityWebViewBinding
    lateinit var url: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        url = intent.extras?.getString("Url")!!
        mBinding.webView.webViewClient = MyWebViewClient(this)
        if (!url.isEmpty() && URLUtil.isValidUrl(url))
            mBinding.webView.loadUrl(url)
        else
            mBinding.webView.loadUrl("https://www.Google.com/")
    }

    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
        }
    }
}