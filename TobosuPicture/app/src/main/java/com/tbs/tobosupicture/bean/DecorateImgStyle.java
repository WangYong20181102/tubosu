package com.tbs.tobosupicture.bean;

import android.widget.Space;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/14.
 */

public class DecorateImgStyle {

    public DecorateImgStyle(){}
    public DecorateImgStyle(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.msg = jsonObject.getString("msg");
            this.status = jsonObject.getInt("status");
            this.data = jsonObject.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * status : 200 / 0
     * msg : success
     * data : {"space":[{"id":"22","class_name":"客厅"},{"id":"23","class_name":"卧室"},{"id":"24","class_name":"厨房"},{"id":"25","class_name":"餐厅"},{"id":"26","class_name":"玄关"},{"id":"27","class_name":"卫生间"},{"id":"28","class_name":"衣帽间"},{"id":"29","class_name":"阳台"},{"id":"30","class_name":"花园"},{"id":"31","class_name":"儿童房"},{"id":"32","class_name":"书房"}],"style":[{"id":"33","class_name":"简欧"},{"id":"34","class_name":"欧式"},{"id":"35","class_name":"田园"},{"id":"36","class_name":"简约"},{"id":"37","class_name":"中式"},{"id":"38","class_name":"现代简约"},{"id":"39","class_name":"美式"},{"id":"40","class_name":"新中式"},{"id":"41","class_name":"韩式"},{"id":"42","class_name":"现代"},{"id":"43","class_name":"新古典"},{"id":"44","class_name":"简单"},{"id":"45","class_name":"简中"},{"id":"46","class_name":"北欧"},{"id":"47","class_name":"混搭"},{"id":"48","class_name":"地中海"},{"id":"49","class_name":"欧式田园"},{"id":"50","class_name":"法式"},{"id":"51","class_name":"日式"},{"id":"52","class_name":"现代欧式"},{"id":"53","class_name":"中式古典"},{"id":"54","class_name":"经典"},{"id":"55","class_name":"古典"},{"id":"56","class_name":"现代中式"},{"id":"57","class_name":"后现代"},{"id":"58","class_name":"东南亚"},{"id":"950","class_name":"洛可可"}],"partial":[{"id":"72","class_name":"背景墙"},{"id":"73","class_name":"榻榻米"},{"id":"74","class_name":"窗帘"},{"id":"75","class_name":"照片墙"},{"id":"76","class_name":"吊顶"},{"id":"77","class_name":"飘窗"},{"id":"78","class_name":"楼梯"},{"id":"79","class_name":"推拉门"},{"id":"80","class_name":"阁楼"},{"id":"81","class_name":"吧台"},{"id":"82","class_name":"隔断"},{"id":"83","class_name":"博古架"},{"id":"84","class_name":"隐形门"},{"id":"85","class_name":"走廊"},{"id":"86","class_name":"窗台"},{"id":"87","class_name":"门厅"}],"layout":[{"id":"59","class_name":"小户型"},{"id":"60","class_name":"套房"},{"id":"61","class_name":"别墅"},{"id":"62","class_name":"公寓"},{"id":"63","class_name":"小面积"},{"id":"64","class_name":"小平米"},{"id":"65","class_name":"复式"},{"id":"66","class_name":"楼房"},{"id":"67","class_name":"大户型"},{"id":"68","class_name":"跃层"},{"id":"69","class_name":"庭院"},{"id":"70","class_name":"错层"},{"id":"71","class_name":"四合院"},{"id":"931","class_name":"一居室"},{"id":"932","class_name":"二居室"},{"id":"933","class_name":"三居室"},{"id":"934","class_name":"四居室"}],"color":[{"id":"130","class_name":"白色"},{"id":"131","class_name":"米色"},{"id":"132","class_name":"黄色"},{"id":"133","class_name":"橙色"},{"id":"134","class_name":"红色"},{"id":"135","class_name":"粉色"},{"id":"136","class_name":"绿色"},{"id":"137","class_name":"蓝色"},{"id":"138","class_name":"紫色"},{"id":"139","class_name":"黑色"},{"id":"140","class_name":"咖啡色"},{"id":"141","class_name":"灰色"},{"id":"142","class_name":"彩色"}]}
     */

    private int status;
    private String msg;
    private JSONObject data;

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

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public static class DecorateStyleBeanArray {
        private ArrayList<SpaceBean> spaceList = new ArrayList<>();
        private ArrayList<StyleBean> styleList = new ArrayList<>();
        private ArrayList<PartialBean> partialList = new ArrayList<>();
        private ArrayList<LayoutBean> layoutList = new ArrayList<>();
        private ArrayList<ColorBean> colorList = new ArrayList<>();

        public DecorateStyleBeanArray(){}

        public DecorateStyleBeanArray(JSONObject jsonObject){
            try {
                JSONArray spaceArr = jsonObject.getJSONArray("space");
                int spaceLength = spaceArr.length();
                for(int i=0;i<spaceLength;i++){
                    String id = spaceArr.getJSONObject(i).getString("id");
                    String class_name = spaceArr.getJSONObject(i).getString("class_name");
                    SpaceBean bean = new SpaceBean();
                    bean.setClass_name(class_name);
                    bean.setId(id);
                    this.spaceList.add(bean);
                }

                JSONArray styleArr = jsonObject.getJSONArray("style");
                int styleLength = spaceArr.length();
                for(int i=0;i<styleLength;i++){
                    String id = styleArr.getJSONObject(i).getString("id");
                    String class_name = styleArr.getJSONObject(i).getString("class_name");
                    StyleBean bean = new StyleBean();
                    bean.setClass_name(class_name);
                    bean.setId(id);
                    this.styleList.add(bean);
                }

                JSONArray partialArr = jsonObject.getJSONArray("partial");
                int partialLength = spaceArr.length();
                for(int i=0;i<partialLength;i++){
                    String id = partialArr.getJSONObject(i).getString("id");
                    String class_name = partialArr.getJSONObject(i).getString("class_name");
                    PartialBean bean = new PartialBean();
                    bean.setClass_name(class_name);
                    bean.setId(id);
                    this.partialList.add(bean);
                }


                JSONArray layoutArr = jsonObject.getJSONArray("layout");
                int layoutLength = spaceArr.length();
                for(int i=0;i<layoutLength;i++){
                    String id = layoutArr.getJSONObject(i).getString("id");
                    String class_name = layoutArr.getJSONObject(i).getString("class_name");
                    LayoutBean bean = new LayoutBean();
                    bean.setClass_name(class_name);
                    bean.setId(id);
                    this.layoutList.add(bean);
                }

                JSONArray colorArr = jsonObject.getJSONArray("color");
                int colorLength = spaceArr.length();
                for(int i=0;i<colorLength;i++){
                    String id = colorArr.getJSONObject(i).getString("id");
                    String class_name = colorArr.getJSONObject(i).getString("class_name");
                    ColorBean bean = new ColorBean();
                    bean.setClass_name(class_name);
                    bean.setId(id);
                    this.colorList.add(bean);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public ArrayList<SpaceBean> getSpaceList() {
            return spaceList;
        }

        public void setSpaceList(ArrayList<SpaceBean> spaceList) {
            this.spaceList = spaceList;
        }

        public ArrayList<StyleBean> getStyleList() {
            return styleList;
        }

        public void setStyleList(ArrayList<StyleBean> styleList) {
            this.styleList = styleList;
        }

        public ArrayList<PartialBean> getPartialList() {
            return partialList;
        }

        public void setPartialList(ArrayList<PartialBean> partial) {
            this.partialList = partial;
        }

        public ArrayList<LayoutBean> getLayoutList() {
            return layoutList;
        }

        public void setLayout(ArrayList<LayoutBean> layoutList) {
            this.layoutList = layoutList;
        }

        public ArrayList<ColorBean> getColorList() {
            return colorList;
        }

        public void setColorList(ArrayList<ColorBean> colorList) {
            this.colorList = colorList;
        }

        public static class SpaceBean extends HouseStyleBean {
            /**
             * id : 22
             * class_name : 客厅
             */
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }
        }

        public static class StyleBean extends HouseStyleBean{
            /**
             * id : 33
             * class_name : 简欧
             */


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }
        }

        public static class PartialBean extends HouseStyleBean{

            /**
             * id : 72
             * class_name : 背景墙
             */

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }
        }

        public static class LayoutBean extends HouseStyleBean{

            /**
             * id : 59
             * class_name : 小户型
             */

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }
        }

        public static class ColorBean extends HouseStyleBean{
            /**
             * id : 130
             * class_name : 白色
             */

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }
        }
    }
}
