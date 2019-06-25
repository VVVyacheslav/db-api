import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbApi {
    private static final String URL = "jdbc:mysql://localhost:3306/db";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "password";

    public Map<String, String> getAccountByName(String name) {
        String query = "SELECT * FROM users WHERE Name = ?;";
        Map<String, String> result = new HashMap<>();
        try (
                Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            if (name == null || name.isEmpty()) {
                throw new SQLException("This name is not exist");
            }
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();

                resultSet.next();
                for (int i = 1; i <= columnCount; i++) {
                    result.put(resultSetMetaData.getColumnName(i), resultSet.getString(i));
                }

                if (resultSet.next()) {
                    throw new SQLException("Name is not unique");
                }

                return result;
            }
        } catch (SQLException sqlEx) {
            return new HashMap<>();
        }
    }

    public int updateLastNameByName(String name, String newLastName) {
        String query = "UPDATE users SET Last_name = ? WHERE Name = ? ;";
        try (
                Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, newLastName);
            preparedStatement.setString(2, name);
            int n = preparedStatement.executeUpdate();

            if (n != 0 && n != 1) {
                throw new SQLException("");
            }

            return n;
        } catch (SQLException sqlEx) {
            return 0;
        }
    }
}
