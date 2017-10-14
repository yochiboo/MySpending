UPDATE log
SET
    payment_date = %d,
    amount = %d,
    memo = '%s',
    class_code = %d,
    category_code = %d,
    wallet_code = %d,
    shop_code = %d,
    create_date = %d,
    update_date = %d
WHERE
    _id = %d