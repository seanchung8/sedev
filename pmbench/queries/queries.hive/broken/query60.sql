select i_item_id, sum(total_sales) as total_sales
from
(
 select
          i_item_id, sum(ss_ext_sales_price) as total_sales
 from
 	store_sales
 	JOIN date_dim
        JOIN customer_address
        JOIN item
 where
         i_item_id in (select
  i_item_id
from
 item
where i_category in ('Children'))
 and     ss_item_sk              = i_item_sk
 and     ss_sold_date_sk         = d_date_sk
 and     d_year                  = 1999
 and     d_moy                   = 9
 and     ss_addr_sk              = ca_address_sk
 and     ca_gmt_offset           = -6 
 group by i_item_id) ss
UNION ALL
(
 select
          i_item_id,sum(cs_ext_sales_price) total_sales
 from
 	catalog_sales,
 	date_dim,
         customer_address,
         item
 where
         i_item_id               in (select
  i_item_id
from
 item
where i_category in ('Children'))
 and     cs_item_sk              = i_item_sk
 and     cs_sold_date_sk         = d_date_sk
 and     d_year                  = 1999
 and     d_moy                   = 9
 and     cs_bill_addr_sk         = ca_address_sk
 and     ca_gmt_offset           = -6 
 group by i_item_id) cs
UNION ALL
(
 select
          i_item_id,sum(ws_ext_sales_price) total_sales
 from
 	web_sales,
 	date_dim,
         customer_address,
         item
 where
         i_item_id               in (select
  i_item_id
from
 item
where i_category in ('Children'))
 and     ws_item_sk              = i_item_sk
 and     ws_sold_date_sk         = d_date_sk
 and     d_year                  = 1999
 and     d_moy                   = 9
 and     ws_bill_addr_sk         = ca_address_sk
 and     ca_gmt_offset           = -6
 group by i_item_id) ws

 group by i_item_id
 order by i_item_id, total_sales
 limit 100;
