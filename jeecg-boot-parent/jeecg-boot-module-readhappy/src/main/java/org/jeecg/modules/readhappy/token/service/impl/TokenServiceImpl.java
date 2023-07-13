package org.jeecg.modules.readhappy.token.service.impl;

import org.jeecg.modules.readhappy.token.entity.Token;
import org.jeecg.modules.readhappy.token.mapper.TokenMapper;
import org.jeecg.modules.readhappy.token.service.ITokenService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: access_tokens
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Service
public class TokenServiceImpl extends ServiceImpl<TokenMapper, Token> implements ITokenService {

}
