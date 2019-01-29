package com.magustek.szjh.utils.base;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * 数据库表父类，提供创建日期、创建人、最后修改日期、最后修改人字段
 *
 * */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends BasePage{
    @TableGenerator(
             name="ID_GENERATOR",
             table = "jpa_id_generator",
             pkColumnName = "id_name",
             pkColumnValue = "custom_id",
             valueColumnName = "value_id",
             initialValue = 1,
             allocationSize=100)
    @GeneratedValue(generator = "ID_GENERATOR") @Id private Long id;                            //记录主键，自动生成
    @CreatedDate @Column(name = "CRTIME") protected Date createDate;//创建日期
    @CreatedBy @Column(name = "CRNAME",length = 40) protected String creator;//创建人
    @LastModifiedDate @Column(name = "CHDATE") protected Date updateDate;//最后修改日期
    @LastModifiedBy @Column(name = "CHNAME",length = 40) protected String updater;//最后修改人
    @Column(name = "status",length = 10) protected String status;//最后修改人

    public void copyCreate(BaseEntity baseEntity){
        this.createDate = baseEntity.getCreateDate();
        this.creator = baseEntity.getCreator();
    }
    public String toJson(){
        return JSON.toJSONString(this);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
