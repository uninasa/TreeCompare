package cn.uninasa.treeCompare.TreeNode;

import cn.uninasa.treeCompare.entity.TreeNodeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

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
        dmlList.delList=new CopyOnWriteArrayList<>();
        dmlList.addList=new CopyOnWriteArrayList<>();
        dmlList.updateList=new CopyOnWriteArrayList<>();
        return dmlList;
    }
}
