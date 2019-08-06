package com.smile.studio.resoucemanager.network.request

import com.google.gson.annotations.SerializedName

class QRCodeRequest(@SerializedName("qrcode") val qrcode: String)