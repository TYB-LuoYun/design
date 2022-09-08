package top.anets.modules.file.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class FileService {



    @Value("${file.ROOT_PATH}")
    private String ROOT_PATH;

    public String fileUpload(MultipartFile file, String  RELATIVE_PATH)  throws Exception{
        return this.fileUpload( file, RELATIVE_PATH,null,null);
    }


    public String fileUpload(MultipartFile file)  throws Exception{
        String RELATIVE_PATH = new SimpleDateFormat("yyyy").format(new Date())+File.separator+new SimpleDateFormat("MM").format(new Date())+File.separator+new SimpleDateFormat("dd").format(new Date());
        return this.fileUpload( file, RELATIVE_PATH,null,null);
    }

    public String fileUpload(MultipartFile file, Integer type)  throws Exception{
        String RELATIVE_PATH = new SimpleDateFormat("yyyy").format(new Date())+File.separator+new SimpleDateFormat("MM").format(new Date())+File.separator+new SimpleDateFormat("dd").format(new Date());
        return this.fileUpload( file, RELATIVE_PATH,null,type);
    }
    public static void main(String[] args) throws InterruptedException {

    }




    /**
     *
     * @param file
     * @param name
     * @return
     * @throws Exception
     */
    public String fileUpload(MultipartFile file, String  RELATIVE_PATH, String name, Integer type)  throws Exception  {
        String FILE_PATH = ROOT_PATH+File.separator+ RELATIVE_PATH;
//        log.info("接受到文件:"+file.getName());
        String  originalFilename= file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        log.info("文件扩展名:"+extName);
        String typeName = "all";
        if(type == null){
            typeName = getType(extName);
        } else{
            typeName = Type.getDir(type);
        }
        long start = System.currentTimeMillis();
        long time = new Date().getTime();
        if(StringUtils.isBlank( name)){
            name =time+originalFilename;
        }
        File targetDir = new File(FILE_PATH + File.separator + typeName);
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
            System.out.println("time consuming:"+(end-start)+"ms");
            return (RELATIVE_PATH+File.separator+typeName+File.separator+name).replaceAll(Matcher.quoteReplacement(File.separator), "/");
    }




    public void getFile(HttpServletRequest request, HttpServletResponse response, String FILE_RELATIVE_URL, Integer type) throws IOException {
        String typeName = "all";
        String extName = FILE_RELATIVE_URL.substring(FILE_RELATIVE_URL.lastIndexOf(".")+1);
        if(type == null){
            typeName = getType(extName);
        } else{
            typeName = Type.getDir(type);
        }
        String relative = typeName+File.separator+ FILE_RELATIVE_URL;
        this.getFile(  request,   response,relative );

    }



    public void getFile(HttpServletRequest request, HttpServletResponse response, String FILE_RELATIVE_URL ) throws IOException {
        String FILE_PATH = ROOT_PATH+File.separator +FILE_RELATIVE_URL;
        File file = new File(FILE_PATH );
        System.out.println("目录地址："+file);
        ServletContext servletContext = request.getServletContext();
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("content-disposition","attachment;filename="+file.getName());

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

