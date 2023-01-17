package top.anets.modules.file.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author sghc
 * @since 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class File extends Model<File> {

    private static final long serialVersionUID=1L;

      @TableId(value = "fid", type = IdType.AUTO)
    private Long fid;

    /**
     * 所属用户id
     */
    private Integer fidUid;

    /**
     * 分类id,3代表电子专票，4代表电子普票
     */
    private Integer fidCid;

    private String fname;

    /**
     * 文档树的上级id
     */
    private Long parentId;

    @TableField("isDir")
    private Integer isDir;

    /**
     * 预览地址,相对
     */
    private String preview;

    /**
     * 文件地址,相对
     */
    private String address;

    private Long size;

    /**
     * 后缀
     */
    private String suffix;

    private String updatetime;

    /**
     * 是否公开
     */
    @TableField("isPublic")
    private Integer isPublic;

    /**
     * 是否分享
     */
    @TableField("isShare")
    private Integer isShare;

    /**
     * 分享密码
     */
    @TableField("sharePassword")
    private String sharePassword;

    /**
     * 分享地址
     */
    @TableField("shareAddress")
    private String shareAddress;

    /**
     * 扩展字段
     */
    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String attr5;

    private Long uploadRootId;

    public File(Integer fidUid, Integer fidCid, String fname, Long parentId, Integer isdir, String address,
                Long size, String suffix, String updatetime, Integer ispublic, Integer isshare, String sharepassword,
                String shareaddress) {
        super();
        this.fidUid = fidUid;
        this.fidCid = fidCid;
        this.fname = fname;
        this.parentId = parentId;
        this.isDir = isdir;
        this.address = address;
        this.size = size;
        this.suffix = suffix;
        this.updatetime = updatetime;
        this.isPublic = ispublic;
        this.isShare = isshare;
        this.sharePassword = sharepassword;
        this.shareAddress = shareaddress;
    }

    public File() {

    }

    @TableField(exist = false)
    private List<File> children;


}
