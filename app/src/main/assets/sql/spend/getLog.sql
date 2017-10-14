select
 log.payment_date as payment_date,
 log.amount as amount,
 log.memo as memo,
 log.class_code as class_code,
 log.category_code as category_code,
 log.wallet_code as wallet_code,
 log.shop_code as shop_code,
 log.create_date as create_date,
 log.update_date as update_date,
 category.name as category_name
from log
left outer join category on log.category_code = category.code
where log._id = %d
