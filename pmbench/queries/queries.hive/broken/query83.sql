  select  sr_items.item_id
       ,sr_item_qty
       ,sr_item_qty/(sr_item_qty+cr_item_qty+wr_item_qty)/3.0 * 100 sr_dev
       ,cr_item_qty
       ,cr_item_qty/(sr_item_qty+cr_item_qty+wr_item_qty)/3.0 * 100 cr_dev
       ,wr_item_qty
       ,wr_item_qty/(sr_item_qty+cr_item_qty+wr_item_qty)/3.0 * 100 wr_dev
       ,(sr_item_qty+cr_item_qty+wr_item_qty)/3.0 average
 from
 (select i_item_id item_id,
        sum(sr_return_quantity) sr_item_qty
 from store_returns
      JOIN item ON 
      JOIN date_dim ON
 where sr_item_sk = i_item_sk
 and   d_date    in 
	(select d_date
	from date_dim
	where d_week_seq in 
		(select d_week_seq
		from date_dim
	  where d_date in ('1998-01-02','1998-10-15','1998-11-10')))
 and   sr_returned_date_sk   = d_date_sk
 group by i_item_id) sr_items
 JOIN
 (select i_item_id item_id,
        sum(cr_return_quantity) cr_item_qty
 from catalog_returns,
      item,
      date_dim
 where cr_item_sk = i_item_sk
 and   d_date    in 
	(select d_date
	from date_dim
	where d_week_seq in 
		(select d_week_seq
		from date_dim
	  where d_date in ('1998-01-02','1998-10-15','1998-11-10')))
 and   cr_returned_date_sk   = d_date_sk
 group by i_item_id) cr_items ON sr_items.item_id=cr_items.item_id 
 JOIN
 (select i_item_id item_id,
        sum(wr_return_quantity) wr_item_qty
 from web_returns,
      item,
      date_dim
 where wr_item_sk = i_item_sk
 and   d_date    in 
	(select d_date
	from date_dim
	where d_week_seq in 
		(select d_week_seq
		from date_dim
		where d_date in ('1998-01-02','1998-10-15','1998-11-10')))
 and   wr_returned_date_sk   = d_date_sk
 group by i_item_id) wr_items ON sr_items.item_id=wr_items.item_id 
 order by sr_items.item_id ,sr_item_qty
 limit 100;

