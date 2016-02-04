select  i_item_desc 
      ,i_category 
      ,i_class 
      ,i_current_price
      ,i_item_id
      ,sum(ws_ext_sales_price) as itemrevenue 
      ,sum(ws_ext_sales_price)*100/sum(sum(ws_ext_sales_price)) over
          (partition by i_class) as revenueratio
from	
	web_sales
    	JOIN item ON web_sales.ws_item_sk = item.i_item_sk 
    	JOIN date_dim ON web_sales.ws_sold_date_sk = date_dim.d_date_sk
where 
  	i_category in ('Jewelry', 'Sports', 'Books')
	and d_date between '2001-01-12' and '2001-02-11'
group by 
	i_item_id
        ,i_item_desc 
        ,i_category
        ,i_class
        ,i_current_price
        ,ws_ext_sales_price
order by 
	i_category
        ,i_class
        ,i_item_id
        ,i_item_desc
        ,revenueratio
limit 100;


