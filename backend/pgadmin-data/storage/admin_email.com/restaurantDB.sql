PGDMP  5                     |            restaurantDB    16.2 (Debian 16.2-1.pgdg120+2)    16.2     2           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            3           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            4           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            5           1262    16384    restaurantDB    DATABASE     y   CREATE DATABASE "restaurantDB" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
    DROP DATABASE "restaurantDB";
                postgres    false            �            1259    24591    menu_position_images    TABLE     %  CREATE TABLE public.menu_position_images (
    id integer NOT NULL,
    link character varying(200) NOT NULL,
    order_number integer NOT NULL,
    menu_position integer NOT NULL,
    CONSTRAINT menu_position_images_order_number_check CHECK (((order_number >= 1) AND (order_number <= 4)))
);
 (   DROP TABLE public.menu_position_images;
       public         heap    postgres    false            �            1259    24590    menu_position_images_id_seq    SEQUENCE     �   CREATE SEQUENCE public.menu_position_images_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.menu_position_images_id_seq;
       public          postgres    false    220            6           0    0    menu_position_images_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.menu_position_images_id_seq OWNED BY public.menu_position_images.id;
          public          postgres    false    219            �            1259    16404    menu_positions    TABLE     �  CREATE TABLE public.menu_positions (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    descr character varying(400),
    availability boolean DEFAULT true NOT NULL,
    date_entered_in_menu date DEFAULT CURRENT_DATE NOT NULL,
    price integer NOT NULL,
    portion character varying(40) NOT NULL,
    menu_section bigint NOT NULL,
    menu_section_name character varying(255)
);
 "   DROP TABLE public.menu_positions;
       public         heap    postgres    false            �            1259    16403    menu_positions_id_seq    SEQUENCE     �   CREATE SEQUENCE public.menu_positions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.menu_positions_id_seq;
       public          postgres    false    218            7           0    0    menu_positions_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.menu_positions_id_seq OWNED BY public.menu_positions.id;
          public          postgres    false    217            �            1259    16386    menu_sections    TABLE     �   CREATE TABLE public.menu_sections (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    descr character varying(400)
);
 !   DROP TABLE public.menu_sections;
       public         heap    postgres    false            �            1259    16385    menu_sections_id_seq    SEQUENCE     �   CREATE SEQUENCE public.menu_sections_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.menu_sections_id_seq;
       public          postgres    false    216            8           0    0    menu_sections_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.menu_sections_id_seq OWNED BY public.menu_sections.id;
          public          postgres    false    215            �           2604    24594    menu_position_images id    DEFAULT     �   ALTER TABLE ONLY public.menu_position_images ALTER COLUMN id SET DEFAULT nextval('public.menu_position_images_id_seq'::regclass);
 F   ALTER TABLE public.menu_position_images ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    220    219    220            �           2604    16419    menu_positions id    DEFAULT     v   ALTER TABLE ONLY public.menu_positions ALTER COLUMN id SET DEFAULT nextval('public.menu_positions_id_seq'::regclass);
 @   ALTER TABLE public.menu_positions ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    217    218    218            �           2604    16394    menu_sections id    DEFAULT     t   ALTER TABLE ONLY public.menu_sections ALTER COLUMN id SET DEFAULT nextval('public.menu_sections_id_seq'::regclass);
 ?   ALTER TABLE public.menu_sections ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    215    216    216            /          0    24591    menu_position_images 
   TABLE DATA           U   COPY public.menu_position_images (id, link, order_number, menu_position) FROM stdin;
    public          postgres    false    220   f        -          0    16404    menu_positions 
   TABLE DATA           �   COPY public.menu_positions (id, name, descr, availability, date_entered_in_menu, price, portion, menu_section, menu_section_name) FROM stdin;
    public          postgres    false    218   �        +          0    16386    menu_sections 
   TABLE DATA           8   COPY public.menu_sections (id, name, descr) FROM stdin;
    public          postgres    false    216   �%       9           0    0    menu_position_images_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.menu_position_images_id_seq', 1, false);
          public          postgres    false    219            :           0    0    menu_positions_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.menu_positions_id_seq', 10, true);
          public          postgres    false    217            ;           0    0    menu_sections_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.menu_sections_id_seq', 10, true);
          public          postgres    false    215            �           2606    24597 .   menu_position_images menu_position_images_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.menu_position_images
    ADD CONSTRAINT menu_position_images_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.menu_position_images DROP CONSTRAINT menu_position_images_pkey;
       public            postgres    false    220            �           2606    16421 "   menu_positions menu_positions_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.menu_positions
    ADD CONSTRAINT menu_positions_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.menu_positions DROP CONSTRAINT menu_positions_pkey;
       public            postgres    false    218            �           2606    16396     menu_sections menu_sections_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.menu_sections
    ADD CONSTRAINT menu_sections_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.menu_sections DROP CONSTRAINT menu_sections_pkey;
       public            postgres    false    216            �           2606    24598 $   menu_position_images fk_menuposition    FK CONSTRAINT     �   ALTER TABLE ONLY public.menu_position_images
    ADD CONSTRAINT fk_menuposition FOREIGN KEY (menu_position) REFERENCES public.menu_positions(id);
 N   ALTER TABLE ONLY public.menu_position_images DROP CONSTRAINT fk_menuposition;
       public          postgres    false    218    220    3222            �           2606    16428    menu_positions fk_menusection    FK CONSTRAINT     �   ALTER TABLE ONLY public.menu_positions
    ADD CONSTRAINT fk_menusection FOREIGN KEY (menu_section) REFERENCES public.menu_sections(id);
 G   ALTER TABLE ONLY public.menu_positions DROP CONSTRAINT fk_menusection;
       public          postgres    false    3220    218    216            /      x������ � �      -      x��W]n#E~�bA�#��M.�	��v�$�Y,������Y�Ǟ��o�W_u�g��î5�]�U_}_Uehܟ.s[���N�,q���Sg��X�%�&.u+W��W�>3��Yl�V���'��\�iz�M��m������=^ȝ�%������g�T�w�ͥ��/�])��p��p�ڥ�+�AѪ�:����`+�^^a�p�_��*�~&ϋ����A=�p��8q3\����)`��E�a��I�S\���<����J�R�N��R�#�3�i�Y�_ SwE��;c�2��h�L��[3���a%�36o���=V�ڕ�,��%�_Ui�p|.�2��N�4��/F��=],i(�Z���d
^x锝#O[�<�%\��P	��+C���b��nV1�+[ ��ER��/-N�?�t�i]F.��t�@�H��V�n����,]�pW����|A�{�,.�����"������p�p�x�!ڰ���LЂ�2/r������0Y1�P�Rh�J�h��J��U�S8������Z�[(����HP��{�m*!���;�U��iq�y�YR�`E*�M�c�	!D���r\(3��tDW���j7�[V�+�C�l�v��.`�j������ �1�'3㿌r@qxx�}�iih��*g������R�*7U�:�O�}t�/{g$�������$���U xnJe�]$mp���L:���0��Z'�](Vʦ�ݯ8-ox-��{Ip���(�g� �X<��/#vV��\���`�
$�!�x��
��l��\��Z���ZU`i�is$�Y?
���H��MkJÇ�+����ڥ|���&1��7�����.�HD(�?p$ �t8��Lⷲ�Pv/G�~�~����G�! ��G�+�}�4����a�����I�b>{U�Oa�	4��:FR�e�_�b��y�"���}��+U˿�`�l�r)#�Zj{:|T:M��U��ս���d>�8Qh�%�q)ǌU����C'[QO��2 �����Wa�|�=*��)�y��A��1xM/�p�`B.�$�y��9�=G�θ�)PA,<���cޜ��KsA��Y|J���}��^�1yCS��A˸�Vsڢ����= ù����f�eɞ��6�A�6���1
�3Á���.)ݨ**L�˃�Ҹ_Θ�^�E6����G�CP�N����ڃO��T��	��:$� Ri�9��I΅��s����u���tFB[�N�t7_>/A������_S�-z      +   ^  x��Vmn�@��� ����w�ai�F�����HU۴'X�1l|�����Cl'V��̛7of9��칍��2���˜��GRFx��_n)�3��R�Y���f��n_/�p�F���1�l�>e�pv%�8�o_��oII.)����Ĺl$��{Ft_��L���:ި���c`�F��%v�
�/�}w�'����C�-f>kBg�+�o��n3!�� "������n��ʗ!s r�TRZ��� d��n#�߼p�p��o���� \�L D��Б�.���z�������7�am���z��� �3����kW`�&��R l��z)�J�H��xT�op��j�\R2v�< UkyJW����P2q�o8�����^J͡$��@F�8�J�֣�X�D��=q�\w	�IG�R�}��S:9ˊ!�R����Os_S����:�P�ih��}�m̴��F�?h:n��xE{lE�Nt:���]a���T{�	n�E�m/L^��?���� ލrjt�q�|��W�&Q-']��4��55�����c=�d���4lǈ�C���~=cܒ���y��g����)5M>�:!n��Ĕz��P%��������U��z��]�cƔ}��B!jZ�
����~��X��@��<��s�Ǧ�`W'��@�)ֲ�*���cs�$.��KO������	�������~�
�R8�9ҏ��`ӗ���-�����gO�D,��3~��8��~�.5�'z8�}����e ��������sU��b�sl{��� ���k���9�X[V�G	+?dkF�/����;���O�0�L�N&     