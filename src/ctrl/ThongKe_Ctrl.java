/*
 * @ (#) ThongKe_Ctrl .java     1.0     10/27/2025
 * Copyright (c) 2025 IUH.All Rights Reserved.
 */
package ctrl;

import dao.ThongKe_DAO;

import java.util.Map;

/*
 *@description: this is a class to test the ThongKe_Ctrl class
 *@author: Bao Nguyen
 *@Date: 10/27/2025
 *@version:      1.0
 */
public class ThongKe_Ctrl {
    ThongKe_DAO thongKeDao = new ThongKe_DAO();

    public double getTongDoanhThu(){
        return  thongKeDao.getTongDoanhThu();
    }
    public double getDoanhThuTBBan(){
        return thongKeDao.getDoanhThuTrungBinhBan();
    }
    public double getTiLeTienMat(){
        return thongKeDao.getTiLeTienMat();
    }
    public double getDoanhThuCaToi(){
        return thongKeDao.getDoanhThuCaToi();
    }
    public Map<String, Double> thongKeDoanhThuTheoThang(){
        return thongKeDao.getDoanhThuTheoThang();
    }
}
