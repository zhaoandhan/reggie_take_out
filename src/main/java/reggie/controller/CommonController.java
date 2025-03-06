package reggie.controller;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reggie.common.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * ClassName:CommonController
 * Package:
 * Description:文件上传和下载
 *
 * @Author 赵琪
 * @Create 2025-03-03 14:26
 * @Version 1.0
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")//@Value 注解可以用来将外部的值动态注入到 Bean 中
    private String basePath;
    /**
     * 文件上传，将本地文件上传到服务器上，可以供其他用户浏览或下载，采用Multipart格式上传文件
     * @param file
     * @return
     */
    //服务端要接收客户端页面上传的文件，本要使用commoms-fileupload和commons-io两个组件，
    // spring框架在spring-web包中对文件上传进行了封装，大大简化了服务端代码，只要在controller的方法中声明一个MultipartFile类型的参数即可接收上传的文件
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {//这个"file"是因为前端传来的数据的名字就叫这个，名字要保持一致
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());
        String originalFilename=file.getOriginalFilename();//原始文件名
        String suffix=originalFilename.substring(originalFilename.lastIndexOf("."));//获取".jpg"
        //使用UUID重新生成文件名，防止文件名重复造成
        String fileName = UUID.randomUUID().toString()+suffix;
        //创建一个目录对象
        File dir=new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在，需要创建
            dir.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath+fileName));
        }catch (IOException e){
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载，文件上传后，也要在客户端显示
     * 文件从服务器传输到本地计算机的过程，本质上是服务端将文件以流的形式写回浏览器的过程
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws FileNotFoundException {//通过输出流像浏览器写回数据，不需要返回值
        try{
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();//创建输出流对象
            response.setContentType("image/jpeg");//设置输出的是什么类型的文件
            int len=0;
            byte[] bytes=new byte[1024];
            while((len=fileInputStream.read(bytes))!=-1){//每次循环读bytes数组那么多，len=-1表示读完了
                outputStream.write(bytes,0,len);//写
                outputStream.flush();//刷新
            }
            fileInputStream.close();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
