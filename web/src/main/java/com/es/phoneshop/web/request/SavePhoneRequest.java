package com.es.phoneshop.web.request;

import com.es.core.model.phone.Color;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class SavePhoneRequest {
    private Long id;
    @NotBlank(message = "The value is required")
    @Size(max = 50, message = "too long")
    private String brand;
    @NotBlank(message = "The value is required")
    @Size(max = 50, message = "too long")
    private String model;// @Pattern(regexp="[0-9]+.[0-9]+",message = "float number")
    @Pattern(regexp = "^[-+]?([0-9]+(\\.[0-9]+)?|\\.[0-9]+)$", message = "float number")
    private String price;
    @Pattern(regexp = "^[-+]?([0-9]+(\\.[0-9]+)?|\\.[0-9]+)$", message = "float number")
    private String displaySizeInches;
    @Pattern(regexp = "[0-9]+", message = "int number")
    private String weightGr;
    @Pattern(regexp = "^[-+]?([0-9]+(\\.[0-9]+)?|\\.[0-9]+)$", message = "float number")
    private String lengthMm;
    @Pattern(regexp = "^[-+]?([0-9]+(\\.[0-9]+)?|\\.[0-9]+)$", message = "float number")
    private String widthMm;
    @Pattern(regexp = "^[-+]?([0-9]+(\\.[0-9]+)?|\\.[0-9]+)$", message = "float number")
    private String heightMm;
    private String announced;
    private String deviceType;
    private String os;
    private Set<Color> colors = new HashSet<>();
    private String displayResolution;
    @Pattern(regexp = "[0-9]+", message = "int number")
    private String pixelDensity;
    private String displayTechnology;
    @Pattern(regexp = "^[-+]?([0-9]+(\\.[0-9]+)?|\\.[0-9]+)$", message = "float number")
    private String backCameraMegapixels;
    @Pattern(regexp = "^[-+]?([0-9]+(\\.[0-9]+)?|\\.[0-9]+)$", message = "float number")
    private String frontCameraMegapixels;
    @Pattern(regexp = "^[-+]?([0-9]+(\\.[0-9]+)?|\\.[0-9]+)$", message = "float number")
    private String ramGb;
    @Pattern(regexp = "^[-+]?([0-9]+(\\.[0-9]+)?|\\.[0-9]+)$", message = "float number")
    private String internalStorageGb;
    @Pattern(regexp = "[0-9]+", message = "int number")
    private String batteryCapacityMah;
    private String bluetooth;
    private String positioning;
    private String imageUrl;
    private String description;
    @Pattern(regexp = "[0-9]+", message = "int number")
    private String stockRequested;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDisplaySizeInches() {
        return displaySizeInches;
    }

    public void setDisplaySizeInches(String displaySizeInches) {
        this.displaySizeInches = displaySizeInches;
    }

    public String getWeightGr() {
        return weightGr;
    }

    public void setWeightGr(String weightGr) {
        this.weightGr = weightGr;
    }

    public String getLengthMm() {
        return lengthMm;
    }

    public void setLengthMm(String lengthMm) {
        this.lengthMm = lengthMm;
    }

    public String getWidthMm() {
        return widthMm;
    }

    public void setWidthMm(String widthMm) {
        this.widthMm = widthMm;
    }

    public String getHeightMm() {
        return heightMm;
    }

    public void setHeightMm(String heightMm) {
        this.heightMm = heightMm;
    }

    public String getAnnounced() {
        return announced;
    }

    public void setAnnounced(String announced) {
        this.announced = announced;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Set<Color> getColors() {
        return colors;
    }

    public void setColors(Set<Color> colors) {
        this.colors = colors;
    }

    public String getDisplayResolution() {
        return displayResolution;
    }

    public void setDisplayResolution(String displayResolution) {
        this.displayResolution = displayResolution;
    }

    public String getPixelDensity() {
        return pixelDensity;
    }

    public void setPixelDensity(String pixelDensity) {
        this.pixelDensity = pixelDensity;
    }

    public String getDisplayTechnology() {
        return displayTechnology;
    }

    public void setDisplayTechnology(String displayTechnology) {
        this.displayTechnology = displayTechnology;
    }

    public String getBackCameraMegapixels() {
        return backCameraMegapixels;
    }

    public void setBackCameraMegapixels(String backCameraMegapixels) {
        this.backCameraMegapixels = backCameraMegapixels;
    }

    public String getFrontCameraMegapixels() {
        return frontCameraMegapixels;
    }

    public void setFrontCameraMegapixels(String frontCameraMegapixels) {
        this.frontCameraMegapixels = frontCameraMegapixels;
    }

    public String getRamGb() {
        return ramGb;
    }

    public void setRamGb(String ramGb) {
        this.ramGb = ramGb;
    }

    public String getInternalStorageGb() {
        return internalStorageGb;
    }

    public void setInternalStorageGb(String internalStorageGb) {
        this.internalStorageGb = internalStorageGb;
    }

    public String getBatteryCapacityMah() {
        return batteryCapacityMah;
    }

    public void setBatteryCapacityMah(String batteryCapacityMah) {
        this.batteryCapacityMah = batteryCapacityMah;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getPositioning() {
        return positioning;
    }

    public void setPositioning(String positioning) {
        this.positioning = positioning;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStockRequested() {
        return stockRequested;
    }

    public void setStockRequested(String stockRequested) {
        this.stockRequested = stockRequested;
    }
}
