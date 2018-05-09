package com.tbs.tbs_mj.bean;
import java.util.ArrayList;

/**
 * Created by Lie on 2017/10/13.
 */

public class DanTuJsonItem {


    /**
     * status : 200
     * msg : success
     * data : [{"id":"824866","collect_id":"12570","cover_url":"https://pic.tbscache.com/impress_pic/2017-09-14/small/p_59b9fb5b7459f.jpg","image_width":960,"image_height":593},{"id":"824871","collect_id":"12569","cover_url":"https://pic.tbscache.com/impress_pic/2017-09-22/small/p_59c4b97fb54bb.jpg","image_width":800,"image_height":600},{"id":"824878","collect_id":"12568","cover_url":"https://pic.tbscache.com/impress_pic/2017-09-22/small/p_59c4b7ffbcc5d.jpg","image_width":960,"image_height":600},{"id":"824906","collect_id":"12567","cover_url":"https://pic.tbscache.com/impress_pic/2017-09-22/small/p_59c4bcffaf748.jpg","image_width":800,"image_height":576},{"id":"825881","collect_id":"12559","cover_url":"https://st.hzcdn.com/simgs/e391d1640458ff58_9-8684/home-design.jpg","image_width":600,"image_height":600},{"id":"825891","collect_id":"12558","cover_url":"https://st.hzcdn.com/simgs/1a3103ab036cbc80_9-1707/contemporary-exterior.jpg","image_width":600,"image_height":600},{"id":"825900","collect_id":"12557","cover_url":"https://st.hzcdn.com/simgs/0b11743c072b5803_9-1700/modern-living-room.jpg","image_width":600,"image_height":600},{"id":"825905","collect_id":"12556","cover_url":"https://st.hzcdn.com/simgs/fac13c0b04eda231_9-9710/scandinavian-deck.jpg","image_width":600,"image_height":600}]
     */

    private int status;
    private String msg;
    private ArrayList<DantuBean> data;

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

    public ArrayList<DantuBean> getData() {
        return data;
    }

    public void setData(ArrayList<DantuBean> data) {
        this.data = data;
    }

    public static class DantuBean {
        /**
         * id : 824866
         * collect_id : 12570
         * cover_url : https://pic.tbscache.com/impress_pic/2017-09-14/small/p_59b9fb5b7459f.jpg
         * image_width : 960
         * image_height : 593
         */

        private String id;
        private String collect_id;
        private String cover_url;
        private int image_width;
        private int image_height;

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
    }
}
