package cn.study.file.service.impl;

import cn.study.file.service.MinioService;
import cn.study.file.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Service
public class MinioServiceImpl implements MinioService {
    @Autowired
    private MinioUtil minioUtil;

    /**
     * 判断 bucket是否存在
     *
     * @param bucketName 桶名称
     */
    @Override
    public boolean bucketExists(String bucketName) {
        return minioUtil.bucketExists(bucketName);
    }

    /**
     * 创建 bucket
     *
     * @param bucketName 桶名称
     */
    @Override
    public void makeBucket(String bucketName) {
        minioUtil.makeBucket(bucketName);
    }

    /**
     * 文件上传
     *
     * @param bucketName 桶名称
     * @param objectName 存储文件名
     * @param filename 文件名
     */
    @Override
    public void putObject(String bucketName, String objectName, String filename) {
        minioUtil.putObject(bucketName, objectName, filename);
    }


    @Override
    public void putObject(String bucketName, String objectName, InputStream stream, String contentType) {
        minioUtil.putObject(bucketName, objectName, stream, contentType);
    }

    /**
     * 文件上传
     *
     * @param bucketName 桶名称
     * @param multipartFile 文件
     */
    @Override
    public void putObject(String bucketName, MultipartFile multipartFile, String filename) {
        minioUtil.putObject(bucketName, multipartFile, filename);
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶名称
     * @param objectName 存储文件名
     */
    @Override
    public boolean removeObject(String bucketName, String objectName) {
        return minioUtil.removeObject(bucketName, objectName);
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @param originalName 远程文件名
     * @param response http响应对象
     */
    @Override
    public void downloadFile(String bucketName, String fileName, String originalName, HttpServletResponse response) {
        minioUtil.downloadFile(bucketName, fileName, originalName, response);
    }

    /**
     * 获取文件路径
     *
     * @param bucketName 桶名称
     * @param objectName 存储文件名
     */
    @Override
    public String getObjectUrl(String bucketName, String objectName) {
        return minioUtil.getObjectUrl(bucketName, objectName);
    }
}
