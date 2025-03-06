CREATE TABLE users (
      id	        BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
      name	    VARCHAR(100)	NOT NULL,
      Field 	    VARCHAR(100)	NOT NULL,
      password	VARCHAR(100)	NOT NULL,
      created_at	DATETIME	NOT NULL,
      updated_at	DATETIME	NOT NULL,
      role	    VARCHAR(255)	NOT NULL,
      address	    VARCHAR(300)	NOT NULL
);

CREATE TABLE stores (
       id  BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
       owner_id	BIGINT	NOT NULL,
       store_name	VARCHAR(100)	NOT NULL,
       order_min	INT	NOT NULL	DEFAULT 0,
       about	    VARCHAR(300)	NOT NULL,
       opened_at	TIME	NOT NULL,
       closed_at	TIME	NOT NULL,
       created_at	DATETIME	NOT NULL,
       updated_at	DATETIME	NOT NULL,
       deleted_at  DATETIME	DEFAULT NULL,
       foreign key (owner_id) references users(id) ON DELETE CASCADE
);

CREATE TABLE menus (
	id          BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	store_id	BIGINT	NOT NULL,
	menu_name	VARCHAR(255)	NOT NULL,
	price	    BIGINT	NOT NULL,
	created_at	DATETIME	NOT NULL,
	updated_at	DATETIME	NOT NULL,
	deleted_at	DATETIME	DEFAULT NULL,
    foreign key (store_id) references stores(id) ON DELETE CASCADE
);

CREATE TABLE reviews (
	id	        BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id	    BIGINT	NOT NULL,
	content	    VARCHAR(600)	NOT NULL,
	rating	    BIGINT	NOT NULL,
	created_at	DATETIME	NOT NULL,
	order_id	BIGINT	NOT NULL,
	store_id	BIGINT	NOT NULL,
    foreign key (user_id) references users(id) ON DELETE CASCADE
);

CREATE TABLE orders (
	id	        BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	menu_id     BIGINT	NOT NULL,
	store_id	BIGINT	NOT NULL,
	user_id	    BIGINT	NOT NULL,
    cart_id     BIGINT	NOT NULL,
	order_state	VARCHAR(50)	NOT NULL,
	created_at	DATETIME	NOT NULL,
    foreign key (user_id) references users(id) ON DELETE CASCADE,
    foreign key (store_id) references stores(id),
    foreign key (menu_id) references menus(id),
    foreign key (cart_id) references carts(id)
);

CREATE TABLE favorites (
    id	        BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
    store_id    BIGINT	NOT NULL,
    user_id	    BIGINT	NOT NULL,
    foreign key (user_id) references users(id) ON DELETE CASCADE,
    foreign key (store_id) references stores(id) ON DELETE CASCADE
);

CREATE TABLE owner_reviews (
    id	        BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
    review_id	BIGINT	NOT NULL,
    store_id	BIGINT	NOT NULL,
    content	    VARCHAR(600)	NOT NULL,
    created_at	DATETIME	NOT NULL,
    update_at	DATETIME	NOT NULL,
    foreign key (review_id) references reviews(id) ON DELETE CASCADE,
    foreign key (store_id) references stores(id) ON DELETE CASCADE
);

CREATE TABLE carts (
       id	        BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
       user_id	BIGINT	NOT NULL,
       store_id	BIGINT	NOT NULL,
       created_at	DATETIME	NOT NULL,
       update_at	DATETIME	NOT NULL,
       foreign key (user_id) references users(id) ON DELETE CASCADE,
       foreign key (store_id) references stores(id) ON DELETE CASCADE
);

