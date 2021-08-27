package module.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lubin
 * @since 2021-08-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("manage_permission")
public class ManagePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 上级ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 路由路径
     */
    @TableField("path")
    private String path;

    /**
     * 路由组件
     */
    @TableField("component")
    private String component;

    /**
     * 重定向路径
     */
    @TableField("redirect")
    private String redirect;

    /**
     * 路由标题信息
     */
    @TableField("title")
    private String title;

    /**
     * 路由图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 权限简称
     */
    @TableField("permission")
    private String permission;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Timestamp createTime;


}
