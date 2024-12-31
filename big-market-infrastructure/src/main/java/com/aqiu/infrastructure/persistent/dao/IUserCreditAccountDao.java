package com.aqiu.infrastructure.persistent.dao;

import com.aqiu.infrastructure.persistent.po.UserCreditAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserCreditAccountDao {
    int updateAddAmount(UserCreditAccount userCreditAccountReq);

    void insert(UserCreditAccount userCreditAccountReq);

    UserCreditAccount queryUserCreditAccount(UserCreditAccount userCreditAccountReq);
}
