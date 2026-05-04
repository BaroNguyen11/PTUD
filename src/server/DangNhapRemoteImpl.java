package server;

import common.DangNhapRemote;
import server.dao.DangNhap_DAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DangNhapRemoteImpl extends UnicastRemoteObject implements DangNhapRemote {
    private final DangNhap_DAO dao = new DangNhap_DAO();

    public DangNhapRemoteImpl() throws RemoteException {
    }

    public boolean authenticate(String username, String password) throws RemoteException { return RemoteCallLogger.log("DangNhapRemoteImpl", "authenticate", new Object[]{username, password}, () -> dao.authenticate(username, password)); }
    public boolean isAdmin(String username) throws RemoteException { return RemoteCallLogger.log("DangNhapRemoteImpl", "isAdmin", new Object[]{username}, () -> dao.isAdmin(username)); }
    public String getMaNhanVien(String username) throws RemoteException { return RemoteCallLogger.log("DangNhapRemoteImpl", "getMaNhanVien", new Object[]{username}, () -> dao.getMaNhanVien(username)); }
    public boolean changePassword(String username, String oldPassword, String newPassword) throws RemoteException { return RemoteCallLogger.log("DangNhapRemoteImpl", "changePassword", new Object[]{username, oldPassword, newPassword}, () -> dao.changePassword(username, oldPassword, newPassword)); }
    public boolean isUsernameExist(String username) throws RemoteException { return RemoteCallLogger.log("DangNhapRemoteImpl", "isUsernameExist", new Object[]{username}, () -> dao.isUsernameExist(username)); }
    public boolean isTaiKhoanHoatDong(String username) throws RemoteException { return RemoteCallLogger.log("DangNhapRemoteImpl", "isTaiKhoanHoatDong", new Object[]{username}, () -> dao.isTaiKhoanHoatDong(username)); }
}
