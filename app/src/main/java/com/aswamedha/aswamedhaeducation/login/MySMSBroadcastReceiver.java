package com.aswamedha.aswamedhaeducation.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class MySMSBroadcastReceiver extends BroadcastReceiver {
    private OTPReceiveListener otpReceiveListener;

    public void setOtpReceiveListener(OTPReceiveListener otpReceiveListener) {
        this.otpReceiveListener = otpReceiveListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())){
            Bundle extras = intent.getExtras();
            assert extras != null;
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            assert status != null;
            switch (status.getStatusCode()){
                case  CommonStatusCodes.SUCCESS:
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE );
                    this.otpReceiveListener.onOTPReceived(message);
                    break;

                case CommonStatusCodes.TIMEOUT:
                    this.otpReceiveListener.onOTPTimeOut();
                    break;
            }
        }
    }
    public interface OTPReceiveListener {

        void onOTPReceived(String otp);

        void onOTPTimeOut();

        void onOTPReceivedError(String error);
    }
}
