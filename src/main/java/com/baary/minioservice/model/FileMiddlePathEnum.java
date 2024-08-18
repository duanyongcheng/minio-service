package com.baary.minioservice.model;

/**
 * 上传文件中间目录枚举
 */
public enum FileMiddlePathEnum {
    /**
     * profile
     */
    PROFILE_PATH("/profile/");

    private final String path;

    FileMiddlePathEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
