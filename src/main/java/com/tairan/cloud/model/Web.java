package com.tairan.cloud.model;

public class Web {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column web.web_code
     *
     * @mbggenerated
     */
    private String webCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column web.web_name
     *
     * @mbggenerated
     */
    private String webName;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column web.web_code
     *
     * @return the value of web.web_code
     *
     * @mbggenerated
     */
    public String getWebCode() {
        return webCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column web.web_code
     *
     * @param webCode the value for web.web_code
     *
     * @mbggenerated
     */
    public void setWebCode(String webCode) {
        this.webCode = webCode == null ? null : webCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column web.web_name
     *
     * @return the value of web.web_name
     *
     * @mbggenerated
     */
    public String getWebName() {
        return webName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column web.web_name
     *
     * @param webName the value for web.web_name
     *
     * @mbggenerated
     */
    public void setWebName(String webName) {
        this.webName = webName == null ? null : webName.trim();
    }
}