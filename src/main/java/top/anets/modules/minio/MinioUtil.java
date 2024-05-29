package top.anets.modules.minio;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.minio.*;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
@Lazy
public class MinioUtil {
    @Autowired
    private MinioConfig prop;

    @Resource
    @Lazy
    private MinioClient minioClient;
//
//    @Autowired
//    private FileService fileService;

    /**
     * 占位符
     */
    private static final String BUCKET_PARAM = "${bucket}";

    /**
     * bucket权限-读写
     */
    private static final String READ_WRITE = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";


    /**
     * bucket权限-只写
     */
    private static final String WRITE_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";

    /**
     * bucket权限-只读
     */
    private static final String READ_ONLY = "{\n" +
            "    \"Version\": \"2012-10-17\",\n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:GetBucketLocation\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::" + BUCKET_PARAM + "\"\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:ListBucket\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::" + BUCKET_PARAM + "\"\n" +
            "            ],\n" +
            "            \"Condition\": {\n" +
            "                \"StringEquals\": {\n" +
            "                    \"s3:prefix\": [\n" +
            "                        \"*\"\n" +
            "                    ]\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:GetObject\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::" + BUCKET_PARAM + "/**\"\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";


    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {

            if (bucketExists(bucketName)) {
                throw new RuntimeException("该桶已经存在");
            }
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
            this.setBucketPolitic(bucketName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            return buckets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file, int code) {
        if (file == null) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new RuntimeException();
        }

        //时间戳生成文件名
        String fileName = UuidUtils.generateUuid() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //年月日生成文件夹
        String objectName = MinioModuleEnum.getNameByCode(code) + "/" + fileName;
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return objectName;
    }

//    /**
//     * 文件上传
//     *
//     * @param path 图片路径
//     * @return Boolean
//     */
//    public String uploadOldImg(String path, int code) {
//
//        MultipartFile file = null;
//        try {
//            file = fileService.createMfileByPath(path);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("下载图片异常:{}", path);
//            return null;
//        }
//        return this.upload(file, code);
//    }


    /**
     * 预览图片
     *
     * @param fileName
     * @return
     */
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs().builder().bucket(prop.getBucketName()).object(fileName).method(Method.GET).build();
        try {
            String url = minioClient.getPresignedObjectUrl(build);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param res      response
     * @return Boolean
     */
    public void download(String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                // res.setContentType("application/force-download");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(prop.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

    /**
     * 删除
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(prop.getBucketName()).object(fileName).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 设置桶策略
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean setBucketPolitic(String bucketName) {
        try {

            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(READ_ONLY.replace(BUCKET_PARAM, bucketName)).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public String uploadByUrl(String url, int code,String fileName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        URL orginUrl = new URL(url);
        URLConnection conn = orginUrl.openConnection();
        String objectName = MinioModuleEnum.getNameByCode(code) + "/" + fileName;
        PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(objectName)
                .stream(orginUrl.openStream(), conn.getContentLengthLong(), -1).contentType(conn.getContentType()).build();
        //文件名称相同会覆盖
        minioClient.putObject(objectArgs);
        return objectName;
    }

    public Map<String, Object> uploadDigitalImgZipByUrl(String url, int code,String fileName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        URL orginUrl = new URL(url);
        URLConnection conn = orginUrl.openConnection();

        String objectName = MinioModuleEnum.getNameByCode(code) + "/" + fileName;
        ZonedDateTime expiryTime = ZonedDateTime.now().plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd天HH时mm分");
        String formattedDate = expiryTime.format(formatter);
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .bucket(prop.getDigitalImgZipBucket())
                .object(objectName)
                .stream(orginUrl.openStream(), conn.getContentLengthLong(), -1)
                .contentType(conn.getContentType())
                .build();
        //文件名称相同会覆盖
        int size = conn.getContentLength();
        BigDecimal fileSize = StorageUnitUtil.convert(new BigDecimal(size), StorageUnitUtil.BYTE, StorageUnitUtil.GB, 2);
        minioClient.putObject(objectArgs);
        Map<String, Object> result = new HashMap<>();
        result.put("objectName", objectName);
        result.put("size", fileSize + "GB");
        result.put("expiryTime", formattedDate);
        return result;
    }


    /**
     * 5 设置 bucket 的声明周期，设置对象过期时间
     * @param bucketName 指定bucket，默认是对指定的这一个桶生效
     * @param expiredDays 过期时间 单位:天
     * @return
     */
    public boolean setBucketLifeCycle (@Nonnull String bucketName, @Nonnull Integer expiredDays,@Nonnull Integer code) {
        // 创建一个带过期时间规则的配置config
        List<LifecycleRule> rules = new LinkedList<>();
        rules.add(
                new LifecycleRule(
                        Status.ENABLED,
                        null,
                        new Expiration((ZonedDateTime) null, expiredDays, null),
                        new RuleFilter(MinioModuleEnum.getNameByCode(code)), // bucket下一级的目录
                        "expiredRule_1",
                        null,
                        null,
                        null));
        LifecycleConfiguration config = new LifecycleConfiguration(rules);

        // 根据 config的配置，创建一个名称为 my-bucketname 的 bucket
        try {
            minioClient.setBucketLifecycle(
                    SetBucketLifecycleArgs.builder().bucket(bucketName).config(config).build());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
