package com.feiyou.headstyle.common.interceptor;

import com.orhanobut.logger.Logger;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.utils.Base64Utils;
import com.feiyou.headstyle.utils.RSAUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by admin on 2017/3/13.
 */

public class EncryptionInterceptor1 implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request oldRequest = chain.request();

            // 添加新的参数
            HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                    .newBuilder()
                    .scheme(oldRequest.url().scheme())
                    .host(oldRequest.url().host())
                    .addQueryParameter("sex", "sex");

            // 新的请求
            Request newRequest = oldRequest.newBuilder()
                    .method(oldRequest.method(), oldRequest.body())
                    .url(authorizedUrlBuilder.build())
                    .build();

            RequestBody oldBody = newRequest.body();

            Buffer buffer = new Buffer();
            oldBody.writeTo(buffer);
            byte[] strOldBody = buffer.readByteArray();
            Logger.e("加密之前的参数--->" + new String(strOldBody,"UTF-8"));

            MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
            byte[] strNewBody = RSAUtils.encryptByPublicKey(strOldBody, Constants.DEFAULT_PUBLIC_KEY);
            Logger.e("加密之后的参数--->" + new String(strNewBody,"UTF-8"));

            byte[] res = RSAUtils.decryptByPrivateKey(strNewBody,Constants.DEFAULT_PRIVATE_KEY);

            Logger.e("加密之后再解密的参数--->" + new String(res,"UTF-8"));

            RequestBody body = RequestBody.create(mediaType, Base64Utils.encode(strNewBody));
            newRequest = newRequest.newBuilder()
                    .url(authorizedUrlBuilder.build())
                    .header("Content-Type", body.contentType().toString())
                    .header("Content-Length", String.valueOf(body.contentLength()))
                    .method(newRequest.method(), body)
                    .build();

            return chain.proceed(newRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
