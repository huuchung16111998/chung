package com.smile.studio.resoucemanager.network.face

import com.smile.studio.resoucemanager.network.model.Profile
import com.smile.studio.resoucemanager.network.model.Resouce
import com.smile.studio.resoucemanager.network.request.FilterReqeust
import com.smile.studio.resoucemanager.network.request.ProfileRequest
import com.smile.studio.resoucemanager.network.request.QRCodeRequest
import com.smile.studio.resoucemanager.network.response.BaseResponse
import com.smile.studio.resoucemanager.network.response.ConfigResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ResouceManager {
    /**
     * Lấy danh sách tài sản mapping theo các thông tin điều kiện lọc
     */
    @POST("api/asset/searchAsset")
    fun filterResouce(@Body filter: FilterReqeust): Observable<BaseResponse<ArrayList<Resouce>>>

    /**
     * Cập nhật thêm mới hoặc chỉnh sửa thông tin tài sản
     */
    @POST("api/asset/insertUpdateAsset")
    fun insertUpdateResouce(@Body resouce: Resouce): Observable<BaseResponse<ConfigResponse>>

    /**
     * Lấy thông tin cấu hình hệ thống (Ds phòng ban, nhân viên, tài sản, phân loại tài sản)
     */
    @GET("api/asset/getDataToSearch")
    fun getSetting(): Observable<BaseResponse<ConfigResponse>>

    /**
     * Lấy thông tin nhân viên theo mã UID
     */
    @POST("api/user/getUserById")
    fun getProfileInfo(@Body profileRequest: ProfileRequest): Observable<BaseResponse<Profile>>

    /**
     * Lấy thông tin mã QRCode
     */
    @POST("api/asset/getAssetByQR")
    fun getInfo(@Body qrCodeRequest: QRCodeRequest): Observable<BaseResponse<Resouce>>
}