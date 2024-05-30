package dao;

import pojo.Music;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MusicDAO {
    public static ArrayList<Music> layDanhSachNhac() {
        ArrayList<Music> danhSachNhac = new ArrayList<>();
        try {
            String sql = "SELECT * FROM MUSIC";
            SQLServerProvider provider = new SQLServerProvider();
            provider.open();
            ResultSet rs = provider.excuteQuery(sql);
            
            while (rs.next()) {
                int id = rs.getInt("ID");
                String tenBaiHat = rs.getString("TENBAI");
                String duongDanNhac = rs.getString("DUONGDAN");
                
                Music music = new Music(id, tenBaiHat, duongDanNhac);
                danhSachNhac.add(music);
            }
            
            provider.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSachNhac;
    }
}
