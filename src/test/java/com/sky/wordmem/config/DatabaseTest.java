package com.sky.wordmem.config;

import com.sky.UnitTest;
import org.junit.Test;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.config
 */
public class DatabaseTest extends UnitTest{

    @Test
    public void testConfig(){
        try {
            Statement s = sqlite.getConnection().createStatement();
            s.execute("");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}
