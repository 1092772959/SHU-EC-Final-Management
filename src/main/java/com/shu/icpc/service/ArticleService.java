package com.shu.icpc.service;

import com.shu.icpc.entity.Admin;
import com.shu.icpc.entity.Article;
import com.shu.icpc.utils.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ArticleService extends CoreService {

    public List<Article> getAll(){
        Object user = loginService.getUserFromSession();
        List<Article> res = null;
        if(user instanceof Admin){
            res = articleDao.findAll();
        }else {
            res = articleDao.findByStatus(Constants.CHECK_STATUS_PASS);
        }
        return res;
    }

    public List<Article> getTitleLike(String titleLike){
        List<Article> res = null;
        Object user = this.loginService.getUserFromSession();

        if(titleLike.isEmpty()){
            if(user instanceof Admin){
                res = this.articleDao.findAll();
            }else{
                res = this.articleDao.findByStatus(Constants.CHECK_STATUS_PASS);
            }
        }else{
            if(user instanceof Admin){
                res = this.articleDao.findByTitleLike(titleLike);
            }else{
                res = this.articleDao.findByTitleLikeAndStatus(titleLike, Constants.CHECK_STATUS_PASS);
            }
        }
        return res;
    }

    public Article getById(Integer id){
        return this.articleDao.findById(id);
    }

    public List<Article> getByAdmin(Integer adminId){
        return this.articleDao.findByAdmin(adminId);
    }

    public List<Article> getByStatus(Integer status){
        try{
            assert(status == Constants.CHECK_STATUS_PASS || status == Constants.CHECK_STATUS_CHECKED
                    || status == Constants.CHECK_STATUS_REJECTED);
        }catch (Exception e){
            return null;
        }

        if(status != Constants.CHECK_STATUS_PASS){
            Object user = this.loginService.getUserFromSession();
            //other users have no access to not passed articles
            if(! (user instanceof Admin)){
                return null;
            }
        }
        return this.articleDao.findByStatus(status);
    }

    public Integer addArticle(Article article){
        if(article.getCoverUrl() == null){
            article.setCoverUrl("http://q7wdge0nf.bkt.clouddn.com/");
        }
        article.setLatestEditTime(Calendar.getInstance().getTime());
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

    public Integer set(Integer id, String content, String coverUrl, String intro,
                       Integer adminId, Article article) {
        article = this.getById(id);
        if(article == null){
            return Constants.ARTICLE_NO_EXISTS;
        }
        if(article.getAdminId() != adminId) {
            return Constants.ARTICLE_NO_ACCESS;
        }
        article.setContent(content);
        article.setCoverUrl(coverUrl);
        article.setIntro(intro);
        Date latest = new Date();
        article.setLatestEditTime(latest);
        int code = this.articleDao.update(article);
        if(code == 0){
            return Constants.ARTICLE_NO_EXISTS;
        }
        return Constants.SUCCESS;
    }

    public Integer delete(Integer id, Integer adminId){
        Article article = this.articleDao.findById(id);
        if(article  == null){
            return Constants.ARTICLE_NO_EXISTS;
        }
        /*
        if(article.getAdminId() != adminId){
            return Constants.ARTICLE_NO_ACCESS;
        }*/

        Integer code = this.articleDao.delete(id);
        if(code == 0){
            return Constants.ARTICLE_NO_EXISTS;
        }
        return Constants.SUCCESS;
    }

}
