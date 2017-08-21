package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/8/3 17:39.
 * 接收到的动态消息
 */

public class _ReceiveMsg {

    /**
     * my_sponsor : {"msg_count":"0","icon":""}
     * my_participation : {"msg_count":"0","icon":""}
     * all_msg_count : 0
     */

    private MySponsor my_sponsor;//我的发起
    private MyParticipation my_participation;//我的参与
    private String all_msg_count;//消息总数

    public MySponsor getMy_sponsor() {
        return my_sponsor;
    }

    public void setMy_sponsor(MySponsor my_sponsor) {
        this.my_sponsor = my_sponsor;
    }

    public MyParticipation getMy_participation() {
        return my_participation;
    }

    public void setMy_participation(MyParticipation my_participation) {
        this.my_participation = my_participation;
    }

    public String getAll_msg_count() {
        return all_msg_count;
    }

    public void setAll_msg_count(String all_msg_count) {
        this.all_msg_count = all_msg_count;
    }

    public static class MySponsor {
        /**
         * msg_count : 0
         * icon :
         */

        private String msg_count;
        private String icon;

        //我的发起
        public MySponsor(String msg_count, String icon) {
            this.msg_count = msg_count;
            this.icon = icon;
        }

        public MySponsor() {

        }

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

    public static class MyParticipation {
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
