package com.tbs.tobosutype.base;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mr.Wang on 2019/1/2 14:21.
 */
public class HistoryRecordBean {
    private boolean isCheck;    //选中状态
    private String id;
    private String uid;
    @SerializedName("data")
    private DataBean dataX;
    private String add_time;
    private String number;
    private String total_price;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public DataBean getDataX() {
        return dataX;
    }

    public void setDataX(DataBean dataX) {
        this.dataX = dataX;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public static class DataBean {

        private String room_length;
        private String room_width;
        private String room_height;
        private String door_height;
        private String door_width;
        private String door_number;
        private String window_height;
        private String window_width;
        private String window_number;
        private String tile_length;
        private String tile_width;
        private String tile_price;
        private String paper_info;
        private String paper_price;
        private String coverage_rate;
        private String coating_price;
        private String cloth_width;
        private String curtains_price;

        public String getRoom_length() {
            return room_length;
        }

        public void setRoom_length(String room_length) {
            this.room_length = room_length;
        }

        public String getRoom_width() {
            return room_width;
        }

        public void setRoom_width(String room_width) {
            this.room_width = room_width;
        }

        public String getRoom_height() {
            return room_height;
        }

        public void setRoom_height(String room_height) {
            this.room_height = room_height;
        }

        public String getDoor_height() {
            return door_height;
        }

        public void setDoor_height(String door_height) {
            this.door_height = door_height;
        }

        public String getDoor_width() {
            return door_width;
        }

        public void setDoor_width(String door_width) {
            this.door_width = door_width;
        }

        public String getDoor_number() {
            return door_number;
        }

        public void setDoor_number(String door_number) {
            this.door_number = door_number;
        }

        public String getWindow_height() {
            return window_height;
        }

        public void setWindow_height(String window_height) {
            this.window_height = window_height;
        }

        public String getWindow_width() {
            return window_width;
        }

        public void setWindow_width(String window_width) {
            this.window_width = window_width;
        }

        public String getWindow_number() {
            return window_number;
        }

        public void setWindow_number(String window_number) {
            this.window_number = window_number;
        }

        public String getTile_length() {
            return tile_length;
        }

        public void setTile_length(String tile_length) {
            this.tile_length = tile_length;
        }

        public String getTile_width() {
            return tile_width;
        }

        public void setTile_width(String tile_width) {
            this.tile_width = tile_width;
        }

        public String getTile_price() {
            return tile_price;
        }

        public void setTile_price(String tile_price) {
            this.tile_price = tile_price;
        }

        public String getPaper_info() {
            return paper_info;
        }

        public void setPaper_info(String paper_info) {
            this.paper_info = paper_info;
        }

        public String getPaper_price() {
            return paper_price;
        }

        public void setPaper_price(String paper_price) {
            this.paper_price = paper_price;
        }

        public String getCoverage_rate() {
            return coverage_rate;
        }

        public void setCoverage_rate(String coverage_rate) {
            this.coverage_rate = coverage_rate;
        }

        public String getCoating_price() {
            return coating_price;
        }

        public void setCoating_price(String coating_price) {
            this.coating_price = coating_price;
        }

        public String getCloth_width() {
            return cloth_width;
        }

        public void setCloth_width(String cloth_width) {
            this.cloth_width = cloth_width;
        }

        public String getCurtains_price() {
            return curtains_price;
        }

        public void setCurtains_price(String curtains_price) {
            this.curtains_price = curtains_price;
        }
    }
}
