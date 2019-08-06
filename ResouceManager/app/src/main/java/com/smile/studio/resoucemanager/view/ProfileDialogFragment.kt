package com.smile.studio.resoucemanager.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smile.studio.libsmilestudio.utils.Debug
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.model.GlobalApp
import com.smile.studio.resoucemanager.network.model.Profile
import kotlinx.android.synthetic.main.dialog_fragment_profile.*


class ProfileDialogFragment : BottomSheetDialogFragment() {

    var profile: Profile? = null

    companion object {

        fun newInstance(profile: Profile): ProfileDialogFragment {
            val bundle = Bundle()
            bundle.putParcelable(Profile::class.java.simpleName, profile)
            val fragment = ProfileDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile = arguments?.getParcelable(Profile::class.java.simpleName)
        val placeholder = BitmapFactory.decodeResource(view.resources, R.drawable.com_facebook_profile_picture_blank_square)
        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(view.resources!!, placeholder)
        circularBitmapDrawable.isCircular = true
        Glide.with(this).load(profile?.image).apply(RequestOptions.diskCacheStrategyOf(ALL).format(DecodeFormat.PREFER_ARGB_8888)).error(circularBitmapDrawable).placeholder(circularBitmapDrawable).circleCrop().thumbnail(0.5f).into(thumb)
        tv_name.text = profile?.name
        tv_email.text = profile?.email
        try {
            tv_brithday.text = GlobalApp().getInstance().dateFormat1.format(GlobalApp().getInstance().dateFormat2.parse(profile?.birthday))
        } catch (e: Exception) {
            Debug.e("--- Lỗi định dạng ${e.message}")
        }
    }
}