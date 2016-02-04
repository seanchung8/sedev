INSERT OVERWRITE TABLE customer_total_return
SELECT sr.sr_customer_sk as ctr_customer_sk,
               sr.sr_store_sk as ctr_store_sk,
               sum(sr.sr_return_tax) as ctr_total_return
FROM store_returns sr
WHERE sr.sr_returned_year = '2000'
GROUP BY sr.sr_customer_sk,
                     sr.sr_store_sk;

SELECT *
FROM (SELECT c_customer_id
             FROM customer_total_return ctr1
             JOIN (SELECT ctr2.ctr_store_sk as ctr_store_sk,
                                       avg(ctr_total_return)*1.2 as avg_ctr_total_return
                        FROM customer_total_return ctr2
                        GROUP BY ctr2.ctr_store_sk) tmp1
             ON (ctr1.ctr_store_sk = tmp1.ctr_store_sk)
             JOIN store s ON (ctr1.ctr_store_sk = s.s_store_sk)
             JOIN customer c ON (ctr1.ctr_customer_sk = c.c_customer_sk)
             WHERE s_state = 'TN' and
                            ctr1.ctr_total_return > tmp1.avg_ctr_total_return
             ORDER BY c_customer_id LIMIT 100) tmp2;
