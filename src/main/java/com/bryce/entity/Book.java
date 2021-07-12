package com.bryce.entity;

public class Book {
    private Integer id;       // int(11) DEFAULT NULL,
    private String bookname;  // varchar(500) DEFAULT NULL,
    private Float price;      // float DEFAULT NULL,
    private String pic;       // varchar(200) DEFAULT NULL,
    private String bookdesc;  // varchar(2000) DEFAULT NULL


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBookdesc() {
        return bookdesc;
    }

    public void setBookdesc(String bookdesc) {
        this.bookdesc = bookdesc;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", bookname=" + bookname + 
            ", price=" + price + ", pic=" + pic + 
            ", bookdesc=" + bookdesc + "]";
    }
}