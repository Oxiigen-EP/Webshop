package com.wsp.webshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "orderitem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq_generator")
    @SequenceGenerator(name = "order_item_seq_generator", sequenceName = "order_item_seq", allocationSize = 1)
    private Long orderitem_id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private WebshopOrder webshopOrder;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    public int quantity;

    public OrderItem() {
    }

    public OrderItem(WebshopOrder webshopOrder, Product product, int quantity) {
        this.webshopOrder = webshopOrder;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getOrderitem_id() {
        return orderitem_id;
    }

    public void setOrderitem_id(Long orderitem_id) {
        this.orderitem_id = orderitem_id;
    }

    public WebshopOrder getWebshopOrder() {
        return webshopOrder;
    }

    public void setWebshopOrder(WebshopOrder webshopOrder) {
        this.webshopOrder = webshopOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
