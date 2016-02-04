select  i_item_id ,sum(total_sales) total_sales
 from  (select * from
( select i_item_id,sum(ss_ext_sales_price) as total_sales
 from
 	store_sales,
 	JOIN date_dim ON store_sales.ss_sold_date_sk = date_dim.d_date_sk
        JOIN customer_address ON store_sales.ss_addr_sk = customer_address.ca_address_sk
        JOIN item store_sales.ss_item_sk = item.i_item_sk
 where i_item_id in (select
     i_item_id
from item
where i_color in ('purple','burlywood','indian'))
 and     d_year                  = 2001
 and     d_moy                   = 1
 and     ca_gmt_offset           = -6 
 group by i_item_id) ss
) tmp1
 group by i_item_id
 order by total_sales
 limit 100;







        union all
        select * from
( select i_item_id,sum(cs_ext_sales_price) total_sales
 from
 	catalog_sales,
 	date_dim,
         customer_address,
         item
 where
         i_item_id               in (select
  i_item_id
from item
where i_color in ('purple','burlywood','indian'))
 and     cs_item_sk              = i_item_sk
 and     cs_sold_date_sk         = d_date_sk
 and     d_year                  = 2001
 and     d_moy                   = 1
 and     cs_bill_addr_sk         = ca_address_sk
 and     ca_gmt_offset           = -6 
 group by i_item_id) cs
        union all
        select * from
( select i_item_id,sum(ws_ext_sales_price) total_sales
 from
 	web_sales,
 	date_dim,
         customer_address,
         item
 where
         i_item_id               in (select
  i_item_id
from item
where i_color in ('purple','burlywood','indian'))
 and     ws_item_sk              = i_item_sk
 and     ws_sold_date_sk         = d_date_sk
 and     d_year                  = 2001
 and     d_moy                   = 1
 and     ws_bill_addr_sk         = ca_address_sk
 and     ca_gmt_offset           = -6
 group by i_item_id) ws
) tmp1
 group by i_item_id
 order by total_sales
 limit 100;



