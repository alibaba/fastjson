package com.alibaba.json.bvtVO;

import java.io.Serializable;


public class AccessHttpConfigModel {
    /**
     * 上传文件的配置.
     *
     * @author wangwb (mailto:wangwb@primeton.com)
     */
    
    public static class FileUploadConfig implements Serializable{
        private String tempDir;

        private int maxSize;

        private int inMemorySize;

        private String exclude;

        /**
         * 获取不允许上传的文件类型.<br>
         * 多个文件类型之间用','分割.
         * @return 获取不允许上传的文件类型.
         */
        public String getExclude() {
            return exclude;
        }

        /**
         * 设置不允许上传的文件类型.<br>
         * 多个文件类型之间用','分割.
         *
         * @param exclude　不允许上传的文件类型.
         */
        public void setExclude(String exclude) {
            this.exclude = exclude;
        }

        /**
         * 获取上传文件时在内存中最大的字节数.
         *
         * @return 上传文件时在内存中最大的字节数.
         */
        public int getInMemorySize() {
            return inMemorySize;
        }

        /**
         * 设置上传文件时在内存中最大的字节数.
         *
         * @param inMemorySize
         *            上传文件时在内存中最大的字节数.
         */
        public void setInMemorySize(int inMemorySize) {
            this.inMemorySize = inMemorySize;
        }

        /**
         * 获取上传文件的最大字节数.
         *
         * @return 上传文件的最大字节数.
         */
        public int getMaxSize() {
            return maxSize;
        }

        /**
         * 设置上传文件的最大字节数.
         *
         * @param maxSize
         *            上传文件的最大字节数.
         */
        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        /**
         * 获取上传文件的保存的临时目录.
         *
         * @return 上传文件的保存的临时目录.
         */
        public String getTempDir() {
            return tempDir;
        }

        /**
         * 设置上传文件的保存的临时目录.
         *
         * @param tempDir
         *            上传文件的保存的临时目录.
         */
        public void setTempDir(String tempDir) {
            this.tempDir = tempDir;
        }
    }

    private String encoding;

    private FileUploadConfig fileUploadConfig;

    /**
     * 获取HttpServletRequest请求的字符集设置
     *
     * @return HttpServletRequest请求的字符集设置
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * 设置HttpServletRequest请求的字符集设置
     *
     * @param encoding HttpServletRequest请求的字符集设置
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 获取上传文件的配置.
     * @return 上传文件的配置.
     */
    public FileUploadConfig getFileUploadConfig() {
        return fileUploadConfig;
    }

    /**
     * 设置上传文件的配置.
     * @param fileUploadConfig 上传文件的配置.
     */
    public void setFileUploadConfig(FileUploadConfig fileUploadConfig) {
        this.fileUploadConfig = fileUploadConfig;
    }
    
    private int suspend=10;

    /**
     * 获取请求的挂起的等待时间.<br>
     * @return 获取请求的挂起的等待时间.
     */
    public int getSuspend() {
        return suspend;
    }

    /**
     * 设置请求挂起的等待时间.<br>
     * @param suspend 请求挂起的等待时间.
     */
    public void setSuspend(int suspend) {
        this.suspend = suspend;
    }
    
    private String loginExcludeUrls;
    
    private String loginErrorPage;
    
    private boolean portal;

    private String loginIncludeUrls;

    public String getLoginErrorPage() {
        return loginErrorPage;
    }

    public void setLoginErrorPage(String loginErrorPage) {
        this.loginErrorPage = loginErrorPage;
    }

    public String getLoginExcludeUrls() {
        return loginExcludeUrls;
    }

    public void setLoginExcludeUrls(String loginExcludeUrls) {
        this.loginExcludeUrls = loginExcludeUrls;
    }

    public boolean isPortal() {
        return portal;
    }

    public void setPortal(boolean portal) {
        this.portal = portal;
    }

    public String getLoginIncludeUrls() {
        return loginIncludeUrls;
    }

    public void setLoginIncludeUrls(String loginIncludeUrls) {
        this.loginIncludeUrls = loginIncludeUrls;
    }

}
