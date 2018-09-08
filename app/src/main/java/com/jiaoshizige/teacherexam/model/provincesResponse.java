package com.jiaoshizige.teacherexam.model;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/2.
 */

public class provincesResponse {
    private List<mProvinces> provinces;

    public List<mProvinces> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<mProvinces> provinces) {
        this.provinces = provinces;
    }

    public static class mProvinces extends BasesortLetters{
        private List<mCitys> citys;
        private String provinceName;

        public List<mCitys> getCitys() {
            return citys;
        }

        public void setCitys(List<mCitys> citys) {
            this.citys = citys;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }
    }
    public static class mCitys{
        private String citysName;

        public String getCitysName() {
            return citysName;
        }

        public void setCitysName(String citysName) {
            this.citysName = citysName;
        }
    }
}
