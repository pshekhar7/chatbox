CREATE TABLE IF NOT EXISTS app_config
(
    name  VARCHAR(64),
    value VARCHAR(255),
    PRIMARY KEY (name)
);

INSERT IGNORE INTO app_config(name, value)
values ('pii-field-encryption', '{"password":"y371bhwr8evtcijp4560zxkmq2ladngf","iv":"8079896567784743"}');