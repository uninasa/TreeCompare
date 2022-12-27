package cn.uninasa.treeCompare.dao;

import cn.uninasa.treeCompare.repository.TreeNodeRepository;
import cn.uninasa.treeCompare.entity.TreeNodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.UUID;
@Service
public class TreeNodeDaoImpl implements TreeNodeDao{
    @Autowired
    private TreeNodeRepository repository;

    @Override
    public List<TreeNodeEntity> findAllByPid(UUID pid) {
        return repository.findAllByPid(pid);
    }

    @Override
    public List<TreeNodeEntity> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Integer deleteByIds(List<UUID> list) {
        return repository.deleteAllByIdIn(list);
    }

    @Override
    @Transactional
    public TreeNodeEntity save(TreeNodeEntity entity) {
        return repository.save(entity);
    }
}
