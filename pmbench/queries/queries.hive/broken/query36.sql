
select  
    sum(ss_net_profit)/sum(ss_ext_sales_price) as gross_margin
   ,i_category
   ,i_class
   ,grouping(i_category)+grouping(i_class) as lochierarchy
   ,rank() over (
 	partition by grouping(i_category)+grouping(i_class),
 	case when grouping(i_class) = 0 then i_category end 
 	order by sum(ss_net_profit)/sum(ss_ext_sales_price) asc) as rank_within_parent
 from
    store_sales
   JOIN date_dim d1 ON d1.d_date_sk = store_sales.ss_sold_date_sk
   JOIN item ON item.i_item_sk  = store_sales.ss_item_sk 
   JOIN store ON store.s_store_sk  = store_sales.ss_store_sk
 where
    d1.d_year = 2000 
 and s_state in ('TN','TN','TN','TN',
                 'TN','TN','TN','TN')
 group by i_category, i_class with rollup
 order by
   lochierarchy desc
  ,case when lochierarchy = 0 then i_category end
  ,rank_within_parent
  limit 100;


