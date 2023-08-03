package com.crking7.datn.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int type;

    @Column
    private int status;

    @Column
    private Date createDate;

    @Column
    private Date modifiedDate;

    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
