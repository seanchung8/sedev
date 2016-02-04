select   
    sum(ws_net_paid) as total_sum
   ,i_category
   ,i_class
   ,grouping(i_category)+grouping(i_class) as lochierarchy
   ,rank() over (
 	partition by grouping(i_category)+grouping(i_class),
 	case when grouping(i_class) = 0 then i_category end 
 	order by sum(ws_net_paid) desc) as rank_within_parent
 from
    web_sales
   JOIN date_dim d1 ON d1.d_date_sk = web_sales.ws_sold_date_sk
   JOIN item ON item.i_item_sk  = web_sales.ws_item_sk
 where
    d1.d_month_seq between 1193 and 1193+11
 group by i_category, i_class with rollup
 order by
   lochierarchy desc,
   case when lochierarchy = 0 then i_category end,
   rank_within_parent
 limit 100;
