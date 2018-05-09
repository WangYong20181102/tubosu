package com.tbs.tbs_mj.bean;

import java.util.List;

/**
 * Created by Lie on 2017/10/13.
 */

public class TaotuJsonItem {


    /**
     * status : 200
     * msg : success
     * data : [{"id":"83385","collect_id":"12572","cover_url":"https://pic.tbscache.com/impress_pic/2017-09-23/small/p_59c6005e48a2d.jpg","image_width":600,"image_height":399,"title":"地中海复式"},{"id":"83405","collect_id":"12566","cover_url":"https://st.hzcdn.com/simgs/6cb1182c095d0c1e_4-8582/traditional-home-bar.jpg","image_width":600,"image_height":600,"title":"美式三居室"},{"id":"83406","collect_id":"12565","cover_url":"https://st.hzcdn.com/simgs/7f215fe5097a23ca_4-3653/traditional-dining-room.jpg","image_width":600,"image_height":600,"title":"美式庭院"},{"id":"83407","collect_id":"12564","cover_url":"https://st.hzcdn.com/simgs/23d1f6d80978a4de_4-1094/traditional-dining-room.jpg","image_width":600,"image_height":600,"title":"美式小平米"},{"id":"83408","collect_id":"12563","cover_url":"https://st.hzcdn.com/simgs/d9a1a991094158eb_4-9934/traditional-dining-room.jpg","image_width":600,"image_height":600,"title":"美式跃层"},{"id":"83409","collect_id":"12562","cover_url":"https://st.hzcdn.com/simgs/5791182009822f5f_4-0682/traditional-dining-room.jpg","image_width":600,"image_height":600,"title":"美式错层"},{"id":"83410","collect_id":"12561","cover_url":"https://st.hzcdn.com/simgs/6fd1f0d8095e9c91_4-4750/traditional-dining-room.jpg","image_width":600,"image_height":600,"title":"美式复式"},{"id":"83418","collect_id":"12560","cover_url":"https://st.hzcdn.com/simgs/5a3106d3072b5809_9-1700/farmhouse-bedroom.jpg","image_width":600,"image_height":600,"title":"混搭小面积"},{"id":"83433","collect_id":"12549","cover_url":"http://cdn111.dev.tobosu.com/impress_pic/2017-11-06/small/p_59fffc03e549e.jpg","image_width":864,"image_height":600,"title":"混搭小户型"}]
     */

    private int status;
    private String msg;
    private List<TaotuBean> data;

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

    public List<TaotuBean> getData() {
        return data;
    }

    public void setData(List<TaotuBean> data) {
        this.data = data;
    }

    public static class TaotuBean {
        /**
         * id : 83385
         * collect_id : 12572
         * cover_url : https://pic.tbscache.com/impress_pic/2017-09-23/small/p_59c6005e48a2d.jpg
         * image_width : 600
         * image_height : 399
         * title : 地中海复式
         */

        private String id;
        private String collect_id;
        private String cover_url;
        private int image_width;
        private int image_height;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCollect_id() {
            return collect_id;
        }

        public void setCollect_id(String collect_id) {
            this.collect_id = collect_id;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public int getImage_width() {
            return image_width;
        }

        public void setImage_width(int image_width) {
            this.image_width = image_width;
        }

        public int getImage_height() {
            return image_height;
        }

        public void setImage_height(int image_height) {
            this.image_height = image_height;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
