package com.lt.blog.service;

import com.lt.blog.pojo.Comment;
import com.lt.blog.pojo.CommentGoods;
import com.lt.blog.utils.Page;

import java.util.List;

/**
 * <p>
 * 评论表服务层接口
 * </p>
 */
public interface CommentService {

    /**
     * 保存评论
     * @param comment
     */
    void save(Comment comment);

    /**
     * 查询当前博客的评论
     * @param blogId
     * @return
     */
    List<Comment> getByBlog(String blogId,Integer pagenum);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(String id);

    /**
     * 根据评论id和用户点赞
     * @param commentGoods
     */
    void goodByCommentIdAndUser(CommentGoods commentGoods);

    /**
     * 根据评论id查询点赞数
     * @param commentId
     * @return
     */
    int getGoodsCount(String commentId);

    /**
     * 分页查询
     * @param page
     * @return
     */
    Page<Comment> getByPage(Page<Comment> page);

    /**
     * 后台分页查询
     * @param page
     * @return
     */
    Page<Comment> getByPageBack(Page<Comment> page);

    void delgoodByCommentIdAndUser(CommentGoods commentGoods);

}
