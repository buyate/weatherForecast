package com.wpl.weatherforecast.bean;

import java.util.List;

public class JsonToGsonCity {
    private String reason;
    private List<result> result;

    public class result {
        private String id;
        private String province;
        private String city;
        private String district;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<JsonToGsonCity.result> getResult() {
        return result;
    }

    public void setResult(List<JsonToGsonCity.result> result) {
        this.result = result;
    }
}
