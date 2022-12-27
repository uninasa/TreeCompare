package cn.uninasa.treeCompare.controller;

import cn.uninasa.treeCompare.TreeNode.DmlList;
import cn.uninasa.treeCompare.TreeNode.Node;
import cn.uninasa.treeCompare.dao.TreeNodeDao;
import cn.uninasa.treeCompare.entity.TreeNodeEntity;
import com.fasterxml.jackson.databind.node.BinaryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.swing.tree.TreeNode;
import java.util.*;
@Api(tags = "树操作")
@CrossOrigin
@RestController
@RequestMapping("/api/tree")
public class TreeNodeController {
    @Autowired
    private TreeNodeDao treeNodeDao;

    @ApiOperation(value = "查找树的list集合")
    @GetMapping("/select")
    public List<TreeNodeEntity> findAllTreeNode(){
        return treeNodeDao.findAll();
    }

    @ApiOperation(value = "修改树")
    @PostMapping("/update")
    @Transactional
    public List<TreeNodeEntity> updateTreeNode(@ApiParam(name = "iList",value = "修改后的树",required = true)
                                               @RequestBody List<TreeNodeEntity> iList){
        //数据库存储的 树数据
        List<TreeNodeEntity> sList=treeNodeDao.findAll();

        //将List转换成树(数据库树 a树、插入树 b树)
        Node<TreeNodeEntity> sNode=new Node<>();
        for(int i=0;sList.get(i).getPid()==null;i++){
            sNode.setNode(sList.get(i));
        }
        Node<TreeNodeEntity> iNode=new Node<>();
        for(int i=0;iList.get(i).getPid()==null;i++){
            iNode.setNode(iList.get(i));
        }
        sNode=transitionTree(sNode,sList);
        iNode=transitionTree(iNode,iList);
        //比较sNode树和iNode树
        DmlList list=compareToTree(sNode,iNode,DmlList.initList());
        //删除节点
        List<UUID> delList=new ArrayList<>();
        list.getDelList().forEach(e->{
            delList.add(e.getId());
        });
        if(delList!=null){
            treeNodeDao.deleteByIds(delList);
        }
        //修改节点
        list.getUpdateList().forEach(e->{
            treeNodeDao.save(e);
        });
        //增加节点
        list.getAddList().forEach(e->{
            treeNodeDao.save(e);
        });
        return treeNodeDao.findAll();
    }

    /**
     * 转换树方法
     * @param node
     * @param list
     * @return
     */
    private Node<TreeNodeEntity> transitionTree(Node<TreeNodeEntity> node, List<TreeNodeEntity> list){

        //叶子节点 当前node无法找到下一个节点
        Boolean isLeaf=true;
        UUID id=node.getNode().getId();
        for (int i=0;i<list.size();i++){
            if(list.get(i).getPid()!=null){
                if(id.compareTo(list.get(i).getPid())==0){
                    isLeaf=false;
                }
            }

        }
        //确定是叶子节点
        if(isLeaf){
            //不执行代码;
        }else{
            //不是叶子节点，则给当前节点找到下一级
            List<Node<TreeNodeEntity>> childList=new ArrayList<>();
            for(int i=0;i<list.size();i++){

                Node<TreeNodeEntity> nodeNew=new Node<>();
                if(list.get(i).getPid()!=null){
                    if(id.compareTo(list.get(i).getPid())==0){
                        nodeNew.setNode(list.get(i));
                        nodeNew=transitionTree(nodeNew,list);
                        childList.add(nodeNew);
                    }
                }

            }
            node.setChildNode(childList);
        }

        return node;
    }

    private DmlList compareToTree(Node<TreeNodeEntity> snode, Node<TreeNodeEntity> inode,DmlList dmlList){


        //如果sNode和iNode都没有子节点，那么则修改数据（来自iNode）
        if(snode.getChildNode()==null&&inode.getChildNode()==null){
            dmlList.getUpdateList().add(inode.getNode());
        }
        //如果sNode没有子节点，iNode有，则增加数据
        else if(snode.getChildNode()==null&&inode.getChildNode()!=null){
            //存储iNode的所有子节点下的节点进addList
            List<TreeNodeEntity> list=addAddList(inode,dmlList.getAddList());
            for (TreeNodeEntity e:list){
                dmlList.getAddList().add(e);
            }

        }
        //如果sNode有子节点，iNode没有，则删除数据
        else if(snode.getChildNode()!=null&&inode.getChildNode()==null){
            //存储sNode的所有子节点下的节点进delList
            List<TreeNodeEntity> list=addAddList(snode,dmlList.getDelList());
            for(TreeNodeEntity e:list){
                dmlList.getDelList().add(e);
            }

        }
        //如果两个节点都有子节点，则对子节点进行对比
        else if(snode.getChildNode()!=null&&inode.getChildNode()!=null){
            //比较这两个List是否一致，如果不一致，则删除多余的，增加新增的
            Map<UUID,TreeNodeEntity> map=new HashMap<>();
            for(Node<TreeNodeEntity> node:snode.getChildNode()){
                map.put(node.getNode().getId(), node.getNode());
            }
            //循环inode,如果map长度增加，则addList，如果不变，则updateList，如果map的长度大于inode的长度，则取出存入delList
            int oldSize=0;
            for(Node<TreeNodeEntity> node:inode.getChildNode()){
                oldSize=map.size();
                map.put(node.getNode().getId(), node.getNode());
                if(oldSize< map.size()){
                    dmlList.getAddList().add(node.getNode());
                    //其下的子节点都要进行Add
                    List<TreeNodeEntity> list=new ArrayList<>();
                    list=addAddList(node,list);
                    if(list!=null){
                        for (TreeNodeEntity e:list){
                            dmlList.getAddList().add(e);
                        }
                    }
                }else if(oldSize== map.size()){
                    dmlList.getUpdateList().add(node.getNode());
                }
            }
            Map<UUID,TreeNodeEntity> iMap=new HashMap<>();
            if(inode.getChildNode().size()<oldSize){
                for(Node<TreeNodeEntity> node:inode.getChildNode()){
                    iMap.put(node.getNode().getId(),node.getNode());
                }
                int iMapOldsize=iMap.size();
                for(Node<TreeNodeEntity> node:snode.getChildNode()){
                    if(oldSize<iMap.size()){
                        dmlList.getDelList().add(node.getNode());
                        //下面的子节点都要删除
                        List<TreeNodeEntity> list=new ArrayList<>();
                        list=addAddList(node,list);
                        for(TreeNodeEntity e:list){
                            dmlList.getDelList().add(e);
                        }
                    }
                }
            }
        }
        return dmlList;
    }

    private List<TreeNodeEntity> addAddList(Node<TreeNodeEntity> inode,List<TreeNodeEntity> list){
        if(inode.getChildNode()==null){
            return list;
        }
        //循环所有的子节点，存入AddList
        for(Node<TreeNodeEntity> n:inode.getChildNode()){
            list.add(n.getNode());
            if(n.getChildNode()!=null){
                addAddList(n,list);
            }
        }
        return list;
    }



}
