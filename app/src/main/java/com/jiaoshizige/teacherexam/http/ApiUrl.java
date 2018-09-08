package com.jiaoshizige.teacherexam.http;


public class ApiUrl {
    public static String URL = "https://wxapi.zgclass.com/api/v1/";
    public static String BASEIMAGE = "https://wxapi.zgclass.com/";
    /*账号密码登录*/
    public static String LOGIN = URL + "login";
    /*验证码*/
    public static String SENDCODE = URL + "sms_send";
    /*验证码登录*/
    public static String CODELOGIN = URL + "code_login";
    /*注册*/
    public static String REGISTER = URL + "register";
    /*忘记密码*/
    public static String FORGRT = URL + "user/reset_password";
    /*完善信息*/
    public static String UPDATA = URL + "user/update";
    /*图书筛选*/
    public static String CHECKBOOK = URL + "book_cat";
    /*规则*/
    public static String RULE = URL + "explain";
    /*退出登录*/
    public static String EXIT = URL + "logout";
    /*消息中心*/
    public static String PUSH = URL + "push";
    /*消息详情*/
    public static String PUSHDETAIL = URL + "push_detail";
    /*删除消息*/
    public static String PUSHDELETE = URL + "push_view";
    /*微信登录*/
    public static String THIRDLOGIN = URL + "third_login";
    /*绑定第三方*/
    public static String BING = URL + "third_bind";
    /*充值记录*/
    public static String RECORD = URL + "teacher_coin/get_pay_log";
    /*是否绑定微信*/
    public static String BIND = URL + "user/is_bind";
    /*绑定微信*/
    public static String BINDWECAT = URL + "bind_wechat";
    /*解除微信绑定*/
    public static String UNBINDING = URL + "unbind";
    /*确认收货*/
    public static String CONFIRM = URL + "order/confirm_shipping";
    /**
     * 资讯列表
     */
    public static String ARTICLELIST = URL + "article_list";
    /**
     * 资讯详情
     */
    public static String DETIALDETIAL = URL + "article_detail";
    /**
     * 班级列表
     */
    public static String CLASSLIST = URL + "group_list";
    /**
     * 班级详情
     */
    public static String CLASSSDETAIL = URL + "group_detail";
    /**
     * 班级详情介绍
     */
    public static String CLASSINSTRODUCE = URL + "group_detail/detail";
    /**
     * 班级课程列表
     */
    public static String CLASSCOURSELIST = URL + "group_detail/course";
    /**
     * 课程列表
     */
    public static String COURSELIST = URL + "course_list";
    /**
     * 课程详情
     */
    public static String COURSERDETAIL = URL + "course_detail";
    /**
     * 课程详情介绍
     */
    public static String COURSEINTRODUCE = URL + "course_detail_introduce";
    /**
     * 课程目录
     */
    public static String COURSECATALOG = URL + "course_detail_catalogs";

    /**
     * 图书列表
     */
    public static String BOOKLIST = URL + "book_list";
    /**
     * 图书详情
     */
    public static String BOOKDETAIL = URL + "book_detail";
    /**
     * 图书介绍
     */
    public static String BOOKINTRODUCE = URL + "book_detail/detail";
    /**
     * 评论列表
     */
    public static String EVALUATELIST = URL + "comment";

    /*个人信息*/
    public static String USER = URL + "user";
    /*考试类型*/
    public static String EXAMTYPE = URL + "exam_type";
    /*我的卡券*/
    public static String MYCOUPON = URL + "my_coupons";
    /*教师币*/
    public static String TEACHER_COIN = URL + "teacher_coin";
    /*我的收藏*/
    public static String COLLECT = URL + "collect/list";
    /*我的订单*/
    public static String ORDER = URL + "order/lists";
    /*测评结果*/
    public static String EVALUATE = URL + "user/evaluate";
    /*预约测评*/
    public static String APPRAISAL = URL + "user/evaluate_order";
    /*获取练习题*/
    public static String PAPER = URL + "paper";
    /*绑定登录设备*/
    public static String BINDING = URL + "user/registration";
    /**
     * 搜索
     */
    public static String SEARCH = URL + "search_tag";
    /**
     * 清除最近搜索
     */
    public static String CLEARSEARCH = URL + "clear_history";
    /**
     * s收藏
     */
    public static String ADDCOLLECT = URL + "collect/add";
    /**
     * 取消收藏
     */
    public static String CANCLECOLLECT = URL + "collect/del";
    /*取消收藏*/
    public static String DELCOLLECT = URL + "collect/del";
    /*图书确认订单*/
    public static String BOOKCONFIRM = URL + "book/order_confirm";
    /*选择收货地址*/
    public static String SELECTADRESS = URL + "address/list";
    /*删除地址*/
    public static String DELADRESS = URL + "address/del";
    /*编辑地址*/
    public static String EDIT = URL + "address/edit";
    /*添加地址*/
    public static String ADD = URL + "address/add";
    /*班级确认订单*/
    public static String CLASS_CONFIRM = URL + "group/order_confirm";
    /*课程确认订单*/
    public static String COURSE_CONFIRM = URL + "course/order_confirm";
    /*确认收货*/
    public static String AFFIRM = URL + "order/confirm_shipping";

    /*评价标签*/
    public static String LABLE = URL + "comment/label";
    /*提交评价*/
    public static String COMMINT = URL + "comment/add";
    /*意见反馈*/
    public static String SUGGEST = URL + "feedback/add";
    /*我的课程*/
    public static String COURSE = URL + "my_course";
    /*订单详情*/
    public static String ORDERDATAIL = URL + "order/detail";
    /*物流详情*/
    public static String LOGISTICS = URL + "order/express_check";
    /*提交订单*/
    public static String CREAT = URL + "order/order_create";
    /*支付*/
    public static String PAY = URL + "pay/order_pay";
    /*公开课*/
    public static String OPEN = URL + "open_course";
    /*公开课详情*/
    public static String OPENDETAIL = URL + "open_course/detail";
    /*公开课订单*/
    public static String OPENORDER = URL + "open_course/order_confirm";
    /*我的公开课*/
    public static String MYOPEN = URL + "order/open_course";
    /*充值*/
    public static String PAYCOIN = URL + "teacher_coin/pay";
    /*领取活动优惠券*/
    public static String GETCOUPONS = URL + "coupons/get";
    /*删除订单*/
    public static String DELETEORDER = URL + "order/delete";
    /**
     * 学习日历日期
     */
    public static String GETWEEK = URL + "get_week";
    /**
     * 学习日历内容
     */
    public static String STUDYCALEND = URL + "learn_record/lists";
    /**
     * 我的班级
     */
//    public static String MYGROUP = URL + "my_group";
    public static String MYGROUP = URL + "my_newgrouptest";

    public static String ALLMYGROUP = URL + "my_grouptest";
    /**
     * 问答列表
     */
    public static String QALIST = URL + "course/qa_list";
    /**
     * 问答详情
     */
    public static String QADETAIL = URL + "course/qa_detail";
    /**
     * 笔记列表
     */
    public static String NOTELIS = URL + "course/note";
    /**
     * 笔记详情
     */
    public static String NOTEDETAIL = URL + "course/note_detail";
    /**
     * 课时列表 作业 笔记
     */
    public static String CHAPTER = URL + "course/chapter";
    /**
     * 提问
     */
    public static String ASKADD = URL + "course/ask_add";
    /**
     * 添加笔记
     */
    public static String NOTEADD = URL + "course/note_add";
    /**
     * 作业列表
     */
    public static String HOMEWORKLIST = URL + "course/work_list";
    /**
     * 作业详情
     */
    public static String HOMEWORKDEATIL = URL + "course/work_detail";
    /**
     * 写作业
     */
    public static String DOHOMEWORK = URL + "course/work_answer";
    /**
     * 删除作业
     */
    public static String DELETHOMEWORK = URL + "course/work_delete";
    /**
     * 回复别人作业
     */
    public static String REPLAYHOMEWORK = URL + "course/work_reply";
    /**
     * 我的作业
     */
    public static String MYHOMEWORK = URL + "course/my_work";
    /**
     * 点赞
     */
    public static String GETZAN = URL + "course/like";
    /**
     * 回复详情
     */
    public static String REPLAYDETIAL = URL + "course/reply_detail";
    /**
     * 回复问答
     */
    public static String REPLYADD = URL + "course/reply_add";
    /**
     * 签到
     */
    public static String USERSIGN = URL + "user/sign";
    /**
     * 签到页面数据
     */
    public static String SIGNDIALOG = URL + "user/sign_index";
    /**
     * 礼物列表
     */
    public static String GIFTLIST = URL + "gift/lists";
    /**
     * 赠送礼物
     */
    public static String SENDFIFT = URL + "gift/give";
    /**
     * 添加学习记录
     */
    public static String AddLEARNRECORD = URL + "learn_record/add";
    /**
     * 课程推荐列表
     */
    public static String COURSETYPE = URL + "coursetype_list";
    /**
     * 课程推荐详情列表
     */
    public static String COURSETYPELISTS = URL + "coursetype_lists";
    /**
     * 课程详情目录
     */
    public static String COURSEDETAILCATALOGS = URL + "course_detail_catalogs_new";
    /**
     * 广告
     */
    public static String ADVERTISEMENT = URL + "advertisement";
    /*资讯分享*/
    public static String ARTICLE = URL + "article_share";
    /*推送主列表*/
    public static String INDEX = URL + "index_p";
    /*删除 查看推送  1查看 2删除*/
    public static String DELETE = URL + "push_view_del";
    /*是否在线直播*/
    public static String ONLINE = URL + "getOnlineSteam";
    /*分享二维码*/
    public static String SHARE = URL + "collect/share_ewm";
    /*新课程列表*/
    public static String NEWCOURSE = URL + "coursetype_list_one";
    /*课程筛选*/
    public static String COURSETYPECAT = URL + "course_cat";
    /*我的题库*/
    public static String QUESTIONBANK = URL + "tiku_list";
    /*题库详情*/
    public static String QUESTIONBANKDETAIL = URL + "kaodian_list";
    /*练习记录*/
    public static String PRACTICENOTE = URL + "my_record";
    /*关卡列表*/
    public static String CHECKPOINT = URL + "guanqia_list";
    /*闯关列表*/
    public static String RECRUITPROBLEM = URL + "chuangguan";
    /*查看我的练习记录*/
    public static String PRACTICERECORDS = URL + "my_record_view";
    /*纠错*/
    public static String ERRORCORRECT = URL + "shiti_error";
    /*试题收藏*/
    public static String SHITICOLLECTION = URL + "shiti_collect";
    /*取消收藏*/
    public static String SHITICANCELCOLLECTION = URL + "shiti_collect_del";
    /*题库收藏列表*/
    public static String TIKUCOLLECTIONLIST = URL + "my_collect";
    /*错题练习*/
    public static String ERRORTOPIC = URL + "error_record";
    /*错题详情*/
    public static String ERRORTOPICDETAIL = URL + "my_error";
    /*移除试题*/
    public static String REMOVEQUESTION = URL + "error_del";
    /*模拟试题开始*/
    public static String SIMULATION = URL + "moni_start";
    /*模考记录*/
    public static String MODELTESTRECORD = URL + "moni_record";
    /*模拟(真题)查看解析*/
    public static String RECORDDETAIL = URL + "moni_view";

    /*模拟题提交试卷*/
    public static String GETPAPER = URL + "moni_post";

    /*生成模拟题*/
    public static String MONIQUESTION = URL + "moni_rand";
    /*真题列表*/
    public static String ZHENTILIST = URL + "zhenti_list";
    /*真题练习*/
    public static String ZHENTIPRICTICE = URL + "zhenti";
    /*真题下载*/
    public static String ZHENTIDOWN = URL + "zhenti_down";
    /*真题提交*/
    public static String ZHENTIPOST = URL + "zhenti_post";
    /*闯关提交*/
    public static String POSTANSWERT = URL + "chuangguan_post";
    /*模拟/真题练习结果*/
    public static String MONIRESULT = URL + "moni_result";
    /*首页直播*/
    public static String HOMEOPEN = URL + "home_open_course";
    /*真题批量提交*/
    public static String LOCALZHENTIPOST = URL + "zhenti_any_post";

}

