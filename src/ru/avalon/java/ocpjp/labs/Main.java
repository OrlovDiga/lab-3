package ru.avalon.java.ocpjp.labs;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Класс описывает представление о коде товара и отражает соответствующую
 * таблицу базы данных Sample (таблица PRODUCT_CODE).
 *
 * @author Daniel Alpatov <danial.alpatov@gmail.com>
 */
public class ProductCode {
    /**
     * Код товара
     */
    private String code;
    /**
     * Кода скидки
     */
    private char discountCode;
    /**
     * Описание
     */
    private String description;


    /**
     * Основной конструктор типа {@link ProductCode}
     *
     * @param code код товара
     * @param discountCode код скидки
     * @param description описание
     */
    public ProductCode(String code, char discountCode, String description) {
        this.code = code;
        this.discountCode = discountCode;
        this.description = description;
    }


    /**
     * Инициализирует объект значениями из переданного {@link ResultSet}
     *
     * @param set {@link ResultSet}, полученный в результате запроса,
     * содержащего все поля таблицы PRODUCT_CODE базы данных Sample.
     */
    private ProductCode(ResultSet set) {
        try {
            this.code = set.getNString("Code");
            this.discountCode = set.getNString("discountCode").charAt(0);
            this.description = set.getNString("Description");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Возвращает код товара
     *
     * @return Объект типа {@link String}
     */
    public String getCode() {
        return code;
    }


    /**
     * Устанавливает код товара
     *
     * @param code код товара
     */
    public void setCode(String code) {
        this.code = code;
    }


    /**
     * Возвращает код скидки
     *
     * @return Объект типа {@link String}
     */
    public char getDiscountCode() {
        return discountCode;
    }


    /**
     * Устанавливает код скидки
     *
     * @param discountCode код скидки
     */
    public void setDiscountCode(char discountCode) {
        this.discountCode = discountCode;
    }


    /**
     * Возвращает описание
     *
     * @return Объект типа {@link String}
     */
    public String getDescription() {
        return description;
    }


    /**
     * Устанавливает описание
     *
     * @param description описание
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Хеш-функция типа {@link ProductCode}.
     *
     * @return Значение хеш-кода объекта типа {@link ProductCode}
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, description, discountCode);
    }


    /**
     * Сравнивает некоторый произвольный объект с текущим объектом типа
     * {@link ProductCode}
     *
     * @param obj Объект, скоторым сравнивается текущий объект.
     * @return true, если объект obj тождественен текущему объекту. В обратном
     * случае - false.
     */
    @Override
    public boolean equals(Object obj) {
        return  (obj instanceof ProductCode &&
                ((ProductCode) obj).getCode().equals(this.code) &&
                ((ProductCode) obj).getDescription().equals(this.description));
    }


    /**
     * Возвращает строковое представление кода товара.
     *
     * @return Объект типа {@link String}
     */
    @Override
    public String toString() {
        return "Код товара: " + code + ", код скидки: " + discountCode + ", описание товара" + description + ".";
    }


    /**
     * Возвращает запрос на выбор всех записей из таблицы PRODUCT_CODE
     * базы данных Sample
     *
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    private static PreparedStatement getSelectQuery(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM PRODUCT_CODE");

    }


    /**
     * Возвращает запрос на добавление записи в таблицу PRODUCT_CODE
     * базы данных Sample
     *
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getInsertQuery(Connection connection) throws SQLException {
        return connection.prepareStatement("INSERT INTO PRODUCT_CODE SET code = '?'," +
                " discountCode = '?', description = '?';");
    }


    /**
     * Возвращает запрос на обновление значений записи в таблице PRODUCT_CODE
     * базы данных Sample
     *
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getUpdateQuery(Connection connection) throws SQLException {
        return connection.prepareStatement("UPDATE PRODUCT_CODE SET code = ?, discountCode = ?, description = ?;");
    }


    /**
     * Преобразует {@link ResultSet} в коллекцию объектов типа {@link ProductCode}
     *
     * @param set {@link ResultSet}, полученный в результате запроса, содержащего
     * все поля таблицы PRODUCT_CODE базы данных Sample
     * @return Коллекция объектов типа {@link ProductCode}
     * @throws SQLException
     */
    private static Collection<ProductCode> convert(ResultSet set) throws SQLException {
        Collection<ProductCode> temp = new ArrayList<>();

        while (set.next()) {
            temp.add(new ProductCode(set));
        }

        return temp;
    }


    /**
     * Сохраняет текущий объект в базе данных.
     * <p>
     * Если запись ещё не существует, то выполняется запрос типа INSERT.
     * <p>
     * Если запись уже существует в базе данных, то выполняется запрос типа UPDATE.
     *
     * @param connection действительное соединение с базой данных
     */
    public void save(Connection connection) throws SQLException {
        boolean isUserExists = false;

        try (PreparedStatement ps = connection.prepareStatement("SELECT 1 FROM PRODUCT_CODE WHERE code = ?;")) {
            ps.setString(1, this.getCode());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isUserExists = true;
                }
            }
            String sql;
            if (isUserExists) {
                sql ="UPDATE PRODUCT_CODE SET  code = ?, discountCode = ?, description = ?;";
            } else {
                sql = "INSERT INTO PRODUCT_CODE (code, discountCode, description) VALUES (?, ?, ?)";
            }

            PreparedStatement pdSt = connection.prepareStatement(sql);
            pdSt.setString(1, this.code);
            pdSt.setString(2, "" + this.getDiscountCode() + "");
            pdSt.setString(3, this.getDescription());
            pdSt.executeUpdate();
        }
    }


    /**
     * Возвращает все записи таблицы PRODUCT_CODE в виде коллекции объектов
     * типа {@link ProductCode}
     *
     * @param connection действительное соединение с базой данных
     * @return коллекция объектов типа {@link ProductCode}
     * @throws SQLException
     */
    public static Collection<ProductCode> all(Connection connection) throws SQLException {
        try (PreparedStatement statement = getSelectQuery(connection)) {
            try (ResultSet result = statement.executeQuery()) {
                return convert(result);
            }
        }
    }
}