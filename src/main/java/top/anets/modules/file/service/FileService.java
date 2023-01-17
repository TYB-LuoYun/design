package top.anets.modules.file.service;

import top.anets.modules.file.model.File;

import java.util.List;

/**
 * @author ftm
 * @date 2023/1/6 0006 15:45
 */
public interface FileService {
    public File upLoadFile(String path, String name, String extName, Long size, long parentId, Integer userId, long uploadRootId);
    public Long upLoadFileAndReturnFid(String path, String name, String extName, Long size, long parentId, Integer userId);
    public List<File> getUserFiles(Integer uid, long parentId);


    public File getFileByFid(long fid);

    public List<File> getRepeatFileByFname(long parentId, Integer userId, String fname);


    public void deleteFile(long fid);


    public long saveDir(long parentId, Integer userId, String fname);


    public List<File> getRepeatDirFname(long parentId, Integer userId, String fname);


    public List<File> getChildren(long parentId, Integer userId);



    public List<File> getClassifyedFiles(Integer userId, Integer fidCid);


    public List<File> getFilesByKey(Integer userId, String key);

    public void updateShare(Long fid, String address, String password);

    public File getFileByAddress(String address);

    List<File> getProgeny(Long fid);
}
