package com.ragdoll.controller;

import com.ragdoll.aesEncript.EncryFileUtil;
import com.ragdoll.constant.ClientConstant;
import com.ragdoll.dao.FileDao;
import com.ragdoll.entity.ClientResult;
import com.ragdoll.entity.FileInfo;
import com.ragdoll.rsaEncript.RSAEncrypt;
import com.ragdoll.uitls.HttpClientUtil;
import com.ragdoll.uitls.JsonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jianming
 * @create 2021-03-08-23:55
 */
@RestController
public class FileController {


    /**
     * 文件上传
     * @param file
     * @return uuid
     * @throws IOException
     */
    @PostMapping("/file/upload")
    public ClientResult uploadFile(MultipartFile file) throws IOException {
        if (file == null) {
            return ClientResult.build("文件不能为空", 405, null);
        }
        String result = HttpClientUtil.doPostUploadFile(ClientConstant.BASE_SERVER_URL + ClientConstant.SERVER_UPLOAD_FILE_URL,
                file.getInputStream(), file.getOriginalFilename());
        return ClientResult.ok(result);
    }

    /**
     * 查找最新的10条文件信息
     * @return
     */
    @GetMapping("/file/find")
    public ClientResult findNewTenUuid() {
        Map<String,String> param = new HashMap<>();
        param.put("type","new");
        String s = HttpClientUtil.doGet(ClientConstant.BASE_SERVER_URL + ClientConstant.SERVER_FIND_FILE_URL, param);
        return ClientResult.ok(JsonUtils.jsonToList(s, FileInfo.class));
    }

    /**
     * 根据uuid查找文件信息
     * @param uuid
     * @return
     */
    @GetMapping("/file/find/{uuid}")
    public ClientResult findByUuid(@PathVariable("uuid") String uuid) {
        if (uuid == null || "".equals(uuid)) {
            return ClientResult.build("uuid不能为空", 405, null);
        }
        Map<String,String> param = new HashMap<>();
        param.put("type","one");
        param.put("uuid",uuid);
        String s = HttpClientUtil.doGet(ClientConstant.BASE_SERVER_URL + ClientConstant.SERVER_FIND_FILE_URL, param);
        return ClientResult.ok(JsonUtils.jsonToPojo(s, FileInfo.class));
    }


    /**
     * 下载文件
     * @param uuid 要下载的uuid
     * @param response
     * @return
     */
    @GetMapping("/file/download/{uuid}")
    public void downloadFile(@PathVariable("uuid") String uuid, HttpServletResponse response) throws Exception {

        ServletOutputStream out = response.getOutputStream();
        FileInfo fileinfo = FileDao.getFileinfo(uuid);
        if (fileinfo == null || fileinfo.getRandomkey() == null || "".equals(fileinfo.getRandomkey().trim())) {
            out.print("该文件不存在");
            return;
        }

        // 从server端获取随机key加密的文件流
        InputStream inputStream = HttpClientUtil.downloadFile(ClientConstant.BASE_SERVER_URL + ClientConstant.SERVER_UPLOAD_FILE_URL + "?uuid=" + uuid);

        // 使用本地私钥解密公钥加密的随机key
        String randomkey = fileinfo.getRandomkey();
        randomkey = RSAEncrypt.decrypt(randomkey, ClientConstant.PRI);

        // 使用解密后的随机key对文件流进行一个对称性解密
        ServletOutputStream outputStream = response.getOutputStream();
        int length = EncryFileUtil.decryptFile(inputStream, outputStream, randomkey);
        // 输出解密后的文件流
        response.setContentLength(length);
    }

    /**
     * 下载加密文件
     * @param uuid 要下载的uuid
     * @param response
     * @return
     */
    @GetMapping("/file/download1/{uuid}")
    public void download1File(@PathVariable("uuid") String uuid, HttpServletResponse response) throws Exception {

        ServletOutputStream out = response.getOutputStream();
        FileInfo fileinfo = FileDao.getFileinfo(uuid);
        if (fileinfo == null || fileinfo.getRandomkey() == null) {
            out.print("该文件不存在");
            return;
        }

        // 从server端获取随机key加密的文件流
        InputStream inputStream = HttpClientUtil.downloadFile(ClientConstant.BASE_SERVER_URL + ClientConstant.SERVER_UPLOAD_FILE_URL + "?uuid=" + uuid);
        System.out.println("aa " + inputStream);
        // 直接返回文件流
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] bty = new byte[1024];
        int length = 0;
        int res = 0;
        while ((length = inputStream.read(bty)) != -1) {
            res += length;
            outputStream.write(bty, 0, length);
            outputStream.flush();
        }
        // 输出解密后的文件流
        response.setContentLength(res);
    }

}
