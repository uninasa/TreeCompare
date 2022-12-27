package cn.uninasa.treeCompare.TreeNode;

import cn.uninasa.treeCompare.entity.TreeNodeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DmlList {
    //需要删除的节点
    private List<TreeNodeEntity> delList;
    //需要新增的节点
    private List<TreeNodeEntity> addList;
    //需要替换的节点
    private List<TreeNodeEntity> updateList;

    public static DmlList initList(){
        DmlList dmlList=new DmlList();
        dmlList.delList=new ArrayList<TreeNodeEntity>();
        dmlList.addList=new ArrayList<TreeNodeEntity>();
        dmlList.updateList=new ArrayList<TreeNodeEntity>();
        return dmlList;
    }
}
