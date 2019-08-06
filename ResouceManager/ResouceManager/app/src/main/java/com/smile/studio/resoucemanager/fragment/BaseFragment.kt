package com.smile.studio.resoucemanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

/**
 * A simple [Fragment] subclass.
 */
open class BaseFragment : androidx.fragment.app.Fragment() {

    var page = 1
    val pagesize = 24
    var numberColumns = 3
    val TIME_DELAY = 1500L
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}

// Required empty public constructor
