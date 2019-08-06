package com.smile.studio.resoucemanager.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smile.studio.libsmilestudio.network.BaseDialogFragment
import com.smile.studio.libsmilestudio.recyclerviewer.OnItemClickListenerRecyclerView
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.adapter.ProfileAdapter
import com.smile.studio.resoucemanager.model.GlobalApp
import kotlinx.android.synthetic.main.dialog_fragment_choice_profile.*

class ChoiceProfileDialogFragment : BaseDialogFragment(), View.OnClickListener, TextWatcher {

    var adapter: ProfileAdapter? = null
    var iAction: IAction? = null

    companion object {

        fun newInstance(): ChoiceProfileDialogFragment {
            val fragment = ChoiceProfileDialogFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_fragment_choice_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProfileAdapter(activity!!, GlobalApp().getInstance().mDataProfile)
        recyclerView.adapter = adapter
        adapter?.onItemClick = object : OnItemClickListenerRecyclerView {
            override fun onClick(view: View?, position: Int) {
                iAction?.perform(position)
            }

            override fun onLongClick(view: View?, position: Int) {
            }

        }
        edt_username.addTextChangedListener(this)
        recyclerView.setHasFixedSize(true)
        btn_delete.setOnClickListener(this)
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s?.length != 0) {
            adapter?.filter?.filter(s.toString())
        } else {
            adapter?.clear()
            adapter?.addAll(GlobalApp().getInstance().mDataProfile)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_delete -> {
                edt_username.setText("")

            }
        }
    }

    interface IAction {
        fun perform(poision: Int)
    }

}