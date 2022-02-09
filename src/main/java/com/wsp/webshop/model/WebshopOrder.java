package com.wsp.webshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wsp.webshop.enums.PostgreSQLEnumType;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "webshop_order")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)

public class WebshopOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "webshop_order_seq_generator")
    @SequenceGenerator(name = "webshop_order_seq_generator", sequenceName = "webshop_order_seq", allocationSize = 1)
    private Long webshoporder_id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "webshopOrder",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderItem> orderItem;

    //    @Enumerated(EnumType.STRING)
//    @Column(columnDefinition = "order_status")
//    @Type( type = "pgsql_enum" )
    public String order_status;

    public BigDecimal total_price_hrk;

    public BigDecimal total_price_eur;

    public WebshopOrder() {
    }

    public WebshopOrder(Customer customer, String order_status, BigDecimal total_price_hrk, BigDecimal total_price_eur) {
        this.customer = customer;
        this.order_status = order_status;
        this.total_price_hrk = total_price_hrk;
        this.total_price_eur = total_price_eur;
    }

    public Long getWebshoporder_id() {
        return webshoporder_id;
    }

    public void setWebshoporder_id(Long webshoporder_id) {
        this.webshoporder_id = webshoporder_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public BigDecimal getTotal_price_hrk() {
        return total_price_hrk;
    }

    public void setTotal_price_hrk(BigDecimal total_price_hrk) {
        this.total_price_hrk = total_price_hrk;
    }

    public BigDecimal getTotal_price_eur() {
        return total_price_eur;
    }

    public void setTotal_price_eur(BigDecimal total_price_eur) {
        this.total_price_eur = total_price_eur;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }
}
