package com.lt.blog.dao;

import com.lt.blog.pojo.BlogGoods;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogGoodsDao extends MongoRepository<BlogGoods, String> {

    /**
     * 根据用户id和博客id查询
     * @param userId
     * @param blogId
     * @return
     */
    int countByUserIdAndBlogId(Integer userId, String blogId);
    void deleteByUserIdAndAndBlogId(Integer userId, String blogId);

}
