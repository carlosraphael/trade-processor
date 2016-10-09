CREATE TABLE IF NOT EXISTS trade.trade (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  created_time DATETIME NULL DEFAULT NULL,
  updated_time DATETIME NULL DEFAULT NULL,
  amount_buy DECIMAL(19,2) NULL DEFAULT NULL,
  amount_sell DECIMAL(19,2) NULL DEFAULT NULL,
  currency_from VARCHAR(255) NOT NULL,
  currency_to VARCHAR(255) NOT NULL,
  originating_country VARCHAR(255) NOT NULL,
  rate DECIMAL(19,2) NOT NULL,
  time_placed DATETIME NOT NULL,
  user_id BIGINT(20) NOT NULL,
  PRIMARY KEY (id),
  INDEX IDX_USER_TRADE (user_id),
  INDEX IDX_TIME_PLACED (time_placed),
  INDEX IDX_ORIG_COUNTRY (originating_country),
  INDEX IDX_CURRENCY (currency_from,currency_to),
  INDEX IDX_AMOUNT (amount_buy,amount_sell, rate))
ENGINE = InnoDB DEFAULT CHARSET=utf8;