package com.jiaoshizige.teacherexam.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

public  class ProvinceBean {

    public static List<Province> getmList() {
        List<Province> mList = new ArrayList<>();
        Province provincegs = new Province();
        provincegs.setProvinceName("山东省");
        provincegs.setId(022);
        mList.add(provincegs);
        Province provincea = new Province();
        provincea.setId(001);
        provincea.setProvinceName("安徽");
        mList.add(provincea);
        Province provinceb = new Province();
        provinceb.setProvinceName("北京");
        provinceb.setId(002);
        mList.add(provinceb);
        Province provincec = new Province();
        provincec.setProvinceName("重庆");
        provincec.setId(003);
        mList.add(provincec);
        Province provincef = new Province();
        provincef.setProvinceName("福建");
        provincef.setId(004);
        mList.add(provincef);
        Province provinceg = new Province();
        provinceg.setProvinceName("甘肃");
        provinceg.setId(005);
        mList.add(provinceg);
        Province provinceg1 = new Province();
        provinceg1.setProvinceName("广东省");
        provinceg1.setId(006);
        mList.add(provinceg1);
        Province provinceg2 = new Province();
        provinceg2.setProvinceName("广西省");
        provinceg2.setId(007);
        mList.add(provinceg2);
        Province provinceg3 = new Province();
        provinceg3.setProvinceName("广西省");
        provinceg3.setId(010);
        mList.add(provinceg3);
        Province provincegh = new Province();
        provincegh.setProvinceName("海南省");
        provincegh.setId(011);
        mList.add(provincegh);
        Province provincegh1 = new Province();
        provincegh1.setProvinceName("河南省");
        provincegh1.setId(012);
        mList.add(provincegh1);
        Province provincegh2 = new Province();
        provincegh2.setProvinceName("河北省");
        provincegh2.setId(013);
        mList.add(provincegh2);
        Province provincegh3 = new Province();
        provincegh3.setProvinceName("黑龙江省");
        provincegh3.setId(014);
        mList.add(provincegh3);
        Province provincegh4 = new Province();
        provincegh4.setProvinceName("湖南省");
        provincegh4.setId(015);
        mList.add(provincegh4);
        Province provincegh5 = new Province();
        provincegh5.setProvinceName("湖北省");
        provincegh5.setId(016);
        mList.add(provincegh5);
        Province provincegj = new Province();
        provincegj.setProvinceName("吉林省");
        provincegj.setId(016);
        mList.add(provincegj);
        Province provincegj1 = new Province();
        provincegj1.setProvinceName("江西省");
        provincegj1.setId(017);
        mList.add(provincegj1);
        Province provincegj2 = new Province();
        provincegj2.setProvinceName("江苏省");
        provincegj2.setId(021);
        mList.add(provincegj2);
        Province provincegl = new Province();
        provincegl.setProvinceName("辽宁省");
        provincegl.setId(022);
        mList.add(provincegl);
        Province provincegn = new Province();
        provincegn.setProvinceName("内蒙古");
        provincegn.setId(022);
        mList.add(provincegn);
        Province provincegn1 = new Province();
        provincegn1.setProvinceName("宁夏回族");
        provincegn1.setId(022);
        mList.add(provincegn1);

        return mList;
    }
}
