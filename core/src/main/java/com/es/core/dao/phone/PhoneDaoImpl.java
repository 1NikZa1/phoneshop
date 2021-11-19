package com.es.core.dao.phone;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PhoneDaoImpl implements PhoneDao {
    private static final String PHONE_BY_ID = "SELECT phones.*, colors.id AS colorId, colors.code AS colorCode " +
            "FROM phones " +
            "LEFT JOIN phone2color ON phone2color.phoneId = phones.id " +
            "LEFT JOIN colors ON colors.id = phone2color.colorId " +
            "WHERE phones.id = ?";
    private static final String INSERT_PHONE = "INSERT INTO phones (id, brand, model, price, displaySizeInches, " +
            "weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, " +
            "displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, " +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "VALUES (:id, :brand, :model, :price, :displaySizeInches, :weightGr, :lengthMm, :widthMm, :heightMm, " +
            ":announced, :deviceType, :os, :displayResolution, :pixelDensity, :displayTechnology, " +
            ":backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, :batteryCapacityMah, " +
            ":talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";
    private static final String UPDATE_PHONE = "UPDATE phones SET brand = :brand, model = :model, price = :price, " +
            "displaySizeInches = :displaySizeInches, weightGr = :weightGr, lengthMm = :lengthMm, widthMm = :widthMm, " +
            "heightMm = :heightMm, announced = :announced, deviceType = :deviceType, os = :os, " +
            "displayResolution = :displayResolution, pixelDensity = :pixelDensity, " +
            "displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, " +
            "frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, " +
            "batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, " +
            "standByTimeHours = :standByTimeHours, bluetooth = :bluetooth, positioning = :positioning, " +
            "imageUrl = :imageUrl, description = :description where id = :id";
    private static final String INSERT_COLOR_FOR_PHONE_ID = "INSERT INTO phone2color (phoneId, colorId) VALUES (?,?)";
    private static final String DELETE_PHONE_COLORS = "DELETE FROM phone2color WHERE phoneId = ?";
    private static final String FIND_ALL_PHONES = "SELECT * FROM phones INNER JOIN stocks ON phones.id = stocks.phoneId WHERE stocks.stock > 0 AND phones.price IS NOT NULL OFFSET ? LIMIT ?";
    private static final String FIND_COLOR = "SELECT id ,code " +
            "FROM colors " +
            "INNER JOIN phone2color ON colors.id = phone2color.colorId " +
            "WHERE phone2color.phoneId = ?";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private BeanPropertyRowMapper<Phone> phoneBeanPropertyRowMapper;
    @Resource
    private BeanPropertyRowMapper<Color> colorBeanPropertyRowMapper;

    public Optional<Phone> get(final Long key) {
        Phone phone = jdbcTemplate.query(PHONE_BY_ID, new PhoneResultSetExtractor(), key);
        if (phone.getId() == null) {
            return Optional.empty();
        }
        return Optional.of(phone);
    }

    public void save(final Phone phone) {
        if (phone.getId() == null) {
            insert(phone);
        } else {
            update(phone);
        }
        if (phone.getColors().isEmpty()) {
            return;
        }
        insertColors(phone.getId(), phone.getColors());
    }

    private void insert(Phone phone) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(phone);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_PHONE, namedParams, keyHolder);
        phone.setId(keyHolder.getKey().longValue());
    }

    private void update(Phone phone) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(phone);
        namedParameterJdbcTemplate.update(UPDATE_PHONE, namedParams);
        deleteColors(phone.getId());
    }

    private void insertColors(Long id, Set<Color> colors) {
        List<Object[]> batchColors = colors.stream()
                .map(color -> new Object[]{id, color.getId()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(INSERT_COLOR_FOR_PHONE_ID, batchColors);
    }

    private void deleteColors(Long id) {
        jdbcTemplate.update(DELETE_PHONE_COLORS, id);
    }

    public List<Phone> findAll(int offset, int limit) {
        List<Phone> phones = jdbcTemplate.query(FIND_ALL_PHONES, phoneBeanPropertyRowMapper, offset, limit);
        phones.forEach(this::setColors);
        return phones;
    }

    private void setColors(final Phone phone) {
        Long phoneId = phone.getId();
        List<Color> colors = jdbcTemplate.query(FIND_COLOR, colorBeanPropertyRowMapper, phoneId);
        phone.setColors(new HashSet<>(colors));
    }

    @Override
    public List<Phone> findAll(String query, int offset, int limit) {


        List<Phone> phones = jdbcTemplate.query(createSqlForSearch(query), phoneBeanPropertyRowMapper, offset, limit);
        phones.forEach(this::setColors);
        return phones;
    }

    private String createSqlForSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return FIND_ALL_PHONES;
        }

        return String.format("SELECT * FROM phones INNER JOIN stocks ON phones.id = stocks.phoneId " +
                "WHERE stocks.stock > 0 AND phones.price IS NOT NULL " +
                "AND (phones.brand ILIKE '%%%s%%' OR phones.model ILIKE '%%%s%%') " +
                "OFFSET ? LIMIT ?", query, query);
    }

    @Override
    public int countPhones(String query) {
        return jdbcTemplate.queryForObject(createSqlForCountPhones(query), Integer.class);
    }

    private String createSqlForCountPhones(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "SELECT COUNT(*) FROM phones " +
                    "INNER JOIN stocks ON phones.id = stocks.phoneId " +
                    "WHERE stocks.stock > 0 AND phones.price IS NOT NULL ";
        }

        return String.format("SELECT COUNT(*) FROM phones " +
                "INNER JOIN stocks ON phones.id = stocks.phoneId " +
                "WHERE stocks.stock > 0 AND phones.price IS NOT NULL " +
                "AND (phones.brand ILIKE '%%%s%%' OR phones.model ILIKE '%%%s%%')", query, query);
    }
}
