--

--c_repo = load '/tmp/C_REPO_201412018.csv' using PigStorage(',') as (
--c_repo = load '/tmp/C_REPO_201412018.csv' USING org.apache.pig.piggybank.storage.CSVExcelStorage() as (
c_repo = load '/tmp/C_REPO_201412018.csv' USING org.apache.pig.piggybank.storage.CSVExcelStorage() as (
trade_id:chararray,
product_type:chararray,
transaction_type:chararray,
booking_point:chararray,
business_unit_name:chararray,
gl_account:chararray,
pl_currency:chararray,
pl_gl_account:chararray,
egl_department:chararray,
egl_location:chararray,
portfolio_id:chararray,
trading:boolean,
cards_id:chararray,
counterparty_id:chararray,
counterpart_branch_id:chararray,
counterparty_branch_jurisdiction_id:chararray,
transit_number:chararray,
internal_deal:boolean,
facility_id:chararray,
net_agreement:chararray,
own_entity_id:chararray,
buy_sell:chararray,
currency:chararray,
dvp:chararray,
start_date:datetime,
maturity_date:datetime,
open_repo:boolean,
original_cash:double,
repo_rate:double,
security_id:chararray,
security_id_type:chararray,
quantity:int,
fixed_rate_repo:boolean,
day_count_basis:chararray,
fixing_frequency:chararray,
fixing_holiday:chararray,
floating_index:chararray,
sp_wr_risk:boolean,
settlement_currency:chararray,
repo_roll_flag:boolean,
repo_roll_group_id:chararray,
rep_roll_lst_cash_flow_date:datetime
);

register /tmp/myudfs.jar
c_repo_valid = foreach c_repo generate flatten(myudfs.validate(*)) as (code:int, trade_id:chararray, product_type:chararray, transaction_type:chararray, booking_point:chararray, business_unit_name:chararray, gl_account:chararray, pl_currency:chararray, pl_gl_account:chararray, egl_department:chararray, egl_location:chararray, portfolio_id:chararray, trading:boolean, cards_id:chararray, counterparty_id:chararray, counterpart_branch_id:chararray, counterparty_branch_jurisdiction_id:chararray, transit_number:chararray, internal_deal:boolean, facility_id:chararray, net_agreement:chararray, own_entity_id:chararray, buy_sell:chararray, currency:chararray, dvp:chararray, start_date:datetime, maturity_date:datetime, open_repo:boolean, original_cash:double, repo_rate:double, security_id:chararray, security_id_type:chararray, quantity:int, fixed_rate_repo:boolean, day_count_basis:chararray, fixing_frequency:chararray, fixing_holiday:chararray, floating_index:chararray, sp_wr_risk:boolean, settlement_currency:chararray, repo_roll_flag:boolean, repo_roll_group_id:chararray, rep_roll_lst_cash_flow_date:datetime);
dump c_repo_valid;

--'code' is a returned column from the java validate UDF
c_repo_valid_group = group c_repo_valid by code;
c_repo_valid_group_count = foreach c_repo_valid_group generate group, COUNT (c_repo_valid);
dump c_repo_valid_group_count;

split c_repo_valid into c_repo_good IF code==0, c_repo_bad IF code==1;


store c_repo_valid INTO 'ctr.c_repo' USING org.apache.hive.hcatalog.pig.HCatStorer();
--store c_repo_good INTO 'ctr.c_repo_good' USING org.apache.hive.hcatalog.pig.HCatStorer();
--store c_repo_bad INTO 'ctr.c_repo_bad' USING org.apache.hive.hcatalog.pig.HCatStorer();



--c_repo_valid = foreach c_repo generate flatten(myudfs.validate(trade_id, product_type, transaction_type, booking_point, business_unit_name, gl_account, pl_currency, pl_gl_account, egl_department, egl_location, portfolio_id, trading, cards_id, counterparty_id, counterpart_branch_id, counterparty_branch_jurisdiction_id, transit_number, internal_deal, facility_id, net_agreement, own_entity_id, buy_sell, currency, dvp, start_date, maturity_date, open_repo, original_cash, repo_rate, security_id, security_id_type, quantity, fixed_rate_repo, day_count_basis, fixing_frequency, fixing_holiday, floating_index, sp_wr_risk, settlement_currency, repo_roll_flag, repo_roll_group_id, rep_roll_lst_cash_flow_date))  as (code:int, trade_id:chararray, product_type:chararray, transaction_type:chararray, booking_point:chararray, business_unit_name:chararray, gl_account:chararray, pl_currency:chararray, pl_gl_account:chararray, egl_department:chararray, egl_location:chararray, portfolio_id:chararray, trading:boolean, cards_id:chararray, counterparty_id:chararray, counterpart_branch_id:chararray, counterparty_branch_jurisdiction_id:chararray, transit_number:chararray, internal_deal:boolean, facility_id:chararray, net_agreement:chararray, own_entity_id:chararray, buy_sell:chararray, currency:chararray, dvp:chararray, start_date:datetime, maturity_date:datetime, open_repo:boolean, original_cash:double, repo_rate:double, security_id:chararray, security_id_type:chararray, quantity:int, fixed_rate_repo:boolean, day_count_basis:chararray, fixing_frequency:chararray, fixing_holiday:chararray, floating_index:chararray, sp_wr_risk:boolean, settlement_currency:chararray, repo_roll_flag:boolean, repo_roll_group_id:chararray, rep_roll_lst_cash_flow_date:datetime);
