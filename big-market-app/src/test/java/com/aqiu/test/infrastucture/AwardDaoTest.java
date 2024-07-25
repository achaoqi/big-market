package com.aqiu.test.infrastucture;

import com.alibaba.fastjson.JSON;
import com.aqiu.infrastructure.persistent.dao.IAwardDao;
import com.aqiu.infrastructure.persistent.po.Award;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AwardDaoTest {

    @Resource
    private IAwardDao awardDao;

    @Test
    public void test_queryAwardList(){
        List<Award> awardList = awardDao.queryAwardList();
        log.info("awardList:{}", JSON.toJSONString(awardList));
    }

}
