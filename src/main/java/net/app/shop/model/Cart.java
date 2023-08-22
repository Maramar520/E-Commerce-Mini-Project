package net.app.shop.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.app.user.model.UserEntity;

@Entity
@Table(name = "shopping_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String sessionToken;
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id")
    private Set<CartItem> items;
    @Transient
    private Double totalPrice;
    @Transient
    private int nbItems;
    @OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
    private UserEntity user;
    @Temporal(TemporalType.DATE)
    private Date date = new Date();
    public Cart( ) {
       this.items = new HashSet<>();
    }
    public Cart(String sessionToken, Set<CartItem> items, Double totalPrice) {
        this.sessionToken = sessionToken;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public int getNbItems() {
        return this.items.size();
    }

    public void setNbItems(int nbItems) {
        this.nbItems = nbItems;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public UserEntity getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Set<CartItem> getItems() {
        return items;
    }

    public void setItems(Set<CartItem> items) {
        this.items = items;
    }

    public Double getTotalPrice() {
        return this.items.stream().map(CartItem::getSubtotal).reduce(0D, Double::sum);
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", sessionToken='" + sessionToken + '\'' +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                ", nbItems=" + nbItems +
                ", user=" + user +
                ", date=" + date +
                '}';
    }
}