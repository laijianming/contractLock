package com.ragdoll.aseEncript;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author jianming
 * @create 2021-03-08-17:04
 */
public class EncryFileUtil {

    /**
     * @param zipOutPath  压缩文件输出的路径
     * @param fileInputStream 要压缩的文件流
     * @throws Exception
     */
    public static void encryptFile(String zipOutPath, InputStream fileInputStream) throws Exception {
        OutputStream zipOutputPathStream = new FileOutputStream(new File(zipOutPath));

        Cipher cipher = AesHelper.getEncryptCipher();
        OutputStream cipherOutputStream = new CipherOutputStream(zipOutputPathStream, cipher);
        ZipOutputStream zipOutputStream = new ZipOutputStream(cipherOutputStream);
        zipOutputStream.putNextEntry(new ZipEntry(""));


        BufferedInputStream bis = new BufferedInputStream(fileInputStream);
        BufferedOutputStream bos = new BufferedOutputStream(zipOutputStream);

        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, len
            );
        }
        bos.flush();
        bos.close();
        bis.close();
        fileInputStream.close();
        zipOutputStream.close();
        cipherOutputStream.close();
        zipOutputPathStream.close();

    }


    /**
     * @param zipInputStream   要解密的压缩文件流
     * @param outputStream 输出流，将解密的文件流保存到输出流中
     * @return 返回文件流长度
     * @throws Exception
     */
    public static int decryptFile(InputStream zipInputStream, OutputStream outputStream) throws Exception {
        Cipher cipher = AesHelper.getDecryptCipher();
        CipherInputStream cipherInputStream = new CipherInputStream(zipInputStream, cipher);
        ZipInputStream decryptZipInputStream = new ZipInputStream(cipherInputStream);
        if (decryptZipInputStream.getNextEntry() == null) {
            return 0;
        }
        BufferedInputStream bis = new BufferedInputStream(decryptZipInputStream);
        byte[] bty = new byte[1024];
        int length = 0;
        int res = 0;
        while ((length = bis.read(bty)) != -1) {
            res += length;
            outputStream.write(bty, 0, length);
        }
        decryptZipInputStream.close();
        cipherInputStream.close();
        zipInputStream.close();
        return res;
    }


}
