package cn.iocoder.springboot.lab23.springmvc.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 产品 VO
 */
public class ProductVO {

    /**
     * 商品编号
     */
    @JacksonXmlProperty(localName = "id")
    private Integer id;
    /**
     * 商品标题
     */
    @JacksonXmlProperty(localName = "title")
    private String title;

    public Integer getId() {
        return id;
    }

    public ProductVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ProductVO setTitle(String title) {
        this.title = title;
        return this;
    }

}
