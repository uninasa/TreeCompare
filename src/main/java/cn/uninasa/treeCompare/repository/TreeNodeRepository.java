package cn.uninasa.treeCompare.repository;

import cn.uninasa.treeCompare.entity.TreeNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

public interface TreeNodeRepository extends JpaRepository<TreeNodeEntity, UUID> {

    @Query("SELECT t FROM TreeNodeEntity t WHERE t.pid=:pid")
    List<TreeNodeEntity> findAllByPid(UUID pid);

    @Modifying
    Integer deleteAllByIdIn(@Param("list") List<UUID> list);
}
