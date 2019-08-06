package com.smile.studio.libsmilestudio.sms;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

import com.smile.studio.libsmilestudio.utils.Utils;

/**
 * Created by admin on 18/04/2017.
 */

public class SMSManager {

    public static void sendSMS(String gateway, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(Utils.fromHtml(gateway).toString(), null, Utils.fromHtml(message).toString(), null, null);
    }

    /**
     * Gửi sms bằng trình soạn tin nhắn mặc định
     *
     * @param context
     * @param gateway
     * @param message
     */
    public static void sendSMSDefault(Context context, String gateway, String message) {
        try {
            Uri uri = Uri.parse("smsto:" + Utils.fromHtml(gateway).toString());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", Utils.fromHtml(message).toString());
            context.startActivity(intent);
        } catch (Exception e) {
            sendSMS(gateway, message);
        }
    }
}
