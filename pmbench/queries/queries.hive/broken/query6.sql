select  a.ca_state state, count(*) cnt
 from customer_address a
     JOIN customer c ON c.current_addr_sk = a.ca_address_sk
     JOIN store_sales s ON c.c_customer_sk = s.ss_customer_sk
     JOIN date_dim d ON s.ss_sold_date_sk = d.d_date_sk
     JOIN item i ON s.ss_item_sk = i.i_item_sk
 where
 	d.d_month_seq = 
 	     (select distinct (d_month_seq)
 	      from date_dim
               where d_year = 2000
 	        and d_moy = 2 )
 	and i.i_current_price > 1.2 * 
             (select avg(j.i_current_price) 
 	     from item j 
 	     where j.i_category = i.i_category)
 group by a.ca_state
 having count(*) >= 10
 order by cnt 
 limit 100;


