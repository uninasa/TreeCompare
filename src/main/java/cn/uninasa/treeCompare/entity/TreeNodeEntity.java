package cn.uninasa.treeCompare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "treeNode",schema = "tree")
public class TreeNodeEntity {
    @Id
    private UUID id;
    @Column(name = "pid")
    private UUID pid;
    @Column(name = "index")
    private Integer index;
    @Column(name = "name")
    private String name;
}
