package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/7/20 16:16.
 */

public class _TestBean {

    /**
     * my_sponsor : {"msg_count":"0","icon":""}
     * my_participation : {"msg_count":"0","icon":""}
     * all_msg_count : 0
     */

    private MySponsorBean my_sponsor;
    private MyParticipationBean my_participation;
    private String all_msg_count;

    public MySponsorBean getMy_sponsor() {
        return my_sponsor;
    }

    public void setMy_sponsor(MySponsorBean my_sponsor) {
        this.my_sponsor = my_sponsor;
    }

    public MyParticipationBean getMy_participation() {
        return my_participation;
    }

    public void setMy_participation(MyParticipationBean my_participation) {
        this.my_participation = my_participation;
    }

    public String getAll_msg_count() {
        return all_msg_count;
    }

    public void setAll_msg_count(String all_msg_count) {
        this.all_msg_count = all_msg_count;
    }

    public static class MySponsorBean {
        /**
         * msg_count : 0
         * icon :
         */

        private String msg_count;
        private String icon;

        public String getMsg_count() {
            return msg_count;
        }

        public void setMsg_count(String msg_count) {
            this.msg_count = msg_count;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public static class MyParticipationBean {
        /**
         * msg_count : 0
         * icon :
         */

        private String msg_count;
        private String icon;

        public String getMsg_count() {
            return msg_count;
        }

        public void setMsg_count(String msg_count) {
            this.msg_count = msg_count;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
