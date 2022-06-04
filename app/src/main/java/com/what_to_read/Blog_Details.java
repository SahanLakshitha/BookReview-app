package com.what_to_read;

import java.sql.Blob;
import java.util.Arrays;

public class Blog_Details implements java.io.Serializable {
    private String post_id;
    private String part;
    private String brand;
    private String description;
    private String email;
    private String name;
    byte[] image;

    public Blog_Details(String post_id, String part, String brand, String description, String email, String name, byte[] image) {
        this.post_id = post_id;
        this.part = part;
        this.brand = brand;
        this.description = description;
        this.email = email;
        this.name = name;
        this.image = image;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Blog_Details{" +
                "post_id='" + post_id + '\'' +
                ", part='" + part + '\'' +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
