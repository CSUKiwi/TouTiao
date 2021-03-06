package com.kiwi.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.kiwi.toutiao.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Kiwi
 * @date 2019/4/26 14:47
 * 七牛云service
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    /**设置好账号的ACCESS_KEY和SECRET_KEY,以及要上传的空间*/
    String ACCESS_KEY = "6hqNmiV4mpcLyNQ-_6IQe_vWo3D4D-UYYKwjcRNF";
    String SECRET_KEY = "dXEOFsxZxhMt4f13eZqyYAER4iNOxYqa4ePtHIKf";
    String bucketname = "kiwi";

    /**密钥配置*/
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    /**创建上传对象*/
    UploadManager uploadManager = new UploadManager();

    private static String QINIU_IMAGE_DOMAIN = "http://pqnu9jox0.bkt.clouddn.com/";

    /**简单上传，使用默认策略，只需要设置上传的空间名就可以了*/
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            //调用put方法上传,先把二进制存进去，文件名，规范
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            //打印返回的信息
            if (res.isOK() && res.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            logger.error("七牛异常:" + e.getMessage());
            return null;
        }
    }
}