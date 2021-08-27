package module.shiro.realm.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author lubin
 * @date 2021/8/12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RealmBean implements Serializable {

    private Long id;

    private List<Long> roleList;

}
