package top.anets.modules.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.anets.modules.file.service.FileClientService;
import top.anets.utils.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("file")
@CrossOrigin
public class FileController {
    @Autowired
    private FileClientService fileService;

    @RequestMapping("upload")
    private Result upload(MultipartFile file, Integer type) {
        try {

            String relative_path = fileService.fileUpload(file,type);


            return Result.success("上传成功", relative_path);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }


    @RequestMapping("getFileByType")
    @ResponseBody
    void getFileByName(HttpServletRequest request, HttpServletResponse response, String fileName, Integer type) throws Exception {
//      该资源谁都可以用
        response.setHeader("Access-Control-Allow-Origin","*" );
        response.setHeader("Access-Control-Allow-Credentials","true" );
        if(fileName.endsWith(FileClientService.Type.PDF.getDir())){
            response.setContentType("application/pdf");
        }else{
            response.setContentType("text/plain;charset=UTF-8");
        }

        fileService.getFile(request, response, fileName,type,false);

    }


    @RequestMapping(value ="getFile")
    @ResponseBody
    void getFile(HttpServletRequest request, HttpServletResponse response, String relativeFileUrl) throws Exception {
        //      该资源谁都可以用
        fileService.getImage(request, response, relativeFileUrl);

    }


    @RequestMapping("downFile")
    @ResponseBody
    void downFile(HttpServletRequest request, HttpServletResponse response, String relativeFileUrl) throws Exception {
        //      该资源谁都可以用
         fileService.getFile(request, response, relativeFileUrl,true);


    }


    /**
     * 分片上传功能，需要与前端配合
     */
    @RequestMapping("fileChunkUpload")
    @ResponseBody
    Boolean fileChunkUpload(String filename,MultipartFile file,Integer chunkNumber,Long chunkSize,Integer totalChunks,String currentChunkSize,String totalSize,String identifier,String relativePath,Integer type){
        boolean b = fileService.fileChunkUpload(filename, file, chunkNumber, chunkSize, totalChunks, currentChunkSize, totalSize, identifier, relativePath, type);
        return b;
    }

}