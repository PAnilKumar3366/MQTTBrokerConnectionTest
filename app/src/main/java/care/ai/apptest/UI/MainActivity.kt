package care.ai.apptest.UI

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import care.ai.apptest.api.MQTTClientHelper
import care.ai.apptest.databinding.ActivityMainBinding
import care.ai.apptest.model.WebSite
import care.ai.apptest.util.Status
import care.ai.apptest.viewmodel.MainViewModel
import care.ai.apptest.viewmodel.ViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity(), WebSiteListAdapter.BtnClickListener {

    private val TAG = "MainActivity"
    lateinit var mainAdapter: WebSiteListAdapter
    lateinit var mainViewModel: MainViewModel
    lateinit var mBinding: ActivityMainBinding
    lateinit var websiteList: MutableList<WebSite>
    var count: Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        //setContentView(R.layout.activity_main)

        setUpViewModel()
        setUPObserver()
        setUpAdapter()
    }

    private fun setUpAdapter() {
        mainAdapter = WebSiteListAdapter(this)
        mBinding.recyclerview.adapter = mainAdapter
    }

    fun setUpViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(MQTTClientHelper(""))
        ).get(MainViewModel::class.java)
    }

    fun setUPObserver() {
        mainViewModel.getWebSiteList().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        mBinding.progressBar.visibility = View.GONE
                        Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                        resource.data?.let { list ->
                            retrieveList(list)
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        mBinding.progressBar.visibility = View.GONE
                        Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        })
    }

    private fun retrieveList(list: MutableList<WebSite>) {
        websiteList = list
        mainAdapter.apply {
            setWebsites(list)
        }
    }

    override fun onBtnClick(position: Int) {
        var intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("Url", websiteList.get(position).url)
        startActivity(intent)
    }
}


inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

