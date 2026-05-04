package server.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import common.entity.ChiTietHoaDon;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDon_DAO extends MongoDaoSupport {

    public boolean themChiTietHoaDon(ChiTietHoaDon cthd) {
        col("ChiTietHoaDon").insertOne(new Document("maHoaDon", cthd.getHoaDon().getMaHoaDon())
                .append("maMonAn", cthd.getMonAn().getMaMonAn())
                .append("soLuong", cthd.getSoLuong()));
        return true;
    }

    public static List<ChiTietHoaDon> getChiTietHoaDonByMaHD(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        for (Document d : docs("ChiTietHoaDon", Filters.eq("maHoaDon", maHD))) list.add(chiTietHoaDon(d));
        return list;
    }

    public boolean themHoacUpdate(String maHD, String maMon, int soLuongThem, double giaBan) {
        Document found = col("ChiTietHoaDon").find(Filters.and(Filters.eq("maHoaDon", maHD), Filters.eq("maMonAn", maMon))).first();
        if (found != null) {
            return col("ChiTietHoaDon").updateOne(Filters.and(Filters.eq("maHoaDon", maHD), Filters.eq("maMonAn", maMon)),
                    Updates.inc("soLuong", soLuongThem)).getModifiedCount() > 0;
        }
        col("ChiTietHoaDon").insertOne(new Document("maHoaDon", maHD).append("maMonAn", maMon).append("soLuong", soLuongThem).append("giaBan", giaBan));
        return true;
    }
}
