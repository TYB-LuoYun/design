package top.anets.modules.file.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ftm
 * @since 2021-11-17
 */
@Slf4j
@Component
public class FileClientService {



    @Value("${file.ROOT_PATH}")
    private String ROOT_PATH;


    /**
     * 默认的分片大小：20MB
     */
    public static final long DEFAULT_CHUNK_SIZE = 20 * 1024 * 1024;

//    public String fileUpload(MultipartFile file, String  RELATIVE_PATH, String name ,Integer type)  throws Exception{
//        return this.fileUpload( file, RELATIVE_PATH,name,type);
//    }

    public String fileUpload(MultipartFile file,String name, Integer type)  throws Exception{
        String RELATIVE_PATH = new SimpleDateFormat("yyyy").format(new Date())+File.separator+new SimpleDateFormat("MM").format(new Date())+File.separator+new SimpleDateFormat("dd").format(new Date());
        return this.fileUpload( file, RELATIVE_PATH,name,type);
    }

    public String fileUpload(MultipartFile file)  throws Exception{
        String RELATIVE_PATH = new SimpleDateFormat("yyyy").format(new Date())+File.separator+new SimpleDateFormat("MM").format(new Date())+File.separator+new SimpleDateFormat("dd").format(new Date());
        return this.fileUpload( file, RELATIVE_PATH,null,null);
    }

    public String fileUpload(MultipartFile file, Integer type)  throws Exception{
        String RELATIVE_PATH = new SimpleDateFormat("yyyy").format(new Date())+File.separator+new SimpleDateFormat("MM").format(new Date())+File.separator+new SimpleDateFormat("dd").format(new Date());
        return this.fileUpload( file, RELATIVE_PATH,null,type);
    }


    /**
     * 分片上传功能
     * @param filename 文件名
     * @param file 分片文件
     * @param chunkNumber 当前分片
     * @param chunkSize 分片大小
     * @param totalChunks 总分片数
     * @param currentChunkSize 现在分片大小
     * @param totalSize 总分片大小
     * @param identifier 唯一标识
     *
     * 下面是前端JS代码
     * import axios from 'axios'
     * export const uploadByPieces = (  {file, pieceSize = 15,  success, error }) => {
     *     // let fileMD5 = ''// 总文件列表
     *     if(!pieceSize){
     *          pieceSize =15
     *     }
     *     // 获取md5
     *     const md5 = (val) => {
     *         return val;
     *     }
     *     let md5String=md5(file.name+new Date().getTime())//当前文件唯一标识  文件名称+时间 MD5加密的唯一标识
     *     var i = 0
     *     const chunkSize = pieceSize * 1024 * 1024 // 5MB一片
     *     const chunkCount = Math.ceil(file.size / chunkSize) // 总片数
     *     console.log(file,chunkCount);
     *       const readFileMD5 = () => {
     *         readChunkMD5()
     *       }
     *       // 针对每个文件进行chunk处理
     *       const readChunkMD5 = () => {
     *         //获取当前需要上传的分片文件
     *         const { chunk } = getChunkInfo(file, i, chunkSize)
     *         // 上传（分片文件，当前分片位置，分片总数）
     *         uploadChunk({ chunk, currentChunk: i, chunkCount })
     *       }
     *       const getChunkInfo = (file, currentChunk, chunkSize) => {
     *         let start = currentChunk * chunkSize
     *         let end = Math.min(file.size, start + chunkSize)
     *         let chunk = file.slice(start, end)
     *         return { start, end, chunk }
     *       }
     *       const uploadChunk = (chunkInfo) => {
     *         console.log(md5String);
     *       // 创建formData，下面是结合不同项目给后端传入的对象。
     *         let fetchForm = new FormData()
     *         fetchForm.append('file', chunkInfo.chunk) //当前的分片文件
     *          fetchForm.append('identifier', md5String)//原文件唯一标识
     *          fetchForm.append('totalSize', file.size)//原文件大小
     *          fetchForm.append('filename', file.name)//原文件名称
     *          fetchForm.append('chunkNumber',chunkInfo.currentChunk + 1)//当前分片 注文件时从0开始的  所以这里+1
     *          fetchForm.append('totalChunks',chunkCount) //分片总数
     *          fetchForm.append("chunkSize",chunkSize)//分片大小
     *          fetchForm.append("currentChunkSize",chunkInfo.chunk.size)//当前分片文件的大小
     *          axios.request(
     *             {
     *                 method:"post",
     *                 url:"http://127.0.0.1:8025/api/upload?currentChunk="+(chunkInfo.currentChunk + 1),
     *                 data:fetchForm,
     *                 headers: {
     *                     'Content-Type': 'multipart/form-data'
     *                 }
     *             }
     *         ).then((res)=>{
     *             if(res.status==200){
     *                let result = res.data;
     *                console.log(result);
     *                 if(i < chunkCount-1){
     *                    i++
     *                    readChunkMD5()
     *                    //实时返回进度    这个code码是自己定义的 可修改 不要和后端的返回码中途
     *                    let progress = ((chunkInfo.currentChunk + 1) /  chunkCount) * 100 + "%"; //百分比进度
     *                  success({process:progress})
     *                 }else{
     *                     console.log("结束");
     *                    //上传完成
     *                    success({process:"100%"})
     *                  }
     *             }else{
     *                 console.log("网络异常")
     *             }
     *         }).catch(res=>{
     *             console.log("请求异常，异常片："+i)
     *             console.log(res)
     *             //重新请求
     *             readChunkMD5()
     *
     *         });
     *      }
     *      readFileMD5() // 开始执行代码
     * }
     * @return
     */
    public boolean fileChunkUpload(String filename, MultipartFile file, Integer chunkNumber, Long chunkSize, Integer totalChunks, String currentChunkSize, String totalSize, String identifier, String relativePath, Integer type){
        if(file == null){
            return true;
        }
        // 这里可以使用 uuid 来指定文件名，上传完成后再重命名，File.separator指文件目录分割符，win上的"\",Linux上的"/"。
        String fullFileName = null;


        String extName = filename.substring(filename.lastIndexOf(".")+1);
        String typeName = "all";
        if(type == null){
            typeName = getType(extName);
        } else{
            typeName = Type.getDir(type);
        }
        String FILE_PATH = null;
        if(StringUtils.isBlank(relativePath)) {
            relativePath = new SimpleDateFormat("yyyy").format(new Date())+File.separator+new SimpleDateFormat("MM").format(new Date())+File.separator+new SimpleDateFormat("dd").format(new Date());

        }
        FILE_PATH = ROOT_PATH+File.separator+ typeName+File.separator+relativePath;

        fullFileName=FILE_PATH +File.separator+ filename;
        // 单文件上传
        if (totalChunks == 1) {
            try {
                this.fileUpload(file,null);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        // 分片上传，这里使用 uploadFileByRandomAccessFile 方法，也可以使用 uploadFileByMappedByteBuffer 方法上传
        boolean flag = uploadFileByRandomAccessFile(fullFileName,chunkSize,chunkNumber,file );
        if (!flag) {
            return false;
        }
        // 保存分片上传信息
//        fileChunkService.saveFileChunk(param);
        return true;
    }


    private boolean uploadFileByRandomAccessFile(String resultFileName, Long chunkSize, Integer chunkNumber , MultipartFile file) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(resultFileName, "rw")) {
            // 分片大小必须和前端匹配，否则上传会导致文件损坏
            chunkSize = chunkSize == 0L ? DEFAULT_CHUNK_SIZE : chunkSize;
            // 偏移量
            long offset = chunkSize * (chunkNumber - 1);
            // 定位到该分片的偏移量
            randomAccessFile.seek(offset);
            // 写入
            randomAccessFile.write(file.getBytes());
        } catch (IOException e) {
            log.error("文件上传失败：" + e);
            return false;
        }
        return true;
    }






    public static void main(String[] args) throws InterruptedException {
        String relative_path ="sey/6665/er.png";
        String now =relative_path.substring(relative_path.lastIndexOf("/")+1);
        System.out.println(now);
    }




    /**
     *
     * @param file
     * @param name
     * @return
     * @throws Exception
     */
    public String fileUpload(MultipartFile file, String  RELATIVE_PATH, String name, Integer type)  throws Exception  {

//        log.info("接受到文件:"+file.getName());
        String  originalFilename= file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        String typeName = "all";
        if(type == null){
            typeName = getType(extName);
        } else{
            typeName = Type.getDir(type);
        }
        String FILE_PATH = ROOT_PATH+File.separator+ typeName+File.separator+RELATIVE_PATH;




        long start = System.currentTimeMillis();
        long time = new Date().getTime();
        if(StringUtils.isBlank( name)){
            name =time+originalFilename;
        }
        File targetDir = new File(FILE_PATH   );
        if(targetDir.exists()==false){
            targetDir.mkdirs();
        }


        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File( targetDir,name)));
        BufferedInputStream reader = new BufferedInputStream(file.getInputStream());
        double begin=(double)reader.available();//获取可用字节

        //byte[]数组的大小，根据复制文件的大小可以调整，1G一下可以5M。1G以上150M，自己多试试
        byte[] b=new byte[1024*5];
        int len=0;
        String progress=null;
        //获取文件大小
        //创建对象并且赋值总大小
//        ConsoleProgressBarDemo cpb = new ConsoleProgressBarDemo(100, '░');

        int i = 0;
        while((len=reader.read(b))!=-1){
            outputStream.write(b,0,len);
            outputStream.flush();
            //显示进度
            if(!String.format("%.2f",(1-reader.available()/begin)*100).equals(progress)){
//                    System.out.println("ava"+reader.available());
                progress=String.format("%.2f",(1-reader.available()/begin)*100);
//                    System.out.println("progress:"+progress+"%");
//                    System.out.println("len"+len);
                double v = begin - reader.available();
//                    cpb.show((int) ((1-reader.available()/begin)*100));
            }
        }
//            cpb.show((int) ((1-0)*100));
        reader.close();
        outputStream.close();
        long end = System.currentTimeMillis();
        log.info("上传完成:"+(RELATIVE_PATH+File.separator+typeName+File.separator+name).replaceAll(Matcher.quoteReplacement(File.separator), "/"));
        System.out.println("time consuming:"+(end-start)+"ms");
        return (typeName+File.separator +RELATIVE_PATH+File.separator+name).replaceAll(Matcher.quoteReplacement(File.separator), "/");
    }




    public void getFile(HttpServletRequest request, HttpServletResponse response, String FILE_RELATIVE_URL, Integer type, boolean isDown) throws IOException {
        String typeName = "all";
        String extName = FILE_RELATIVE_URL.substring(FILE_RELATIVE_URL.lastIndexOf(".")+1);
        if(type == null){
            typeName = getType(extName);
        } else{
            typeName = Type.getDir(type);
        }
        String relative = typeName+File.separator+ FILE_RELATIVE_URL;
        this.getFile(  request,   response,relative ,isDown);

    }
    public void getImage(HttpServletRequest request, HttpServletResponse response, String FILE_RELATIVE_URL) {
        String FILE_PATH = ROOT_PATH+File.separator +FILE_RELATIVE_URL;
        String extName = FILE_RELATIVE_URL.substring(FILE_RELATIVE_URL.lastIndexOf(".")+1);
        OutputStream os = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(new FileInputStream(new File(FILE_PATH)));

            response.setContentType("image/"+extName);
            os = response.getOutputStream();
            if (image != null) {
                ImageIO.write(image, extName, os);
            }
        }catch (Exception e){
            log.info(e.getMessage());
//            e.printStackTrace();
        }finally {
            if (os != null) {
                try {
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void getFile(HttpServletRequest request, HttpServletResponse response, String FILE_RELATIVE_URL, boolean isDown ) throws IOException {
        String FILE_PATH = ROOT_PATH+File.separator +FILE_RELATIVE_URL;
        File file = new File(FILE_PATH );
        System.out.println("目录地址："+file);
        ServletContext servletContext = request.getServletContext();
        ServletOutputStream outputStream = response.getOutputStream();

        if(isDown  == true){
            response.setHeader("content-disposition","attachment;filename="+file);
        }
        long start = System.currentTimeMillis();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        double begin=(double)reader.available();//获取可用字节

        //byte[]数组的大小，根据复制文件的大小可以调整，1G一下可以5M。1G以上150M，自己多试试
        byte[] b=new byte[1024*5];
        int len=0;
        String progress=null;
//        PrintProgressBar printProgressBar = new PrintProgressBar(reader.available()) ;
//        ConsoleProgressBarDemo cpb = new ConsoleProgressBarDemo(100, '░');
        while((len=reader.read(b))!=-1){
            outputStream.write(b,0,len);
            outputStream.flush();
            //显示进度
            if(!String.format("%.2f",(1-reader.available()/begin)*100).equals(progress)){
                progress=String.format("%.2f",(1-reader.available()/begin)*100);
//                System.out.println("progress:"+progress+"%");
//                cpb.show((int) ((1-reader.available()/begin)*100));
            }
        }
//        cpb.show((int) ((1-0)*100));
        reader.close();
        outputStream.close();
        long end = System.currentTimeMillis();
        System.out.println("time consuming:"+(end-start)+"ms");

    }

    //  根据文件扩展名判断类型
    public String getType(String extName){
        String fidCid =null;
        if("gif,jpg,bmp,png,psd,ico".indexOf(extName)!=-1){
            fidCid= Type.IMG.getDir();
        }else if("rar,7z,zip".indexOf(extName)!=-1){
            fidCid= Type.ZIP.getDir();
        }else if("exe,apk".indexOf(extName)!=-1){
            fidCid= Type.EXE.getDir();
        }else if("avi,rmvb,3gp,flv,mp4".indexOf(extName)!=-1){
            fidCid= Type.VIDEO.getDir();
        }else if("mp3,wav,krc,lrc".indexOf(extName)!=-1){
            fidCid= Type.AUDIO.getDir();
        }else if("doc,excel,ppt,pptx,pdf,chm,txt,md,docx,xls,xlsx,html,css,js,java".indexOf(extName)!=-1){
            fidCid= Type.OTHERS.getDir();
        }
        return fidCid;
    }

    public Integer getTypeCode(String extName){
        Integer fidCid =null;
        if("jpeg,gif,jpg,bmp,png,psd,ico".indexOf(extName)!=-1){
            fidCid= Type.IMG.value;
        }else if("rar,7z,zip".indexOf(extName)!=-1){
            fidCid= Type.ZIP.value;
        }else if("exe,apk".indexOf(extName)!=-1){
            fidCid= Type.EXE.value;
        }else if("avi,rmvb,3gp,flv,mp4".indexOf(extName)!=-1){
            fidCid= Type.VIDEO.value;
        }else if("mp3,wav,krc,lrc".indexOf(extName)!=-1){
            fidCid= Type.AUDIO.value;
        }else if("doc,excel,ppt,pptx,pdf,chm,txt,md,docx,xls,xlsx,html,css,js,java".indexOf(extName)!=-1){
            fidCid= Type.OTHERS.value;
        }
        return fidCid;
    }

    public Boolean deleteFile(String address) {
        return true;
    }


    public enum Type {
        PDF("PDF","pdf",6),
        IMG("图片","img",7),
        DOCX("文档","docx",10),
        ZIP("压缩包","zip",20),
        EXE("可执行文件","exe",21),
        VIDEO("视频","video",22),
        AUDIO("音频","audio",23),
        OTHERS("其它","other",50),



        TEXT_REPORT("报告打印","text_report",0), // 报告打印 0
        EXAMINATION("检查单打印","examination",1), // 检查单打印 1
        APPLY("病理申请单打印","applay",2), // 病理申请单打印 2
        SIGNATURE("活检标签打印","signature",3), // 活检标签打印3
        CONSENT("知情同意书","consent",4), // 知情同意书 4
        NAME_SIGN("名字签名","name_sign",5), //名字签名 5
        REPORT_TEMPLATE("报告模板","report_template",8), //报告模板 8
        PACSIMG_ZIP("临时pacs影像包","pacsimg_zip",9), //临时pacs影像包 9
        TEMPORARY_DOCX("临时docx文件","temporary_docx",11),//临时docx文件 11
        TEMPORARY_HTML("临时HTML","temporary_html",12),//临时HTML 12

        HOSPITAL_lOGO("院区logo","hospital_logo",13),//院区logo 13
        WPACS_IMG("wpacs图片","wpacs_img",14),//wpacs图片 14
        JASPER("ireport模板","jasper",15),//ireport模板 15
        IDENTITY("身份证图片","identity",16),//身份证图片 16
        DOCTOR_PROVE_CER("医师资质证书","doctor_prove_cer",17);//医师资质证书  17

        private String name;
        private String dir;
        private Integer value;
        Type(String name,String dir , int value) {
            this.dir = dir;
            this.name = name;
            this.value = value;
        }

        public String getDir() {
            return this.dir;
        }
        public String getName() {
            return this.name;
        }
        public Integer getValue() {
            return this.value;
        }

        public static String getDir(Integer type){
            Type[] values = Type.values();
            for (Type item:values) {
                if(item.getValue().equals(type)){
                    return  item.getDir();
                }
            }
            return "all";
        }
    }
}

