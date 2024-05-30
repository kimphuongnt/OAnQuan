/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

/**
 *
 * @author Kim Phuong
 */
public class Music {
    private int id;
    private String tenBaiHat;
    private String duongDanNhac;

    public Music() {
    }

    public Music(int id, String tenBaiHat, String duongDanNhac) {
        this.id = id;
        this.tenBaiHat = tenBaiHat;
        this.duongDanNhac = duongDanNhac;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getDuongDanNhac() {
        return duongDanNhac;
    }

    public void setDuongDanNhac(String duongDanNhac) {
        this.duongDanNhac = duongDanNhac;
    }
    
    
}
