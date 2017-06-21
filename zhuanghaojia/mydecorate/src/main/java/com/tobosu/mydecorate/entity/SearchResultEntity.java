package com.tobosu.mydecorate.entity;

import java.util.List;

/**  搜索结果实体类
 * Created by Lie on 2017/6/2.
 */

public class SearchResultEntity {

    /**
     * status : 200 操作成功 /  0 操作失败/没有更多数据!
     * msg : success
     * data : [{"aid":"304","title":"老房装修，一定要知道的装修步骤","view_count":"0","collect_count":"0","tup_count":"0","author_name":"今朝环保装修","author_id":"290742","time":"5月前","img_url":["http://imgs.bzw315.com/UploadFiles/Version2/3486/20160111/201601111728488580.jpg","http://imgs.bzw315.com/UploadFiles/image/News/20151215184132_3031.jpg","http://imgs.bzw315.com/UploadFiles/Version2/3486/20160111/201601111728209633.jpg","http://imgs.bzw315.com/UploadFiles/Version2/3486/20160111/201601111729585295.jpg"],"img_num":4,"style":1},{"aid":"301","title":"厨房装修注意事项及细节","view_count":"0","collect_count":"0","tup_count":"0","author_name":"美家帮装修公司","author_id":"309438","time":"5月前","img_url":["http://pics.sc.chinaz.com/files/pic/pic9/201505/apic12007.jpg"],"img_num":1,"style":1},{"aid":"298","title":"【总结】装了三套房，总结装修流程！！！","view_count":"0","collect_count":"0","tup_count":"0","author_name":"南昌新传奇装饰","author_id":"33246","time":"5月前","img_url":["http://cdn01.tobosu.net/mt_content/2016-12-11/p_584cb24038b45.jpg","http://pic.to8to.com/attch/image/20161211/20161211094919_71817.jpg","http://pic.to8to.com/attch/image/20161211/20161211094933_66451.jpg","http://pic.to8to.com/attch/image/20161211/20161211094946_83111.jpg","http://pic.to8to.com/attch/image/20161211/20161211094959_41083.jpg","http://pic.to8to.com/attch/image/20161211/20161211095030_71030.jpg","http://pic.to8to.com/attch/image/20161211/20161211095042_69508.jpg"],"img_num":7,"style":1},{"aid":"297","title":"靠马路的房子风水学知识大全","view_count":"1","collect_count":"0","tup_count":"0","author_name":"南昌新传奇装饰","author_id":"33246","time":"5月前","img_url":["http://pic.to8to.com/attch/image/20161211/20161211093028_17974.jpg","http://pic.to8to.com/attch/image/20161211/20161211093048_90782.jpg","http://pic.to8to.com/attch/image/20161211/20161211093105_70538.jpg","http://pic.to8to.com/attch/image/20161211/20161211093128_71361.jpg","http://pic.to8to.com/attch/image/20161211/20161211093203_75842.jpg"],"img_num":5,"style":3},{"aid":"287","title":"2016年下半年房价走势，17年适合买房吗？","view_count":"12","collect_count":"0","tup_count":"0","author_name":"家装小喵","author_id":"312891","time":"5月前","img_url":["http://cdn01.tobosu.net/mt_content/2016-12-10/p_584b7de3eb251.jpg","http://cdn01.tobosu.net/mt_content/2016-12-10/p_584b7df023654.jpg","http://cdn01.tobosu.net/mt_content/2016-12-10/p_584b7dfa53a81.jpg"],"img_num":3,"style":2},{"aid":"278","title":"好好的新房装修成这样，一般人见都没见过","view_count":"3","collect_count":"0","tup_count":"0","author_name":"新房室内装修","author_id":"312917","time":"5月前","img_url":["http://dingyue.nosdn.127.net/ETVszQnh6M=iqinZ4HhfgtMWV48evYQvsaota1mUpeEvM1481289838838.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/PGsfaz7F8R2TrY=8qttLlmYXByGxq08g40kFXCqWs9a201481289838841.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/6VXldBOFVKUMxY8r=bTkqA45vy3WL4zhElhegc5AfQ5xP1481289838842.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/1=6=rb2fzJKOOlRwbLTOABlDzTBQ272Ga2ZcU6JtvBohR1481289838843.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/uZuehH4MaXBtECnr3NJ2gsBSTdKy61YPJX6yq7mUuIypi1481289838845.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/5UllRAM6LBpBELGEGqgj4Ubk5=YRQW8TdnIWa9VV9BNYY1481289838847.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/12Q6pDGlbgzpKEh9ZkN8kAEF0pBa4ilAl0SkTjw8OCul21481289838850.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/eIMVO4BkftwEz5c8sY3u7e3G2d90echyHu=WcROgiQpHU1481289838850.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/Zjqd45aCE6yK2XCf3M2se8jflA7ouGtfYyAGaB7Xgkkot1481289838851.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50"],"img_num":9,"style":2},{"aid":"277","title":"晒晒90后小夫妻的婚房，这楼梯墙彻底震撼到我了！","view_count":"5","collect_count":"0","tup_count":"0","author_name":"新房室内装修","author_id":"312917","time":"5月前","img_url":["http://dingyue.nosdn.127.net/ALDO=zHXPBnHrgd1pffm07YI3SQEPry3wxZdndTlJfx6n1481290375772.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://img2.cache.netease.com/3g/2015/11/2/20151102141437e4822.png","http://dingyue.nosdn.127.net/vtl=6TgbYwQWaVt01dJbGLxx5tbLLt7Zomz1egR1nCQYb1481290375775.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/pic1GrrqG0st54p9AHqUpKYNplob3VwqLXO=RX1LyLLoP1481290375776.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/iqNNX3I1USTs9JCy71rFD8L1G7dI6gBxz8nGpmPcFfIV71481290375777.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/QEl34f97D45PlEAewjHy7fvpgLyWnaa8bbYgmZ7IZlthS1481290375777.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/PxYjD9J=1Ry8E0GKAPPiWzVd7X=PEw60NdchtO1f7FK3a1481290375777.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/PybnG6x1OsKXTm9VqUaLM6dBS79GWK9404kfDBNawpOvh1481290375777.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50","http://dingyue.nosdn.127.net/OLax6K4nhn0RnACs6j=qY3srYiUCN4i9T7ld9chV1ror31481290375777.jpg?imageView&amp;thumbnail=690x10000&amp;quality=50"],"img_num":9,"style":3},{"aid":"276","title":"老公花了七万不到！就能把新房装成这样，我想都不敢想！","view_count":"3","collect_count":"0","tup_count":"0","author_name":"新房室内装修","author_id":"312917","time":"5月前","img_url":["http://p3.pstatp.com/large/12d600009822d603902e","http://p3.pstatp.com/large/12d70000de5171c229d9","http://p3.pstatp.com/large/12d90002733346d12752","http://p1.pstatp.com/large/12d80003ea6d12752a8c","http://p9.pstatp.com/large/12d600009823e9475cc4","http://p3.pstatp.com/large/12d80003ea6e450be863","http://p3.pstatp.com/large/12d70000de53bf85a6ed","http://p3.pstatp.com/large/12d6000098267bcfeb4e","http://p3.pstatp.com/large/12d80003ea70f5e650d9"],"img_num":9,"style":1},{"aid":"267","title":"【室内装修】一个错误毁了一套房？那是你不知这10个技巧！","view_count":"67","collect_count":"0","tup_count":"1","author_name":"武汉江南美装饰","author_id":"310056","time":"5月前","img_url":["http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a69209965c.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a6ba54622f.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a6bc67e906.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a6bfc63d67.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a6c1ca545e.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a6c7a04b82.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a6cab0f4c1.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a6d9a73c0d.jpg"],"img_num":8,"style":3},{"aid":"257","title":"这样厨房，才是女主人的最爱！","view_count":"25","collect_count":"0","tup_count":"1","author_name":"广州苹果装饰","author_id":"308608","time":"5月前","img_url":["http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a4e2ee4a48.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a4e5c4742f.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a4ee98bfcc.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a4f06aa0bc.jpg","http://cdn01.tobosu.net/mt_content/2016-12-09/p_584a4f26d7629.jpg"],"img_num":5,"style":2}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * aid : 304
         * title : 老房装修，一定要知道的装修步骤
         * view_count : 0
         * collect_count : 0
         * tup_count : 0
         * author_name : 今朝环保装修
         * author_id : 290742
         * time : 5月前
         * img_url : ["http://imgs.bzw315.com/UploadFiles/Version2/3486/20160111/201601111728488580.jpg","http://imgs.bzw315.com/UploadFiles/image/News/20151215184132_3031.jpg","http://imgs.bzw315.com/UploadFiles/Version2/3486/20160111/201601111728209633.jpg","http://imgs.bzw315.com/UploadFiles/Version2/3486/20160111/201601111729585295.jpg"]
         * img_num : 4
         * style : 1
         */

        private String aid;
        private String title;
        private String view_count;
        private String collect_count;
        private String tup_count;
        private String author_name;
        private String author_id;
        private String time;
        private int img_num;
        private int style;
        private List<String> img_url;

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getCollect_count() {
            return collect_count;
        }

        public void setCollect_count(String collect_count) {
            this.collect_count = collect_count;
        }

        public String getTup_count() {
            return tup_count;
        }

        public void setTup_count(String tup_count) {
            this.tup_count = tup_count;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getImg_num() {
            return img_num;
        }

        public void setImg_num(int img_num) {
            this.img_num = img_num;
        }

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public List<String> getImg_url() {
            return img_url;
        }

        public void setImg_url(List<String> img_url) {
            this.img_url = img_url;
        }
    }
}
