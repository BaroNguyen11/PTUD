package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DangNhapRemote extends Remote {
    boolean authenticate(String username, String password) throws RemoteException;
    boolean isAdmin(String username) throws RemoteException;
    String getMaNhanVien(String username) throws RemoteException;
    boolean changePassword(String username, String oldPassword, String newPassword) throws RemoteException;
    boolean isUsernameExist(String username) throws RemoteException;
    boolean isTaiKhoanHoatDong(String username) throws RemoteException;
}
