package com.smile.studio.resoucemanager.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL
import com.bumptech.glide.request.RequestOptions
import com.smile.studio.libsmilestudio.recyclerviewer.OnItemClickListenerRecyclerView
import com.smile.studio.libsmilestudio.utils.Debug
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.network.model.Profile

class ProfileAdapter(val mContext: Context, val mData: ArrayList<Profile>) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>(), Filterable {

    var mDataFiltered: ArrayList<Profile>? = mData
    var onItemClick: OnItemClickListenerRecyclerView? = null

    fun add(data: Profile) {
        try {
            val index = itemCount
            this.mDataFiltered?.add(data)
            if (index == 0) {
                notifyDataSetChanged()
            } else {
                notifyItemInserted(index)
            }
        } catch (e: Exception) {
            Debug.e("--- Lỗi: ${e.message}")
        }
    }

    fun addAll(mData: ArrayList<Profile>?) {
        val start = this.mDataFiltered?.size!!
        this.mDataFiltered?.addAll(mData!!)
        notifyItemRangeInserted(start, itemCount)
    }

    fun remove(data: Profile) {
        try {
            val index = this.mDataFiltered?.indexOf(data)!!
            this.mDataFiltered?.removeAt(index)
            if (index != 0)
                notifyItemRemoved(index)
            else {
                notifyDataSetChanged()
            }
        } catch (e: Exception) {
            Debug.e("--- Lỗi: ${e.message}")
        }
    }

    fun clear() {
        this.mDataFiltered?.clear()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as Profile).name.toString()
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (TextUtils.isEmpty(charString)) {
                    mDataFiltered = mData
                } else {
                    val fiteredList = ArrayList<Profile>()
                    mData.forEach {
                        if (it.name?.contains(charString, true)!!) {
                            fiteredList.add(it)
                        }
                    }
                    mDataFiltered = fiteredList
                }
                val filterResult = FilterResults()
                filterResult.values = mDataFiltered
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mDataFiltered = results?.values as ArrayList<Profile>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_item_spinner_profile, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataFiltered?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val placeholder = BitmapFactory.decodeResource(mContext.resources, R.drawable.com_facebook_profile_picture_blank_square)
        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.resources, placeholder)
        circularBitmapDrawable.isCircular = true
        Glide.with(mContext).load(mDataFiltered?.get(position)?.image).apply(RequestOptions.diskCacheStrategyOf(ALL).format(DecodeFormat.PREFER_ARGB_8888)).error(circularBitmapDrawable).placeholder(circularBitmapDrawable).circleCrop().thumbnail(0.5f).into(holder.thumb)
        holder.tv_title?.text = mDataFiltered?.get(position)?.name
        holder.itemView.setOnClickListener {
            onItemClick?.onClick(it, position)
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tv_title = view.findViewById<TextView>(R.id.tv_title)
        val thumb = view.findViewById<ImageView>(R.id.thumb)
    }
}