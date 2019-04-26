package com.kiwi.toutiao.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * @author Kiwi
 * @date 2019/4/25 14:53
 * 工具类，部分函数可以做成静态的。
 */
public class ToutiaoUtil {
    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);

    /**整个域名的前缀，要保证机器有写入权限*/
    public static String TOUTIAO_DOMAIN = "http://127.0.0.1.8080/";
    /**本地上传文件列表*/
    public static String IMAGE_DIR = "D:/upload/";
    /**图片可能格式*/
    public static String[] IMAGE_FILE_EXTD = new String[]{"png","bmp","jpg","jpeg"};

    /**对图片扩展名进行判断*/
    public static boolean isFileAllowed(String fileName){
        for (String ext: IMAGE_FILE_EXTD){
            if (ext.equals(fileName))
                return true;
        }
        return false;
    }

    /**查看调用是否成功，查看code是0还是1*/
    public static String getJSONString(int code){
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg){
        JSONObject json = new JSONObject();
        json.put("code", code);
        //返回消息
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map){
        JSONObject json = new JSONObject();
        json.put("code", code);
        //把map里面的值保存进来
        for (Map.Entry<String, Object> entry : map.entrySet()){
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }

    /**对密码进行MD5加密*/
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }
}
