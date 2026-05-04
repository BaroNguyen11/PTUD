package server.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import common.entity.BanAn;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class QuanLyBan_DAO extends MongoDaoSupport {

    public List<BanAn> getAllBanAn() {
        List<BanAn> list = new ArrayList<>();
        for (Document d : col("BanAn").find().sort(Sorts.ascending("maBan"))) list.add(banAn(d));
        return list;
    }

    public BanAn getBanAnByMa(String maBan) {
        return banAn(one("BanAn", "maBan", maBan));
    }

    public List<BanAn> searchBanAn(String keyword) {
        List<BanAn> list = new ArrayList<>();
        for (Document d : col("BanAn").find(Filters.or(
                Filters.regex("maBan", contains(keyword)),
                Filters.regex("loai", contains(keyword)),
                Filters.regex("trangThai", contains(keyword)),
                Filters.regex("viTri", contains(keyword))
        ))) list.add(banAn(d));
        return list;
    }

    public boolean addBanAn(BanAn b) {
        col("BanAn").insertOne(banAnDoc(b));
        return true;
    }

    public boolean updateBanAn(BanAn b) {
        return update("BanAn", Filters.eq("maBan", b.getMaBan()), banAnDoc(b));
    }

    public boolean deleteBanAn(String maBan) {
        return delete("BanAn", Filters.eq("maBan", maBan));
    }

    public String generateMaBan() {
        return nextId("BanAn", "maBan", "B", 3);
    }
}
