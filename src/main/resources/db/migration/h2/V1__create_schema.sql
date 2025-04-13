CREATE SEQUENCE IF NOT EXISTS dish_dish_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS user_storage_user_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS meal_meal_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS dish
(
    dish_id integer NOT NULL DEFAULT nextval('dish_dish_id_seq'),
    name character varying NOT NULL,
    calorie integer NOT NULL,
    proteins double precision NOT NULL,
    fats double precision NOT NULL,
    carbohydrates double precision NOT NULL,
    CONSTRAINT dish_pkey PRIMARY KEY (dish_id)
);

CREATE TABLE IF NOT EXISTS user_storage
(
    user_id integer NOT NULL DEFAULT nextval('user_storage_user_id_seq'),
    name character varying NOT NULL,
    email character varying NOT NULL,
    gender character varying NOT NULL,
    age integer NOT NULL,
    height integer NOT NULL,
    weight integer NOT NULL,
    goal character varying NOT NULL,
    calorie_norm integer NOT NULL,
    CONSTRAINT user_storage_pkey PRIMARY KEY (user_id)
);
    
CREATE TABLE IF NOT EXISTS meal
(
    meal_id bigint NOT NULL DEFAULT nextval('meal_meal_id_seq'),
    user_id integer NOT NULL,
    dish_id integer NOT NULL,
    date date NOT NULL,
    "time" time without time zone NOT NULL,
    CONSTRAINT meal_pkey PRIMARY KEY (meal_id),
    CONSTRAINT meal_dish_id_fkey FOREIGN KEY (dish_id)
        REFERENCES dish (dish_id) 
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT meal_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES user_storage (user_id) 
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
 