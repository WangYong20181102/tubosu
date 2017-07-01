package com.tbs.tobosutype.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/6/26 11:33.
 * 装修开支主页索要用到的实体类
 */
//{-
//        "status":200,
//        "msg":"success",
//        "data":{-
//        "decorate_expense":[+
//        {-
//        "type_id":"2",
//        "type_name":"建材",
//        "count_cost":"51.28%",
//        "cost":"23000.50"
//        },
//        {-
//        "type_id":"3",
//        "type_name":"五金",
//        "count_cost":"47.94%",
//        "cost":"21500.00"
//        },
//        {-
//        "type_id":"4",
//        "type_name":"家具",
//        "count_cost":"0.78%",
//        "cost":"350.00"
//        }
//        ],
//        "all_cost":"44850.50",
//        "cost":"29.90%",
//        "expected_cost":"150000.00",
//        "decorate_record":[+
//        {-
//        "expend_name":"防盗网",
//        "cost":"6500.00",
//        "expend_time":"2017-06-18",
//        "content":"装修开支防盗网费用"
//        },
//        {-
//        "expend_name":"门窗",
//        "cost":"15000.00",
//        "expend_time":"2017-06-16",
//        "content":"装修开支门窗费用"
//        },
//        {-
//        "expend_name":"钢筋",
//        "cost":"23000.50",
//        "expend_time":"2017-06-13",
//        "content":"装修开支钢筋费用"
//        },
//        {-
//        "expend_name":"沙发",
//        "cost":"350.00",
//        "expend_time":"2017-06-11",
//        "content":"装修开支沙发费用"
//        }
//        ]
//        }
//        }

public class _DecorationExpent {
    String all_cost;//总开支
    String cost;//总开支占总预算的百分比
    String expected_cost;//总预算
    ArrayList<decorate_expense> decorateExpenseList = new ArrayList<>();
    ArrayList<decorate_record> decorate_recordList = new ArrayList<>();

    public _DecorationExpent(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array1 = jsonObject.getJSONArray("decorate_expense");
            for (int i = 0; i < array1.length(); i++) {
                this.decorateExpenseList.add(new decorate_expense(array1.get(i).toString()));
            }
            this.all_cost = jsonObject.getString("all_cost");
            this.cost = jsonObject.getString("cost");
            this.expected_cost = jsonObject.getString("expected_cost");
            JSONArray array2 = jsonObject.getJSONArray("decorate_record");
            for (int i = 0; i < array2.length(); i++) {
                this.decorate_recordList.add(new decorate_record(array2.get(i).toString()));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAll_cost() {
        return all_cost;
    }

    public void setAll_cost(String all_cost) {
        this.all_cost = all_cost;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getExpected_cost() {
        return expected_cost;
    }

    public void setExpected_cost(String expected_cost) {
        this.expected_cost = expected_cost;
    }

    public List<decorate_expense> getDecorateExpenseList() {
        return decorateExpenseList;
    }

    public void setDecorateExpenseList(ArrayList<decorate_expense> decorateExpenseList) {
        this.decorateExpenseList = decorateExpenseList;
    }

    public ArrayList<decorate_record> getDecorate_recordList() {
        return decorate_recordList;
    }

    public void setDecorate_recordList(ArrayList<decorate_record> decorate_recordList) {
        this.decorate_recordList = decorate_recordList;
    }

    //饼状图数据
    public class decorate_expense {
        String type_id;//类型id
        String type_name;//类型名称
        String count_cost;//花费占比
        String cost;//花费金额

        public decorate_expense(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                this.type_id = jsonObject.getString("type_id");
                this.type_name = jsonObject.getString("type_name");
                this.count_cost = jsonObject.getString("count_cost");
                this.cost = jsonObject.getString("cost");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getCount_cost() {
            return count_cost;
        }

        public void setCount_cost(String count_cost) {
            this.count_cost = count_cost;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }
    }

    //装修开支明细
    public class decorate_record {
        String type_id; // 大类id
        String id; // 记录id
        String expend_name;//开支明细的名称
        String cost;//开支明细的花费
        String expend_time;//开支明细的花费时间
        String content;//花费相关的描述

        public decorate_record(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                this.type_id = jsonObject.getString("type_id");
                this.id = jsonObject.getString("id");
                this.expend_name = jsonObject.getString("expend_name");
                this.cost = jsonObject.getString("cost");
                this.expend_time = jsonObject.getString("expend_time");
                this.content = jsonObject.getString("content");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getExpend_name() {
            return expend_name;
        }

        public void setExpend_name(String expend_name) {
            this.expend_name = expend_name;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getExpend_time() {
            return expend_time;
        }

        public void setExpend_time(String expend_time) {
            this.expend_time = expend_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }
    }
}
