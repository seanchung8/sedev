  select  c_customer_id,c_salutation,c_first_name,c_last_name,c_preferred_cust_flag
       ,c_birth_day,c_birth_month,c_birth_year,c_birth_country,c_login,c_email_address
       ,c_last_review_date,ctr_total_return
 from
 (select wr_returning_customer_sk as ctr_customer_sk
        ,ca_state as ctr_state, 
 	sum(wr_return_amt) as ctr_total_return
 from web_returns
     JOIN date_dim ON web_returns.wr_returned_date_sk = date_dim.d_date_sk 
     JOIN customer_address ON web_returns.wr_returning_addr_sk = customer_address.ca_address_sk 
 where
   d_year =2002
 group by wr_returning_customer_sk, ca_state) ctr1
     JOIN customer ON ctr1.ctr_customer_sk = customer.c_customer_sk
     JOIN customer_address ON customer_address.ca_address_sk = customer.c_current_addr_sk
 where ctr1.ctr_total_return > (select avg(ctr_total_return)*1.2
 			  from customer_total_return ctr2 
                  	  where ctr1.ctr_state = ctr2.ctr_state)
       and ca_state = 'IL'
 order by c_customer_id,c_salutation,c_first_name,c_last_name,c_preferred_cust_flag
                  ,c_birth_day,c_birth_month,c_birth_year,c_birth_country,c_login,c_email_address
                  ,c_last_review_date,ctr_total_return
limit 100;


