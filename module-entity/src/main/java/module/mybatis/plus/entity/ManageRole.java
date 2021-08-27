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
 * @since 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("manage_role")
public class ManageRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 角色名称
     */
    @TableField("name")
    private String name;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建id
     */
    @TableField("create_id")
    private Long createId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Timestamp createTime;


}
