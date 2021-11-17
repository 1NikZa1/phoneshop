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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JdbcPhoneDao implements PhoneDao {
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
    private static final String FIND_ALL_PHONES = "SELECT * FROM phones OFFSET ? LIMIT ?";
    private static final String FIND_COLOR = "SELECT id ,code " +
            "FROM colors " +
            "INNER JOIN phone2color ON colors.id = phone2color.colorId " +
            "WHERE phone2color.phoneId = ?";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
        if (offset < 0 || limit < 0) throw new IllegalArgumentException();

        List<Phone> phones = jdbcTemplate.query(FIND_ALL_PHONES, new BeanPropertyRowMapper(Phone.class), offset, limit);
        phones.forEach(this::setColors);
        return phones;
    }

    private void setColors(final Phone phone) {
        Long phoneId = phone.getId();
        List<Color> colors = jdbcTemplate.query(FIND_COLOR, new BeanPropertyRowMapper(Color.class), phoneId);
        phone.setColors(new HashSet<>(colors));
    }
}
