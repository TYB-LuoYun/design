package top.anets.modules.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.anets.modules.file.mapper.FileMapper;
import top.anets.modules.file.model.File;
import top.anets.modules.file.service.FileService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sghc
 * @since 2021-05-21
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Autowired
    private FileMapper fileMapper;
    @Override
    public File upLoadFile(String path, String name, String extName, Long size, long parentId, Integer userId,long uploadRootId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //to judge the file type
        System.out.println("suffix"+extName);
        Integer fidCid;
        if("gif,jpg,bmp,png,psd,ico".indexOf(extName)!=-1){
            fidCid=3;
        }else if("rar,7z,zip".indexOf(extName)!=-1){
            fidCid=7;
        }else if("exe,apk".indexOf(extName)!=-1){
            fidCid=6;
        }else if("avi,rmvb,3gp,flv,mp4".indexOf(extName)!=-1){
            fidCid=1;
        }else if("mp3,wav,krc,lrc".indexOf(extName)!=-1){
            fidCid=2;
        }else if("doc,excel,ppt,pptx,pdf,chm,txt,md,docx,xls,xlsx,html,css,js,java".indexOf(extName)!=-1){
            fidCid=5;
        }else if("torrent".indexOf(extName)!=-1){
            fidCid=4;
        }else{
            fidCid=8;
        }

        File file =new File(userId, fidCid, name, parentId, 0,path, size, extName, simpleDateFormat.format(new Date()), 1, 0, null, null);
        file.setUploadRootId(uploadRootId);
        fileMapper.insert(file);
        return file;
    }
    @Override
    public List<File> getUserFiles(Integer uid, long parentId) {
        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("parent_id",parentId)
                .eq("fid_uid",uid);
        List<File> list = fileMapper.selectList(fileQueryWrapper);
        return list;
    }



    private List<Long> recursiveInvoiceDays(Long parentId) {

//        查parentId的文件
        System.out.println(parentId);
        System.out.println(this==null);
        List<File> files = this.getUserFiles(-1, parentId);
        System.out.println(files==null);
        if(files==null||files.size()<=0){
            List<Long>  nulls = new ArrayList<>();
            return  nulls;
        }
        ArrayList<Long> longs = new ArrayList<>();
        for (File file:files) {
            if(file.getIsDir().equals("0")||(file.getAttr3()!=null&&file.getAttr3().equals("4"))){//遍历到子级了
                longs.add(parentId);
                break;
            }else
            if(file.getAttr3()!=null&&file.getAttr3().equals("3")){
                longs.add(file.getFid());
            }else{
                List<Long> longList = this.recursiveInvoiceDays(file.getFid());
                longs.addAll(longList);
            }
        }
        return longs;
    }

    @Override
    public List<File> getRepeatFileByFname(long parentId, Integer userId, String fname) {
        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("fid_uid",userId)
                .eq("parent_id",parentId)
                .eq("fname",fname)
                .eq("isDir",0);
        List<File> list = fileMapper.selectList(fileQueryWrapper);
        return list;
    }
    @Override
    public void deleteFile(long fid) {
        fileMapper.deleteById(fid);

    }
    @Override
    public long saveDir(long parentId, Integer userId, String fname) {
        File file = new File();
        file.setParentId(parentId);
        file.setFidUid(userId);
        file.setFname(fname);
        file.setFidCid(9);
        file.setIsDir(1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = format.format(new Date());
        file.setUpdatetime(time);
        fileMapper.insert(file);
        System.out.println("==================返回的主键:"+file.getFid());
        return file.getFid();
    }
    @Override
    public List<File> getRepeatDirFname(long parentId, Integer userId, String fname) {
        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("fid_uid",userId)
                .eq("parent_id",parentId)
                .eq("fname",fname)
                .eq("isDir",1);
        List<File> list = fileMapper.selectList(fileQueryWrapper);
        return list;

    }
    @Override
    public File getFileByFid(long fid) {

//		File file = fileMapper.getFileAndUserInfoByFid(fid);
        File file = fileMapper.selectById(fid);
        return file;
    }
    @Override
    public List<File> getChildren(long parentId, Integer userId) {
        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("parent_id",parentId)
                .eq("fid_uid",userId);
        List<File> list = fileMapper.selectList(fileQueryWrapper);
        return list;
    }

    @Override
    public List<File> getClassifyedFiles(Integer userId, Integer fidCid) {
        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("fid_uid",userId)
                .eq("fid_cid",fidCid);
        return fileMapper.selectList(fileQueryWrapper);
    }

    @Override
    public List<File> getFilesByKey(Integer userId, String key) {
        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("fid_uid",userId)
                .eq("fname","%"+key+"%" );
        return fileMapper.selectList(fileQueryWrapper);
    }

    @Override
    public void updateShare(Long fid, String address, String password) {
        File file = fileMapper.selectById(fid);
        file.setIsPublic(1);
        file.setIsShare(1);
        file.setShareAddress(address);
        file.setSharePassword(password);
        fileMapper.updateById(file);
    }
    @Override
    public File getFileByAddress(String address) {
        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("shareAddress",address);
        //		File file = fileMapper.getFileAndUserInfo(address);
        List<File> list = fileMapper.selectList(fileQueryWrapper);

        if(list!=null&&list.size()>0){
            return list.get(0);
        }else {
            return null;
        }

    }



    @Override
    public Long upLoadFileAndReturnFid(String path, String name, String extName, Long size, long parentId,
                                       Integer userId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //to judge the file type
        System.out.println("suffix"+extName);
        Integer fidCid;
        if("gif,jpg,bmp,png,psd,ico".indexOf(extName)!=-1){
            fidCid=3;
        }else if("rar,7z,zip".indexOf(extName)!=-1){
            fidCid=7;
        }else if("exe,apk".indexOf(extName)!=-1){
            fidCid=6;
        }else if("avi,rmvb,3gp,flv,mp4".indexOf(extName)!=-1){
            fidCid=1;
        }else if("mp3,wav,krc,lrc".indexOf(extName)!=-1){
            fidCid=2;
        }else if("doc,excel,ppt,pptx,pdf,chm,txt,md,docx,xls,xlsx,html,css,js,java".indexOf(extName)!=-1){
            fidCid=5;
        }else if("torrent".indexOf(extName)!=-1){
            fidCid=4;
        }else{
            fidCid=8;
        }

        File file =new File(userId, fidCid, name, parentId, 0,path, size, extName, simpleDateFormat.format(new Date()), 1, 0, null, null);
        fileMapper.insert(file);
        return file.getFid();
    }

//    @Override
    public long mkdirs(String dir ) {
        return this.mkdirs(dir, 0,null,null);
    }
//    @Override
    public long mkdirs(String dir, long parentId,Integer userId,String level) {

        log.info("当前路径:"+dir);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updatetime = format.format(new Date());
//		File file =new File(userId, null, name, parentId, 0,null, null,null, simpleDateFormat.format(new Date()), 1, 0, null, null);

        String[] split = dir.split("(\\\\+|/+|,+|\\.+)");
        ArrayList<String> list = new ArrayList<>();
        for (String string : split) {

            if(StringUtils.isNoneBlank(string)) {
                System.out.println("拆分:"+string);
                list.add(string);
            }
        }
        String current = null;
        String next ="";
        for (int i = 0; i < list.size(); i++) {
            current = list.get(0);
            if(i!=0) {
                next+=list.get(i)+',';
            }
        }
        System.out.println("当前:"+current);
        System.out.println("next:"+next);

        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId)
                .eq("fname",current);
        List<File> byExample = fileMapper.selectList(queryWrapper);
        if(byExample!=null&&byExample.size()>0&&byExample.get(0).getIsDir()==1) {
            if(StringUtils.isNotBlank(next)) {
                level=Integer.parseInt(level)+1+"";
                return this.mkdirs(next, byExample.get(0).getFid(),userId,level);
            }else {
                return byExample.get(0).getFid();
            }
        }else {
            File record = new File(userId, null, current, parentId, 1, null, null, null, updatetime, null, null, null,null);
            record.setAttr3(level);
            //如果没有找到，就需要创建目录
            fileMapper.insert(record);
            log.info("创建目录后返回id:"+record.getFid());
            if(StringUtils.isNotBlank(next)) {
                level=Integer.parseInt(level)+1+"";
                log.info("存在下级目录:"+next+";级别:"+level+";用户"+userId);
                long mkdirs = this.mkdirs(next, record.getFid(), userId, level);
                log.info("递归返回的ID："+mkdirs);
                return mkdirs;
            }else {
                return record.getFid();
            }

        }
    }


    @Override
    public List<File> getProgeny(Long fid) {
        List<File> list = this.list(Wrappers.<File>lambdaQuery().eq(File::getParentId, fid));
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        list.forEach(item->{
            item.setChildren(this.getProgeny(item.getFid()));
        });
        return list;
    }

//    @Override
//    public List<File> getInvoiceFiles(Integer fid_uid, Integer fid_cid, Integer pid, String orgId, String invoiceType) {
//        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
//        fileQueryWrapper.eq("fid_uid", fid_uid).eq("fid_cid", fid_cid).eq("parent_id", pid).eq("attr1", orgId);
//        return fileMapper.selectList(fileQueryWrapper);
//    }



    public static void main(String[] args) {
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(13l);

        ArrayList<Long> longs2 = new ArrayList<>();
        longs.addAll(longs2);
        System.out.print(longs.size());
//        String  dir = "3344\\55553\\\\我们";
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String updatetime = format.format(new Date());
////		File file =new File(userId, null, name, parentId, 0,null, null,null, simpleDateFormat.format(new Date()), 1, 0, null, null);
//
//        String[] split = dir.split("(\\\\+|/+|,+|\\.+)");
//        ArrayList<String> list = new ArrayList<>();
//        for (String string : split) {
//            System.out.println(string);
//        }
    }



//    @Override
//    public long upLoadInvoice(String path, String name, String extName, Long size, long parentId, Integer userId,
//                           String preview,InvoiceHead  invoice) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        //to judge the file type
//        System.out.println("suffix"+extName);
//        Integer fidCid;
//        if("gif,jpg,bmp,png,psd,ico".indexOf(extName)!=-1){
//            fidCid=3;
//        }else if("rar,7z,zip".indexOf(extName)!=-1){
//            fidCid=7;
//        }else if("exe,apk".indexOf(extName)!=-1){
//            fidCid=6;
//        }else if("avi,rmvb,3gp,flv,mp4".indexOf(extName)!=-1){
//            fidCid=1;
//        }else if("mp3,wav,krc,lrc".indexOf(extName)!=-1){
//            fidCid=2;
//        }else if("doc,excel,ppt,pptx,pdf,chm,txt,md,docx,xls,xlsx,html,css,js,java".indexOf(extName)!=-1){
//            fidCid=5;
//        }else if("torrent".indexOf(extName)!=-1){
//            fidCid=4;
//        }else{
//            fidCid=8;
//        }
//
//        File file =new File(userId, fidCid, name, parentId, 0,path, size, extName, simpleDateFormat.format(new Date()), 1, 0, null, null);
//        file.setPreview(preview);
//
////        file.setAttr1(invoice.getOrgId());
////        file.setAttr2(invoice.getInvoiceType());
////        file.setAttr4(invoice.getId()+"");
////        file.setAttr5(invoice.getAttribute3());
//
//         fileMapper.insert(file);
//         return file.getFid();
//    }
//
//
//    @Override
//    public void upLoadFile(String path, String name, String extName, Long size, long parentId, Integer userId,
//                           String preview) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        //to judge the file type
//        System.out.println("suffix"+extName);
//        Integer fidCid;
//        if("gif,jpg,bmp,png,psd,ico".indexOf(extName)!=-1){
//            fidCid=3;
//        }else if("rar,7z,zip".indexOf(extName)!=-1){
//            fidCid=7;
//        }else if("exe,apk".indexOf(extName)!=-1){
//            fidCid=6;
//        }else if("avi,rmvb,3gp,flv,mp4".indexOf(extName)!=-1){
//            fidCid=1;
//        }else if("mp3,wav,krc,lrc".indexOf(extName)!=-1){
//            fidCid=2;
//        }else if("doc,excel,ppt,pptx,pdf,chm,txt,md,docx,xls,xlsx,html,css,js,java".indexOf(extName)!=-1){
//            fidCid=5;
//        }else if("torrent".indexOf(extName)!=-1){
//            fidCid=4;
//        }else{
//            fidCid=8;
//        }
//
//        File file =new File(userId, fidCid, name, parentId, 0,path, size, extName, simpleDateFormat.format(new Date()), 1, 0, null, null);
//        file.setPreview(preview);
//        fileMapper.insert(file);
//    }
}
