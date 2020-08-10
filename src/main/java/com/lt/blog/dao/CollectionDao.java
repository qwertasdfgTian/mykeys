package com.lt.blog.dao;

import com.lt.blog.pojo.BlogCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionDao extends MongoRepository<BlogCollection, String> {

    /**
     * 根据博客id和用户id查询
     * @param blogId
     * @param userId
     * @return
     */
    int countByBlogIdAndUserId(String blogId, Integer userId);

    /**
     * 根据用户id查询数量
     * @param userId
     * @return
     */
    int countByUserId(Integer userId);
    void deleteByUserIdAndBlogId(Integer userId,String blogId);
}
