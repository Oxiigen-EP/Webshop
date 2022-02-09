package com.wsp.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wsp.webshop.validation.UniqueValue;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_generator")
    @SequenceGenerator(name = "product_seq_generator", sequenceName = "product_seq", allocationSize = 1)
    private Long product_id;

    @UniqueValue
    @NotNull(message = "Šifra je obavezna!")
    @Size(min = 10, max = 10, message
            = "Šifra mora biti duljine od 10 znakova")
    private String code;

    @NotNull(message = "Naziv proizvoda je obavezan!")
    private String product_name;

    @PositiveOrZero(message = "Cijena mora biti veća ili jednaka 0!")
    private BigDecimal price_hrk;

    private String description;

    private Boolean is_avaliable;

    @OneToOne(mappedBy = "product")
    private OrderItem orderitem;

    public Product() {
    }

    public Product(String code, String product_name, BigDecimal price_hrk, String description, Boolean is_avaliable) {
        this.code = code;
        this.product_name = product_name;
        this.price_hrk = price_hrk;
        this.description = description;
        this.is_avaliable = is_avaliable;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public BigDecimal getPrice_hrk() {
        return price_hrk;
    }

    public void setPrice_hrk(BigDecimal price_hrk) {
        this.price_hrk = price_hrk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIs_avaliable() {
        return is_avaliable;
    }

    public void setIs_avaliable(Boolean is_avaliable) {
        this.is_avaliable = is_avaliable;
    }

}
