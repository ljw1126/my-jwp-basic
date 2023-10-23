package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateJdbcTemplate {
    public void update(User user, UserDao userDao) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(userDao.createQueryForUpdate());

            userDao.setValuesForUpdate(user, pstmt);

            pstmt.executeUpdate();
        } finally {
            if(pstmt != null) pstmt.close();
            if(con != null) con.close();
        }
    }
}
