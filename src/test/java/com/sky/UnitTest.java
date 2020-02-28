package com.sky;

import com.sky.wordmem.App;
import com.sky.wordmem.adaptor.dictAdaptor.DictAdaptor;
import com.sky.wordmem.adaptor.dictAdaptor.MarriamWebster;
import com.sky.wordmem.dao.NoteDao;
import com.sky.wordmem.dao.TestDao;
import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.utils.RequestUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.sql.DataSource;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
@SpringBootTest
@ActiveProfiles("test")
public abstract class UnitTest {

    @Autowired
    public DataSource sqlite;
    @Autowired
    public TestDao testDao;
    @Autowired
    public WordDao wordDao;
    @Autowired
    public NoteDao noteDao;
    @Autowired
    public RequestUtil requestUtil;
    @Autowired
    public DictAdaptor marriamWebster;
}
