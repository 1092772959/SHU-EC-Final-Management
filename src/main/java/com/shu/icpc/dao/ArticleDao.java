package com.shu.icpc.dao;

import com.shu.icpc.entity.Article;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import java.util.List;

public interface ArticleDao {
    List<Article> findAll();

    List<Article> findByAdmin(Integer adminId);

    List<Article> findByTitleLike(String titleLike);

    Article findById(Integer id);

    Integer insert(Article article);

    int delete(Integer id);

    int update(Article article);

    int updateStatus(Integer id, Integer status);
}
