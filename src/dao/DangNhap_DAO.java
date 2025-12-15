/*
 * @ (#) DangNhap_DAO.java     1.0     10/28/2025
 * Copyright (c) 2025 IUH. All Rights Reserved.
 */
package dao;

import ConnectDB.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * @description: DAO class for handling login authentication
 * @author: Bao Nguyen
 * @date: 10/28/2025
 * @version: 1.0
 */
public class DangNhap_DAO {

    /**
     * Xác thực thông tin đăng nhập
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return true nếu đăng nhập thành công, false nếu thất bại
     */
    public boolean authenticate(String username, String password) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            String sql = "SELECT * FROM dbo.TaiKhoan WHERE taiKhoan = ? AND matKhau = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();
            return rs.next(); // Trả về true nếu có bản ghi phù hợp

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close(); // Đóng connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Kiểm tra tài khoản có phải là admin không
     * @param username Tên đăng nhập
     * @return true nếu là admin, false nếu không
     */
    public boolean isAdmin(String username) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            String sql = "SELECT taiKhoanQuanLi FROM TaiKhoan WHERE taiKhoan = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("taiKhoanQuanLi");
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lấy mã nhân viên từ tài khoản
     * @param username Tên đăng nhập
     * @return Mã nhân viên hoặc null nếu không tìm thấy
     */
    public String getMaNhanVien(String username) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            String sql = "SELECT maNhanVien FROM dbo.TaiKhoan WHERE taiKhoan = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("maNhanVien");
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
//    public String getTenNhanVien() {
//        Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = ConnectDB.getConnection();
//            String sql = "SELECT nv.tenNhanVien FROM dbo.TaiKhoan tk " +
//                    "JOIN dbo.NhanVien nv ON tk.maNhanVien = nv.maNhanVien " +
//                    "WHERE tk.taiKhoan = ?";
//            pstmt = con.prepareStatement(sql);
//            pstmt.setString(1, username);
//
//            rs = pstmt.executeQuery();
//            if (rs.next()) {
//                return rs.getString("tenNhanVien");
//            }
//            return null;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (pstmt != null) pstmt.close();
//                if (con != null) con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * Đổi mật khẩu cho tài khoản
     * @param username Tên đăng nhập
     * @param oldPassword Mật khẩu cũ
     * @param newPassword Mật khẩu mới
     * @return true nếu đổi mật khẩu thành công, false nếu thất bại
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // Kiểm tra mật khẩu cũ trước
            if (!authenticate(username, oldPassword)) {
                return false;
            }

            con = ConnectDB.getConnection();
            String sql = "UPDATE dbo.TaiKhoan SET matKhau = ? WHERE taiKhoan = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Kiểm tra tên đăng nhập có tồn tại không
     * @param username Tên đăng nhập
     * @return true nếu tồn tại, false nếu không
     */
    public boolean isUsernameExist(String username) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            String sql = "SELECT COUNT(*) FROM dbo.TaiKhoan WHERE taiKhoan = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isTaiKhoanHoatDong(String username) {
        boolean isActive = false;
        // Giả sử bảng TaiKhoan có cột 'trangThaiHoatDong' (BIT/BOOLEAN)
        // 1 = Hoạt động, 0 = Khóa
        String sql = "SELECT trangThaiHoatDong FROM TaiKhoan WHERE taiKhoan = ?";

        try (java.sql.Connection con = ConnectDB.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isActive = rs.getBoolean("trangThaiHoatDong");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isActive;
    }
}