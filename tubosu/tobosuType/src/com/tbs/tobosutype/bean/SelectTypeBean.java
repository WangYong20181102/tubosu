package com.tbs.tobosutype.bean;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/16 09:10.
 */
public class SelectTypeBean {
    /**
     * 一级id
     */
    private String id;
    /**
     * 一级分类名称
     */
    private String category_name;
    /**
     * 一级子类集合
     */
    private List<Child> child;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "SelectTypeBean{" +
                "id='" + id + '\'' +
                ", category_name='" + category_name + '\'' +
                ", child=" + child +
                '}';
    }

    public static class Child{
        /**
         * 二级id
         */
        private String id;
        /**
         * 二级分类名称
         */
        private String category_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        @Override
        public String toString() {
            return "Child{" +
                    "id='" + id + '\'' +
                    ", category_name='" + category_name + '\'' +
                    '}';
        }
    }

}
