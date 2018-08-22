package org.luncert.view.datasource.mysql.entity;

import lombok.Data;

@Data
public class Record {

    int id;

    long pigId;

    float weight;

    String description;

    String timestamp;

    String picName;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        Record record = new Record();

        public Builder id(int id) {
            record.id = id;
            return this;
        }

        public Builder pigId(long pigId) {
            record.pigId = pigId;
            return this;
        }

        public Builder weight(float weight) {
            record.weight = weight;
            return this;
        }

        public Builder description(String description) {
            record.description = description;
            return this;
        }

        public Builder timestamp(String timestamp) {
            record.timestamp = timestamp;
            return this;
        }

        public Builder picName(String picName) {
            record.picName = picName;
            return this;
        }

        public Record build() {
            return record;
        }

    }

}