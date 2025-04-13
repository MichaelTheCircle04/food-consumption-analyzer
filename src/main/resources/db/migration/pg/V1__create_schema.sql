CREATE SEQUENCE IF NOT EXISTS public.dish_dish_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.dish_dish_id_seq
    OWNER TO postgres;

CREATE SEQUENCE IF NOT EXISTS public.user_storage_user_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.user_storage_user_id_seq
    OWNER TO postgres;

CREATE SEQUENCE IF NOT EXISTS public.meal_meal_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.meal_meal_id_seq
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS public.dish
(
    dish_id integer NOT NULL DEFAULT nextval('dish_dish_id_seq'::regclass),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    calorie double precision NOT NULL,
    proteins double precision NOT NULL,
    fats double precision NOT NULL,
    carbohydrates integer NOT NULL,
    CONSTRAINT dish_pkey PRIMARY KEY (dish_id)
);

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.dish
    OWNER to postgres;
    
ALTER SEQUENCE public.dish_dish_id_seq
    OWNED BY public.dish.dish_id;

CREATE TABLE IF NOT EXISTS public.user_storage
(
    user_id integer NOT NULL DEFAULT nextval('user_storage_user_id_seq'::regclass),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    email character varying COLLATE pg_catalog."default" NOT NULL,
    gender character varying COLLATE pg_catalog."default" NOT NULL,
    age integer NOT NULL,
    height integer NOT NULL,
    weight integer NOT NULL,
    goal character varying COLLATE pg_catalog."default" NOT NULL,
    calorie_norm integer NOT NULL,
    CONSTRAINT user_storage_pkey PRIMARY KEY (user_id)
);

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.user_storage
    OWNER to postgres;

ALTER SEQUENCE public.user_storage_user_id_seq
    OWNED BY public.user_storage.user_id;
    
CREATE TABLE IF NOT EXISTS public.meal
(
    meal_id bigint NOT NULL DEFAULT nextval('meal_meal_id_seq'::regclass),
    user_id integer NOT NULL,
    dish_id integer NOT NULL,
    date date NOT NULL,
    "time" time without time zone NOT NULL,
    CONSTRAINT meal_pkey PRIMARY KEY (meal_id),
    CONSTRAINT meal_dish_id_fkey FOREIGN KEY (dish_id)
        REFERENCES public.dish (dish_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT meal_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.user_storage (user_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
 
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.meal
    OWNER to postgres;

ALTER SEQUENCE public.meal_meal_id_seq
    OWNED BY public.meal.meal_id;