package org.jeecg.modules.readhappy.article.service.impl;

import org.jeecg.modules.readhappy.article.entity.Article;
import org.jeecg.modules.readhappy.article.mapper.ArticleMapper;
import org.jeecg.modules.readhappy.article.service.IArticleService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: article
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
