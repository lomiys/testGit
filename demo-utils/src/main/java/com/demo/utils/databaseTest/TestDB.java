package com.demo.utils.databaseTest;

import com.vdian.vdds.engine.jdbc.DistributedDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author yaoyu
 * @date 16/4/27
 * @说明:
 */
public class TestDB {
    public static void main(String[] args) throws SQLException {
        DistributedDataSource dataSource=new DistributedDataSource();
        dataSource.setLogicDBName("BANJIA_APP");
        dataSource.setLogicAccountName("BANJIA_APP_user");
        dataSource.setLogicAccountPass("root");
        dataSource.init();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from vmp_shop_coupon where shop_id=1280000 limit 5";
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                //get result
            }
        } catch (Exception e) {
            //exception process
        } finally {
            rs.close();
            ps.close();
            conn.close();
        }
    }
}
