package com.shu.icpc.service;

import com.shu.icpc.entity.Article;
import com.shu.icpc.utils.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ArticleService extends CoreService {

    public List<Article> getAll(){
        return this.articleDao.findAll();
    }

    public List<Article> getTitleLike(String titleLike){
        if(titleLike.isEmpty()){
            return this.articleDao.findAll();
        }
        return this.articleDao.findByTitleLike(titleLike);
    }

    public Article getById(Integer id){
        return this.articleDao.findById(id);
    }

    public List<Article> getByAdmin(Integer adminId){
        return this.articleDao.findByAdmin(adminId);
    }

    public Integer addArticle(Article article){
        if(article.getCoverUrl().isEmpty()){
            article.setCoverUrl("http://q7wdge0nf.bkt.clouddn.com/");
        }
        article.setStatus(Constants.CHECK_STATUS_CHECKED);
        this.articleDao.insert(article);
        return Constants.SUCCESS;
    }

    public Integer updateStatus(Integer id, Integer status){
        try{
            assert (status == Constants.CHECK_STATUS_PASS || status == Constants.CHECK_STATUS_CHECKED
            || status == Constants.CHECK_STATUS_REJECTED);
        }catch (Exception e){
            // code not in range
            return Constants.CHECK_ILLEGAL_CODE;
        }
        Integer code = this.articleDao.updateStatus(id, status);
        if(code == 0){
            return Constants.ARTICLE_NO_EXISTS;
        }
        return Constants.SUCCESS;
    }

    public Integer set(Integer id, String content, String coverUrl, Article article){
        article = this.getById(id);
        if(article == null){
            return Constants.ARTICLE_NO_EXISTS;
        }
        article.setContent(content);
        article.setCoverUrl(coverUrl);
        Date latest = new Date();
        article.setLatestEditTime(latest);
        Integer code = this.articleDao.update(article);
        if(code == 0){
            return Constants.ARTICLE_NO_EXISTS;
        }
        return Constants.SUCCESS;
    }

    public Integer delete(Integer id){
        Integer code = this.articleDao.delete(id);
        if(code == 0){
            return Constants.ARTICLE_NO_EXISTS;
        }
        return Constants.SUCCESS;
    }

}
