insert into log (
    payment_date,
    amount, memo,
    class_code,
    category_code,
    wallet_code,
    shop_code,
    create_date,
    update_date
) values (
    %d,
    %d,
    '%s',
    %d,
    %d,
    %d,
    %d,
    %d,
    %d
)