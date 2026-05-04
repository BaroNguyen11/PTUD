package server.dao;

import com.mongodb.client.model.Filters;
import common.entity.MonAn;
import org.bson.Document;

import java.util.List;

public class QLMon_DAO extends MongoDaoSupport {

    public List<MonAn> getDanhSachMonAn() {
        return new MonAn_DAO().getAllMonAn();
    }

    public boolean insertMon(MonAn mon) {
        col("MonAn").insertOne(monAnDoc(mon));
        return true;
    }

    public String taoMaMonAn() {
        return nextId("MonAn", "maMonAn", "MA", 3);
    }

    public boolean updateMon(MonAn mon) {
        return update("MonAn", Filters.eq("maMonAn", mon.getMaMonAn()), monAnDoc(mon));
    }
}
