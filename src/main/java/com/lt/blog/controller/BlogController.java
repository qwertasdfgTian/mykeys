package com.lt.blog.controller;

import com.lt.blog.enums.ResultEnum;
import com.lt.blog.pojo.Blog;
import com.lt.blog.pojo.BlogCollection;
import com.lt.blog.pojo.BlogGoods;
import com.lt.blog.pojo.Type;
import com.lt.blog.service.BlogService;
import com.lt.blog.service.TypeService;
import com.lt.blog.utils.Page;
import com.lt.blog.utils.Result;
import com.lt.blog.utils.StringUtils;
import com.lt.blog.vo.BlogVo;
import com.lt.blog.vo.TimeLineVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;

    /**
     * 保存
     *
     * @param blog
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<Object> save(@RequestBody Blog blog) {
        blogService.save(blog);
        return new Result<>("添加成功！");
    }

    /**
     * 根据id查询
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Result<Blog> get(@PathVariable String id) {
        Blog blog = blogService.getById(id);
        return new Result<>(blog);
    }

    /**
     * 更新
     *
     * @param blog
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<Object> update(@RequestBody Blog blog) {
        blogService.update(blog);
        return new Result<>("更新成功！");
    }

    /**
     * 根据id阅读
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
    public Result<BlogVo> read(@PathVariable String id) {
        BlogVo blog = blogService.readById(id);
        return new Result<>(blog);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public Result<Object> delete(@PathVariable String id) {

        Blog blog = blogService.getById(id);
        Type type=typeService.getById(blog.getBlogType());
        type.setTypeBlogCount(type.getTypeBlogCount()-1);
        typeService.update(type);
        blogService.deleteById(id);
        return new Result<>("删除成功！");
    }

    /**
     * 查询时间轴
     *
     * @return
     */
    @RequestMapping(value = "/getTimeLine", method = RequestMethod.GET)
    public Result<List<TimeLineVo>> getTimeLine() {
        List<TimeLineVo> timeList = new ArrayList<>(16);
        List<BlogVo> blogVoList = blogService.getTimeLine();
        blogVoList.forEach(e -> {
            String blogMonth = e.getBlogMonth();
            TimeLineVo timeLineVo = new TimeLineVo();
            timeLineVo.setMonth(blogMonth);
            if(timeList.contains(timeLineVo)) {
                // 取出对应的数据
                TimeLineVo timeLine = getTimeLineForList(timeList, timeLineVo);
                List<BlogVo> list = timeLine.getList();
                if(list == null) {
                    list = new ArrayList<>(8);
                }
                list.add(e);
                timeLine.setList(list);
            } else {
                List<BlogVo> list = timeLineVo.getList();
                if(list == null) {
                    list = new ArrayList<>(8);
                }
                list.add(e);
                timeLineVo.setList(list);
                timeList.add(timeLineVo);
            }
        });
        return new Result<>(timeList);
    }

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/getByPage", method = RequestMethod.POST)
    public Result<Page<BlogVo>> getByPage(@RequestBody Page<BlogVo> page) {
        String sortColumn = page.getSortColumn();
        if (StringUtils.isNotBlank(sortColumn)) {
            // 排序列不为空
            String[] sortColumns = {"blog_goods", "blog_read", "blog_collection",
                    "type_name", "blog_comment", "created_time", "update_time"};
            List<String> sortList = Arrays.asList(sortColumns);
            if (!sortList.contains(sortColumn.toLowerCase())) {
                return new Result<>(ResultEnum.PARAMS_ERROR.getCode(), "排序参数不合法！");
            }
        }
        page = blogService.getByPage(page);
        return new Result<>(page);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping(value = "/recomRead", method = RequestMethod.GET)
    public Result<List<BlogVo>> recomRead() {
        List<BlogVo> blogList = blogService.recomRead();
        return new Result<>(blogList);
    }

    /**
     * 点赞
     * @param blogGoods
     * @return
     */
    @RequestMapping(value = "/good", method = RequestMethod.POST)
    public Result<Object> good(@RequestBody BlogGoods blogGoods) {
        if(StringUtils.isBlank(blogGoods.getBlogId())) {
            return new Result<>("博客id不能为空！");
        }
        blogService.goodByBlogAndUser(blogGoods);
        return new Result<>("点赞成功！");
    }

    /**
     * 取消点赞
     * @param blogGoods
     * @return
     */
    @RequestMapping(value = "/delgood", method = RequestMethod.POST)
    public Result<Object> delgood(@RequestBody BlogGoods blogGoods) {
        if(StringUtils.isBlank(blogGoods.getBlogId())) {
            return new Result<>("博客id不能为空！");
        }
        blogService.delgoodByBlogAndUser(blogGoods);
        return new Result<>("已取消点赞！");
    }

    /**
     * 根据博客id和当前登录用户查询点赞记录
     * @param blogId
     * @return
     */
    @RequestMapping(value = "/getGood/{blogId}", method = RequestMethod.GET)
    public Result<Integer> getGood(@PathVariable String blogId) {
        int count = blogService.getGoodsCount(blogId);
        return new Result<>(count);
    }

    /**
     * 收藏
     * @param blogCollection
     * @return
     */
    @RequestMapping(value = "/collection", method = RequestMethod.POST)
    public Result<Object> collection(@RequestBody BlogCollection blogCollection) {
        if(StringUtils.isBlank(blogCollection.getBlogId())) {
            return new Result<>("博客id不能为空！");
        }
        blogService.collectionByBlogId(blogCollection);
        return new Result<>("收藏成功！");
    }
    /**
     * 取消收藏
     * @param blogCollection
     * @return
     */
    @RequestMapping(value = "/delcollection", method = RequestMethod.POST)
    public Result<Object> delcollection(@RequestBody BlogCollection blogCollection) {
        if(StringUtils.isBlank(blogCollection.getBlogId())) {
            return new Result<>("博客id不能为空！");
        }
        blogService.delcollectionByBlogId(blogCollection);
        return new Result<>("已取消收藏！");
    }

    /**
     * 根据博客id和当前登录用户查询收藏记录
     * @param blogId
     * @return
     */
    @RequestMapping(value = "/getCollection/{blogId}", method = RequestMethod.GET)
    public Result<Integer> getCollection(@PathVariable String blogId) {
        int count = blogService.getCollectionCount(blogId);
        return new Result<>(count);
    }

    /**
     * 分页查询我的收藏
     * @param page
     * @return
     */
    @RequestMapping(value = "/getCollectionList", method = RequestMethod.POST)
    public Result<Page<BlogCollection>> getCollectionList(@RequestBody Page<BlogCollection> page) {
        page = blogService.getCollectionByPage(page);
        return new Result<>(page);
    }

    /**
     * 获取对应的timeLine
     * @param timeList
     * @param timeLineVo
     * @return
     */
    private TimeLineVo getTimeLineForList(List<TimeLineVo> timeList, TimeLineVo timeLineVo) {
        for (TimeLineVo lineVo : timeList) {
            if(timeLineVo.getMonth().equals(lineVo.getMonth())) {
                return lineVo;
            }
        }
        return null;
    }

}
