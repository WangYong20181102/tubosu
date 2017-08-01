package com.tbs.tobosupicture.bean;

import java.util.List;

public class AddressDtailsEntity {
    public String Province;
    public String City;
    public String Area;
    public Data ProvinceItems;

    public class Data {
        public List<ProvinceEntity> Province;
    }

    public class ProvinceEntity {
        public String province_id;
        public String province_name;
        public List<CityEntity> City;


        public class CityEntity {
            public String city_id;
            public String city_name;
            public List<AreaEntity> Area;
        }



        public class AreaEntity {
            public String district_id;
            public String district_name;
        }
    }
}
