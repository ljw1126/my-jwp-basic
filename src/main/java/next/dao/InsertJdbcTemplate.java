package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertJdbcTemplate {
    public void insert(User user, UserDao userDao) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(userDao.createQueryForInsert());

            userDao.setValuesForInsert(user, pstmt);

            pstmt.executeUpdate();

        } finally {
            if(pstmt != null) pstmt.close();
            if(con != null) con.close();
        }
    }
}
