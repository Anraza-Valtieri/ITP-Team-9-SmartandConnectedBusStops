
package com.sit.itp_team_9_smartandconnectedbusstops.SGUV;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONUVParser {

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("api_info")
    @Expose
    private ApiInfo apiInfo;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public ApiInfo getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
    }

}
