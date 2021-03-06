CREATE TABLE IF NOT EXISTS trade.realtime_snapshot (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  created_time DATETIME NULL DEFAULT NULL,
  updated_time DATETIME NULL DEFAULT NULL,
  trade_volume_max BIGINT(20) NULL DEFAULT NULL,
  trade_volume_min BIGINT(20) NULL DEFAULT NULL,
  trade_volume_mean DECIMAL(19,2) NULL DEFAULT NULL,
  amount_sell_max DECIMAL(19,2) NULL DEFAULT NULL,
  amount_sell_min DECIMAL(19,2) NULL DEFAULT NULL,
  amount_sell_mean DECIMAL(19,2) NULL DEFAULT NULL,
  amount_buy_max DECIMAL(19,2) NULL DEFAULT NULL,
  amount_buy_min DECIMAL(19,2) NULL DEFAULT NULL,
  amount_buy_mean DECIMAL(19,2) NULL DEFAULT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB DEFAULT CHARSET=utf8;