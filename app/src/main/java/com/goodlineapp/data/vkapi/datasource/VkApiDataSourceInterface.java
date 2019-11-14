package com.goodlineapp.data.vkapi.datasource;

public interface VkApiDataSourceInterface {
    void loadMoreUsers(String request, String method, Long user_id);
    void loadMoreUsers(String request, String method);
    void loadAboutUser(Long user_id);
}
