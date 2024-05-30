/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import gui.Profile;
import pojo.HashPas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import pojo.NguoiDung;

public class NguoiDungDAO extends SQLServerProvider {

    NguoiDung nd = null;

    public NguoiDung dangnhap(String username, String password) {
        try {
            String sql = "select * from NGUOIDUNG where TENDN = '" + username + "' and MATKHAUHASH ='" + password + "'";
            SQLServerProvider provider = new SQLServerProvider();
            provider.open();
            ResultSet rs = provider.excuteQuery(sql);

            if (rs.next()) {
                nd = new NguoiDung();
                nd.setTendn(rs.getString(2));
                nd.setEmail(rs.getString(4));
                nd.setMatkhau(rs.getString(3));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nd;
    }

    public static boolean them(NguoiDung nd) {
        boolean kq = false;
        String haspas = HashPas.hashPassword(nd.getMatkhau());
        String sql = "insert into NGUOIDUNG(TENDN, MATKHAUHASH, EMAIL) values('" + nd.getTendn() + "','" + haspas + "','" + nd.getEmail() + "')";
        SQLServerProvider provider = new SQLServerProvider();
        provider.open();
        int rs = provider.excuteUpdate(sql);
        if (rs == 1) {
            kq = true;
        }
        provider.close();
        return kq;
    }

    public boolean isUsernameAvailable(String username) {
        boolean isAvailable = false;
        try {
            String sql = "select count(*) from NGUOIDUNG where TENDN = '" + username + "'";
            SQLServerProvider provider = new SQLServerProvider();
            provider.open();
            ResultSet rs = provider.excuteQuery(sql);

            if (rs.next() && rs.getInt(1) == 0) {
                isAvailable = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isAvailable;
    }

    public NguoiDung truyXuat(String username) {
        String sql = "SELECT TENDN, MATKHAUHASH,EMAIL,HOTEN,NGAYSINH,GIOITINH,DIEM FROM NGUOIDUNG WHERE TENDN = '" + username + "'";
        SQLServerProvider provider = new SQLServerProvider();
        provider.open();
        ResultSet rs = provider.excuteQuery(sql);
        try {
            if (rs.next()) {
                nd = new NguoiDung();
                nd.setTendn(rs.getString(1));
                nd.setMatkhau(rs.getString(2));
                nd.setEmail(rs.getString(3));
                nd.setHoten(rs.getString(4));
                nd.setNgaysinh(rs.getDate(5));
                nd.setGioitinh(rs.getString(6));
                nd.setDiem(rs.getInt(7));

            }

        } catch (SQLException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Không truy xuất được dữ liệu");
        }
        return nd;
    }

    public boolean capNhatDiem(String username, int diemMoi) {
        boolean updated = false;

        String sql = "UPDATE NGUOIDUNG SET DIEM = DIEM + " + diemMoi + " WHERE TENDN = '" + username + "'";
        SQLServerProvider provider = new SQLServerProvider();
        provider.open();

        int rowsUpdated = provider.excuteUpdate(sql);
        if (rowsUpdated > 0) {
            updated = true;
        }
        return updated;
    }

}
