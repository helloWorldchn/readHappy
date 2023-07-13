package org.jeecg.modules.readhappy.member.service.impl;

import org.jeecg.modules.readhappy.member.entity.Member;
import org.jeecg.modules.readhappy.member.mapper.MemberMapper;
import org.jeecg.modules.readhappy.member.service.IMemberService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: member
 * @Author: jeecg-boot
 * @Date:   2020-06-10
 * @Version: V1.0
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

}
