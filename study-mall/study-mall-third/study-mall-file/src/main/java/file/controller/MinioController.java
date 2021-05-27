package file.controller;


import cn.study.common.utils.R;
import cn.study.common.utils.StringUtils;
import file.conf.MinioConfig;
import file.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("minio")
@CrossOrigin
public class MinioController {

    @Autowired
    private MinioService minioService;

    @Autowired
    private MinioConfig minioConfig;

    /**
     * 使用minio上传文件
     * @param file 上传的文件
     * @param bucketName 对象存储桶名称
     */
    @PostMapping("/uploadFile")
    public R uploadFile(MultipartFile file, String bucketName) {
        try {
            bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minioConfig.getBucketName();
            if (!minioService.bucketExists(bucketName)) {
                minioService.makeBucket(bucketName);
            }
            String fileName = file.getOriginalFilename();
            String objectName = new SimpleDateFormat("yyyy/MM/dd/").format(new Date()) + UUID.randomUUID().toString().replaceAll("-", "")
                    + fileName.substring(fileName.lastIndexOf("."));
            minioService.putObject(bucketName, file, objectName);
            return R.ok().put("data",minioService.getObjectUrl(bucketName, objectName));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传失败");
        }
    }
}
