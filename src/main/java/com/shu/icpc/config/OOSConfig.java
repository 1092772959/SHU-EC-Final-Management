package com.shu.icpc.config;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.config.Config;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;
import org.json.JSONException;

public class OOSConfig {
    public static void main(String[] args) throws AuthException, JSONException {
        Config.ACCESS_KEY = "agw3i0sMcq54VFo7d8alfYJ47qzR4YJ2etZcsm5B";
        Config.SECRET_KEY = "KiUDriRU84yuFUt8eECbBqYhjiZs3OfUZ7RgLSZ4";

        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在
        String bucketName = "ooscn";
        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = putPolicy.token(mac);
        
    }
}
