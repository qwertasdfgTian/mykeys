package com.lt.blog.service.impl;

import com.lt.blog.dao.CommentDao;
import com.lt.blog.dao.CommentGoodsDao;
import com.lt.blog.mapper.BlogMapper;
import com.lt.blog.mapper.UserMapper;
import com.lt.blog.pojo.Blog;
import com.lt.blog.pojo.Comment;
import com.lt.blog.pojo.CommentGoods;
import com.lt.blog.pojo.User;
import com.lt.blog.service.CommentService;
import com.lt.blog.utils.IdWorker;
import com.lt.blog.utils.Page;
import com.lt.blog.utils.ShiroUtils;
import com.lt.blog.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论表服务实现类
 * </p>
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private CommentGoodsDao commentGoodsDao;

    @Override
    public void save(Comment comment) {
        // 评论数+1
        // 查询博客
        Blog blog = blogMapper.getById(comment.getCommentBlog());
        blog.setBlogComment(blog.getBlogComment() + 1);
        blogMapper.update(blog);
        comment.setBlog(blog);
        comment.setId(idWorker.nextId() + "");
        comment.setCommentGood(0);
        comment.setCreatedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        // 查询用户
        User user = userMapper.getById(comment.getCommentUser());
        comment.setUser(user);
        commentDao.save(comment);
    }

    @Override
    public List<Comment> getByBlog(String blogId,Integer pagenum) {
        // 查询博客评论
        List<Comment> commentList = commentDao.findByCommentBlogOrderByCreatedTimeDescCommentGoodDesc(blogId);
        // 查询点赞情况
        // 取出评论id
        List<String> commentIds = commentList.stream().map(Comment::getId).collect(Collectors.toList());
        List<CommentGoods> commentGoodsList = commentGoodsDao.findByCommentIdIn(commentIds);
        // 查询是否是该用户点赞的评论
        User user = (User) ShiroUtils.getLoginUser();
        // 遍历去封装评论情况
        commentList.forEach(e -> {
            commentGoodsList.forEach(good -> {
                if (e.getId().equals(good.getCommentId())&&user.getUserId()==good.getUserId()) {
                    // 匹配到了评论记录
                    e.setCommentFlag(true);
                }
            });
        });
        return commentList;
    }

    @Override
    public void deleteById(String id) {
        commentDao.deleteById(id);
    }

    @Override
    public void goodByCommentIdAndUser(CommentGoods commentGoods) {
        User user = (User) ShiroUtils.getLoginUser();
        commentGoods.setUserId(user.getUserId());
        // 取出评论id，点赞数+1
        String commentId = commentGoods.getCommentId();
        Comment comment = commentDao.findById(commentId).get();
        comment.setCommentGood(comment.getCommentGood() + 1);
        commentDao.save(comment);
        try {
            commentGoods.setId(idWorker.nextId() + "");
            commentGoodsDao.save(commentGoods);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getGoodsCount(String commentId) {
        User user = (User) ShiroUtils.getLoginUser();
        int count = commentGoodsDao.countByUserIdAndCommentId(user.getUserId(), commentId);
        return count;
    }

    @Override
    public Page<Comment> getByPage(Page<Comment> page) {
        User user = (User) ShiroUtils.getLoginUser();
        Comment comment = new Comment();
        comment.setCommentUser(user.getUserId());
        Example<Comment> example = Example.of(comment);
        Pageable pageable = PageRequest.of(page.getCurrentPage() - 1, page.getPageSize());
        org.springframework.data.domain.Page<Comment> p = commentDao.findAll(example, pageable);
        // 封装总页数、总条数、数据
        page.setTotalCount((int)p.getTotalElements());
        page.setTotalPage(p.getTotalPages());
        page.setList(p.getContent());
        return page;
    }

    @Override
    public Page<Comment> getByPageBack(Page<Comment> page) {
        Comment comment = new Comment();
        String blogTitle = (String) page.getParams().get("blogTitle");
        if(StringUtils.isBlank(blogTitle)) {
            blogTitle = "";
        }
        String nickname = (String) page.getParams().get("nickname");
        if(StringUtils.isBlank(nickname)) {
            nickname = "";
        }
        Blog blog = new Blog();
        blog.setBlogTitle(blogTitle);
        comment.setBlog(blog);
        User user = new User();
        user.setNickname(nickname);
        comment.setUser(user);
        Pageable pageable = PageRequest.of(page.getCurrentPage() - 1, page.getPageSize());
        org.springframework.data.domain.Page<Comment> p = commentDao.getByBlogTitleAndNickname(comment, pageable);
        // 封装总页数、总条数、数据
        page.setTotalCount((int)p.getTotalElements());
        page.setTotalPage(p.getTotalPages());
        page.setList(p.getContent());
        return page;
    }

    @Override
    public void delgoodByCommentIdAndUser(CommentGoods commentGoods) {
        User user = (User) ShiroUtils.getLoginUser();
        commentGoods.setUserId(user.getUserId());
        // 取出评论id，点赞数-1
        String commentId = commentGoods.getCommentId();
        Comment comment = commentDao.findById(commentId).get();
        comment.setCommentGood(comment.getCommentGood() - 1);
        commentDao.save(comment);
        try {
            commentGoodsDao.deleteByUserIdAndCommentId(user.getUserId(),commentGoods.getCommentId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
