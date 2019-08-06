package com.smile.studio.resoucemanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.adapter.CompanyAdapter
import com.smile.studio.resoucemanager.adapter.DepartmentAdapter
import com.smile.studio.resoucemanager.adapter.ResouceTypeAdapter
import com.smile.studio.resoucemanager.adapter.StatusAdapter
import com.smile.studio.resoucemanager.model.GlobalApp
import com.smile.studio.resoucemanager.network.request.FilterReqeust
import kotlinx.android.synthetic.main.dialog_fragment_filter_setting.*

class FilterSettingDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {

    var iAction: IAction? = null
    var adapterCompany: CompanyAdapter? = null
    var adapterDepartment: DepartmentAdapter? = null
    var adapterResouceType: ResouceTypeAdapter? = null
    var adapterResouceStatus: StatusAdapter? = null
    var filter = FilterReqeust()

    companion object {

        fun newInstance(): FilterSettingDialogFragment {
            val fragment = FilterSettingDialogFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_filter_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterCompany = CompanyAdapter(activity!!, GlobalApp().getInstance().mDataCompany)
        spinner_company.adapter = adapterCompany
        spinner_company.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filter.company = adapterCompany?.mData?.get(position)?.value!!
            }

        }
        adapterDepartment = DepartmentAdapter(activity!!, GlobalApp().getInstance().mDataDepartment)
        spinner_department.adapter = adapterDepartment
        spinner_department.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filter.idDepartment = adapterDepartment?.mData?.get(position)?.id!!
            }

        }
        adapterResouceType = ResouceTypeAdapter(activity!!, GlobalApp().getInstance().mDataResouceType)
        spinner_resouce_type.adapter = adapterResouceType
        spinner_resouce_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filter.assetType = adapterResouceType?.mData?.get(position)?.value
            }

        }
        adapterResouceStatus = StatusAdapter(activity!!, GlobalApp().getInstance().mDataStatus)
        spinner_resouce_status.adapter = adapterResouceStatus
        spinner_resouce_status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filter.status = adapterResouceStatus?.mData?.get(position)?.status
            }

        }
        tv_profile.setOnClickListener(this)
        btn_filter.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_profile -> {
                val dialog = ChoiceProfileDialogFragment.newInstance()
                dialog.iAction = object : ChoiceProfileDialogFragment.IAction {
                    override fun perform(poision: Int) {
                        dialog.dismiss()
                        tv_profile.text = GlobalApp().getInstance().mDataProfile.get(poision).name
                        filter.uid = GlobalApp().getInstance().mDataProfile.get(poision).id
                    }
                }
                dialog.show(fragmentManager, ChoiceProfileDialogFragment::class.java.simpleName)
            }
            R.id.btn_filter -> {
                filter.keyword = edt_keyword.text.toString()
                iAction?.callback(filter)
            }
        }
    }

    interface IAction {
        fun callback(mFilter: FilterReqeust)
    }
}