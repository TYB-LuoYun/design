package top.anets.modules.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.anets.modules.file.service.FileService;
import top.anets.utils.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("file")
public class FileController {
    @Autowired
    private FileService fileService;

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
        fileService.getFile(request, response, fileName,type);
    }


    @RequestMapping("getFile")
    @ResponseBody
    void getFile(HttpServletRequest request, HttpServletResponse response, String relativeFileUrl) throws Exception {
        fileService.getFile(request, response, relativeFileUrl);
    }
}