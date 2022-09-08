package com.boyiz.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boyiz.gulimall.product.entity.BrandEntity;
import com.boyiz.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Test
    public void testRedissonClient(){
        System.out.println(redissonClient);

    }

    @Test
    public void testRedis(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //保存
        ops.set("hello","world");
        //查询
        String hello = ops.get("hello");
        System.out.println(hello);
    }

    @Test
    public void upLoad() {
//
//        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//        String endpoint = "oss-cn-shanghai.aliyuncs.com";
//        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = "";
//        String accessKeySecret = "";
//
//        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "gulimall-tstst";
//        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
//        String objectName = "photo.png";
//        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
//        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
//        String filePath = "/Users/boyiz/Downloads/1111.png";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        try {
//            InputStream inputStream = new FileInputStream(filePath);
//            // 创建PutObject请求。
//            ossClient.putObject(bucketName, objectName, inputStream);
//        } catch (
//                OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                    + "but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (
//                ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//                System.out.println("上传完成。");
//            }
//        }


    }


    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("华为");
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
        brandService.updateById(brandEntity);
        System.out.println("SUCCESS");
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1));
        list.forEach(System.out::println);
    }

}
