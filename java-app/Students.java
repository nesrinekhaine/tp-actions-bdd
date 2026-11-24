import java.sql.*;
import java.util.Optional;

public class Students {

    public static int addStudent(
            Connection conn, String name, double grade)
            throws SQLException {

        String sql = "INSERT INTO students (name, grade) VALUES (?, ?)";

        try (PreparedStatement stmt =
                 conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setDouble(2, grade);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public static Optional<Student> getStudent(
            Connection conn, int id) throws SQLException {

        String sql = "SELECT * FROM students WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("grade")
                    ));
                }
            }
        }

        return Optional.empty();
    }

    public static double averageGrade(Connection conn)
            throws SQLException {

        String sql = "SELECT AVG(grade) FROM students";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            rs.next();
            return rs.getDouble(1);
        }
    }

    public static class Student {
        public final int id;
        public final String name;
        public final double grade;

        public Student(int id, String name, double grade) {
            this.id = id;
            this.name = name;
            this.grade = grade;
        }
    }
}
