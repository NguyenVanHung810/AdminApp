package com.example.adminapp;

import java.util.List;

public class CategoryModel {
    private String categoryId;
    private String categoryImage;
    private String categoryName;
    private Long index;
    private List<BrandModel> brandModelList;

    public CategoryModel(String categoryId, String categoryImage, String categoryName, Long index, List<BrandModel> brandModelList) {
        this.categoryId = categoryId;
        this.categoryImage = categoryImage;
        this.categoryName = categoryName;
        this.index = index;
        this.brandModelList = brandModelList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public List<BrandModel> getBrandModelList() {
        return brandModelList;
    }

    public void setBrandModelList(List<BrandModel> brandModelList) {
        this.brandModelList = brandModelList;
    }
}
