package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/30 0030.
 */

/**
 * 订单详情
 */
@HttpResponse(parser = JsonResponseParser.class)
public class OrderDatailResponse extends SupportResponse {
    public mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String remark;//备注
        private String id;//订单id
        private String express_company;//快递
        private String order_sn;//订单编号
        private String type;//1课程订单2图书订单3班级订单
        private String status;//1.待付款2.已付款待发货4.待收货5.待评价6.已完成
        private String created_at;
        private String shipping_time;//发货时间
        private String consignee;//收货人
        private String invoice_no;//快递编号
        private String province;//省
        private String city;//市
        private String county;//县
        private String address;//详细地址
        private String mobile;//手机
        private String pay_type;//：1支付宝，2微信
        private float price;//订单价格
        private float pay_price;//实付金额
        private float shipping_price;//邮费
        private float coupons_money;//优惠券抵扣金额
        private float integral;//教师币抵扣金额
        private float deductible_amount;//活动优惠金额
        private List<mGoods> goods;
        private List<mGive_Books>give_books;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public List<mGive_Books> getGive_books() {
            return give_books;
        }

        public void setGive_books(List<mGive_Books> give_books) {
            this.give_books = give_books;
        }

        private  mExpress_check_info express_check_info;//物流信息，为空的时候不显示

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRemark() {
            return remark;
        }

        public String getExpress_company() {
            return express_company;
        }

        public void setExpress_company(String express_company) {
            this.express_company = express_company;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShipping_time() {
            return shipping_time;
        }

        public void setShipping_time(String shipping_time) {
            this.shipping_time = shipping_time;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getInvoice_no() {
            return invoice_no;
        }

        public void setInvoice_no(String invoice_no) {
            this.invoice_no = invoice_no;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public float getPay_price() {
            return pay_price;
        }

        public void setPay_price(float pay_price) {
            this.pay_price = pay_price;
        }

        public float getShipping_price() {
            return shipping_price;
        }

        public void setShipping_price(float shipping_price) {
            this.shipping_price = shipping_price;
        }

        public float getCoupons_money() {
            return coupons_money;
        }

        public void setCoupons_money(float coupons_money) {
            this.coupons_money = coupons_money;
        }

        public float getIntegral() {
            return integral;
        }

        public void setIntegral(float integral) {
            this.integral = integral;
        }

        public float getDeductible_amount() {
            return deductible_amount;
        }

        public void setDeductible_amount(float deductible_amount) {
            this.deductible_amount = deductible_amount;
        }

        public List<mGoods> getGoods() {
            return goods;
        }

        public void setGoods(List<mGoods> goods) {
            this.goods = goods;
        }

        public mExpress_check_info getExpress_check_info() {
            return express_check_info;
        }

        public void setExpress_check_info(mExpress_check_info express_check_info) {
            this.express_check_info = express_check_info;
        }
    }

    public static class mGive_Books{
        private String id;
        private String book_name;
        private String images;
        private String price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }


    public static class mGoods{
        private String group_id;
        private String goods_name;//商品名称
        private float price;//价格
        private float market_price;//市场价
        private String book_ids;
        private String images;//图片
        private String goods_num;//数量

        public String getBook_ids() {
            return book_ids;
        }

        public void setBook_ids(String book_ids) {
            this.book_ids = book_ids;
        }

        public String getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public float getMarket_price() {
            return market_price;
        }

        public void setMarket_price(float market_price) {
            this.market_price = market_price;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }
    }
    public static class mExpress_check_info{
        private String time;//时间
        private String ftime;//
        private String context;//内容
        private String location;//

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFtime() {
            return ftime;
        }

        public void setFtime(String ftime) {
            this.ftime = ftime;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
