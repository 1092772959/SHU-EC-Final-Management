package com.shu.icpc.Component;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.shu.icpc.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class OSSService {

    @Value("${oss.access_key}")
    private String ACCESS_KEY;

    @Value("${oss.secret_key}")
    private String SECRET_KEY;

    @Value("${oss.bucket}")
    private String BUCKET_PUBLIC;

    @Value("${oss.bucket.private}")
    public String BUCKET_PRIVATE;

    private String token;

    private Auth auth;

    @Resource
    private UploadManager uploadManager;

	@Bean
	private UploadManager getUploadManager(){
		Configuration cfg = new Configuration(Region.region0());
		return new UploadManager(cfg);
	}

    public String getToken (String bucket) {
		if(auth == null){
			auth = Auth.create(this.ACCESS_KEY, this.SECRET_KEY);
		}
        this.token = auth.uploadToken(bucket);
        return token;
    }

    public String genPublicUrl(){
		String res = null;
		return res;
	}

	public String genPrivateUrl(String key, String urlBase){
		String res = null;

		String encodedFileName = null;
		try {
			encodedFileName = URLEncoder.encode(key, "utf-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String publicUrl = String.format("%s/%s", urlBase, encodedFileName);

		//default expired time is 3600s
		Auth auth = Auth.create(this.ACCESS_KEY, this.SECRET_KEY);
		String finalUrl = auth.privateDownloadUrl(publicUrl);
		System.out.println(finalUrl);
		return finalUrl;
	}

	/**
	 *
	 * @param key
	 * @param bytes
	 * @param bucket
	 * @param msg (contains error msg if error happens)
	 * @return
	 */
    public Integer uploadBytes(String key, byte[] bytes, String bucket, StringBuilder msg){
		if(token == null){
			getToken(bucket);
		}

		for(int i=0;i < Constants.RETRY_TIME;++i){
			try{
				Response resp = uploadManager.put(bytes, key, token);

			}catch (QiniuException ex){
				//refresh if token expired
				if(ex.getMessage().contains("expire")){
					token = getToken(bucket);
					continue;
				}
				msg.append(ex.getMessage());
				ex.printStackTrace();
				return -1;
			}
		}
		return 0;
	}

}
