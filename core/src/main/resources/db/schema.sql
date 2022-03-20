drop table if exists phone2color;
drop table if exists colors;
drop table if exists stocks;
drop table if exists phones;
drop table if exists orders;
drop table if exists order_items;
drop table if exists comments;
drop table if exists brands;
drop table if exists colors;
drop table if exists device_types;
drop table if exists operational_systems;


create table colors
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50),
    UNIQUE (code)
);

create table brands
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    UNIQUE (name)
);

create table device_types
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    UNIQUE (name)
);

create table operational_systems
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    UNIQUE (name)
);

create table phones
(
    id                    BIGINT AUTO_INCREMENT primary key,
    brand                 BIGINT       NOT NULL references brands (id),
    model                 VARCHAR(254) NOT NULL,
    price                 FLOAT,
    displaySizeInches     FLOAT,
    weightGr              SMALLINT,
    lengthMm              FLOAT,
    widthMm               FLOAT,
    heightMm              FLOAT,
    announced             DATETIME,
    deviceType            BIGINT references device_types (id),
    os                    BIGINT references operational_systems (id),
    displayResolution     VARCHAR(50),
    pixelDensity          SMALLINT,
    displayTechnology     VARCHAR(50),
    backCameraMegapixels  FLOAT,
    frontCameraMegapixels FLOAT,
    ramGb                 FLOAT,
    internalStorageGb     FLOAT,
    batteryCapacityMah    SMALLINT,
    bluetooth             VARCHAR(50),
    positioning           VARCHAR(100),
    imageUrl              VARCHAR(254),
    description           VARCHAR(4096),
    stock                 SMALLINT,
    reserved              SMALLINT,
    stockRequested        SMALLINT,
    CONSTRAINT UC_phone UNIQUE (brand, model)
);

create table phone2color
(
    phoneId BIGINT,
    colorId BIGINT,
    CONSTRAINT FK_phone2color_phoneId FOREIGN KEY (phoneId) REFERENCES phones (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_phone2color_colorId FOREIGN KEY (colorId) REFERENCES colors (id) ON DELETE CASCADE ON UPDATE CASCADE
);

create table orders
(
    id              BIGINT auto_increment PRIMARY KEY,
    secureId        VARCHAR(256),
    subtotal        DOUBLE      NOT NULL,
    deliveryPrice   DOUBLE      NOT NULL,
    totalPrice      DOUBLE      NOT NULL,
    firstName       VARCHAR(50) NOT NULL,
    lastName        VARCHAR(50) NOT NULL,
    deliveryAddress VARCHAR(50) NOT NULL,
    contactPhoneNo  VARCHAR(50) NOT NULL,
    additionalInfo  VARCHAR(512),
    date            TIMESTAMP   NOT NULL,
    status          VARCHAR(20) NOT NULL,
    review          VARCHAR(512),
    reviewDate      TIMESTAMP
);

create table order_items
(
    id       BIGINT auto_increment PRIMARY KEY,
    phoneId  BIGINT  NOT NULL references phones (id),
    orderId  BIGINT  NOT NULL references orders (id),
    quantity INTEGER NOT NULL
);