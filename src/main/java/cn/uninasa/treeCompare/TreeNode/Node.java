package cn.uninasa.treeCompare.TreeNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node<T> {
    //当前节点信息
    private T node;
    //子节点信息
    private List<Node<T>> childNode;

}
