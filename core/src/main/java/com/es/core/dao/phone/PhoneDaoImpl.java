package com.es.core.dao.phone;

import com.es.core.model.phone.Brand;
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
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PhoneDaoImpl implements PhoneDao {
    private static final String PHONE_BY_ID = "SELECT phones.*, brands.name AS brandName, operational_systems.name AS osName, device_types.name AS deviceTypeName, colors.id AS colorId, colors.code AS colorCode " +
            "FROM phones " +
            "LEFT JOIN phone2color ON phone2color.phoneId = phones.id " +
            "LEFT JOIN colors ON colors.id = phone2color.colorId " +
            "LEFT JOIN brands ON brands.id = phones.brand " +
            "LEFT JOIN operational_systems ON operational_systems.id = phones.os " +
            "LEFT JOIN device_types ON device_types.id = phones.deviceType " +
            "WHERE phones.id = ?";
    private static final String PHONE_BY_MODEL = "SELECT phones.*, colors.id AS colorId, colors.code AS colorCode " +
            "FROM phones " +
            "LEFT JOIN phone2color ON phone2color.phoneId = phones.id " +
            "LEFT JOIN colors ON colors.id = phone2color.colorId " +
            "WHERE phones.model = ?";
    private static final String INSERT_PHONE = "INSERT INTO phones (id, brand, model, price, displaySizeInches, " +
            "weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, " +
            "displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, " +
            "batteryCapacityMah, bluetooth, positioning, imageUrl, description, stockRequested) " +
            "VALUES (:id, :brand, :model, :price, :displaySizeInches, :weightGr, :lengthMm, :widthMm, :heightMm, " +
            ":announced, :deviceType, :os, :displayResolution, :pixelDensity, :displayTechnology, " +
            ":backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, :batteryCapacityMah, " +
            ":bluetooth, :positioning, :imageUrl, :description, :stockRequested)";
    private static final String UPDATE_PHONE = "UPDATE phones SET brand = :brand, model = :model, price = :price, " +
            "displaySizeInches = :displaySizeInches, weightGr = :weightGr, lengthMm = :lengthMm, widthMm = :widthMm, " +
            "heightMm = :heightMm, announced = :announced, deviceType = :deviceType, os = :os, " +
            "displayResolution = :displayResolution, pixelDensity = :pixelDensity, " +
            "displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, " +
            "frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, " +
            "batteryCapacityMah = :batteryCapacityMah, " +
            "bluetooth = :bluetooth, positioning = :positioning, " +
            "imageUrl = :imageUrl, description = :description, stockRequested = :stockRequested, stock = :stock WHERE id = :id";
    private static final String INSERT_BRAND = "INSERT INTO brands (id, name) " +
            "VALUES (:id, :name)";
    private static final String INSERT_COLOR = "INSERT INTO colors (id, code) " +
            "VALUES (:id, :code)";
    private static final String UPDATE_BRAND = "UPDATE brands SET name = :name WHERE id = :id";
    private static final String UPDATE_COLOR = "UPDATE colors SET code = :code WHERE id = :id";
    private static final String INSERT_COLOR_FOR_PHONE_ID = "INSERT INTO phone2color (phoneId, colorId) VALUES (?,?)";
    private static final String DELETE_PHONE_COLORS = "DELETE FROM phone2color WHERE phoneId = ?";
    private static final String DELETE_BRAND = "DELETE FROM brands WHERE id = ?";
    private static final String DELETE_COLOR = "DELETE FROM colors WHERE id = ?";
    private static final String DELETE_PHONE = "DELETE FROM phones WHERE id = ?";
    private static final String FIND_ALL_PHONES = "SELECT p.*, brands.name AS brandName,operational_systems.name AS osName, device_types.name AS deviceTypeName, colors.id AS colorId, colors.code AS colorCode " +
            "FROM ( SELECT * FROM phones " +
            "WHERE price IS NOT NULL OFFSET ? LIMIT ? ) AS p " +
            "LEFT JOIN phone2color ON phone2color.phoneId = p.id " +
            "LEFT JOIN brands ON brands.id = p.brand " +
            "LEFT JOIN operational_systems ON operational_systems.id = p.os " +
            "LEFT JOIN device_types ON device_types.id = p.deviceType " +
            "LEFT JOIN colors ON colors.id = phone2color.colorId";
    private static final String FIND_ALL_ORDERED_PHONES_BY_QUERY = "SELECT p.*, brands.name AS brandName, operational_systems.name AS osName, device_types.name AS deviceTypeName, colors.id AS colorId, colors.code AS colorCode " +
            "FROM ( SELECT * FROM phones " +
            "WHERE price IS NOT NULL " +
            "AND (brand ILIKE '%%%s%%' OR model ILIKE '%%%s%%') " +
            "ORDER BY %s %s " +
            "OFFSET ? LIMIT ? ) AS p " +
            "LEFT JOIN brands ON brands.id = p.brand " +
            "LEFT JOIN operational_systems ON operational_systems.id = p.os " +
            "LEFT JOIN device_types ON device_types.id = p.deviceType " +
            "LEFT JOIN phone2color ON phone2color.phoneId = p.id " +
            "LEFT JOIN colors ON colors.id = phone2color.colorId ";
    private static final String FIND_ALL_ORDERED_PHONES = "SELECT p.*, brands.name AS brandName, operational_systems.name AS osName, device_types.name AS deviceTypeName, colors.id AS colorId, colors.code AS colorCode " +
            "FROM ( SELECT * FROM phones " +
            "WHERE price IS NOT NULL %s%s " +
            "ORDER BY %s %s " +
            "OFFSET ? LIMIT ? ) AS p " +
            "LEFT JOIN brands ON brands.id = p.brand " +
            "LEFT JOIN operational_systems ON operational_systems.id = p.os " +
            "LEFT JOIN device_types ON device_types.id = p.deviceType " +
            "LEFT JOIN phone2color ON phone2color.phoneId = p.id " +
            "LEFT JOIN colors ON colors.id = phone2color.colorId ";
    private static final String ALL_COLORS = "SELECT id, code " +
            "FROM colors ";
    private static final String ALL_BRANDS = "SELECT id, name " +
            "FROM brands ";
    private static final String ALL_TYPES = "SELECT id, name " +
            "FROM device_types ";
    private static final String ALL_OS = "SELECT id, name " +
            "FROM operational_systems ";
    private static final String GET_COLOR_BY_ID = "SELECT * " +
            "FROM colors WHERE id = ?";
    private static final String GET_BRAND_BY_ID = "SELECT * " +
            "FROM brands WHERE id = ?";
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private BeanPropertyRowMapper<Phone> phoneBeanPropertyRowMapper;

    public Optional<Phone> get(final Long key) {
        List<Phone> phones = jdbcTemplate.query(PHONE_BY_ID, new PhoneResultSetExtractor(), key);
        if (phones.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(phones.get(0));
    }

    @Override
    public Optional<Brand> getBrand(Long key) {
        return Optional.of((Brand) jdbcTemplate.queryForObject(GET_BRAND_BY_ID, new Object[]{key}, new BeanPropertyRowMapper(Brand.class)));
    }

    @Override
    public Optional<Color> getColor(final Long key) {
        return Optional.of((Color) jdbcTemplate.queryForObject(GET_COLOR_BY_ID, new Object[]{key}, new BeanPropertyRowMapper(Color.class)));
    }

    @Override
    public Optional<Phone> getByModel(String model) {
        List<Phone> phones = jdbcTemplate.query(PHONE_BY_MODEL, new PhoneResultSetExtractor(), model);
        if (phones.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(phones.get(0));
    }

    @Override
    public Set<Color> getAllColors() {
        Set<Color> results = new HashSet<>();
        jdbcTemplate.query(ALL_COLORS, (ResultSet rs) -> {
            while (rs.next()) {
                results.add(new Color(Long.parseLong(rs.getString("id")), rs.getString("code")));
            }
            return results;
        });
        return results;
    }

    @Override
    public Map<String, String> getAllBrands() {
        HashMap<String, String> results = new HashMap<>();
        jdbcTemplate.query(ALL_BRANDS, (ResultSet rs) -> {
            while (rs.next()) {
                results.put(rs.getString("id"), rs.getString("name"));
            }
            return results;
        });
        return results;
    }

    @Override
    public Map<String, String> getAllDeviceTypes() {
        HashMap<String, String> results = new HashMap<>();
        jdbcTemplate.query(ALL_TYPES, (ResultSet rs) -> {
            while (rs.next()) {
                results.put(rs.getString("id"), rs.getString("name"));
            }
            return results;
        });
        return results;
    }

    @Override
    public Map<String, String> getAllOpSystems() {
        HashMap<String, String> results = new HashMap<>();
        jdbcTemplate.query(ALL_OS, (ResultSet rs) -> {
            while (rs.next()) {
                results.put(rs.getString("id"), rs.getString("name"));
            }
            return results;
        });
        return results;
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

    @Override
    public void delete(final Phone phone) {
        deleteColors(phone.getId());
        jdbcTemplate.update(DELETE_PHONE, phone.getId());
    }

    @Override
    public void saveBrand(final Brand brand) {
        if (brand.getId() == null) {
            insertBrand(brand);
        } else {
            updateBrand(brand);
        }
    }

    @Override
    public void saveColor(final Color color) {
        if (color.getId() == null) {
            insertColor(color);
        } else {
            updateColor(color);
        }
    }

    private void insertColor(Color color) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(color);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_COLOR, namedParams, keyHolder);
        color.setId(keyHolder.getKey().longValue());
    }

    private void updateColor(Color color) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(color);
        namedParameterJdbcTemplate.update(UPDATE_COLOR, namedParams);
    }

    @Override
    public void deleteBrand(final Brand brand) {
        jdbcTemplate.update(DELETE_BRAND, brand.getId());
    }

    @Override
    public void deleteColor(final Color color) {
        deleteColors(color.getId());
        jdbcTemplate.update(DELETE_COLOR, color.getId());
    }

    private void insertBrand(Brand brand) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(brand);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_BRAND, namedParams, keyHolder);
        brand.setId(keyHolder.getKey().longValue());
    }

    private void updateBrand(Brand brand) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(brand);
        namedParameterJdbcTemplate.update(UPDATE_BRAND, namedParams);
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

    @Override
    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(FIND_ALL_PHONES, phoneBeanPropertyRowMapper, offset, limit);
    }

    @Override
    public List<Phone> findAll(String query, String sortField, String sortOrder, int offset, int limit) {
        return jdbcTemplate.query(createSqlForSearch(query, sortField, sortOrder), new PhoneResultSetExtractor(), offset, limit);
    }

    private String createSqlForSearch(String query, String sortField, String sortOrder) {
        if (sortOrder == null || !sortOrder.equals("desc")) {
            sortOrder = "asc";
        }

        if (query == null || query.trim().isEmpty()) {
            if (sortField == null || sortField.isEmpty()) {
                return FIND_ALL_PHONES;
            } else {
                return createSqlForSorting(FIND_ALL_ORDERED_PHONES, "", sortField, sortOrder);
            }
        }

        return createSqlForSorting(FIND_ALL_ORDERED_PHONES_BY_QUERY, query, sortField, sortOrder);
    }

    private String createSqlForSorting(String sql, String query, String sortField, String sortOrder) {
        if (sortField == null) {
            sortField = "id";
        }
        switch (sortField) {
            case "displaySize":
                return String.format(sql, query, query, "displaySizeInches", sortOrder);
            case "price":
                return String.format(sql, query, query, sortField, sortOrder);
            case "model":
                return String.format(sql, query, query, "LOWER(model)", sortOrder);
            default:
                return String.format(sql, query, query, "id", sortOrder);
        }
    }

    @Override
    public int countPhones(String query) {
        return jdbcTemplate.queryForObject(createSqlForCountPhones(query), Integer.class);
    }

    private String createSqlForCountPhones(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "SELECT COUNT(*) FROM phones " +
                    "WHERE price IS NOT NULL ";
        }

        return String.format("SELECT COUNT(*) FROM phones " +
                "WHERE price IS NOT NULL " +
                "AND (brand ILIKE '%%%s%%' OR model ILIKE '%%%s%%')", query, query);
    }
}
