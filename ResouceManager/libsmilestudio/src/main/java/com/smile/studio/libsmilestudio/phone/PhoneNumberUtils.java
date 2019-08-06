package com.smile.studio.libsmilestudio.phone;

import android.util.Patterns;

import com.smile.studio.libsmilestudio.utils.Debug;

import java.util.regex.Pattern;

/**
 * Created by Leeprohacker on 2/7/18.
 */

public class PhoneNumberUtils {


    public static final String VN_MOBIFONE = "VN MOBIFONE";
    public static final String VN_MOBIFONE1 = "VN_MOBIFONE";
    public static final String MOBIPHONE = "MOBIPHONE";
    public static final String MOBIFONE = "MOBIFONE";
    public static final String VN_MOBIPHONE = "VN MOBIPHONE";
    public static final String VN_MOBIPHONE1 = "VN MOBIPHONE";
    public static final String VIETTEL = "VIETTEL";
    public static final String VN_VINAPHONE = "VN_VINAPHONE";
    public static final String VINAPHONE = "VINAPHONE";
    public static final String VINAFONE = "VINAPHONE";

    public static final int TELCO_NONE = 0;
    public static final int TELCO_MOBIFONE = 1;
    public static final int TELCO_VINAPHONE = 2;
    public static final int TELCO_VIETTEL = 3;

    public static int getTelco(String carrier) {
        if (carrier.toUpperCase().trim().contains(MOBIFONE)) {
            return TELCO_MOBIFONE;
        } else if (carrier.toUpperCase().trim().contains(VINAPHONE)) {
            return TELCO_VINAPHONE;
        } else if (carrier.toUpperCase().trim().contains(VIETTEL)) {
            return TELCO_VIETTEL;
        } else {
            return TELCO_NONE;
        }
    }

    public static boolean isEqualPhoneNumber(String phoneNumber1, String phoneNumber2) {
        phoneNumber1 = convertPhone(phoneNumber1);
        phoneNumber2 = convertPhone(phoneNumber2);
        return phoneNumber1.equals(phoneNumber2);
    }

    public static boolean isEqualPhoneTelco(String phoneNumber1, String phoneNumber2) {
        int telcoByPhone1 = getTelcoByPhone(phoneNumber1);
        int telcoByPhone2 = getTelcoByPhone(phoneNumber2);
        return telcoByPhone1 == telcoByPhone2;
    }


    public static String convertPhone(String phone) {
        Debug.e("--- Phone Input convert: " + phone);
        if (Patterns.PHONE.matcher(phone).find()) {
            phone = phone.replaceAll(Pattern.compile("[^\\d]|[\\.]").pattern(), "").replaceFirst(Pattern.compile("^(\\+84)|^(84)").pattern(), "0");
        }
        Debug.e("--- Phone output convert: " + phone);
        return phone;
    }

    public static String convertPhoneToStartWith84(String phone) {
        Debug.e("--- Phone Input convert: " + phone);
        if (Patterns.PHONE.matcher(phone).find()) {
            phone = phone.replaceAll(Pattern.compile("[^\\d]|[\\.]").pattern(), "").replaceFirst(Pattern.compile("^(\\+84)|^(0)").pattern(), "84");
        }
        Debug.e("--- Phone output convert: " + phone);
        return phone;
    }

    public static String convertPhoneToStartWith84Plus(String phone) {
        Debug.e("--- Phone Input convert: " + phone);
        if (Patterns.PHONE.matcher(phone).find()) {
            phone = phone.replaceAll(Pattern.compile("[^\\d]|[\\.]").pattern(), "").replaceFirst(Pattern.compile("^(\\+84)|^(0)").pattern(), "+84");
        }
        Debug.e("--- Phone output convert: " + phone);
        return phone;
    }

    /**
     * Lấy nhà mạng VietNam theo đầu số điện thoại 2017, 2018
     *
     * @param phone
     * @return
     */
    public static int getTelcoByPhone(String phone) {
        Debug.e("--- Phone Input check: " + phone);
        if (!Patterns.PHONE.matcher(phone).matches()) {
            Debug.e("--- step 1");
            return TELCO_NONE;
        }
        phone = convertPhone(phone);
        if (Pattern.compile("^(086)|^(096)|^(097)|^(098)").matcher(phone).find() && phone.length() == 10) {
            Debug.e("--- step 2");
            return TELCO_VIETTEL;
        }
        if (Pattern.compile("^(0162)|^(0163)|^(0164)|^(0165)|^(0166)|^(0167)|^(0168)|^(0169)").matcher(phone).find() && phone.length() == 11) {
            Debug.e("--- step 3");
            return TELCO_VIETTEL;
        }
        if (Pattern.compile("^(032)|^(033)|^(034)|^(035)|^(036)|^(037)|^(038)|^(039)").matcher(phone).find() && phone.length() == 10) {
            Debug.e("--- step 4");
            return TELCO_VIETTEL;
        }
        if (Pattern.compile("^(090)|^(093)|^(089)").matcher(phone).find() && phone.length() == 10) {
            Debug.e("--- step 5");
            return TELCO_MOBIFONE;
        }
        if (Pattern.compile("^(0120)|^(0121)|^(0122)|^(0126)|^(0128)").matcher(phone).find() && phone.length() == 11) {
            Debug.e("--- step 6");
            return TELCO_MOBIFONE;
        }
        if (Pattern.compile("^(070)|^(079)|^(077)|^(076)|^(078)").matcher(phone).find() && phone.length() == 10) {
            Debug.e("--- step 7");
            return TELCO_MOBIFONE;
        }
        if (Pattern.compile("^(091)|^(094)|^(088)").matcher(phone).find() && phone.length() == 10) {
            Debug.e("--- step 8");
            return TELCO_VINAPHONE;
        }
        if (Pattern.compile("^(0123)|^(0124)|^(0125)|^(0127)|^(0129)").matcher(phone).find() && phone.length() == 11) {
            Debug.e("--- step 9");
            return TELCO_VINAPHONE;
        }
        if (Pattern.compile("^(083)|^(084)|^(085)|^(081)|^(082)").matcher(phone).find() && phone.length() == 10) {
            Debug.e("--- step 10");
            return TELCO_VINAPHONE;
        }
        Debug.e("--- step 11");
        return TELCO_NONE;
    }

}
