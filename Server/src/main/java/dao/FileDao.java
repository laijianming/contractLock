package dao;

import entity.FileInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jianming
 * @create 2021-03-08-21:21
 */
public class FileDao {

    static Connection conn;

    static {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            // 获得数据库连接
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/CONTRACT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileInfo getFileinfo(String uuid) {
        String sql = "SELECT * FROM FILEINFO WHERE fileuuid = ? LIMIT 1";
        FileInfo fileInfo = new FileInfo();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uuid);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                fileInfo.setFileuuid(resultSet.getString(1));
                fileInfo.setFilename(resultSet.getString(2));
                fileInfo.setRandomkey(resultSet.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileInfo;
    }

    public static List<FileInfo> getAllFileinfo() {
        String sql = "SELECT * FROM FILEINFO";
        List<FileInfo> res = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileuuid(resultSet.getString(1));
                fileInfo.setFilename(resultSet.getString(2));
                fileInfo.setRandomkey(resultSet.getString(3));
                res.add(fileInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void saveFileinfo(FileInfo fileInfo) {
        String sql = "INSERT INTO FILEINFO (FILEUUID, FILENAME, RANDOMKEY) VALUES (?,?,?);";
        List<FileInfo> res = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fileInfo.getFileuuid());
            ps.setString(2, fileInfo.getFilename());
            ps.setString(3, fileInfo.getRandomkey());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
