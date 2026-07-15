ERD설계

1. User         = id, username, name, email, password, role, createdAt
2. Product      = id, name, user_id(FK), price, stock, description, imageUrl, createdAt, updatedAt
3. Cart         = id, user_id(FK), createdAt
4. CartItem     = id, cart_id(FK), product_id(FK), quantity, createdAt, updatedAt
5. Order        = id, user_id(FK), totalprice, status, createdAt
6. OrderItem    = id, order_id(FK), product_id(FK), quantity, price
