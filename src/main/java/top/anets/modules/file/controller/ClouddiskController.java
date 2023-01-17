package top.anets.modules.file.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.anets.modules.file.model.File;
import top.anets.modules.file.service.FileClientService;
import top.anets.modules.file.service.FileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ftm
 * @date 2023/1/6 0006 15:39
 */
@RequestMapping("file")
@RestController
public class ClouddiskController {
    @Autowired
    private FileService fileService;

    @Autowired
    private FileClientService client;

    @RequestMapping("uploadDir.action")
    public File fileUploadDir(MultipartFile file, HttpServletRequest request){
        String  filepath=null;
        try {
            String  filename= file.getOriginalFilename();
            String extName = filename.substring(filename.lastIndexOf(".")+1);

            Long parentId = Long.parseLong(request.getParameter("parentId"));
            Integer userId = Integer.parseInt(request.getParameter("userId"));
            String path = request.getParameter("webkitRelativePath");

            System.out.println("filename:"+filename);
            System.out.println("path:"+path);

            String nextpath=path;
            Long uploadRootId = null;
            //解析路径
            while(nextpath.indexOf("/")!=-1){
                String dir =nextpath.substring(0, nextpath.indexOf("/"));
//		    	Check if  it is repeated
                List<File> repeatedDirs = fileService.getRepeatDirFname(parentId, userId,dir);
                if(repeatedDirs!=null&&repeatedDirs.size()>0){
                    parentId=repeatedDirs.get(0).getFid();
                }else{
                    //		    	create directory
                    parentId = fileService.saveDir(parentId, userId, dir);
                }
                nextpath= nextpath.substring(nextpath.indexOf("/")+1);
                if(uploadRootId == null){
                    uploadRootId = parentId;
                }
            }
            filename=nextpath;
            //文件上传
            //check if it is repeated
            List<File> list = fileService.getRepeatFileByFname(parentId, userId, filename);

            File cloundFile = null;
            if(list!=null&&list.size()>0){
                for (File filer : list) {
                    //delete repeat files
                    System.out.println("repeated files,deleting...:"+filer.getFid());
                    Boolean deleteFile = client.deleteFile(filer.getAddress());
                    //delete data in database
                    fileService.deleteFile(filer.getFid());
                    if(!deleteFile){
                        throw new RuntimeException("替换时发生错误");
                    }
                }
                //replace
//                filepath = client.uploadFile(file.getBytes(), extName);
                filepath= client.fileUpload(file,filename,null);
                if(StringUtils.isNoneBlank(filepath)){
                    cloundFile = fileService.upLoadFile(filepath, filename, extName, file.getSize(),parentId,userId,uploadRootId);
                }

            }else {
//                filepath = client.uploadFile(file.getBytes(), extName);
                filepath= client.fileUpload(file,filename,null);
                if(StringUtils.isNoneBlank(filepath)){
                    cloundFile = fileService.upLoadFile(filepath, filename, extName, file.getSize(),parentId,userId,uploadRootId);
                }
            }
            return cloundFile;
        } catch (Exception e) {
            try {
                if(filepath!=null&&client!=null){
                    Boolean deleteFile = client.deleteFile(filepath);
                    System.out.println("出错！已经删除源文件");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping("/list.action")
    public List<File> getUserFiles(Integer uid,long parentId){
        List<File> list =null;
        try {
            list = fileService.getUserFiles(uid, parentId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return   list;
    }



}
