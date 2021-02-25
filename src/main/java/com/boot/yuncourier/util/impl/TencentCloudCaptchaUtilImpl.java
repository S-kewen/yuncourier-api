package com.boot.yuncourier.util.impl;

import com.boot.yuncourier.util.TencentCloudCaptchaUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.captcha.v20190722.CaptchaClient;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultRequest;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import org.springframework.stereotype.Component;

/**
 * @Author: skwen
 * @Description: TencentCloudApiServiceImpl-騰訊雲Api操作service
 * @Date: 2020-02-01
 */

@Component
public class TencentCloudCaptchaUtilImpl implements TencentCloudCaptchaUtil {
    final static String CaptchaAppId = "2081433308";
    final static String AppSecretKey = "0AufBXx6xuTZim8epGoCv9w**"; // TODO:设置Token
    final static String SecretId = "AKIDHansoosIjy2hDcnGZggV9hGKH2nXorpg";
    final static String SecretKey = "pS8d6Sj05yjIjy30udAViufPxIRaXKvJ";
    class CaptchaResponse {
        private String RequestId;
        private int CaptchaCode;
        private String CaptchaMsg;
        private int EvilLevel;
        public String getRequestId() {
            return RequestId;
        }
        public void setRequestId(String requestId) {
            RequestId = requestId;
        }
        public int getCaptchaCode() {
            return CaptchaCode;
        }
        public void setCaptchaCode(int captchaCode) {
            CaptchaCode = captchaCode;
        }
        public String getCaptchaMsg() {
            return CaptchaMsg;
        }
        public void setCaptchaMsg(String captchaMsg) {
            CaptchaMsg = captchaMsg;
        }
        public int getEvilLevel() {
            return EvilLevel;
        }
        public void setEvilLevel(int evilLevel) {
            EvilLevel = evilLevel;
        }

        public CaptchaResponse(String requestId, int captchaCode, String captchaMsg, int evilLevel) {
            super();
            RequestId = requestId;
            CaptchaCode = captchaCode;
            CaptchaMsg = captchaMsg;
            EvilLevel = evilLevel;
        }
        public CaptchaResponse() {
            super();
        }
        @Override
        public String toString() {
            return "CaptchaResponse [RequestId=" + RequestId + ", CaptchaCode=" + CaptchaCode + ", CaptchaMsg="
                    + CaptchaMsg + ", EvilLevel=" + EvilLevel + "]";
        }



    }
    @Override
    public boolean codeResponse(String userIp, String ticket, String randstr) {
        String str = "";
        try {
            Credential cred = new Credential(SecretId, SecretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("captcha.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CaptchaClient client = new CaptchaClient(cred, "ap-guangzhou", clientProfile);

            String params = "{\"CaptchaType\":9,\"Ticket\":\"" + ticket + "\",\"UserIp\":\"" + userIp
                    + "\",\"Randstr\":\"" + randstr + "\",\"" + "CaptchaAppId\":" + CaptchaAppId
                    + ",\"AppSecretKey\":\"" + AppSecretKey + "\"}";

            DescribeCaptchaResultRequest req = DescribeCaptchaResultRequest.fromJsonString(params,
                    DescribeCaptchaResultRequest.class);
            DescribeCaptchaResultResponse resp = client.DescribeCaptchaResult(req);

            str = DescribeCaptchaResultRequest.toJsonString(resp);

        } catch (TencentCloudSDKException e) {

            e.toString();
        }
		/*CaptchaResponse CaptchaResponse = null;
		String key="CaptchaCode\":([\\s\\S]*?),\"CaptchaMsg\":\"([\\s\\S]*?)\",\"EvilLevel\":([\\s\\S]*?),\"RequestId\":\"([\\s\\S]*?)\"}";
		Matcher matcher = Pattern.compile(key).matcher(str);
		  while(matcher.find()){
			  CaptchaResponse.setCaptchaCode(Integer.parseInt(matcher.group(1)));
			  CaptchaResponse.setCaptchaMsg(matcher.group(2));
			  CaptchaResponse.setEvilLevel(Integer.parseInt(matcher.group(3)));
			  CaptchaResponse.setRequestId(matcher.group(4));
			  }*/
        if (str.indexOf("OK")>=0) {
            return true;
        }else {
            return false;
        }
        //return CaptchaResponse;
    }
}
