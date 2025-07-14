//package cc.mrbird.febs.server.tjdkxm.util;
//
//import cc.mrbird.febs.common.core.utils.FileUtil;
//import com.alibaba.fastjson.JSONObject;
//import lombok.Data;
//import okhttp3.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 文件
// *
// * @author ZNKJ-R
// * @version V1.0
// * @date 2022/2/17 14:11
// */
//@Data
//public class FileUtils extends FileUtil {
//    /**
//     * 上传地址
//     */
//    private final static String UPLOAD_URL = "http://124.70.177.246:8801/file/uploadFiles";
//    /**
//     * 下载传地址
//     */
//    private final static String DOWNLOAD_URL = "http://124.70.177.246:8900";
//
//    private static final MediaType MULTIPART = MediaType.parse("multipart/form-data; charset=utf-8");
//    private static OkHttpClient okHttpClient = new OkHttpClient();
//
//
//    public static List<JSONObject> upload(MultipartFile[] files) throws IOException {
//        ArrayList<JSONObject> arrayList = new ArrayList<>();
//        Response response = null;
//        OkHttpClient okHttpClient = new OkHttpClient();
//        for (MultipartFile file : files) {
//            RequestBody requestBody = RequestBody.create(MULTIPART, file.getBytes());
//            Request request = new Request.Builder().url(UPLOAD_URL).post(requestBody).build();
//            try {
//                response = okHttpClient.newCall(request).execute();
//                System.out.println(response.body());
//            } catch (Exception e) {
//            } finally {
//                if (response != null) {
//                    response.close();
//                }
//            }
//        }
//
//        return arrayList;
//    }
//
//    public static JSONObject uploadFile(byte[] file,String fileName) throws IOException {
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", fileName,
//                        RequestBody.create(MULTIPART, file))
//                .build();
//
//        Request request = new Request.Builder()
//                .header("BASE_PATH", "tianjiudikuang")
//                .addHeader("Content-Type", "multipart/form-data")
//                .url(UPLOAD_URL)
//                .post(requestBody)
//                .build();
//        Response response = okHttpClient.newCall(request).execute();
//        ResponseBody body = response.body();
//        JSONObject jsonObject=new JSONObject();
//        if (body!=null) {
//            jsonObject = JSONObject.parseObject(body.string());
//        }
//        return jsonObject;
//    }
//
//    public static void main(String[] args) throws IOException {
//        File file = new File("D:\\data\\SVN\\天久地矿\\houduan\\febs-gateway\\target\\febs-gateway-2.2-RELEASE.jar");
//        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
//        BufferedInputStream in = null;
//        try {
//            //文件过大时可能有溢出问题
//            in = new BufferedInputStream(new FileInputStream(file));
//            //声明每次读取长度
//            short bufSize = 1024;
//            //创建读取长度的容器的byte
//            byte[] buffer = new byte[bufSize];
//            //定义标记变量
//            int len1;
//            //read方法的参数含义可以自行百度，第二个参数0并非固定，有不通的含义
//            while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
//                //循环读取文件
//                bos.write(buffer, 0, len1);
//            }
////读取完成的装换成byte数组返回
//            byte[] var7 = bos.toByteArray();
//            RequestBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("file", "123456",
//                            RequestBody.create(MULTIPART, var7))
//                    .build();
//
//            Request request = new Request.Builder()
//                    .header("BASE_PATH", "tianjiudikuang")
//                    .addHeader("Content-Type", "multipart/form-data")
//                    .url(UPLOAD_URL)
//                    .post(requestBody)
//                    .build();
//            Response response = okHttpClient.newCall(request).execute();
//            ResponseBody body = response.body();
//            JSONObject jsonObject=new JSONObject();
//            if (body!=null) {
//                jsonObject = JSONObject.parseObject(body.string());
//                System.out.println("jsonObject = " + jsonObject);
//            }
//
//
//        } finally {
//            try {
//                //最后关闭流
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException var14) {
//                var14.printStackTrace();
//            }
//
//            bos.close();
//        }
//    }
//
//}