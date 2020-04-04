package com.shu.icpc.Component;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class OSSService {

    @Value("${oss.access_key}")
    private String ACCESS_KEY;

    @Value("${oss.secret_key}")
    private String SECRET_KEY;

    @Value("${oss.bucket}")
    private String bucket;

    public String getToken () {
        Auth auth = Auth.create(this.ACCESS_KEY, this.SECRET_KEY);
        String token = auth.uploadToken(bucket);
        return token;
    }
}
