
select  distinct(i_product_name)
 from item i1
 where i_manufact_id between 0 and 0+40 
   and (select count(*) as item_cnt
        from item
        where (i_manufact = i1.i_manufact and
        ((i_category = 'Women' and 
        (i_color = 'purple' or i_color = 'grey') and 
        (i_units = 'Carton' or i_units = 'Gross') and
        (i_size = 'petite' or i_size = 'small')
        ) or
        (i_category = 'Women' and
        (i_color = 'burnished' or i_color = 'bisque') and
        (i_units = 'Oz' or i_units = 'Cup') and
        (i_size = 'N/A' or i_size = 'extra large')
        ) or
        (i_category = 'Men' and
        (i_color = 'frosted' or i_color = 'dodger') and
        (i_units = 'Each' or i_units = 'Ton') and
        (i_size = 'economy' or i_size = 'medium')
        ) or
        (i_category = 'Men' and
        (i_color = 'chocolate' or i_color = 'cream') and
        (i_units = 'Unknown' or i_units = 'Lb') and
        (i_size = 'petite' or i_size = 'small')
        ))) or
       (i_manufact = i1.i_manufact and
        ((i_category = 'Women' and 
        (i_color = 'light' or i_color = 'salmon') and 
        (i_units = 'Pound' or i_units = 'Tsp') and
        (i_size = 'petite' or i_size = 'small')
        ) or
        (i_category = 'Women' and
        (i_color = 'midnight' or i_color = 'tan') and
        (i_units = 'Bunch' or i_units = 'Box') and
        (i_size = 'N/A' or i_size = 'extra large')
        ) or
        (i_category = 'Men' and
        (i_color = 'red' or i_color = 'cornflower') and
        (i_units = 'Dozen' or i_units = 'Dram') and
        (i_size = 'economy' or i_size = 'medium')
        ) or
        (i_category = 'Men' and
        (i_color = 'green' or i_color = 'metallic') and
        (i_units = 'N/A' or i_units = 'Ounce') and
        (i_size = 'petite' or i_size = 'small')
        )))) > 0
 order by i_product_name
 limit 100;


