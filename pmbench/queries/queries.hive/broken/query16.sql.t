select  
   count(distinct cs_order_number) as ordercount
  ,sum(cs_ext_ship_cost) as totalshippingcost
  ,sum(cs_net_profit) as totalnetprofit
from
   catalog_sales cs1
  ,date_dim
  ,customer_address
  ,call_center
where
    d_date between '1999-2-01' and 
           (cast('1999-2-28' as date))
and cs1.cs_ship_date_sk = d_date_sk
and cs1.cs_ship_addr_sk = ca_address_sk
and ca_state = 'IL'
and cs1.cs_call_center_sk = cc_call_center_sk
and cc_county in ('Williamson County','Williamson County','Williamson County','Williamson County',
                  'Williamson County'
)
and exists (select *
            from catalog_sales cs2
            where cs1.cs_order_number = cs2.cs_order_number
              and cs1.cs_warehouse_sk <> cs2.cs_warehouse_sk)
order by count(distinct cs_order_number)
limit 100;



and not exists(select *
               from catalog_returns cr1
               where cs1.cs_order_number = cr1.cr_order_number)

