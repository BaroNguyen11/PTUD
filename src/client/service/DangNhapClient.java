package client.service;

import client.RemoteServices;

public class DangNhapClient {
    public boolean authenticate(String username, String password) { try { return RemoteServices.dangNhap().authenticate(username, password); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean isAdmin(String username) { try { return RemoteServices.dangNhap().isAdmin(username); } catch (Exception e) { throw new RuntimeException(e); } }
    public String getMaNhanVien(String username) { try { return RemoteServices.dangNhap().getMaNhanVien(username); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean changePassword(String username, String oldPassword, String newPassword) { try { return RemoteServices.dangNhap().changePassword(username, oldPassword, newPassword); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean isUsernameExist(String username) { try { return RemoteServices.dangNhap().isUsernameExist(username); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean isTaiKhoanHoatDong(String username) { try { return RemoteServices.dangNhap().isTaiKhoanHoatDong(username); } catch (Exception e) { throw new RuntimeException(e); } }
}
