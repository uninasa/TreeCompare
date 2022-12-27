package cn.uninasa.treeCompare.dao;

import cn.uninasa.treeCompare.entity.TreeNodeEntity;

import java.util.List;
import java.util.UUID;

public interface TreeNodeDao {
    List<TreeNodeEntity> findAllByPid(UUID pid);
    List<TreeNodeEntity> findAll();
    Integer deleteByIds(List<UUID> list);
    TreeNodeEntity save(TreeNodeEntity entity);
}
