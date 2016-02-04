select  i_item_id
       ,i_item_desc
       ,i_current_price
 from item
  JOIN inventory ON item.i_item_sk = inventory.inv_item_sk
  JOIN date_dim ON inventory.inv_date_sk = date_dim.d_date_sk
  JOIN catalog_sales ON item.i_item_sk = catalog_sales.cs_item_sk
 where i_current_price between 22 and 22 + 30
 and d_date between cast('1998-01-02' as date) and (cast('1998-03-03' as date))
 and i_manufact_id in (115,103,84,527)
 and inv_quantity_on_hand between 100 and 500
 group by i_item_id,i_item_desc,i_current_price
 order by i_item_id
 limit 100;

