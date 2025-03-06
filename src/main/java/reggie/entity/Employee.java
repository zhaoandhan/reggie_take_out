package reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
//映射工具：MyBatis-Plus
@Data//@Data注解是Lombok的，用于自动生成getter、setter、toString()、equals()等方法
public class Employee implements Serializable {//实现 Serializable 接口并定义 serialVersionUID，支持对象序列化（如缓存、RPC 传输）
    //实体类通常对应数据库的表，spring通过其他模块或框架，比如Spring Data JPA或者MyBatis，来实现这种映射
    //Spring Data JPA是Spring生态系统中的一部分，它简化了数据访问层的开发
    //JPA（Java Persistence API）是Java EE的一个规范
    // 实体类和数据库的映射主要是通过JPA的注解来完成的，比如@Entity、@Table、@Id、@Column等。
    // 这些注解告诉JPA实现（如Hibernate）如何将Java类映射到数据库表。例如，@Entity标记一个类为实体类，对应数据库表,@Table(name = "table_name")指定对应的数据库表名，@Id表示主键，@GeneratedValue定义主键生成策略（如自增、UUID）,@Column(name = "column_name")定义字段与列的映射关系。
    //Spring Data JPA进一步简化了这些操作，通过Repository接口自动生成CRUD方法，用户只需要定义接口并继承JpaRepository，就可以获得基本的数据库操作方法，而无需编写具体的实现代码。此外，Spring通过依赖注入管理这些Repository实例，使得在服务层中可以方便地注入并使用它们。
    //如果是使用MyBatis的话，映射方式有所不同，通常是通过XML文件或注解来配置SQL语句和结果映射。MyBatis需要手动编写SQL语句，并通过映射文件或注解将查询结果映射到Java对象。MyBatis需要手动配置。
    //spring Boot的自动配置机制根据依赖（如Hibernate）自动配置了JPA的相关设置，使得开发者只需要关注实体类的注解和Repository接口的定义。
    //实体类和数据库的映射是通过JPA规范及其实现（如Hibernate）完成的，而Spring框架（特别是Spring Data JPA）简化了这一过程，提供了更高层次的抽象和自动化配置，使得开发者可以更便捷地进行数据库操作。
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;//驼峰命名法，对应数据库中的id_number，在application.yml中开启了这种映射方法

    private Integer status;
    @TableField(fill=FieldFill.INSERT)//插入时使用填充字段，就是比如有好几张表，都有这个公共字段，mybatis plus就可以帮我自动填充
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)// 注解处理字段填充和列名映射，实现自动填充，仅在插入时填充
    //@TableField指定哪些是公共字段
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)// 在插入和更新时填充
    private Long updateUser;

}
