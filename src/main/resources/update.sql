--countries
alter table countries add column iso_alpha2 							varchar(2);
alter table countries add column iso_alpha3 							varchar(3);
alter table countries add column  iso_numeric                       integer(4);
alter table countries add column  currency_code                     varchar(3);
alter table countries add column  currency_name                     varchar(32);
alter table countries add column currrency_symbol                  varchar(3);
alter table countries drop column   flag;
alter table countries add column   flag_image                        varchar(3);

--poker chip images
alter table chip_images drop column associations;
alter table  chip_images add column thumbnail blob null;
alter table chip_images rename to poker_chip_images;

--locations
alter table locations rename column country to country_id;

--casinos
alter table casinos rename column opened to open_date;
alter table casinos rename column closed to close_date;
alter table casinos rename column locationID to location_id;
alter table casinos rename column theme to status;