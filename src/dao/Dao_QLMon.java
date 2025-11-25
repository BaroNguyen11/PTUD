package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.MonAn;

public class Dao_QLMon {

	public List<MonAn> getDanhSachMonAn() {
	    List<MonAn> ds = new ArrayList<>();

	    String sql = "SELECT maMonAn, tenMonAn, loaiMon, giaTien, moTa, hinhAnh FROM MonAn";

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            String ma = rs.getString("maMonAn");
	            String ten = rs.getString("tenMonAn");
	            String loai = rs.getString("loaiMon");
	            double gia = rs.getDouble("giaTien");
	            String moTa = rs.getString("moTa");
	            String hinhAnh = rs.getString("hinhAnh");

	            ds.add(new MonAn(ma, ten, loai, gia, moTa, hinhAnh));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return ds;
	}

}
