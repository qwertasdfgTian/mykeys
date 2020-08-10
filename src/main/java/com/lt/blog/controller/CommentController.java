package com.lt.blog.controller;

import com.lt.blog.enums.ResultEnum;
import com.lt.blog.pojo.Comment;
import com.lt.blog.pojo.CommentGoods;
import com.lt.blog.pojo.User;
import com.lt.blog.service.CommentService;
import com.lt.blog.utils.Page;
import com.lt.blog.utils.Result;
import com.lt.blog.utils.ShiroUtils;
import com.lt.blog.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 保存
     *
     * @param comment
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<Object> save(@RequestBody Comment comment) {
        User user = (User) ShiroUtils.getLoginUser();
        comment.setCommentUser(user.getUserId());
        if (StringUtils.isBlank(comment.getCommentBlog())) {
            return new Result<>(ResultEnum.PARAMS_NULL.getCode(), "博客id不能为空！");
        }
        commentService.save(comment);
        return new Result<>("评论成功！");
    }

    /**
     * 查询当前博客的评论
     *
     * @return
     */
    @RequestMapping(value = "/getByBlog", method = RequestMethod.POST)
    public Result<Page<Comment>> getByBlog(@RequestBody Page page) {
        List<Comment> comments = commentService.getByBlog(page.getBlogId(),page.getCurrentPage());
        List<Comment> comment=new ArrayList<>();
        int num=0;
        for (Comment comment1 : comments) {
            num++;
        }
        int i=(page.getCurrentPage()-1)*5;
        for (int k=0;k<5;k++,i++) {
            if(i<comments.size())
                comment.add(comments.get(i));
            else break;
        }
        Page<Comment> p=new Page<>();
        p.setList(comment);
        p.setTotalCount(num);
        p.setPageSize(5);
        return new Result<>(p);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.DELETE)
    public Result<Object> deleteById(@PathVariable String id) {
        commentService.deleteById(id);
        return new Result<>("删除成功！");
    }

    /**
     * 点赞
     *
     * @param commentGoods
     * @return
     */
    @RequestMapping(value = "/good", method = RequestMethod.POST)
    public Result<Object> good(@RequestBody CommentGoods commentGoods) {
        if (StringUtils.isBlank(commentGoods.getCommentId())) {
            return new Result<>("评论id不能为空！");
        }
        commentService.goodByCommentIdAndUser(commentGoods);
        return new Result<>("点赞成功！");
    }

    /**
     * 取消评论点赞
     *
     * @param commentGoods
     * @return
     */
    @RequestMapping(value = "/delgood", method = RequestMethod.POST)
    public Result<Object> delgood(@RequestBody CommentGoods commentGoods) {
        if (StringUtils.isBlank(commentGoods.getCommentId())) {
            return new Result<>("评论id不能为空！");
        }
        commentService.delgoodByCommentIdAndUser(commentGoods);
        return new Result<>("已取消点赞！");
    }

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public Result<Page<Comment>> getList(@RequestBody Page<Comment> page) {
        page = commentService.getByPage(page);
        return new Result<>(page);
    }

    /**
     * 后台分页查询
     * @param page
     * @return
     */
    @RequestMapping(value = "/getByPage", method = RequestMethod.POST)
    public Result<Page<Comment>> getByPage(@RequestBody Page<Comment> page) {
        page = commentService.getByPageBack(page);
        return new Result<>(page);
    }

}
