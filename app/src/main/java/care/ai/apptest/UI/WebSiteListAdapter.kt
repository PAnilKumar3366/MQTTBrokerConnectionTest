package care.ai.apptest.UI

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import care.ai.apptest.R
import care.ai.apptest.model.WebSite
import com.bumptech.glide.Glide


class WebSiteListAdapter(val btnClickListener: BtnClickListener) :
    RecyclerView.Adapter<WebSiteListAdapter.WebSiteViewHolder>() {

    private val TAG = "WesitelistAdapter"

    companion object {
        var mClickListener: BtnClickListener? = null
    }

    var webSiteList = mutableListOf<WebSite>()

    lateinit var context: Context

    fun setWebsites(webSite: MutableList<WebSite>): Unit {
        this.webSiteList = webSite
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebSiteViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.website_item, parent, false);
        this.context = parent.context
        return WebSiteViewHolder(view)
    }

    override fun onBindViewHolder(holder: WebSiteViewHolder, position: Int) {
        try {
            val singleItem: WebSite = webSiteList.get(position)
            holder.text_name.setText(singleItem.name)
            Glide.with(holder.itemView.context).load(singleItem.icon).centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(Glide.with(holder.icon).load(R.drawable.ic_baseline_error_24))
                .into(holder.icon)
            holder.icon.setOnClickListener(View.OnClickListener {
                btnClickListener.onBtnClick(position)

            })
            Log.d(TAG, "onBindViewHolder: Image Url " + singleItem.url)
            var textColor: String = singleItem.textColor + "#"
            var bgColor: String = singleItem.bgColor + "#"
            Log.d(TAG, "onBindViewHolder: text color " + textColor)
            holder.itemView.setBackgroundColor(Color.parseColor(textColor))


        } catch (e: Exception) {
            Log.e(TAG, "onBindViewHolder: ", e)
        }

    }

    override fun getItemCount(): Int {
        return webSiteList.size
    }

    class WebSiteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text_name = itemView.findViewById<TextView>(R.id.name)
        val icon = itemView.findViewById<ImageView>(R.id.icon)
    }

    open interface BtnClickListener {
        fun onBtnClick(position: Int)
    }
}