package com.example.SiAntik;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class getLaporanResponse {

        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("message")
        private String message;
        @Expose
        @SerializedName("nullCount")
        private int nullCount;
        @Expose
        @SerializedName("positifCount")
        private int positifCount;
        @Expose
        @SerializedName("negatifCount")
        private int negatifCount;

        public getLaporanResponse(String status, String message, int nullCount, int positifCount, int negatifCount){
            this.status = status;
            this.message = message;
            this.nullCount = nullCount;
            this.positifCount = positifCount;
            this.negatifCount = negatifCount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getNullCount() {
            return nullCount;
        }

        public void setNullCount(int nullCount) {
            this.nullCount = nullCount;
        }

        public int getPositifCount() {
            return positifCount;
        }

        public void setPositifCount(int positifCount) {
            this.positifCount = positifCount;
        }

        public int getNegatifCount() {
            return negatifCount;
        }

        public void setNegatifCount(int negatifCount) {
            this.negatifCount = negatifCount;
        }
}


